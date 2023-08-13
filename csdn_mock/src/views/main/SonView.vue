<template>
    <div>
        <div>标题：{{title}}</div>
        <div>发布人：{{publisher}}</div>
        <el-button @click="add">累计添加</el-button>
    </div>
</template>

<script>
    export default {
        name: "SonView",
        props:{
            title:{
                type:String,
            },
            publisher:{
                type:String,
            }
        },
        methods:{
            add(){
                this.$emit('add');
            }
        }
    }
</script>

<style scoped>

</style>