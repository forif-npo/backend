package forif.univ_hanyang;

import org.apache.commons.beanutils.BeanUtilsBean;

import java.lang.reflect.InvocationTargetException;

public class CustomBeanUtils {
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
            e.printStackTrace();
        }
    }
}
