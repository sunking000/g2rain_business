## ksyun-dami-common定义
ksyun-dami-common为开发中常用工具集合，包含配置管理diamond，(持续丰富中)等

### 配置管理diamond
#### diamond说明
- 配置管理系统提供系统参数配置管理，例如数据库的配置信息等，配置参数修改以后可以实时推送到客户端(基于netty4)，
方便系统动态修改运行参数。
- 可以建多个项目，每个项目分为三种profile（development、test、production）， 能够控制profile 级别的权限。
- 所有参数均由development profile配置，test和production profile继承development profile配置，也可以覆盖其配置。
  test和production profile只提供修改功能。
- client 备份配置信息到本地文件系统，如果server不可用，可以使用本地备份。client 能够定时重连server，保证client高可用。
- client 提供ConfigurationListener，当某个属性发生变化（add、update、clear）, ConfigurationListener能够接收到ConfigurationEvent。

#### 使用说明
- 项目pom.xml中配置引用
	``` xml 
	<dependency>
  		<groupId>com.ksyun.dami</groupId>
  		<artifactId>ksyun-dami-common</artifactId>
  		<version>0.0.8</version>
	</dependency>
	```
- spring配置添加bean
	``` xml
	<!-- 资源工厂，业务方法从resouceFactory获取配置信息，支持返回string, Property, jsonObject -->
	<bean id="resouceFactory" class="com.ksyun.dami.common.diamond.ResouceFactory"></bean>
	<!-- 监听器，用于监听服务器端配置变更 -->
	<bean id="resourceListener" class="com.ksyun.dami.common.diamond.ResourceListener">
		<property name="resouceFactory" ref="resouceFactory"></property>
	</bean>
	<!-- 连接配置服务器的配置 -->
	<bean id="propertiesConfiguration"
		class="com.ksyun.dami.common.diamond.ResourceClientConfiguration">
		<constructor-arg index="0" value="${diamond.host}" />
		<constructor-arg index="1" value="${diamond.port}" />
		<constructor-arg index="2" value="${diamond.project.code}" />
		<constructor-arg index="3" value="${diamond.project.profile}" />
		<constructor-arg index="4" value="${diamond.project.modules}"/>
		<constructor-arg index="5">
			<list>
				<ref bean="resourceListener" />
			</list>
		</constructor-arg>
	</bean>
	```
- 实例配置
``` java
	diamond.common.host=10.69.57.59
	diamond.common.port=8283
	diamond.common.project.code=DAMI_COMMON
	diamond.common.project.profile=development
	diamond.common.project.modules=REGION
```
- 示例代码
``` java
	@Autowired
    private ResouceFactory resouceFactory;
    @Test
    public void testGet() {
		//获取资源对象
        ResourceDefinition resourceDefinition = resouceFactory.get("test");
        System.out.println(JSONObject.toJSONString(resourceDefinition));
        String value = resouceFactory.getValue("test");
        System.out.println("value : " + value);
		//获取properties类型内容
        Properties properties = resouceFactory.getProperties("test_property");
        for(Object key : properties.keySet()) {
            System.out.println("key:" + key + "; value:" + properties.get(key));
        }
		//获取JSON类型
        JSONObject jsonObject = resouceFactory.getJSONObject("test_json_object");
        System.out.println(jsonObject.toJSONString());
		//获取java对象类型  
        RegionConfig regionConfig = resouceFactory.getObject("region_Name_Match_Config", RegionConfig.class);
        RegionMeta regionMeta = regionConfig.get("TJWQRegion");
        System.out.println(JSONObject.toJSONString(regionMeta));
    }
    ```

- 配置规范
	1. 服务器中DAMI_COMMON项目为通用配置，如:机房映射表，商品类型表，异常定义等，该配置由管理员统一管理，管理员目前是孙豪杰
	2. 各项目自用配置由管理员在配置服务器中创建对应项目，并分配权限到项目owner，由项目owner自行配置相关数据
	

- 服务器信息
	1. 服务器地址:http://10.69.57.59:8090/superdiamond/
	2. 登录权限需要单独申请，请联系管理员孙豪杰
	

### 统一异常处理
#### 异常处理目标
- 统一catch异常，阻止java异常向调用方扩散，导致系统进入不可预测的流程
- 统一异常码和对应处理消息，避免错误信息提示不一致的情况

