package com.liyawa.elasticsearchboot.controller;

import com.liyawa.elasticsearchboot.service.IndexService;
import com.liyawa.elasticsearchboot.util.ReturnJacksonUtil;

import org.elasticsearch.client.indices.GetFieldMappingsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author xin.lu3
 * @Date 2021/8/16
 */
@RestController("/index")
public class IndexRestController {
  @Autowired
  IndexService indexService;

  @PostMapping("/create-index")
  @ResponseBody
  public String createIndex() throws Exception{
    String index = indexService.createIndex();
    return ReturnJacksonUtil.resultOk(index);
  }

  @PutMapping("/create-mapping")
  public String putMapping() throws Exception{
    Boolean mapping = indexService.putMapping();
    return ReturnJacksonUtil.resultOk(mapping);
  }

  @PostMapping("/create-alias")
  public String createAlias() throws Exception{
    Boolean alias = indexService.createAlias();
    return ReturnJacksonUtil.resultOk(alias);
  }

  @GetMapping("/fields-mapping")
  public String getFieldMapping() throws Exception{
    List<String> fieldMapping = indexService.getFieldMapping();
    return ReturnJacksonUtil.resultOk(fieldMapping);
  }
}
