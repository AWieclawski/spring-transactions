package edu.awieclawski.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class MapUtils {

    public static <T> void populateMapOfListsByStringKey(Map<String, List<T>> map, T dto, String key) {
        if (map.get(key) != null) {
            map.get(key).add(dto);
        } else {
            List<T> dtoList = new ArrayList<>();
            dtoList.add(dto);
            map.put(key, dtoList);
        }
    }

    public static <T> Map<String, List<T>> getMapOfListsFromListByStringKeyField(List<T> elements, String fieldName) {
        Map<String, List<T>> map = new HashMap<>();
        for (T element : elements) {
            boolean isField = ReflectionUtils.isFieldDeclaredInEmbeddedEntity(element.getClass(), fieldName);
            if (isField) {
                String fieldValue = ReflectionUtils.getStringFieldValueInEmbeddedEntity(element, fieldName);
                if (fieldValue != null) {
                    populateMapOfListsByStringKey(map, element, fieldValue);
                }
            }
        }
        return map;
    }

    public static <T> Map<String, T> getMapFromListByStringKeyField(List<T> elements, String fieldName) {
        Map<String, T> map = new HashMap<>();
        for (T element : elements) {
            boolean isField = ReflectionUtils.isFieldDeclaredInEmbeddedEntity(element.getClass(), fieldName);
            if (isField) {
                String fieldValue = ReflectionUtils.getStringFieldValueInEmbeddedEntity(element, fieldName);
                if (fieldValue != null) {
                    map.put(fieldValue, element);
                }
            }
        }
        return map;
    }

}