#### 原理
- 利用servlet规范中filter的机制，最上层catch调用链的信息
- 使用Exception的分级机制，对于自定义异常com.ksyun.dami.common.exception.BussinessRuntimeException转换成对应的返回信息，对于其他类异常返回ErrorCode=fail，
异常信息为服务器端异常
- 异常时返回Json格式字符串信息
- 支持二级错误输出
- 支持提示信息占位符处理

#### 异常类定义
``` java
public class BussinessRuntimeException extends RuntimeException {
    /**
     * 主错误码
     */
    private String errorCode;
    /**
     * 二级错误码
     */
    private List<String> subErrorCode;
    /**
     * 主错误码占位符信息
     */
    private Map<String, String> placeHolder;
    /**
     * 二级错误码占位符信息
     */
    private Map<String, Map<String, String>> subErrorPlaceHolder;

    public BussinessRuntimeException() {
        super();
    }

    public BussinessRuntimeException(String errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public BussinessRuntimeException(Throwable t) {
        super(t);
    }

    public BussinessRuntimeException(String errorCode, Throwable t) {
        super(t);
        this.errorCode = errorCode;
    }

    public BussinessRuntimeException(String errorCode, List<String> subErrorCode, Throwable t) {
        super(t);
        this.errorCode = errorCode;
        this.subErrorCode = subErrorCode;
    }

    public void putPlaceHolder(String key, String value) {
        if (placeHolder == null) {
            this.placeHolder = new HashMap<String, String>();
        }

        this.placeHolder.put(key, value);
    }

    public void putPlaceHolder(String errorCode, String key, String value) {
        if (this.subErrorPlaceHolder == null) {
            this.subErrorPlaceHolder = new HashMap<String, Map<String, String>>();
        }

        Map<String, String> subPlaceHolder = this.subErrorPlaceHolder.get(errorCode);
        if (subPlaceHolder == null) {
            subPlaceHolder = new HashMap<String, String>();
            this.subErrorPlaceHolder.put(errorCode, subPlaceHolder);
        }
        subPlaceHolder.put(key, value);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public List<String> getSubErrorCode() {
        return subErrorCode;
    }

    public void setSubErrorCode(List<String> subErrorCode) {
        this.subErrorCode = subErrorCode;
    }

    public Map<String, String> getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(Map<String, String> placeHolder) {
        this.placeHolder = placeHolder;
    }

    public Map<String, Map<String, String>> getSubErrorPlaceHolder() {
        return subErrorPlaceHolder;
    }

    public void setSubErrorPlaceHolder(Map<String, Map<String, String>> subErrorPlaceHolder) {
        this.subErrorPlaceHolder = subErrorPlaceHolder;
    }
}
```
#### 用法说明
示例采用Spring boot框架，其他类项目参考filter的配置方式引入filter
###### 配置pom.xml
``` xml
<dependency>
	<groupId>com.ksyun.dami</groupId>
	<artifactId>ksyun-dami-common</artifactId>
	<version>0.0.9</version>
</dependency>
```

###### 使用Spring boot配置类引入配置
``` java
@Configuration
@ImportResource(locations = {"classpath:spring-diamond-customer-seller.xml"})
public class BeanConfig implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    @Bean
    public FilterRegistrationBean filterRegistration() {
        ResouceFactory resouceFactory = (ResouceFactory) applicationContext.getBean("commonResouceFactory");
        FilterRegistrationBean registration = new FilterRegistrationBean();
        BussinessRuntimeExceptionFilter exceptionFilter = new BussinessRuntimeExceptionFilter(resouceFactory);
        registration.setFilter(exceptionFilter);
        registration.addUrlPatterns("/*");
        return registration;
    }
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
```

