<template>
    <div>
        <!--帖子主体-->
        <el-row :gutter="20" >
            <el-col :span="18":offset="2" class="container">
                <p class="title">{{postData.title}}</p>
                <el-card class="box-card">
                    <el-row>
                        <i class="iconfont icon-yuanchuang1"></i>
                        <span>置顶&nbsp;{{postData.user.userName}}&nbsp;
                            <i class="iconfont icon-exchange-full"></i>已于{{postData.updateTime |  datefmt('YYYY-MM-DD')}}修改&nbsp;
                            <i class="el-icon-view"></i>{{postData.readingCount}}&nbsp;
                            <i class="iconfont icon-shoucang"></i>收藏{{postData.collections}}
                            <span>分类专栏:</span> <el-tag>{{postData.tags}}</el-tag>
                        </span>
                    </el-row>
                </el-card>
                <p class="content">{{postData.content}}</p>
                <el-card class="intercationList">
                    <el-row>
                        <el-col :span="10">
                            <el-avatar size="small" :src=postData.user.headImage></el-avatar>
                            <span>{{postData.user.userName}}</span>&nbsp;
                            <el-button type="danger" round>关注</el-button>
                        </el-col>
                        <el-col :span="8" :offset="4" class="funList">
                            <el-button  type="text" @click="handleLikeBtn">
                                <i class="iconfont icon-dianzan2" v-if="!isLike"></i>
                                <i class="iconfont icon-yidianzan1" v-if="isLike"></i>
                                点赞{{postData.likes}}
                            </el-button>
                            <el-button  type="text" @click="handleCollectionBtn">
                                <i class=" iconfont icon-shoucang1" v-if="!isCollection"></i>
                                <i class="iconfont icon-yishoucang" v-if="isCollection"></i>
                                收藏{{postData.collections}}
                            </el-button>
                            <el-button  type="text" @click="getCommentList();showCommentList = true">
                                <i class="iconfont icon-pinglun" ></i>
                                评论 {{commentCount}}
                            </el-button>
                        </el-col>
                    </el-row>
                </el-card>
            </el-col>
        </el-row>
        <!--评论区-->
        <el-drawer
                title="标题"
                :visible.sync="showCommentList"
                direction="rtl"
                :with-header="false">
            <div class="comments">
                <span>评论&nbsp;{{postData.commentCount}}</span>
                <i class="el-icon-close closeCommentsBtn" @click="closeComments"></i>
                <el-divider/>
                <!--编辑个人评论-->
                <el-row :gutter="10"  class="myComment">
                    <el-col :span="2">
                        <el-avatar size="medium" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"></el-avatar>
                    </el-col>
                    <el-col :span="14" :offset="1">
                            <el-input
                                placeholder="输入评论..."
                                v-model="comments">
                            </el-input>
                    </el-col>
                    <el-col :span="4" :offset="2"><el-button round type="danger" class="commentBtn" @click="addComment">评论</el-button></el-col>
                </el-row>
                <!--评论列表-->
                <div v-for="(comment,index) in commentRootData">
                    <el-row :gutter="10">
                        <el-col :span="2">
                            <el-avatar size="medium" src="https://img.wxcha.com/m00/90/a5/d1d167451213c94da52f7ddf31d8da3b.jpg" class="avatar"></el-avatar>
                        </el-col>
                        <el-col :span="18" :offset="1">
                            <span class="userName">{{comment.user.userName}}</span>
                            <span class="commentCreateTime">{{comment.createTime | datefmt('YYYY.MM.DD')}}</span>
                            <el-button class="iconfont icon-dianzan1 commentLikeBtn" type="text" ></el-button>
                            <el-button type="text" @click="showReply === index?showReply = -1:showReply = index" class="replyBtn">{{showReply === index?"收起":"回复"}}</el-button>
                            <span class="commentContent">{{comment.commentContent}}</span>
                        </el-col>
                    </el-row>
                    <!--回复的编辑框-->
                    <el-row v-if="showReply === index">
                        <el-col :span="14" :offset="3">
                            <el-input
                                    placeholder="回复:徐阳"
                                    v-model="comments">
                            </el-input>
                        </el-col>
                        <el-col :span="4" :offset="2"><el-button round type="danger" class="commentBtn" @click="addComment">评论</el-button></el-col>
                    </el-row>
                    <br/>
                    <!--帖子回复-->
                    <div v-for="reply in selectChildComment(comment,commentChildData)">
                        <el-row :gutter="10">
                            <el-col :span="2" :offset="3"><!--用户头像-->
                                <el-avatar size="small" src="https://img.wxcha.com/m00/c3/3c/90dc57044c2661b42aa62b08a452b81c.jpg" class="avatar"></el-avatar>
                            </el-col>
                            <el-col :span="15"><!--用户名-->
                                <span class="userName">{{reply.user.userName}}</span>
                                <span class="commentCreateTime">{{reply.createTime | datefmt('YYYY.MM.DD')}}</span>
                                <el-button class="iconfont icon-dianzan1 commentLikeBtn" type="text"></el-button><el-button type="text" class="replyBtn">回复&nbsp;</el-button>
                                <span class="commentContent">{{reply.commentContent}}</span>
                            </el-col>
                        </el-row>
                    </div>
                    <el-divider/>
                </div>
            </div>
        </el-drawer>
    </div>
