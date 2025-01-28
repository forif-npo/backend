package forif.univ_hanyang.util;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

public class CustomBeanUtils {
    private static final Logger logger = LoggerFactory.getLogger(CustomBeanUtils.class);
    public static void copyNonNullProperties(Object dest, Object orig) {
        BeanUtilsBean notNull = new BeanUtilsBean() {
            @Override
            public void copyProperty(Object dest, String name, Object value)
                    throws IllegalAccessException, InvocationTargetException {
                if(value != null) {
                    super.copyProperty(dest, name, value);
                }
            }
        };
        try {
            notNull.copyProperties(dest, orig);
        } catch (Exception e) {
            logger.error("Error occurred while copying properties", e);
        }
    }
}
