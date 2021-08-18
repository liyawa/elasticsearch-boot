package com.liyawa.elasticsearchboot.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.util.JSONPObject;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author xin.lu3
 * @Date 2021/8/16
 */
@Slf4j
public class JsonMapper {
  public static final JsonMapper INSTANCE = new JsonMapper();
  public static final JsonMapper MAPPER = JsonMapper.nonDefaultMapper();

  private ObjectMapper mapper;

  public JsonMapper() {
    this(null);
  }

  public JsonMapper(Include include) {
    mapper = new ObjectMapper();
    // The style of set contains the output of the properties
    if (include != null) {
      mapper.setSerializationInclusion(include);
    }
    // Set the input ignored in JSON string but Java objects exist in the actual no attributes
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
  }

  /**
   * Create only output not Null attribute to the Json string Mapper.
   */
  public static JsonMapper nonNullMapper() {
    return new JsonMapper(Include.NON_NULL);
  }

  /**
   * Create only output is not Null and not Empty (such as a List. IsEmpty) attributes to Json
   * string Mapper.
   * <p>
   * note that need to be careful to use, give special attention to the situation of the empty.
   */
  public static JsonMapper nonEmptyMapper() {
    return new JsonMapper(Include.NON_EMPTY);
  }

  /**
   * All the default output Mapper, different from the INSTANCE, can be further configuration
   */
  public static JsonMapper defaultMapper() {
    return new JsonMapper();
  }

  /**
   * Create only output the attribute of the initial value was changed to the Json string Mapper,
   * the most economical way of storage should be used in the internal interface.
   */
  public static JsonMapper nonDefaultMapper() {
    return new JsonMapper(Include.NON_DEFAULT);
  }

  /**
   * Object can be a POJO, can also be a Collection or array.
   */
  public String toJson(Object object) {

    try {
      return mapper.writeValueAsString(object);
    } catch (IOException e) {
      log.warn("write to json string error:" + object, e);
      return null;
    }
  }

  public <T> String toJson(Object obj, Class<T> jsonViewClazz) {
    try {
      return mapper.writerWithView(jsonViewClazz).writeValueAsString(
          obj);
    } catch (IOException e) {
      log.warn("write to json string error:" + obj, e);
      return null;
    }
  }

  @SuppressWarnings({"serial", "rawtypes"})
  public String toJson(Object obj, Map<Class, Set<String>> include,
                       Map<Class, Set<String>> exclude) {

    if ((null == include || include.isEmpty())
        && (null == exclude || exclude.isEmpty())) {
      toJson(obj);
    }

    ObjectMapper mapper = new ObjectMapper();

    // Set includes filters
    FilterProvider filters = new SimpleFilterProvider();
    if (null != include && !include.isEmpty()) {
      for (Entry<Class, Set<String>> entry : include.entrySet()) {
        Class clazz = entry.getKey();
        Set<String> includeFileds = entry.getValue();
        ((SimpleFilterProvider) filters).addFilter(clazz.getName(),
            SimpleBeanPropertyFilter.filterOutAllExcept(includeFileds));
      }
    }

    // Set to exclude filters
    if (null != exclude && !exclude.isEmpty()) {
      for (Entry<Class, Set<String>> entry : exclude.entrySet()) {
        Class clazz = entry.getKey();
        Set<String> excludeFileds = entry.getValue();
        ((SimpleFilterProvider) filters).addFilter(clazz.getName(),
            SimpleBeanPropertyFilter.serializeAllExcept(excludeFileds));
      }
    }
    mapper.setFilterProvider(filters);

    // What is filter
    final Set<String> filterNames = new HashSet<String>();
    if (null != include && !include.isEmpty()) {
      for (Class clazz : include.keySet()) {
        filterNames.add(clazz.getName());
      }
    }
    if (null != exclude && !exclude.isEmpty()) {
      for (Class clazz : exclude.keySet()) {
        filterNames.add(clazz.getName());
      }
    }

    mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {

      public Object findFilterId(Annotated ac) {
        String name = ac.getName();
        if (filterNames.contains(name)) {
          return name;
        } else {
          return null;
        }
      }
    });

    try {
      return mapper.writeValueAsString(obj);
    } catch (IOException e) {
      log.warn("write to json string error:" + obj, e);
      return null;
    }
  }

  /**
   * Deserialization POJO or simple Collection such as a List < String >.
   * <p>
   * if a JSON string is Null or "Null" string, returns Null. If a JSON string for "[]", returns an
   * empty set.
   * <p>
   * for deserialization complex Collection such as a List < MyBean >, please use the fromJson
   * (String, JavaType)
   *
   * @see #fromJson(String, JavaType)
   */
  public <T> T fromJson(String jsonString, Class<T> clazz) {
    if (StringUtils.isEmpty(jsonString)) {
      return null;
    }

    try {
      return mapper.readValue(jsonString, clazz);
    } catch (IOException e) {
      log.warn("parse json string error:" + jsonString, e);
      return null;
    }
  }

  /**
   * Deserialization complex Collection such as a List < Bean >, contructCollectionType () or
   * contructMapType () structure types, and then call the function.
   *
   * @see #createCollectionType(Class, Class...)
   */
  public <T> T fromJson(String jsonString, JavaType javaType) {
    if (StringUtils.isEmpty(jsonString)) {
      return null;
    }

    try {
      return mapper.readValue(jsonString, javaType);
    } catch (IOException e) {
      log.warn("parse json string error:" + jsonString, e);
      return null;
    }
  }

  public <T> T fromJson(String jsonString, TypeReference<T> valueTypeRef) {
    if (StringUtils.isEmpty(jsonString)) {
      return null;
    }

    try {
      return mapper.readValue(jsonString, valueTypeRef);
    } catch (IOException e) {
      log.warn("parse json string error:" + jsonString, e);
      return null;
    }
  }

  /**
   * To construct the Collection types.
   */
  public JavaType buildCollectionType(Class<? extends Collection> collectionClass,
                                      Class<?> elementClass) {
    return mapper.getTypeFactory().constructCollectionType(collectionClass, elementClass);
  }

  /**
   * Tectonic Map types.
   */
  public JavaType buildMapType(Class<? extends Map> mapClass, Class<?> keyClass,
                               Class<?> valueClass) {
    return mapper.getTypeFactory().constructMapType(mapClass, keyClass, valueClass);
  }

  /**
   * When contains only part of the Bean attribute in the JSON, update an existing Bean, cover only
   * that part of the property.
   */
  public void update(String jsonString, Object object) {
    try {
      mapper.readerForUpdating(object).readValue(jsonString);
    } catch (JsonProcessingException e) {
      log.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
    } catch (IOException e) {
      log.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
    }
  }

  /**
   * Output the json data format.
   */
  public String toJsonP(String functionName, Object object) {
    return toJson(new JSONPObject(functionName, object));
  }

  /**
   * Set whether to use Enum toString function to read and write Enum, False always use Enum name ()
   * function to read and write Enum, the default is False. Note that this function must be in
   * Mapper created, read and write all the call before action.
   */
  public void enableEnumUseToString() {
    mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
    mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
  }

  /**
   * Remove the Mapper further set up or use other serialization API.
   */
  public ObjectMapper getMapper() {
    return mapper;
  }
}