package com.g2rain.business.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.g2rain.business.common.enums.ErrorCodeEnum;
import com.g2rain.business.common.exception.BussinessRuntimeException;

/**
 *
 * @ClassName DateFormatUtil
 * @Description 日期格式化工具类
 *
 * @author sunhaojie@kingsoft.com
 * @date 2017年8月1日 下午7:35:37
 */
public class DateFormatUtil {
	private static Logger logger = LoggerFactory.getLogger(DateFormatUtil.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat stardardFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	public static final SimpleDateFormat stardardFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
	public static final SimpleDateFormat stardardFormat3 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ");
	public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	// static {
	// dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
	// }

    /**
     * 格式化日期为:yyyy-MM-dd HH:mm:ss 格式
     *
     * @Title format
     * @param date
     * @return String
     *
     * @author sunhaojie@kingsoft.com
     * @date 2017年8月1日 下午7:37:41
     */
    public static String format(Date date) {
        if (date == null) {
            return null;
        }

        return dateFormat.format(date);
    }

	public static String formatTime(LocalDateTime localDateTime) {
		if (localDateTime == null) {
			return null;
		}

		return timeFormatter.format(localDateTime);
	}

	public static String format(LocalDateTime localDateTime) {
		if (localDateTime == null) {
			return null;
		}

		return formatter.format(localDateTime);
	}

	public static String universalizeDatetime(String datatimeString) {
		if (StringUtils.isBlank(datatimeString)) {
			return null;
		}
		
		boolean isStardardFormatFlag = false;
		boolean hasColon = false;
		int size = 0;
		for (char c : datatimeString.toCharArray()) {
			if (c == 'T') {
				isStardardFormatFlag = true;
			} else if (c == ':') {
				hasColon = true;
			}
			size++;
		}
		Date date = null;
		if (isStardardFormatFlag) {
			if (size == 19) {
				try {
					date = stardardFormat.parse(datatimeString);
				} catch (ParseException e) {
					throw new BussinessRuntimeException(ErrorCodeEnum.PARAMETER_ERROR);
				}
			} else if (size == 24) {
				try {
					date = stardardFormat3.parse(datatimeString);
				} catch (ParseException e) {
					logger.error("时间格式化错误，datatime_string:{}, format_pattern:{}", datatimeString,
							stardardFormat3.toPattern());
					date = new Date();
				}
			} else {
				try {
					date = stardardFormat2.parse(datatimeString);
				} catch (ParseException e) {
					logger.error("时间格式化错误，datatime_string:{}, format_pattern:{}", datatimeString,
							stardardFormat2.toPattern());
					date = new Date();
				}
			}

		} else {
			if (hasColon) {
				// 已经是yyyy-MM-dd HH:mm:ss格式了
				return datatimeString;
			} else {
				if (size == 10) {
					date = new Date(Long.valueOf(datatimeString) * 1000);
				} else {
					date = new Date(Long.valueOf(datatimeString));
				}
			}
		}

		return dateFormat.format(date);
	}

    /**
     * 格式化long型日期格式
     *
     * @Title format
     * @param date
     * @return String
     *
     * @author sunhaojie@kingsoft.com
     * @date 2017年8月11日 下午5:54:36
     */
    public static String format(long date) {
        if (date == 0) {
            return null;
        }

        return dateFormat.format(new Date(date));
    }

	public static synchronized Date parse(String dateString) {
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

	public static void main(String[] args) {
		// System.out.println(DateFormatUtil.parse("2019-02-13 16:13:50").getTime());
		// System.out.println(DateFormatUtil.parse("2018-12-19 14:15:15").getTime() /
		// 1000);
		// System.out.println("格式化:" +
		// DateFormatUtil.format(DateFormatUtil.parse("2019-02-13 16:13:50")));
		// System.out.println(DateFormatUtil.format(1555794323000l));
		// System.out.println("universalizeDatetime:" + universalizeDatetime("2019-02-13
		// 16:13:50"));
		// System.out.println(stardardFormat.format(new Date()));
		// System.out.println("universalizeDatetime:" +
		// universalizeDatetime("2019-05-27T21:31:19.170+0800"));
		// System.out.println("universalizeDatetime:" +
		// universalizeDatetime("2019-06-01T13:59:12+0800"));
		// System.out.println("universalizeDatetime:" +
		// universalizeDatetime("2019-05-27T21:31:19"));
		// System.out.println(universalizeDatetime("1555794323000"));
		// System.out.println(universalizeDatetime("1555794323"));
	}
}
