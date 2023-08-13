<template>
    <el-container>
        <el-header class="header">
            <el-menu :default-active="$route.path" class="el-menu-demo" mode="horizontal" :router="true" >
                <el-menu-item index="/index/postList">首页</el-menu-item>
                <el-menu-item index="/index/creation">创作中心</el-menu-item>
                <el-menu-item index="/3">消息中心</el-menu-item>
                <el-button @click="exit" class="exitBtn">退出登陆</el-button>
                <div class="login">
                    <el-button v-if="userData.userId === undefined"   type="primary" plain @click="loginDialogVisible = true">登录</el-button>
                    <el-avatar v-if="userData.userId !== undefined"  :src="userData.headImage" ></el-avatar>
                </div>
            </el-menu>
        </el-header>
        <!--登陆框-->
        <el-dialog
                title="密码登陆"
                :visible.sync="loginDialogVisible"
                width="30%"
                center>
            <div class="form">
                <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm" >
                    <el-form-item  prop="userName">
                        <span slot="label" class="title">用户名</span>
                        <el-input v-model="ruleForm.userName"></el-input>
                    </el-form-item>
                    <el-form-item prop="pass">
                        <span slot="label" class="title">密码</span>
                        <el-input type="password" v-model="ruleForm.pass" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="submitForm('ruleForm')">登陆</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </el-dialog>
        <el-main class="postContainter">
            <router-view/>
        </el-main>
        <el-footer class="footer">关于我们
            招贤纳士
            商务合作
            寻求报道

            400-660-0108

            kefu@csdn.net

            在线客服
            工作时间 8:30-22:00
            公安备案号11010502030143
            京ICP备19004658号
            京网文〔2020〕1039-165号
            经营性网站备案信息
            北京互联网违法和不良信息举报中心
            家长监护
            网络110报警服务
            中国互联网举报中心
            Chrome商店下载
            账号管理规范
            版权与免责声明
            版权申诉
            出版物许可证
            营业执照
            ©1999-2023北京创新乐知网络技术有限公司</el-footer>
    </el-container>
</template>

<script>
    import postListView from "./main/postListView"
    import Cookies from 'js-cookie'
    import EventBus from '../assets/js/EventBus'
    export default {
        name: "IndexView",
        data(){
            let validatePass = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入密码'));
                } else {
                    if (this.ruleForm.pass !== '') {
                        this.$refs.ruleForm.validateField('checkPass');
                    }
                    callback();
                }
            };
            return{
                loginDialogVisible:false,
                ruleForm:{
                    userName:'',
                    pass:'',
                },
                rules: {
                    userName: [
                        { required: true, message: '请输入用户名称', trigger: 'blur' },
                        { min: 2, max: 10, message: '长度在 2到 10个字符', trigger: 'blur' }
                    ],
                    pass: [
                        { validator: validatePass, trigger: 'blur' }
                    ],
                },
                userData:{
                    userId:undefined,
                    userName:'',
                    headImage:'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
                },
                avatarSize:'medium',
            }
        },
        components:{
            postListView
        },
        methods:{
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {//输入格式校验
                    if (valid) {
                        this.login();
                    } else {
                        this.$notify({
                            title: '提示',
                            message: '登陆失败',
                            type: 'error'
                        });
                        return false;
                    }
                });
            },
            login(){//数据库用户数据校验
                this.$axios.get("/api-db/login",{params:{
                        userName:this.ruleForm.userName,
                        pass:this.ruleForm.pass
                    }}).then((e)=>{
                    if(e.data === -1){//用户名未注册
                        this.$notify({
                            title: '警告',
                            message: '用户名不存在，请重新输入',
                            type: 'warning'
                        });
                    }
                    else if(e.data === 0){//密码不正确
                        this.$message({
                            message:'密码不正确，请重新输入',
                            type:'error'
                        })
                        this.ruleForm.pass = '';
                    }
                    else if(e.data === 1){//登陆
                        // Cookies.set('userName',this.ruleForm.userName);//传送用户名
                        // Cookies.set('role',this.ruleForm.role);//传送用户名
                        // EventBus.$emit("role",this.ruleForm.role);//传送角色值，首页可以赋予不同的菜单
                        this.loginDialogVisible = false;
                        this.$notify({
                            title: '提示',
                            message: '登陆成功',
                            type: 'success'
                        });
                        this.axios.get("/api-db/getUserAfterLogin",{params:{//获取到数据库里的用户其他信息
                                userName:this.ruleForm.userName
                            }
                        })
                            .then((e)=>{
                                // 方案1: 通过localStorage
                                // let userData = {
                                //     isLogin:true,
                                //     userId:e.data.userId,
                                //     userName:e.data.userName,
                                //     headImage:e.data.headImage,
                                // };
                                // localStorage.setItem("userData",JSON.stringify(userData));
                                // this.$store.state.userData = userData;
                                // this.userData = userData;
                                // this.getLocalStorage();

                                //方案2:通过cookie
                                Cookies.set("userName",e.data.userName);
                                Cookies.set("userId",e.data.userId);
                                Cookies.set("headImage",e.data.headImage);
                                this.getCookie();
                            });
                        this.ruleForm = {};//清空登陆框数据
                    }
                    else{
                        this.loginDialogVisible = false;
                        this.$message({
                            message: '哪里出错了',
                            type: 'error'
                        });
                    }
                })
                    .catch((e)=>{
                        console.log(e)
                    })
            },
            getCookie(){
                this.userData.userId = Cookies.get("userId");
                this.userData.userName = Cookies.get("userName");
                this.userData.headImage = Cookies.get("headImage");
            },
            exit(){
                // //方法1:
                // localStorage.clear();
                // this.userData ={};
                // this.$store.state.userData = {};
                //方法2:
                Cookies.remove("userId");
                Cookies.remove("userName");
                Cookies.remove("headImage");
                this.getCookie();
            },
            getLocalStorage(){
                console.log(JSON.parse(localStorage.getItem("userData")));
            },
            avatarHover(){
                this.avatarSize = 'large';
            }
        },
        created(){
            this.getCookie();
        },
        mounted(){
            // let userInfo = JSON.parse(localStorage.getItem("userData"));
            // this.userData = userInfo;
        },
        beforeUpdate(){
            this.userData.userId = Cookies.get("userId");
            this.userData.userName = Cookies.get("userName");
            this.userData.headImage = Cookies.get("headImage");
        }
    }
</script>

<style scoped>
    .postContainter{
        background-color: #f4f4f4;
    }
    .el-menu-demo{
        margin-left: 110px;
    }
    .footer{
        font: 12px Extra Small;
        text-align: center;
    }
    .login{
        padding-top: 10px;
        float:right;
        margin-right: 400px;
    }
    .exitBtn{
        float: right;
        margin: 10px 0px;
    }
</style>