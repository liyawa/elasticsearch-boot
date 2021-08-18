package com.liyawa.elasticsearchboot.enums;

import java.util.Locale;

/**
 * @Author xin.lu3
 * @Date 2021/8/16
 */
public enum WebCodeEnum {

  OK(0, "success"),
  BAD_REQUEST(100004, "Bad Request");
  private int errorCode;

  private String enErrorMsg;

  WebCodeEnum(int errorCode, String enErrorMsg) {
    this.errorCode = errorCode;
    this.enErrorMsg = enErrorMsg;
  }

  public int getErrorCode() {
    return errorCode;
  }

  public String getEnErrorMsg() {
    return enErrorMsg;
  }

  public String getErrorMsg(Locale locale) {
    return enErrorMsg;
  }
}
