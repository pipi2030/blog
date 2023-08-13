import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state:{
      userData: {}
  },
  getters: {
  },
  mutations: {
      // setUserData(state, v) {
      //     localStorage.setItem('userData', JSON.stringify(v));//将传递的数据先保存到localStorage中
      //     state.userData = v;// 之后才是修改state中的状态
      // },
  },
  actions: {
  },
  modules: {
  }
})
