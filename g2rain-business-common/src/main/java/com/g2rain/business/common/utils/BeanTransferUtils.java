package com.g2rain.business.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @Description
 * @date 2018/10/13
 */
public class BeanTransferUtils {

    private static Logger logger = LoggerFactory.getLogger(BeanTransferUtils.class);

    public static <T> T beanTransfer(Object source,Class<T> targetClass){
        if(source == null){
            return null;
        }
        T target = null;
        try {
            target = targetClass.newInstance();
            BeanUtils.copyProperties(source,target);
        }catch (Exception e){
            logger.error("bean transfer error",e);
        }
        return target;
    }

    public static <T> List<T> batchBeanTransfer(List<?> sources, Class<T> targetClass){
        List<T> targetList = new ArrayList<T>();
        for (Object obj : sources){
            try {
                T target = targetClass.newInstance();
                BeanUtils.copyProperties(obj,target);
                targetList.add(target);
            }catch (Exception e){
                logger.error("batch transfer error",e);
            }

        }
        return targetList;
    }
}
