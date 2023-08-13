<template>
    <div class="app">
        <el-row class="search"><!--搜索框-->
            <el-col :span="12" :offset="2"><el-input v-model="input"  placeholder="请输入搜索内容(不区分大小写)" clearable></el-input></el-col>
            <el-col :span="6" class="cBtn"><el-button type="danger" @click="getPostList">C一下</el-button></el-col>
        </el-row>
        <el-row class="postContainter">
            <el-col :span="14" :offset="2">
                <div v-for="item in postData" class="postList">
                    <el-button @click="handleTitle(item.postId)" type="text"><span class="title">{{item.title || "无标题"}}</span></el-button>
                    <p class="content">{{item.content}}</p>
                    <el-row class="details">
                        <el-col :xs="8" :sm="6" :md="4" :lg="8" :xl="3" class="leftStatics" >
                            <span class="el-icon-view">{{item.readingCount}}</span>
                            <span class="iconfont icon-dianzan">{{item.likes}}</span>
                            <span class="el-icon-star-off">{{item.collections}}</span>
                        </el-col>
                        <el-col :xs="8" :sm="6" :md="8" :lg="8" :xl="11" :offset="6" class="rightStatics">
                            <span>&nbsp;&nbsp;{{item.createTime |  datefmt('YYYY-MM-DD')}}</span>
                        </el-col>
                    </el-row>
                    <el-divider></el-divider>
                </div>
                <!--<el-empty v-if="isFind" description="没有找到搜索内容"></el-empty>-->
            </el-col>
        </el-row>
        <!--&lt;!&ndash;分页&ndash;&gt;-->
        <!--<div class="pagination">-->
            <!--<el-pagination-->
                    <!--background-->
                    <!--:current-page="currentPage"-->
                    <!--layout="prev, pager, next"-->
                    <!--:total="total"-->
                    <!--:page-size="pageSize"-->
                    <!--@current-change="handleCurrentChange">-->
            <!--</el-pagination>-->
        <!--</div>-->
    </div>
</template>

<script>
    import EventBus from "../../assets/js/EventBus"
    import Cookies from 'js-cookie'
    export default {
        name: "postListView",
        data(){
            return{
                input:'',
                postData:[{
                    postId:0,
                    title:'',
                    tags:'',
                    content:'',
                    createTime:'',
                    updateTime:'',
                    user:{
                      userId:0,
                      userName:'',
                      pass:'',
                    },
                    readingCount:0,
                    likes:0,
                    collections:0,
                }],
                showData:[],
                isFind:false,
                currentPage:1,
                total:100,
                pageSize:10,
                userId:0
            }
        },
        methods:{
            handleTitle(id){
                this.$router.push(`/index/postDetail/${id}`);
            },
            getPostList(){
                //接收搜索框内的关键词，进行分页查询
                this.axios.get("/api-es/post" + "?input=" + this.input + "&page=" + this.currentPage + "&userId=" + this.userId)
                    .then((e)=>{
                    // //    分页
                    //     this.postData = e.data.records;
                    //     this.total = e.data.total;
                    //     this.pageSize = e.data.size;

                    //不分页
                        this.postData = e.data;
                        Cookies.set("input",this.input,{expires : 1 / 24 / 60});
                    })
                    .catch((error)=>{
                        console.log(error);
                    });
            },
            handleCurrentChange(currentPage){
                this.currentPage = currentPage;
                this.getPostList();
            }
        },
        created(){
            if(typeof (Cookies.get("input")) != "undefined"){
                this.input = Cookies.get("input");
            }
            if(typeof (Cookies.get("userId")) != "undefined"){
                this.userId = Cookies.get("userId");
            }
            this.getPostList();
            // this.searchList();
        },
        destroyed(){
            Cookies.remove('input');
        },
        watch:{
        },
        computed:{
        }
    }
</script>

<style scoped>
    .cBtn{
        text-align: left;
        margin-left: 10px;
    }
    .statics span{
        margin: 20px;
    }
    .postContainter{
        background-color: #f4f4f4;
    }
    .postList{
        background-color: white;
        padding: 5px 20px 2px 20px;
    }
    .title{
        text-align: left;
        font-size: 18px;
        font-family: PingFang SC,Hiragino Sans GB,Arial,Microsoft YaHei,Verdana,Roboto,Noto,Helvetica Neue,sans-serif;
    }
    .content{
        font-size: 14px;
        font-family: PingFang SC,Hiragino Sans GB,Arial,Microsoft YaHei,Verdana,Roboto,Noto,Helvetica Neue,sans-serif;
        /*显示两行*/
        overflow:hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
    }
    .details{
        font-size: 17px;
    }
    .search{
        margin: 20px;
    }
    .leftStatics span{
        padding-right: 20px;
    }
    .rightStatics{
        text-align: right;
    }
    .rightStatics span{
        padding-left: 20px;
    }
    .app {
        height: 80vh;
    }
    .pagination{
        margin: 20px 60px;
    }


</style>