# 这是一个博客项目

技术栈：SpringBoot + Vue + ELementUI + MySQL + Elasticsearch + Jieba + Redis + Dubbo + Nacos + Apisix

主要功能：该项目具有查询博客列表，查看博客详情页，发表博客，点赞收藏评论回复，用户登陆等功能

文件结构：[MicroServices](https://github.com/pipi2030/blog/tree/master/MicroServices)文件夹为后端代码、[csdn_mock](https://github.com/pipi2030/blog/tree/master/csdn_mock)文件夹为前端代码

## 页面展示

1.搜索博客

![博客列表](https://img1.imgtp.com/2023/08/13/znEGCMav.png)

2.博客详情页

![博客详情页](https://img1.imgtp.com/2023/08/13/O4TXHsFY.jpg)

3.登陆界面

![用户登陆.jpg](../../../Typora文档/imgs/fNzxLeuj-16919235467264.jpg)

评论界面

![点赞收藏评论效果.jpg](../../../Typora文档/imgs/cz3VCtNO.jpg)



- 为了解决查询速率慢，查询效果不理想的问题，使用了 Elasticseach 技术配合 JieBa 中文分词，使得检

索结果更人性化，查询效率得到了提高。同时为了应对大批量的查询，使用了 Redis 缓存技术。

- 为了方便项目的快速迭代部署，尝试使用了微服务技术。使用了 RPC 框架 Dubbo，注册中心 Nacos

与云网关 Apisix，将项目分成了基于 ES 和基于 Mysql 数据库的两个服务。



