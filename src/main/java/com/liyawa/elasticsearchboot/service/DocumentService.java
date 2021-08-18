package com.liyawa.elasticsearchboot.service;

import com.google.common.collect.Maps;

import com.liyawa.elasticsearchboot.util.JsonUtil;

import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author xin.lu3
 * @Date 2021/8/16
 */
@Service
public class DocumentService {
  @Autowired
  RestHighLevelClient client;

  public String createDocument() throws Exception{
    IndexRequest request = new IndexRequest("whatever");
    Map<String,Object> docMap = Maps.newHashMap();
    docMap.put("user_name","whatever.lu");
    docMap.put("user_age","18");
    docMap.put("user_sex","womam");
    request.source(JsonUtil.toJson(docMap), XContentType.JSON);
    request.opType(DocWriteRequest.OpType.INDEX);//不主动设置id，否则使用create
    IndexResponse response = client.index(request, RequestOptions.DEFAULT);
    DocWriteResponse.Result result = response.getResult();
    String resultLowercase = result.getLowercase();
    return resultLowercase;
  }
}