###### diamon错误信息配置 spring-diamond-customer-seller.xml
``` xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" 
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xmlns:tx="http://www.springframework.org/schema/tx"
	   	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.1.xsd
	   		http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-3.0.xsd">
	<!-- common配置   begin -->
	<!-- 资源工厂，业务方法从resouceFactory获取配置信息，支持返回string, Property, jsonObject -->
	<bean id="commonResouceFactory" class="com.ksyun.dami.common.diamond.ResouceFactory"></bean>
	<!-- 监听器，用于监听服务器端配置变更 -->
	<bean id="commonResourceListener" class="com.ksyun.dami.common.diamond.ResourceListener">
		<property name="resouceFactory" ref="commonResouceFactory"></property>
	</bean>
	<!-- 连接配置服务器的配置 -->
	<bean id="commonPropertiesConfiguration"
		class="com.ksyun.dami.common.diamond.ResourceClientConfiguration">
		<constructor-arg index="0" value="${diamond.common.host}" />
		<constructor-arg index="1" value="${diamond.common.port}" />
		<constructor-arg index="2" value="${diamond.common.project.code}" />
		<constructor-arg index="3" value="${diamond.common.project.profile}" />
		<constructor-arg index="4" value="${diamond.common.project.modules}"/>
		<constructor-arg index="5">
			<list>
				<ref bean="commonResourceListener" />
			</list>
		</constructor-arg>
	</bean>
	<!-- common配置   end -->
</beans>	
```
###### 属性配置
``` properties
diamond.common.host=10.69.57.59
diamond.common.port=8283
diamond.common.project.code=DAMI_COMMON
diamond.common.project.profile=development
diamond.common.project.modules=ERROR_CODE
```
#### 异常结果示例
``` json
{
    "errorCode": "BUSINESS_FAIL_PLACE_HOLDER",
    "message": "业务异常:你好",
    "method": "POST",
    "path": "/customer_seller/hello",
    "status": 500,
    "timestamp": 1502251819888
}
``` 

#### 错误码定义规范
- 格式：项目名_模块名_具体错误，项目名，模块名和具体错误使用大写英文单词，单词见建议使用"-"分割
- 统一异常配置提供，错误码，英文描述，中文描述

### 统一http接口调用
#### 背景
- 目前公司内部系统间调用以http接口为主
- 接口定义没有统一规范，存在json入参，form入参，权限控制等各类接口

#### 目标
- 统一的调用方式，包括重试机制，连接超时时间等
- 适应多类型的调用

#### 示例
- 配置pom.xml
``` xml
<dependency>
	<groupId>com.ksyun.dami</groupId>
	<artifactId>ksyun-dami-common</artifactId>
	<version>0.0.9</version>
</dependency>
```
- json调用,post和put调用入参为json，get和delete入参为键值对
``` java
Map<String, Object> params = new HashMap<String, Object>();
params.put("orderIds", orderId);
Map<String, String> headers = new HashMap<String, String>();
headers.put("Content-Type", "application/json;charset=utf-8");
String result = HttpClientUtil.get(tradeQueryOrderUrl, headers, params);
if (StringUtils.isNotBlank(result)) {
    JSONObject resultObject = JSONObject.parseObject(result);
    JSONArray jsonArray = resultObject.getJSONArray("orders");
    if (jsonArray.size() > 0) {
        TradeOrderVo tradeOrderVo = jsonArray.getObject(0, TradeOrderVo.class);
        return tradeOrderVo;
    }
}
```
- 带有权限校验(HTTP基本认证Basic Authentication)的请求访问
``` java
Map<String, Object> params = new HashMap<String, Object>();
params.put("user_id", userId);
Map<String, String> headers = new HashMap<String, String>();
headers.put("Content-Type", "application/json;charset=utf-8");
AuthenticationRequestCallBack callBack = new AuthenticationRequestCallBack(HttpClientUtil.getMethodName,
    profileUserinfoUrl, headers, params);
// profileUsername 用户名  profilePassword 密码
callBack.setAuthentication(null, 0, profileUsername, profilePassword);
String result = HttpClientUtil.execute(callBack, null);
JSONObject resultObject = JSONObject.parseObject(result);
JSONObject dataObject = resultObject.getJSONObject("data");
if (dataObject == null || dataObject.isEmpty()) {
    return null;
}
```
- json串方式入参的json传递格式
``` java
// method为请求方法  url为访问路径  
StringContentRequestCallBack stringContentRequestCallBack = new StringContentRequestCallBack(method, url,
                headers, null);
stringContentRequestCallBack.setContent(content);
callBack = stringContentRequestCallBack;
String result = HttpClientUtil.execute(callBack, null);
```

- form方式请求
``` java
// method为请求方法  url为访问路径   header 请求头 params入参
FormBasicRequestCallBack callBack = new FormBasicRequestCallBack(method,
            url, header, params);
String result = HttpClientUtil.execute(callBack, null);
```

