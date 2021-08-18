package com.liyawa.elasticsearchboot.controller;

import com.liyawa.elasticsearchboot.model.WebSiteInfo;
import com.liyawa.elasticsearchboot.service.SearchService;
import com.liyawa.elasticsearchboot.util.ReturnJacksonUtil;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author xin.lu3
 * @Date 2021/7/31
 */
@RestController("/search")
public class SearchRestController {

  @Autowired
  SearchService searchService;

  @PostMapping("/simple-search")
  @ResponseBody
  public String simpleSearch() throws Exception{
    List<String> list = searchService.simpleSearch();
    return ReturnJacksonUtil.resultOk(list);
  }

  @GetMapping("/simple-match")
  public String simpleMatchSearch() throws Exception{
    List<String> list = searchService.simpleMatchSearch();
    return ReturnJacksonUtil.resultOk(list);
  }

}
