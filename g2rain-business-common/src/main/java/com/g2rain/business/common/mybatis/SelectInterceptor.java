package com.g2rain.business.common.mybatis;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.g2rain.business.common.enums.OrganTypeEnum;
import com.g2rain.business.common.utils.CommonContextContainer;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

@Slf4j
@Intercepts({
		@Signature(type = Executor.class, method = "query",
    args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
		@Signature(type = Executor.class, method = "query",
	    args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
		@Signature(type = Executor.class, method = "queryCursor", args = { MappedStatement.class, Object.class,
				RowBounds.class }),
})
public class SelectInterceptor implements Interceptor {
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		// 当前登录不属于门店用户时
		// if (CommonContextContainer.getContext().getOrganType() !=
		// OrganTypeEnum.STORE) {
		final MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		/**
		 * sql语句id, Mapper中类名+方法名
		 */
		String sqlId = mappedStatement.getId();
		String methodName = StringUtils.substringAfterLast(sqlId, ".");
		String className = sqlId.substring(0, sqlId.lastIndexOf("."));
		DataOrganIsolation dataOrganIsolation = null;
		if (!DataOrganIsolationFlagCache.containsKey(sqlId)) {
			Class<?> classType = Class.forName(className);
			dataOrganIsolation = classType.getAnnotation(DataOrganIsolation.class);
			if (dataOrganIsolation != null) {
				boolean unsupport = false;
				for (Method method : classType.getMethods()) {
					if (method.getName().equals(methodName) && method.isAnnotationPresent(Unsupported.class)) {
						unsupport = true;
						break;
					}
				}
				if (unsupport) {
					DataOrganIsolationFlagCache.put(sqlId, null);
				} else {
					DataOrganIsolationFlagCache.put(sqlId, dataOrganIsolation);
				}
			} else {
				DataOrganIsolationFlagCache.put(sqlId, null);
			}
			// }

			if (dataOrganIsolation != null) {
				Object[] args = invocation.getArgs();
				Object parameter = args[1];
				BoundSql boundSql = null;
				if (args.length == 6) {
					boundSql = (BoundSql) args[5];
				} else {
					boundSql = mappedStatement.getBoundSql(parameter);
				}


				if (StringUtils.isBlank(boundSql.getSql())) {
					return null;
				}
				String originalSql = boundSql.getSql().trim();
				String newSql = handleParameter(originalSql, dataOrganIsolation);

				BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), newSql,
						boundSql.getParameterMappings(), boundSql.getParameterObject());
				if (invocation.getArgs().length == 6) {
					args[5] = newBoundSql;
				} else {
					MappedStatement newMs = copyFromMappedStatement(mappedStatement,
							new BoundSqlSqlSource(newBoundSql));
					args[0] = newMs;
				}

				for (ParameterMapping mapping : boundSql.getParameterMappings()) {
					String prop = mapping.getProperty();
					if (boundSql.hasAdditionalParameter(prop)) {
						newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
					}
				}
			}
		}

		return invocation.proceed();
	}

	public String handleParameter(String originalSql, DataOrganIsolation dataOrganIsolation) {
		Select select = null;
		try {
			select = (Select) CCJSqlParserUtil.parse(originalSql);
		} catch (JSQLParserException e) {
			log.error("sql解析错误, sql:{}", originalSql);
			log.error(e.getMessage(), e);
			return originalSql;
		}

		PlainSelect plainSelect = ((PlainSelect) select.getSelectBody());
		Expression where = plainSelect.getWhere();
		Expression storeOrganExpression = null;
		if (CommonContextContainer.getContext().getOrganType() != OrganTypeEnum.STORE) {
			storeOrganExpression = generateStoreOrganInExpression(dataOrganIsolation);
			if (where == null) {
				plainSelect.setWhere(storeOrganExpression);
			} else {
				AndExpression and = new AndExpression(where, storeOrganExpression);
				// 设置新的where条件
				plainSelect.setWhere(and);
			}
		}

		return plainSelect.toString();
	}

	public Expression generateStoreOrganEqualExpression(DataOrganIsolation dataOrganIsolation) {
		return null;
	}

	public Expression generateStoreOrganInExpression(DataOrganIsolation dataOrganIsolation) {
		String holdStoreOrganIds = CommonContextContainer.getHoldStoreOrganIds();
		final InExpression storeOrganIdsExpression = new InExpression();
		storeOrganIdsExpression.setLeftExpression(new Column(dataOrganIsolation.columnName()));
		List<Expression> expressions = new ArrayList<>();
		String[] holdCompanys = StringUtils.isNotBlank(holdStoreOrganIds) ? holdStoreOrganIds.split(",")
				: new String[] { "NO_STORE_HOLD" };
		for (String item : holdCompanys) {
			expressions.add(new StringValue("'" + item + "'"));
		}
		if (dataOrganIsolation.containCurrent()) {
			expressions.add(new StringValue("'" + CommonContextContainer.getOrganId() + "'"));
		}
		ExpressionList expressionList = new ExpressionList(expressions);
		storeOrganIdsExpression.setRightItemsList(expressionList);

		return storeOrganIdsExpression;
	}

	private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
		MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource,
				ms.getSqlCommandType());
		builder.resource(ms.getResource());
		builder.fetchSize(ms.getFetchSize());
		builder.statementType(ms.getStatementType());
		builder.keyGenerator(ms.getKeyGenerator());
		if (ms.getKeyProperties() != null) {
			for (String keyProperty : ms.getKeyProperties()) {
				builder.keyProperty(keyProperty);
			}
		}
		builder.timeout(ms.getTimeout());
		builder.parameterMap(ms.getParameterMap());
		builder.resultMaps(ms.getResultMaps());
		builder.cache(ms.getCache());
		return builder.build();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}
}

class BoundSqlSqlSource implements SqlSource {
	BoundSql boundSql;

	public BoundSqlSqlSource(BoundSql boundSql) {
		this.boundSql = boundSql;
	}

	@Override
	public BoundSql getBoundSql(Object parameterObject) {
		return boundSql;
	}
}