#### 代码实现解析
- 链接管理和请求执行类
``` java
public class HttpClientUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    public final static String charset = "UTF-8";
    public final static String getMethodName = "GET";
    public final static String postMethodName = "POST";
    public final static String putMethodName = "PUT";
    public final static String deleteMethodName = "DELETE";
    /**
     * 默认连接建立时长
     */
    public final static int DEFAULT_CONNECT_TIMEOUT = 20000;
    /**
     * 默认服务器端响应时长
     */
    public final static int DEFAULT_SOCKET_TIMEOUT = 10000;
    public static String delete(String url, Map<String, String> headerMap, Map<String, Object> paramMap) {
        return delete(url, headerMap, paramMap, null, null);
    }
    private static String delete(String url, Map<String, String> headerMap, Map<String, Object> paramMap,
                                RequestCallBack callBack, ResponseExecutor responseExecutor) {
        logger.info(url);
        //处理地址
        HttpDelete httpDelete = new HttpDelete(url);
        //处理head
        setHeader(httpDelete, headerMap);
        //处理参数
        RequestCallBack requestCallBack = null;
        if (callBack == null) {
            requestCallBack = new BasicRequestCallBack(paramMap, httpDelete, deleteMethodName);
        } else {
            requestCallBack = callBack;
            requestCallBack.setMethod(deleteMethodName);
            requestCallBack.setParamMap(paramMap);
            requestCallBack.setRequest(httpDelete);
        }
        return execute(requestCallBack, responseExecutor);
    }
    public static String put(String url, Map<String, String> headerMap, Map<String, Object> paramMap) {
        return put(url, headerMap, paramMap, null, null);
    }
    private static String put(String url, Map<String, String> headerMap, Map<String, Object> paramMap,
                             RequestCallBack callBack, ResponseExecutor responseExecutor) {
        logger.info(url);
        //初始化对象
        HttpPut httpPut = new HttpPut(url);
        //处理heard
        setHeader(httpPut, headerMap);
        //处理入参
        RequestCallBack requestCallBack = null;
        if (callBack == null) {
            requestCallBack = new BasicRequestCallBack(paramMap, httpPut, putMethodName);
        } else {
            requestCallBack = callBack;
            requestCallBack.setMethod(putMethodName);
            requestCallBack.setParamMap(paramMap);
            requestCallBack.setRequest(httpPut);
        }

        return execute(requestCallBack, responseExecutor);
    }
    public static String post(String url, Map<String, String> headerMap, Map<String, Object> paramMap) {
        return post(url, headerMap, paramMap, null, null);
    }
    private static String post(String url, Map<String, String> headerMap, Map<String, Object> paramMap,
                              RequestCallBack callBack, ResponseExecutor responseExecutor) {
        logger.info(url);
        //初始化对象
        HttpPost httpPost = new HttpPost(url);
        //处理heard
        setHeader(httpPost, headerMap);
        //处理入参
        RequestCallBack requestCallBack = null;
        if (callBack == null) {
            requestCallBack = new BasicRequestCallBack(paramMap, httpPost, postMethodName);
        } else {
            requestCallBack = callBack;
            requestCallBack.setMethod(postMethodName);
            requestCallBack.setParamMap(paramMap);
            requestCallBack.setRequest(httpPost);
        }
        return execute(requestCallBack, responseExecutor);
    }
    public static String get(String url, Map<String, String> headerMap, Map<String, Object> paramMap) {
        return get(url, headerMap, paramMap, null, null);
    }
    private static String get(String url, Map<String, String> headerMap, Map<String, Object> paramMap,
                             RequestCallBack callBack, ResponseExecutor responseExecutor) {
        logger.info(url);
        //处理地址
        HttpGet httpget = new HttpGet(url);
        //处理head
        setHeader(httpget, headerMap);
        //处理参数
        RequestCallBack requestCallBack = null;
        if (callBack == null) {
            requestCallBack = new BasicRequestCallBack(paramMap, httpget, getMethodName);
        } else {
            requestCallBack = callBack;
            requestCallBack.setMethod(getMethodName);
            requestCallBack.setParamMap(paramMap);
            requestCallBack.setRequest(httpget);
        }

        return execute(requestCallBack, responseExecutor);
    }
    public static String execute(RequestCallBack callBack, ResponseExecutor responseExecutor) {
        CloseableHttpResponse response = null;
        RequestConfig requestConfig = null;
        if (callBack.getRequestConfig() != null) {
            requestConfig = callBack.getRequestConfig();
        } else {
            requestConfig = RequestConfig.custom().setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)
                .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT).build();
        }
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
        HttpRequestBase request = callBack.callback();
        try {
            HttpClientContext httpClientContext = callBack.getContext();
            if (httpClientContext != null) {
                response = httpclient.execute(request, httpClientContext);
            } else {
                response = httpclient.execute(request);
            }
            ResponseExecutor executor = null;
            if (responseExecutor == null) {
                executor = new StringResponseExecutor(response);
            } else {
                executor = responseExecutor;
            }
            return executor.execute();
        } catch (HttpClientResponseStatusErrorException e) {
            logger.error("结果值错误", e);
            return e.getResponseValue();
        } catch (Exception e) {
            logger.error("调用接口异常", e);
            throw new BussinessRuntimeException(ErrorCodeEnum.HTTP_CLIENT_IO_ERROR.name(), e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    logger.error("关闭连接错误", e);
                }
            }
            if (httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    logger.error("关闭连接错误", e);
                }
            }

        }
    }
    public static void setHeader(HttpRequestBase request, Map<String, String> headerMap) {
        if (MapUtils.isNotEmpty(headerMap)) {
            for (String name : headerMap.keySet()) {
                request.addHeader(name, headerMap.get(name));
            }
        }
    }
}
```
- 请求处理接口
``` java
public interface RequestCallBack {
    //请求回调
    public HttpRequestBase callback();
    //获取上下文
    public HttpClientContext getContext();
	//获取请求配置
    public RequestConfig getRequestConfig();
	//设置请求配置
    public void setRequestConfig(RequestConfig requestConfig);
	//获取参数map
    public Map<String, Object> getParamMap();
	//设置参数map
    public void setParamMap(Map<String, Object> paramMap);
	//获取请求类
    public HttpRequestBase getRequest();
	//设置请求类
    public void setRequest(HttpRequestBase request);
	//获取调用方法
    public String getMethod();
	//设置调用方法
    public void setMethod(String method);
}
```
- 应答处理接口
``` java
public interface ResponseExecutor {
    //结果处理
    public String execute() throws HttpClientResponseStatusErrorException;
}
```

