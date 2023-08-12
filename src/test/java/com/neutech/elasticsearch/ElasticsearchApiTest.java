package com.neutech.elasticsearch;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neutech.entity.Post;
import com.neutech.service.PostService;
import net.minidev.json.JSONObject;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ElasticsearchApiTest {
    @Autowired
    private RestHighLevelClient client;
    /**
     * 创建索引测试
     */
    @Test
    void createIndex() throws IOException {
        //1、构建 创建索引的请求
        CreateIndexRequest request = new CreateIndexRequest("test");//索引名
        //2、客户端执行请求,获取响应
        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
        //3、打印
        System.out.println("创建成功，创建的索引名为：" + response.index());
    }
    /**
     * 获取索引测试
     */
//    @Test
//    void getIndex() throws IOException {
//        //1、构建 获取索引的请求
//        GetIndexRequest request = new GetIndexRequest("text");
//        //2、客户端判断该索引是否存在
//        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
//        //3、打印
//        System.out.println("该索引是否存在："+exists);
//    }
    /**
     * 删除索引测试
     */
    @Test
    void deleteIndex() throws IOException {
        //1、构建 删除索引请求
        DeleteIndexRequest request = new DeleteIndexRequest("test");
        //2、客户段执行删除的请求
        AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);
        //3、打印
        System.out.println("是否删除成功："+response.isAcknowledged());
    }
    /**
     * 创建文档
     */
    @Test
    void createDocument() throws IOException {
//        User user = new User().setId(1).setUsername("张三");
        //1、构建请求
        IndexRequest request = new IndexRequest();
        //2、设置规则  PUT /user_index/user/_doc/1
        request.index("post_index").id("1");//设置id
        Post post = new Post().setTitle("es创建文档测试").setContent("666");
//        request.timeout(TimeValue.timeValueSeconds(1));//设置超时时间
//        3、将数据放入到请求中,以JSON的格式存放
        ObjectMapper mapper = new ObjectMapper();
//        Map<String, Object> postMap = objectMapper.convertValue(post, Map.class);
//        String jsonString = JSONObject.toJSONString(postMap);
        String postJson = mapper.writeValueAsString(post);
        request.source(postJson, XContentType.JSON);

        //4、客户端发送请求,获取响应结果
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);

        //5、打印
        System.out.println("响应结果："+response.toString());
        client.close();
    }
    /**
     * 批量插入数据
     */
    @Test
    void createBulkDocument() throws IOException {
        //构建批量插入的请求
        BulkRequest request = new BulkRequest();
        //设置超时时间
        request.timeout("10s");

        //设置数据
        List<Post> list = new ArrayList<>();
        list.add(new Post().setPostId(5).setTitle("5").setContent("666"));
        list.add(new Post().setPostId(6).setTitle("6").setContent("666"));
        list.add(new Post().setPostId(7).setTitle("7").setContent("666"));
        list.add(new Post().setPostId(8).setTitle("8").setContent("666"));

        //批量插入请求设置
        for (int i = 0; i < list.size(); i++) {
            ObjectMapper mapper = new ObjectMapper();
            String postJson = mapper.writeValueAsString(list.get(i));
            request.add(
                    new IndexRequest("test")//设置索引
                            .id(String.valueOf(i+1))//设置文档的id，如果没有指定，会随机生成，自己测试
                            .source(postJson, XContentType.JSON)//设置要添加的资源，类型为JSON
            );
        }
        BulkResponse response = client.bulk(request, RequestOptions.DEFAULT);
        System.out.println("批量插入是否失败："+response.hasFailures());
    }
    /**
     * 查询
     */
    @Test
    void query() throws IOException {
        //1、构建搜索请求
        SearchRequest request = new SearchRequest("post_index");

        //2、设置搜索条件，使用该构建器进行查询
        SearchSourceBuilder builder = new SearchSourceBuilder();//生成构建器

        //查询条件我们可以用工具类QueryBuilders来构建
        //QueryBuilders.termQuery()：精确匹配
        //QueryBuilders.matchAllQuery()：全文匹配

        //构建精确匹配查询条件
        //构建精确匹配查询条件
//        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("content.keyword", "666");
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
//        WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery("username", "张");
//        builder.query(termQueryBuilder);
        builder.query(matchAllQueryBuilder);

        //3、将搜索条件放入搜索请求中
        request.source(builder);
        request.source().size(1000);  //改变期望获得文档的大小
        //4、客户端执行搜索请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        //5、打印测试
        SearchHit[] hits = response.getHits().getHits();
        System.out.println("共查询到"+hits.length+"条数据");
//        System.out.println("查询结果：");
//        for (int i = 0; i < hits.length; i++) {
//            System.out.println(hits[i].getSourceAsString());
//        }
        System.err.println(hits);
    }


}
