package edu.awieclawski.utils;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.Arrays;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReflectionUtils {

    public static <T> boolean classContainsField(Class<T> clazz, String fieldName) {
        return Arrays.stream(clazz.getDeclaredFields())
                .anyMatch(fd -> fieldName.equals(fd.getName()));
    }

    public static <T> Class<?> getFieldType(Class<T> clazz, String fieldName) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(fd -> fieldName.equals(fd.getName()))
                .map(Field::getType).findFirst().orElseThrow(RuntimeException::new);
    }

    @SneakyThrows
    public static <T> String getStringFieldValue(@NotNull T obj, @NotNull String fieldName) {
        return (String) FieldUtils.readField(obj, fieldName, true);
    }

    public static <T> String getSafeStringFieldValue(@NotNull T obj, @NotNull String fieldName) {
        try {
            return getStringFieldValueInEmbeddedEntity(obj, fieldName);
        } catch (Exception ignore) {
            return "";
        }
    }

    @SneakyThrows
    public static Object getFieldValue(@NotNull Object obj, @NotNull String fieldName) {
        return FieldUtils.readField(obj, fieldName, true);
    }

    public static String getCleanFieldValue(@NotNull Object obj, String fieldName) {
        String result = getSafeStringFieldValue(obj, fieldName);
        return result != null ? result.replaceAll("\\s", "").toLowerCase() : "";
    }


    public static <T> boolean isFieldDeclared(Class<T> clazz, String fdName) {
        try {
            return ReflectionUtils.classContainsField(clazz, fdName);
        } catch (Exception e) {
            log.warn("Processing Field [{}] in Class [{}] failed!", fdName, clazz.getSimpleName(), e);
        }
        return false;
    }

    public static <T> boolean isFieldDeclaredInEmbeddedEntity(Class<T> clazz, String propertyName) {
        if (propertyName.contains(".")) {
            String[] properties = propertyName.split("\\.");
            boolean result;
            String parent = properties[0];
            String child = properties[1];
            result = isFieldDeclared(clazz, parent);
            if (result) {
                try {
                    Class<?> fndClz = getFieldType(clazz, parent);
                    result = isFieldDeclared(fndClz, child);
                } catch (RuntimeException e) {
                    log.warn("Getting Type of Field [{}] in Class [{}] failed!", parent, clazz.getSimpleName(), e);
                }
            }
            return result;
        } else {
            return isFieldDeclared(clazz, propertyName);
        }
    }

    public static String getStringFieldValueInEmbeddedEntity(Object obj, String propertyName) {
        Class<?> clazz = obj.getClass();
        if (propertyName.contains(".")) {
            String[] properties = propertyName.split("\\.");
            String result = null;
            String parent = properties[0];
            String child = properties[1];
            Object parentFieldValue = getFieldValue(obj, parent);
            if (parentFieldValue != null) {
                try {
                    result = getStringFieldValue(parentFieldValue, child);
                } catch (RuntimeException e) {
                    log.warn("Getting Value of Field [{}] in Class [{}] failed!", propertyName, clazz.getSimpleName(), e);
                }
            }
            return result;
        } else {
            return getStringFieldValue(obj, propertyName);
        }
    }

}