- 关键逻辑
``` java
//请求回调类
RequestCallBack callBack;
//应答处理类
ResponseExecutor responseExecutor;
CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
HttpRequestBase request = callBack.callback();
response = httpclient.execute(request);
ResponseExecutor executor = new StringResponseExecutor(response);
String responseString = executor.execute();
```
如上述代码所示，RequestCallBack用于处理请求，为适应多种类型的请求方式，可以实现多个场景的RequestCallBack类。ResponseExecutor为应答处理器，response的处理器，
目前默认实现应答返回String值
如果已提供方式不能满足需求，可以通过自定义RequestCallBack或者ResponseExecutor适应新的场景

### 常用工具类
#### 配置pom.xml
``` xml
<dependency>
	<groupId>com.ksyun.dami</groupId>
	<artifactId>ksyun-dami-common</artifactId>
	<version>0.0.9</version>
</dependency>
```
#### 日期格式化 仅仅处理常用日期格式"yyyy-MM-dd HH:mm:ss"
```java
public class DateFormatUtil {
    private static Logger logger = LoggerFactory.getLogger(DateFormatUtil.class);
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String format(Date date) {
        if (date == null) {
            return null;
        }

        return dateFormat.format(date);
    }
    public static Date parse(String dateString) {
        if (dateString == null) {
            return null;
        }
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            logger.error("{} format illegal", dateString);
            throw new BussinessRuntimeException(ErrorCodeEnum.DATE_FORMAT_ILLEGAL.name());
        }
    }
}
```

#### email隐藏关键信息,'@'前隐藏3位字符串
```java
public class EmailHideMaster {
    public static String hideMaster(String userEmail) {
        String newUserMobile = null;
        if (StringUtils.isNotBlank(userEmail)) {
            String frontPart = userEmail.substring(0, userEmail.indexOf("@"));
            String behindPart = userEmail.substring(userEmail.indexOf("@"));
            int endOffset = frontPart.length() - 3;
            if (endOffset <= 0) {
                endOffset = 0;
            }
            frontPart = frontPart.substring(0, endOffset);
            newUserMobile = frontPart + "***" + behindPart;
        }
        return newUserMobile;
    }
    public static void main(String[] args) {
        String hideMaster = hideMaster("a@kingsoft.com");
        System.out.println(hideMaster);
    }
}
```

