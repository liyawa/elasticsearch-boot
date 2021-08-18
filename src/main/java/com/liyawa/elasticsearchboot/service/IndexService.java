package com.liyawa.elasticsearchboot.service;

import com.google.common.collect.Maps;

import com.liyawa.elasticsearchboot.util.JsonUtil;

import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetFieldMappingsRequest;
import org.elasticsearch.client.indices.GetFieldMappingsResponse;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author xin.lu3
 * @Date 2021/8/16
 */
@Service
public class IndexService {
  @Autowired
  RestHighLevelClient client;

  public String createIndex() throws Exception{
    CreateIndexRequest request = new CreateIndexRequest("whatever");
    CreateIndexResponse createIndexResponse = client.indices().create(request,
        RequestOptions.DEFAULT);
    String index = createIndexResponse.index();
    return index;
  }

  public Boolean putMapping() throws Exception{
    PutMappingRequest request = new PutMappingRequest("whatever");
    Map<String,Object> jsonMap = Maps.newHashMap();
    Map<String,Object> properties = Maps.newHashMap();
    Map<String,Object> message = Maps.newHashMap();
    message.put("type","text");
    properties.put("message",message);
    jsonMap.put("properties",properties);
    request.source(JsonUtil.toJson(jsonMap), XContentType.JSON);
    request.setTimeout(TimeValue.timeValueMinutes(2));
    request.setMasterTimeout(TimeValue.timeValueMinutes(1));
    AcknowledgedResponse acknowledgedResponse = client.indices().putMapping(request,
        RequestOptions.DEFAULT);
    boolean acknowledged = acknowledgedResponse.isAcknowledged();
    return acknowledged;
  }

  public Boolean createAlias() throws Exception{
    IndicesAliasesRequest request = new IndicesAliasesRequest();
    IndicesAliasesRequest.AliasActions aliasActions =
        new IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.AliasActions.Type.ADD)
        .index("whatever")
        .alias("ever");
    request.addAliasAction(aliasActions);
    AcknowledgedResponse acknowledgedResponse = client.indices().updateAliases(request,
        RequestOptions.DEFAULT);
    boolean acknowledged = acknowledgedResponse.isAcknowledged();
    return acknowledged;
  }

  public List<String> getFieldMapping() throws Exception{
    GetFieldMappingsRequest request = new GetFieldMappingsRequest();
    request.indices("whatever");
    request.fields("message");
    request.indicesOptions(IndicesOptions.lenientExpandOpen());
    GetFieldMappingsResponse fieldMapping = client.indices().getFieldMapping(request,
        RequestOptions.DEFAULT);
    Map<String, Map<String, GetFieldMappingsResponse.FieldMappingMetadata>> mappings =
        fieldMapping.mappings();
    Collection<Map<String, GetFieldMappingsResponse.FieldMappingMetadata>> values =
        mappings.values();
    List<List<String>> collectList = mappings.values().stream()
        .map(mappingMaps ->
            mappingMaps.values().stream()
                .map(mapping -> mapping.fullName()).collect(Collectors.toList()))
        .collect(Collectors.toList());
    List<String> collect =
        collectList.stream().flatMap(Collection::stream).collect(Collectors.toList());
    return collect;
  }
}
