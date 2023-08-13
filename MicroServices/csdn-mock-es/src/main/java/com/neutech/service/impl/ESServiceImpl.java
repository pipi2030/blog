package com.neutech.service.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neutech.entity.Post;
import com.neutech.service.ESService;
import com.neutech.utils.JiebaUtil;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ESServiceImpl implements ESService {
    @Resource
    private RestHighLevelClient client;
    @Value("${config.elasticsearch.index}")
    private String index;
    @Override
    public void createBulkDocument(List<Post> list) throws IOException {
        System.out.println(list.size());
        //构建批量插入的请求
        BulkRequest request = new BulkRequest();
        //批量插入请求设置
        for (int i = 0; i < list.size(); i++) {
            ObjectMapper mapper = new ObjectMapper();
            String postJson = mapper.writeValueAsString(list.get(i));
            request.add(
                    new IndexRequest(index)//设置索引
//                            .id(String.valueOf(i+1))//设置文档的id，如果没有指定，会随机生成，自己测试
                            .source(postJson, XContentType.JSON)//设置要添加的资源，类型为JSON
            );
        }
        BulkResponse response = client.bulk(request, RequestOptions.DEFAULT);
        if(!response.hasFailures()){
            System.out.println("批量插入成功");
        }else {
            System.out.println("批量插入失败");
        }
    }
    @Override
    public List<Post> query(String input) throws IOException {
        //1、构建搜索请求
        SearchRequest request = new SearchRequest(index);
        //2、设置搜索条件，使用该构建器进行查询
        SearchSourceBuilder builder = new SearchSourceBuilder();//生成构建器

        //查询条件我们可以用工具类QueryBuilders来构建
        //QueryBuilders.termQuery()：精确匹配
        //QueryBuilders.matchAllQuery()：全文匹配
        //QueryBuilders.wildcardQuery(): 模糊匹配
        //构建精确匹配查询条件
        //构建精确匹配查询条件
//        TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("title", JiebaUtil.getSingleWorld(input));
//        TermsQueryBuilder termsQueryBuilder = QueryBuilders.moreLikeThisQuery(new String[]{"title"},new String[]{input});
//        builder.query(termsQueryBuilder);
//        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
//        builder.query(matchAllQueryBuilder);

//      关键词先用jieba分词，然后结果模糊匹配取并集
        List<String> words = JiebaUtil.getSingleWorld(input);
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        //把搜索的标题关键词分词查询结果取并集合
        for (String word : words) {
            WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery("title.keyword", "*" + word + "*");
            boolQueryBuilder.should(wildcardQueryBuilder);
        }
        builder.query(boolQueryBuilder);
//        搜索内容
//        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("content.keyword",input);
//        builder.query(termQueryBuilder);
        //3、将搜索条件放入搜索请求中
        request.source(builder);
        request.source().size(1000);
        //4、客户端执行搜索请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
//      获取结果
        SearchHits hits = response.getHits();
        List<Post> posts = new ArrayList<>();
        for(SearchHit hit:hits){
            Post post = JSON.parseObject(hit.getSourceAsString(), Post.class);
            posts.add(post);
        }
        //5、打印测试
//        System.out.println(posts.size());
        return posts;
    }
    /**
     * 删除索引测试
     */
    @Override
    public void deleteIndex() throws IOException {
        //1、构建 删除索引请求
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        //2、客户段执行删除的请求
        AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);
        //3、打印
        System.out.println("是否删除成功："+response.isAcknowledged());
    }


}