#### 手机号隐藏关键信息,隐藏中间4位数字
```java
public class MobileHideMaster {
    public static String hideMaster(String userMobile) {
        String newUserMobile = null;
        if (userMobile != null && userMobile.length() >= 11) {
            int userMobileStringLength = userMobile.length();
            String first = userMobile.substring(0, userMobileStringLength - 8);
            String second = "****";
            String third = userMobile.substring(userMobileStringLength - 4);
            newUserMobile = first + second + third;
        }
        return newUserMobile;
    }
}
```

#### 8位短码生成器，包括大写，小写字母以及数据任意8位的组合
```java
public class RandomEightBitStringUtil {
    public static String[] chars = new String[] {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
        "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
        "W", "X", "Y", "Z"};
    public static String generateShortUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }
    public static String[] generateShortUuid(int count) {
        if (count <= 0) {
            count = 0;
        }
        String[] result = new String[count];
        for (int i = 0; i < count; i++) {
            result[i] = generateShortUuid();
        }
        return result;
    }
    public static void main(String[] args) {
        String shortUUID = RandomEightBitStringUtil.generateShortUuid();
        System.out.println(shortUUID);
    }
}
```

### 可重复读取的Request和Response实现
#### 原因：
servlet原生的Request和Response都是不可重复读取的，一些场景下需要在业务处理之前进行数据读取，比如日志输出，权限验证等。

#### 原理：
request:通过filter把request输入流读取后，数据存放到byte[]中，通过BufferedServletInputStream重复读取数据。

response:通过filter重写response的方法getWriter和getOutputStream，返回包装后的输出流，把相关数据分别写入ByteArrayOutputStream和客户端的输出流。

#### 实现：
##### request
重写ServletInputStream
``` java
public class BufferedServletInputStream extends ServletInputStream {
	private ByteArrayInputStream inputStream;
	public BufferedServletInputStream(byte[] buffer) {
		this.inputStream = new ByteArrayInputStream(buffer);
	}
	@Override
	public int available() throws IOException {
		return inputStream.available();
	}
	@Override
	public int read() throws IOException {
		return inputStream.read();
	}
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return inputStream.read(b, off, len);
	}
}
```
重写request类
``` java
public class BufferedServletRequestWrapper extends HttpServletRequestWrapper {
    private static Logger logger = LoggerFactory.getLogger(BufferedServletRequestWrapper.class);
	private byte[] buffer;
	//初始化包装类，把客户端输入流信息读取后写入byte[]中
	public BufferedServletRequestWrapper(HttpServletRequest request)
			throws IOException {
		super(request);
		InputStream is = request.getInputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte buff[] = new byte[1024];
		int read;
		while ((read = is.read(buff)) > 0) {
			baos.write(buff, 0, read);
		}
		this.buffer = baos.toByteArray();
	}
	//重载获取输入流方法
	@Override
	public ServletInputStream getInputStream() throws IOException {
		return new BufferedServletInputStream(this.buffer);
	}
    //新增获取请求头方法
    public String getHeader() {
        @SuppressWarnings("unchecked")
        Enumeration<String> headerNames = this.getHeaderNames();
        Map<String, String> headers = new HashMap<String, String>();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headers.put(name, this.getHeader(name));
        }
        return JSONObject.toJSONString(headers);
    }
    //新增获取参数方法
    public String getParam() {
        String method = this.getMethod();
        String paramString = "";
        if("get".equalsIgnoreCase(method) || "delete".equalsIgnoreCase(method)) {
            Map<String, String> params = new HashMap<String, String>();
            @SuppressWarnings("unchecked")
            Enumeration<String> parameterNames = this.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String parameterName = parameterNames.nextElement();
                String parameterValue = this.getParameter(parameterName);
                params.put(parameterName, parameterValue);
            }
            paramString = JSONObject.toJSONString(params);
        } else {
            StringBuffer sb = new StringBuffer("");
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(this.getInputStream(), "utf-8"));
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sb.append(temp);
                }
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getMessage(), e);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
            paramString = sb.toString();
        }

        return paramString;
    }
}
```
##### Response
``` java
public class BufferedServletResponseWrapper extends HttpServletResponseWrapper {
    private static Logger logger = LoggerFactory.getLogger(BufferedServletResponseWrapper.class);
    private ByteArrayOutputStream bos;
    private PrintWriter printWriter;
    //初始化response
    public BufferedServletResponseWrapper(HttpServletResponse response) {
        super(response);
        this.bos = new ByteArrayOutputStream();
        this.printWriter = new PrintWriter(bos);
    }
    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriterWrapper(super.getWriter(), this.printWriter);
    }
    private ServletOutputStream getSuperOutputStream() throws IOException {
        return super.getOutputStream();
    }
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        ServletOutputStream servletOutputStream = new ServletOutputStreamWrapper(this);
        return servletOutputStream;
    }
    //新增获取结果值的方法
    public String getResult() {
        byte[] bytes = this.bos.toString().getBytes();
        try {
            return new String(bytes, "UTF-8");
        } catch (Exception e) {
            logger.error("getResult error", e);
            return null;
        }
    }
    //输出流包装类，写入数据时同步写入客户端输出流和ByteArrayOutputStream
    class ServletOutputStreamWrapper extends ServletOutputStream {
        private BufferedServletResponseWrapper response;
        ServletOutputStreamWrapper(BufferedServletResponseWrapper response) {
            this.response = response;
        }
        /* (non-Javadoc)
         * @see java.io.OutputStream#write(int)
         */
        @Override
        public void write(int b) throws IOException {
            response.bos.write(b);
            response.getSuperOutputStream().write(b);
        }
    }
	//输出流包装类，写入数据时同步写入客户端输出流和PrintWriter
    class PrintWriterWrapper extends PrintWriter {
        private PrintWriter branch;
        public PrintWriterWrapper(PrintWriter main, PrintWriter branch) {
            super(main, true);
            this.branch = branch;
        }
        public void write(char buf[], int off, int len) {
            super.write(buf, off, len);
            super.flush();
            branch.write(buf, off, len);
            branch.flush();
        }
        public void write(String s, int off, int len) {
            super.write(s, off, len);
            super.flush();
            branch.write(s, off, len);
            branch.flush();
        }
        public void write(int c) {
            super.write(c);
            super.flush();
            branch.write(c);
            branch.flush();
        }
        public void flush() {
            super.flush();
            branch.flush();
        }
    }
}
```
#### 使用filter重置request和response
``` java
public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
	HttpServletRequest httpServletRequest = null;
    HttpServletResponse httpServletResponse = null;
	if (request instanceof HttpServletRequest) {
		httpServletRequest = (HttpServletRequest) request;
        httpServletRequest = new BufferedServletRequestWrapper(httpServletRequest);
	}
    if (response instanceof HttpServletResponse) {
        httpServletResponse = (HttpServletResponse) response;
        httpServletResponse = new BufferedServletResponseWrapper(httpServletResponse);
    }
    if (httpServletRequest != null && httpServletResponse != null) {
        chain.doFilter(httpServletRequest, httpServletResponse);
    } else {
        chain.doFilter(request, response);
    }
}
```

