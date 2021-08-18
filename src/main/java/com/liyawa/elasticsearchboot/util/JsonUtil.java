package com.liyawa.elasticsearchboot.util;

import com.google.common.base.Throwables;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import net.sf.json.JSONObject;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author xin.lu3
 * @Date 2021/8/16
 */
@Slf4j
public class JsonUtil {

  private final static String DATE_TIME_FORMATTER = "yyyy-MM-dd HH:mm:ss";
  private  static final ObjectMapper mapper = new Jackson2ObjectMapperBuilder()
    .findModulesViaServiceLoader(true)
    .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(
      DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER)))
    .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(
      DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER)))
    .build();
  static {
    //Allow special characters
    mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS,true);
    //Allow serialization
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    //No attribute value will not report an error
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    //Ignore escaping
    mapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER,true);
    mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY,true);
  }

  /**
   * Ignore null fields
   */
  public static String toJson(Object obj) {
    try {
      return mapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Failed to json:" + obj, e);
    }
  }

  public static String toLog(Object o) {
    try {
      return mapper.writeValueAsString(o);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("format error:" + o, e);
    }
  }

  public static String toString(Object o) {
    try {
      return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("format error:" + o, e);
    }
  }

  public static <T> List<T> toList(String jsonText, Class<T> clazz) {
    if (StringUtils.isEmpty(jsonText)) {
      return Collections.emptyList();
    }

    try {
      JavaType e = mapper.getTypeFactory().constructParametrizedType(List.class, List.class, clazz);
      return (List<T>) mapper.readValue(jsonText, e);
    } catch (IOException ex) {
      throw Throwables.propagate(ex);
    }
  }

  public static Map<String, String> toMap(String content) {
    JSONObject jsonObject = JSONObject.fromObject(content);
    Iterator<String> it = jsonObject.keys();
    Map<String, String> map = new HashedMap();
    while (it.hasNext()) {
      String key = it.next();
      Object value = jsonObject.get(key);
      map.put(key, value == null ? null : value.toString());
    }
    return map;
  }


  /**
   * Convert json into object TypeReference
   */
  public static <T> T jsonToObject(String src, TypeReference<T> typeReference) {
    if (StringUtils.isEmpty(src) || typeReference == null) {
      return null;
    }
    try {
      return (T) (typeReference.getType().equals(String.class) ? src : mapper.readValue(src
              , typeReference));
    } catch (Exception e) {
      log.error(e.getMessage(),e);
      return null;
    }
  }

  /**
   * Convert json into object TypeReference
   */
  public static <T> T fromJson(String src, Class<T> clazz) {
    return jsonToObject(src, new TypeReference<T>(){
      @Override
      public Type getType() {
        return clazz;
      }
    });
  }


  public static  List<Map<String,String>> toList(String json){
    return jsonToObject(json, new TypeReference<List<Map<String, String>>>() {
    });
  }

  public static List<Map<String, Object>> jsonToList(String json){
    return jsonToObject(json, new TypeReference<List<Map<String, Object>>>() {
    });
  }

  public static Object toObj(String json) {
    if (StringUtils.isEmpty(json)) {
      return json;
    }
    try {
      String trimJson = json.trim();
      if (trimJson.charAt(0) == '[') {
        return toList(trimJson, Object.class);
      } else if (trimJson.charAt(0) == '{') {
        return mapper.readValue(trimJson, new TypeReference<Map<String, Object>>() {
        });
      }
    } catch (Exception e) {
      LogFactory.getLog(JsonUtil.class).error("parse values error", e);
      return json;
    }
    return json;
  }

  public static JsonNode toJsonNode(String jsonText) {
    try {
      return mapper.readTree(jsonText);
    } catch (Exception var3) {
      throw Throwables.propagate(var3);
    }
  }
  public static Map<String,Object> toJSONObjectMapByIterator(JSONObject obj){
    //map object
    Map<String, Object> data =new HashMap<>();
    //Cycle conversion
    Iterator it =obj.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
      data.put(entry.getKey(), entry.getValue());
    }
    return data;
  }
}
