<template>
    <div>
        <el-row class="search"><!--搜索框-->
            <el-col :span="12" :offset="2"><el-input v-model="input"  placeholder="请输入搜索内容(不区分大小写)" clearable></el-input></el-col>
            <el-col :span="6" class="cBtn"><el-button type="danger" @click="searchList">C一下</el-button></el-col>
        </el-row>
        <el-row class="postContainter">
            <el-col :span="14" :offset="2">
                <div v-for="item in postData" class="postList">
                    <el-button @click="handleTitle(item.postId)" type="text"><span class="title">{{item.title}}</span></el-button>
                    <p class="content">{{item.content}}</p>
                    <el-row class="details">
                        <el-col :xs="8" :sm="6" :md="4" :lg="8" :xl="3" class="leftStatics" >
                            <span class="el-icon-view">{{item.readingCount}}</span>
                            <span class="iconfont icon-dianzan">{{item.likes}}</span>
                            <span class="el-icon-star-off">{{item.collections}}</span>
                        </el-col>
                        <el-col :xs="8" :sm="6" :md="8" :lg="8" :xl="11" :offset="6" class="rightStatics">
                            <span>{{item.user.userName}}&nbsp;&nbsp;{{item.createTime |  datefmt('YYYY-MM-DD')}}</span>
                        </el-col>
                    </el-row>
                    <el-divider></el-divider>
                </div>
                <el-empty v-if="isFind" description="没有找到搜索内容"></el-empty>
            </el-col>
        </el-row>
        <el-button class="el-icon-circle-plus addPost" type="danger" @click="editor">发布文章</el-button>
    </div>
</template>

<script>
    import EventBus from "../../assets/js/EventBus"
    import Cookies from 'js-cookie'
    export default {
        name: "CreationView",
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
            }
        },
        methods:{
            handleTitle(id){
                this.$router.push(`/index/postDetail/${id}`);
            },
            searchList(){
                let currentInput = this.input.replace('/\s*/gi','').toLowerCase();
                let tempList = [];
                this.showData.forEach((post)=>{
                    if(post.title.toLowerCase().includes(currentInput) || post.content.toLowerCase().includes(currentInput)){
                        if(tempList.indexOf(post) == '-1'){
                            tempList.push(post);
                        }
                    }
                });
                this.postData = tempList;
            },
            editor(){
                this.$router.push("/index/creation/editor");
            }
        },
        created(){
            this.axios.get("/api-db/getCreation",{params:{
                    // userId:this.$store.state.userData.userId
                    // userId:this.$store.state.userData.userId
                    userId:Cookies.get("userId")
                }
            })
                .then((e)=>{
                    this.postData = e.data;
                    this.showData = this.postData;
                })
                .catch((error)=>{
                    console.log(error);
                });
        },
        watch:{
            postData(){
                if (this.postData.length === 0){
                    this.isFind = true;
                }
            }
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
    .title{
        text-align: left;
    }
    .statics span{
        margin: 20px;
    }
    .postContainter{
        height: 500px;
        background-color: #f4f4f4;
    }
    .postList{
        background-color: white;
        padding: 5px 20px 2px 20px;
    }
    .title{
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
    .addPost{
        position: fixed;
        top: 160px;
        right: 170px;
    }
</style>