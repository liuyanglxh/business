package com.liuyang.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectConvertUtil {
    private static ObjectMapper mapper = new ObjectMapper();

    //包含null值字段转换mapper
    private static ObjectMapper includeNullMapper = new ObjectMapper();

    static {
        //序列、反序列化时去除null值字段
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
        // 保证兼容性, 避免扩展字段后反序列化报错
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //序列、反序列化时包含null值字段
        includeNullMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        includeNullMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
        includeNullMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }

    public static ObjectMapper getIncludeNullMapperMapper() {
        return includeNullMapper;
    }

    public static String writeAsString(Object o) {
        if (o instanceof String) {
            return o.toString();
        }
        try {
            return getMapper().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static <T> T toObject(String s, Class<T> clazz) {
        if (s == null) {
            return null;
        }
        try {
            return getMapper().readValue(s, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T convertValue(Object fromValue, Class<T> toValueType){
        return mapper.convertValue(fromValue, toValueType);
    }

    public static <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef){
        return mapper.convertValue(fromValue, toValueTypeRef);
    }

}
