import Vue from 'vue'
import './plugins/axios'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/icon/iconfont.css'//引入外部图标
import mavonEditor from "mavon-editor";
import "mavon-editor/dist/css/index.css";
Vue.use(mavonEditor);
Vue.use(ElementUI);
Vue.config.productionTip = false;
import './plugins/axios'
import axios from 'axios'//方便进行前后端交互
Vue.prototype.$http = axios;
// axios.defaults.baseURL = '/api';
import moment from 'moment'
//定义一个全局过滤器实现日期格式化
Vue.filter('datefmt',function (input,fmtstring) {//当input为时间戳时，需转为Number类型
    // 使用momentjs这个日期格式化类库实现日期的格式化功能
    return moment(input).format(fmtstring);
});
//引入qs模块
import qs from 'qs'
Vue.prototype.$qs = qs;
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
