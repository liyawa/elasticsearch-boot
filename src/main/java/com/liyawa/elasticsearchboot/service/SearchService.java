package com.liyawa.elasticsearchboot.service;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author xin.lu3
 * @Date 2021/8/16
 */
@Service
public class SearchService {
  @Autowired
  RestHighLevelClient searchClient;

  public List<String> simpleSearch() throws Exception{
    SearchRequest searchRequest = new SearchRequest();
    searchRequest.indices("whatever");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(QueryBuilders.matchAllQuery());
    searchRequest.source(searchSourceBuilder);
    SearchResponse searchResponse = searchClient.search(searchRequest, RequestOptions.DEFAULT);
    List<String> collect = Arrays.stream(searchResponse.getHits().getHits())
        .map(hit -> hit.getSourceAsString()).collect(Collectors.toList());
    return collect;

  }

  public List<String> simpleMatchSearch() throws Exception{
    SearchRequest searchRequest = new SearchRequest();
    searchRequest.indices("whatever");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    QueryBuilder matchQueryBuilder =
        QueryBuilders.matchQuery("user_name", "whatever lu").fuzziness(Fuzziness.ONE);
    searchSourceBuilder.query(matchQueryBuilder);
    searchRequest.source(searchSourceBuilder);
    SearchResponse searchResponse = searchClient.search(searchRequest, RequestOptions.DEFAULT);
    List<String> collect = Arrays.stream(searchResponse.getHits().getHits())
        .map(hit -> hit.getSourceAsString()).collect(Collectors.toList());
    return collect;
  }
}