</template>

<script>
    import EventBus from "../../assets/js/EventBus";
    import Cookies from 'js-cookie';
    export default {
        name: "postDetailView",
        data() {
            return {
                postData: {
                    postId: '3',
                    title: '列存数据仓库怎样更高效',
                    tags: 'Spring',
                    content: '结构化数据的编码方式一般都不会非常紧凑，常常还有一定的可压缩余地。数据仓库通常会在列存的基础上对数据进行压缩，在物理上减少数据存储量，从而减少读取时间，提高性能。数据表相同字段的数据类型一般都是一样的，甚至有些情况取值都很接近，这样的一批数据通常会有较好的压缩率。列存是将相同字段值存储在一起的，所以比行存更有利于数据压缩。\n' +
                        '\n' +
                        '但是，通用的压缩算法不能假定数据有某种特征，只能将数据当作随意的字节流去编码，有时并不能获得最好的压缩率。而且，高压缩率的算法压缩出来的数据，解压缩时常常会增加CPU的运算量，消耗更多的时间。这部分多消耗的时间，甚至会大于压缩节省的硬盘读取时间，得不偿失。\n' +
                        '\n' +
                        '如果我们先对数据做一些处理，人为地制造某些数据特征来利用，再配合压缩算法，就可以实现较高的压缩率，同时保持较低的CPU消耗。\n',
                    createTime: '2023/4/20 14:30:25',
                    updateTime: '2023/4/20 14:30:25',
                    readingCount:22,
                    likes:33,
                    collections:43,
                    user: {
                        userId: 1,
                        userName: '徐阳',
                        pass: '123333',
                        headImage:'https://empty',
                    },
                },
                isLike:false,
                isCollection:false,
                showCommentList:false,
                comments:'',
                //myCommentData里放置将要发给服务器的数据
                myCommentData:{
                    userId:1,
                    postId:this.$route.params.id,
                    commentContent:this.comments,
                    createTime:new Date(),
                    user:{
                        userName:"徐阳",
                    }
                },
                commentData:[{
                    commentId:0,
                    commentContent:'',
                    createTime:'',
                    postId:'',
                    userId:'',
                    replyToCommentId:'',
                    user:{
                        userId:0,
                        userName:'',
                        pass:'',
                        headImage:'https://empty',
                    },
                    likes:0,
                }],
                commentCount:0,
                showReply:-1,//是否显示回复的编辑框 默认不显示
                commentRootData:[],//所有没有被回复的评论
                commentChildData:[]//跟帖
            }
        },
        methods:{
            handleLikeBtn(){
                if(!this.isLike){
                    this.isLike = true;
                    this.postData.likes += 1;
                }else{
                    this.isLike = false;
                    this.postData.likes -= 1;
                }
            },
            handleCollectionBtn(){
                if(!this.isCollection){
                    this.isCollection = true;
                    this.postData.collections += 1;
                }else{
                    this.isCollection = false;
                    this.postData.collections -= 1;
                }
            },
            handleStatics(){
                if(this.isLike){
                    this.axios.get('/api-db/post/addLikes',{params:{
                            postId:this.postData.postId,
                        }})
                        .then(
                        )
                        .catch((Error)=>{
                            console.log(Error)
                        })
                }
                if(this.isCollection){
                    this.axios.get('/api-db/post/addCollections',{params:{
                            postId:this.postData.postId,
                        }})
                        .then(
                        )
                        .catch((Error)=>{
                            console.log(Error)
                        })
                }
            },
            closeComments(){
                this.showCommentList = false;
            },
            getCommentList(){//打开评论区时进行查询数据库的操作
                this.axios.post("/api-db/comments",this.$qs.stringify({
                    postId:this.postData.postId
                })).then((e)=>{
                    this.commentRootData = [];
                    this.commentChildData = [];
                    this.commentData = e.data;
                    //对评论数据进行进一步处理，方便在前端真实的展示（具有回复的层次感）
                    //具体来说，获得两种数据，一个是没有回复的，一个是带有回复的，
                    // 没有回复的直接展示，对于有回复的，得把回复内容跟上去
                    this.commentData.forEach((comment)=>{
                        if(comment.replyToCommentId === null){
                            this.commentRootData.push(comment);
                        }
                        else{
                            this.commentChildData.push(comment);
                        }
                    })
                })
                    .catch((Error)=>{
                        console.log(Error)
                    })
            },
            getPost(){this.axios.post('/api-db/getPost',this.$qs.stringify({
                postId: this.$route.params.id,//当前路径参数里的id赋予axios
            }))
                .then((e)=>{
                    this.postData = e.data;
                })
                .catch((Error)=>{
                    console.log(Error)
                })},
            getCommentCount(){
                this.axios.get('/api-db/getCountOfComments',{params:{
                        postId:this.$route.params.id
                    }})
                    .then((count)=>{
                        this.commentCount = count.data;
                    })
                    .catch((e)=>{
                        console.log(e)
                    })
            },
            addComment(){
                let currentTime = new Date();
                if(this.comments !== ''){
                    this.$axios({//400报错了，修改了前端请求头
                        method:"post",
                        url:"/api-db/addComments",
                        data:{
                            userId:1,
                            postId:this.postData.postId,
                            commentContent:this.comments,
                            createTime:currentTime,
                        }
                    })
                        .then(()=>{
                            this.$message({
                                message: '评论成功',
                                type: 'success'
                            });
                            this.getCommentList();//重新加载评论列表
                            this.getCommentCount();
                            this.comments = '';
                        })
                        .catch((Error)=>{
                            console.log(Error)
                        });
                }
            },
            selectChildComment(rootComment,commentChildData){
                let childComment = commentChildData.filter((comment)=>{
                    return (comment.replyToCommentId === rootComment.commentId);
                });
                return childComment;
            },
        },
        created(){
            this.getPost();
            this.getCommentCount();
        },
        destroyed(){
            this.handleStatics();
        }
    }
</script>

<style scoped>
    .icon-yuanchuang1{
        color: red;
    }
    .box-card{
        font: 12px Extra Small;
        color: grey;
        margin: 20px;
    }
    .container{
        background-color: white;
    }
    .title{
        padding: 10px;
        font:20px Extra large bold;
    }
    .content{
        font: 14px Base;
        line-height: 50px;
        margin: 20px;
    }
    .intercationList{
        margin: 10px 0px;
        width: 100%;
    }
    .commentBtn{
        float: right;
    }
    .closeCommentsBtn{
        float: right;
    }
    .comments{
        margin: 10px;
    }
    .myComment{
        padding: 10px 0px;
        margin: 10px 0px;
        background-color: #FAFBFC;
    }
    .userName{
        text-align: left;
        font: 21px Extra large;
        font-weight: bold;
    }
    .commentCreateTime{
        padding-left: 10px;
        font: 8px Extra Small;
    }
    .commentContent{
        display: block;
        font: 20px large;
    }
    .replyBtn,.commentLikeBtn{
        float: right;
    }
</style>