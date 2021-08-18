package com.liyawa.elasticsearchboot.controller;

import com.liyawa.elasticsearchboot.service.DocumentService;
import com.liyawa.elasticsearchboot.util.ReturnJacksonUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author xin.lu3
 * @Date 2021/8/16
 */
@RestController("/doc")
public class DocController {
  @Autowired
  DocumentService documentService;

  @PostMapping("/create-doc")
  public String createDoc() throws Exception{
    String document = documentService.createDocument();
    return ReturnJacksonUtil.resultOk(document);
  }

}
