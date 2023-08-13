<template>
    <div>
        <el-row class="funList">
            <el-col :span="4">
                <el-button class="el-icon-arrow-left backToLastBtn" type="text" size="medium" @click="backToLastBtn">文章管理</el-button>
            </el-col>
            <el-col :span="10" >
                <el-input
                        type="text"
                        placeholder="[无标题]"
                        v-model="title"
                        maxlength="20"
                        show-word-limit
                ></el-input>
            </el-col>
            <el-col :span="4" :offset="1">
                <el-button type="danger" plain round><span>保存草稿</span></el-button>
                <el-button type="danger" round @click="publishArticle"><span>发布文章</span></el-button>
            </el-col>
            <el-col :span="4"><el-avatar :src="$store.state.userData.headImage"></el-avatar></el-col>
        </el-row>
        <mavon-editor
                class="mavonEditor"
                :toolbars="toolbars"
                v-model="articleContent"
        ></mavon-editor>
    </div>
</template>
<script>
    export default {
        name: "WritePostView",
        data(){
            return {
                title:'',
                articleContent: '',
                toolbars: {//markdown编辑器配置
                    bold: true, // 粗体
                    italic: true, // 斜体
                    header: true, // 标题
                    underline: true, // 下划线
                    mark: true, // 标记
                    superscript: true, // 上角标
                    quote: true, // 引用
                    ol: true, // 有序列表
                    link: true, // 链接
                    imagelink: true, // 图片链接
                    help: true, // 帮助
                    code: true, // code
                    subfield: false, // 是否需要分栏
                    fullscreen: true, // 全屏编辑
                    readmodel: true, // 沉浸式阅读
                    undo: true, // 上一步
                    trash: true, // 清空
                    save: true, // 保存（触发events中的save事件）
                    navigation: true // 导航目录
                },
            }
        },
        methods:{
            publishArticle(){
                if(this.title === ''){
                    this.$message({
                        message:"标题不可为空",
                        type:"error"
                    });
                    return;
                }
                if(this.articleContent === ''){
                    this.$message({
                        message:"编辑内容不可为空",
                        type:"error"
                    });
                    return;
                }
                let createTime = new Date();
                this.axios.post("/api-db/publishArticle",{
                    title:this.title,
                    content:this.articleContent,
                    createTime:createTime,
                    publisherId:this.$store.state.userData.userId,
                    readingCount:0,
                    likes:0,
                    collections:0
                });
                this.$message({
                    message:"发布成功",
                    type:"success"
                });
                this.backToLastBtn();//返回文章列表
                this.title = '';
                this.articleContent = '';
            },
            backToLastBtn(){
                this.$router.push("/index/creation");
            }
        }
    }
</script>

<style scoped>
   .mavonEditor{
       height: 700px;
   }
   .funList{
       padding: 10px 0px;
       background-color: #f3f3f3;
   }
    .backToLastBtn{
        padding-left: 10px;
    }
</style>