#### spring boot项目中的应用
配置pom.xml
``` xml
<dependency>
	<groupId>com.ksyun.dami</groupId>
	<artifactId>ksyun-dami-common</artifactId>
	<version>0.0.12</version>
</dependency>
```
filter配置实现
``` java
public class BeanConfig implements ApplicationContextAware {
	@Bean
	public FilterRegistrationBean resetRequestResponseFilterRegistration() {
	    FilterRegistrationBean resetRequestResponseFilterRegistration = new FilterRegistrationBean();
	    ResetRequestResponseFilter resetRequestResponseFilter = new ResetRequestResponseFilter();
	    resetRequestResponseFilterRegistration.setFilter(resetRequestResponseFilter);
	    resetRequestResponseFilterRegistration.addUrlPatterns("/*");
	    //执行顺序设定，设置为最先执行的filter
	    resetRequestResponseFilterRegistration.setOrder(1);
	    return resetRequestResponseFilterRegistration;
	}
}
```

### 基于可重复读取的Request和Response的入参和结果的日志输出配置
#### 目标
1、打印request的header和param数据

2、打印response中的结果

3、打印服务执行的开始时间，结束时间以及执行时长

#### 实现
``` java
public class LogFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(LogFilter.class);
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
        ServletException {
        long startTime = System.currentTimeMillis();
        String requestId = UUID.randomUUID().toString();
        BufferedServletRequestWrapper bufferedServletRequestWrapper = null;
        //获取method
        String method = ((HttpServletRequest) request).getMethod();
        //获取调用路径
        String path = ((HttpServletRequest) request).getServletPath();
        //只有可重复读取的包装类才可以读取header和param数据
        if (request instanceof BufferedServletRequestWrapper) {
            bufferedServletRequestWrapper = (BufferedServletRequestWrapper) request;
            String header = bufferedServletRequestWrapper.getHeader();
            String param = bufferedServletRequestWrapper.getParam();
            logger.info("requestId:{}, method:{}, path:{}, param:{}", requestId, method, path, param);
            logger.debug("requestId:{}, header:{}", requestId, header);
        }
        //执行业务操作
        chain.doFilter(request, response);
        BufferedServletResponseWrapper bufferedServletResponseWrapper = null;
        //只有可重复读取的包装类才可以读取结果值
        if (response instanceof BufferedServletResponseWrapper) {
            bufferedServletResponseWrapper = (BufferedServletResponseWrapper) response;
            logger.info("requestId:{}, result:{}", requestId, bufferedServletResponseWrapper.getResult());
        }
        long endTime = System.currentTimeMillis();
        long takedTime = endTime - startTime;
        //输出开始时间，结束时间以及执行时间
        logger.info("requestId:{}, startTime:{}, endTime:{}, takedTime:{}", requestId,
            DateFormatUtil.format(startTime),
            DateFormatUtil.format(endTime), takedTime
            + "ms");
    }
    public void destroy() {
    }
}
```
#### spring boot项目中的应用
``` xml
<dependency>
	<groupId>com.ksyun.dami</groupId>
	<artifactId>ksyun-dami-common</artifactId>
	<version>0.0.12</version>
</dependency>
```
filter配置实现
``` java
public class BeanConfig implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    @Bean
    public FilterRegistrationBean resetRequestResponseFilterRegistration() {
        FilterRegistrationBean resetRequestResponseFilterRegistration = new FilterRegistrationBean();
        ResetRequestResponseFilter resetRequestResponseFilter = new ResetRequestResponseFilter();
        resetRequestResponseFilterRegistration.setFilter(resetRequestResponseFilter);
        resetRequestResponseFilterRegistration.addUrlPatterns("/*");
        resetRequestResponseFilterRegistration.setOrder(1);
        return resetRequestResponseFilterRegistration;
    }
    @Bean
    public FilterRegistrationBean logFilterRegistration() {
        FilterRegistrationBean logFilterRegistration = new FilterRegistrationBean();
        LogFilter logFilter = new LogFilter();
        logFilterRegistration.setFilter(logFilter);
        logFilterRegistration.addUrlPatterns("/*");
        logFilterRegistration.setOrder(2);
        return logFilterRegistration;
    }
}
```

