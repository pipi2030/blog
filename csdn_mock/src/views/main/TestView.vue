<template>
    <div class="father">
        <p>父组件</p>
        <p>当前点击次数：{{num}}</p>
        <son-view @add="fatherAdd"/>
    </div>
</template>
<script>
    import SonView from "./SonView"
    export default {
        name: "TestView",
        data(){
            return{
                num:0,
            }
        },
        components:{
            SonView,
        },
        methods:{
            fatherAdd(){
                this.num = this.num + 1;
            }
        }
    }
</script>

<style scoped>

</style>