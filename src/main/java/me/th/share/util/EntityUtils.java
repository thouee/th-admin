package me.th.share.util;

import cn.hutool.core.util.ReflectUtil;

import java.lang.reflect.Field;
import java.util.function.Predicate;

public class EntityUtils {

    private static final String SERIAL_VERSION_UID = "serialVersionUID";

    private EntityUtils() {
    }

    /**
     * 合并实体，将 target 中不同于 source 的属性，合并到 source 中，返回合并后的 source
     *
     * @param source -
     * @param target -
     * @return T
     */
    public static <T> T merge(T source, T target) {
        return merge(source, target, null);
    }

    /**
     * 合并实体，将 target 中不同于 source 的属性，合并到 source 中，返回合并后的 source
     *
     * @param source -
     * @param target -
     * @param filter 属性过滤，当 filter 为 true 时将不合并对应的属性
     * @return T
     */
    public static <T> T merge(T source, T target, Predicate<Field> filter) {
        Field[] fields = ReflectUtil.getFields(source.getClass());
        for (Field field : fields) {
            if (SERIAL_VERSION_UID.equals(field.getName())) {
                continue;
            }
            if (filter != null && filter.test(field)) {
                continue;
            }
            Object value = ReflectUtil.getFieldValue(target, field);
            if (value != null) {
                ReflectUtil.setFieldValue(source, field, value);
            }
        }
        return source;
    }
}