### 分布式集群task
#### 目标
1、通过配置通用的任务触发器，把定时任务从项目代码中解耦

2、业务逻辑提供url接口供任务触发器调用

3、支持界面配置任务，接口创建任务等

4、支持实时任务，定时任务，表达式任务等多类型

5、可追踪的任务执行日志

#### 界面管理
1、链接地址:http://10.69.57.59:8081/index.htm

2、用户名密码，请联系孙豪杰

3、支持功能：任务配置，日志查询，节点管理

#### 代码创建任务
1、pom.xml文件引入相关包
``` xml
<dependency>
	<groupId>com.ksyun.dami</groupId>
	<artifactId>ksyun-dami-common</artifactId>
	<version>0.0.13</version>
</dependency>
```

2、配置在spring配置文件中配置job提交客户端类
``` xml
<bean id="jobClientUtil" class="com.ksyun.dami.common.taskjob.JobClientUtil">
	<constructor-arg index="0" name="clusterName" value="ksyun_dami_cluster" />
    <constructor-arg index="1" name="nodeGroup" value="ksyun_dami_jobClient"/>
    <constructor-arg index="2" name="registryAddress" value="redis://10.69.42.31:6379"/>
</bean>
```

3、提交job代码
``` java
SubmitJobParam param = new SubmitJobParam();
//执行集群  不需要修改
param.setTaskTrackerNodeGroup("ksyun_dami_taskTracker");
//quartz格式执行策略  优先trigger配置
param.setCronExpression("0 15 10 * * ? *");
//具体执行时间  如果存在cronExpression表达式则该字段无效
param.setTriggerTime(new Date());
//执行http业务类扩展入参
Map<String, String> extParams = new HashMap<String, String>();
//http调用方法
extParams.put("method", "post");
//调用url
extParams.put("url", "http://10.69.57.59:8082/test/post_hello");
//具体入参
extParams.put("params", "{\"name\":\"test1\",\"firstName\":\"test2\"}");
//header文件
extParams.put("headers", "{\"Content-Type\":\"application/json\"}");
param.setExtParams(extParams);
//提交任务
jobClientUtil.submitJob(param);
```

