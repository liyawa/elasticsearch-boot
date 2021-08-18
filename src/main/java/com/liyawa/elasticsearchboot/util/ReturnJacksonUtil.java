package com.liyawa.elasticsearchboot.util;

/**
 * Copyright ©2016Renren. All rights reserved.
 */


import com.liyawa.elasticsearchboot.constant.StringConstants;
import com.liyawa.elasticsearchboot.enums.WebCodeEnum;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @Author xin.lu3
 * @Date 2021/8/16
 */
public class ReturnJacksonUtil {


  public static String resultOk(Locale locale) {
    WebCodeEnum webCodeEnum = WebCodeEnum.OK;
    return result(webCodeEnum.getErrorCode(), webCodeEnum.getErrorMsg(locale), null, null);
  }

  public static String resultOk() {
    WebCodeEnum webCodeEnum = WebCodeEnum.OK;
    return result(webCodeEnum.getErrorCode(), webCodeEnum.getErrorMsg(Locale.ENGLISH), null, null);
  }

  public static String resultOk(Object data, Locale locale) {
    WebCodeEnum webCodeEnum = WebCodeEnum.OK;
    return result(webCodeEnum.getErrorCode(), webCodeEnum.getErrorMsg(locale), data, null);
  }

  public static String resultOk(Object data) {
    WebCodeEnum webCodeEnum = WebCodeEnum.OK;
    return result(webCodeEnum.getErrorCode(), webCodeEnum.getErrorMsg(Locale.ENGLISH), data, null);
  }


  public static String resultOk(String key, Object value, Locale locale) {
    Map<String, Object> data = new HashMap<>();
    data.put(key, value);
    WebCodeEnum webCodeEnum = WebCodeEnum.OK;
    return result(webCodeEnum.getErrorCode(), webCodeEnum.getErrorMsg(locale), data, null);
  }

  public static String resultOk(String key, Object value) {
    Map<String, Object> data = new HashMap<>();
    data.put(key, value);
    return result(WebCodeEnum.OK, (Object) data);
  }

  public static String resultOkWithEnum(WebCodeEnum webCodeEnum, Locale locale) {
    return result(webCodeEnum.getErrorCode(), webCodeEnum.getErrorMsg(locale), null, null);
  }

  public static String resultOkWithExclude(Object data, Class clazz, Locale locale, String...
      excludeFileds) {
    Map<Class, Set<String>> exclude = new HashMap<Class, Set<String>>();
    exclude.put(clazz, new HashSet<String>(Arrays.asList(excludeFileds)));
    WebCodeEnum webCodeEnum = WebCodeEnum.OK;
    return result(webCodeEnum.getErrorCode(), webCodeEnum.getErrorMsg(locale), data, null, exclude);
  }

  public static String resultOkWithInclude(Object data, Class clazz, Locale locale, String...
      includeFileds) {
    Map<Class, Set<String>> include = new HashMap<Class, Set<String>>();
    include.put(clazz, new HashSet<String>(Arrays.asList(includeFileds)));
    WebCodeEnum webCodeEnum = WebCodeEnum.OK;
    return result(webCodeEnum.getErrorCode(), webCodeEnum.getErrorMsg(locale), data, include, null);
  }

  public static String resultWithFailed(WebCodeEnum webCodeEnum, Locale locale) {
    return result(webCodeEnum.getErrorCode(), webCodeEnum.getErrorMsg(locale), null, null);
  }

  public static String resultWithFailed(WebCodeEnum webCodeEnum) {
    return result(webCodeEnum.getErrorCode(), webCodeEnum.getErrorMsg(Locale.ENGLISH), null, null);
  }

  public static String resultFailed(Object msg) {
    return result(WebCodeEnum.BAD_REQUEST.getErrorCode(), String.valueOf(msg), null, null);
  }

  public static String resultFormat(WebCodeEnum errorCodeEnum, Object... args) {
    String msg = String.format(errorCodeEnum.getEnErrorMsg(), args);
    return result(errorCodeEnum.getErrorCode(), msg, (Object) null);
  }

  public static String result(WebCodeEnum errorCodeEnum) {
    return result(errorCodeEnum.getErrorCode(), errorCodeEnum.getErrorMsg(Locale.ENGLISH),null);
  }

  public static String result(WebCodeEnum errorCodeEnum, Object data) {
    return result(errorCodeEnum.getErrorCode(), errorCodeEnum.getErrorMsg(Locale.ENGLISH), data);
  }

  public static String result(int resultCode, String resultMsg, Object data) {
    return result(resultCode, resultMsg, data, (Class) null);
  }

  /**
   * According to the view of data objects in the fields in the customize output
   */
  public static <T> String result(int resultCode, String resultMsg, Object data, Class<T>
      jsonViewClazz) {

    Map<String, Object> status = new HashMap<>();
    status.put(StringConstants.RESULT_CODE, resultCode);
    status.put(StringConstants.RESULT_MSG, String.valueOf(resultMsg));

    Map<String, Object> result = new HashMap<>();
    result.put(StringConstants.RESULT_STATUS, status);
    if (null != data) {
      result.put(StringConstants.RESULT_DATA, data);
    }
    if (null == jsonViewClazz) {
      return JsonMapper.INSTANCE.toJson(result);
    } else {
      return JsonMapper.INSTANCE.toJson(result, jsonViewClazz);
    }
  }

  public static String result(int resultCode, String resultMsg, Object data, Map<Class,
      Set<String>> include, Map<Class, Set<String>> exclude) {

    Map<String, Object> status = new HashMap<String, Object>();
    status.put(StringConstants.RESULT_CODE, resultCode);
    status.put(StringConstants.RESULT_MSG, String.valueOf(resultMsg));

    Map<String, Object> result = new HashMap<String, Object>();
    result.put(StringConstants.RESULT_STATUS, status);
    if (null != data) {
      result.put(StringConstants.RESULT_DATA, data);
    }
    return JsonMapper.INSTANCE.toJson(result, include, exclude);
  }


  public static Map<String, Object> resultMap(String type, String label, Object data,
                                              String dataName) {
    Map<String, Object> result = new HashMap<>();
    if (StringUtils.isNotBlank(type)) {
      result.put("type", type);
    }
    if (StringUtils.isNotBlank(label)) {
      result.put("label", label);
    }
    result.put(StringUtils.isNotBlank(dataName) ? dataName : "data", data);
    return result;
  }

}

