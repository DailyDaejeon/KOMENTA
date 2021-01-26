import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/member/login',
      name: 'Login',
      component: () => import('@/views/user/Login.vue'),
    },
    {
      path: '/member/join',
      name: 'Signup',
      component: () => import('@/views/user/Signup.vue'),
    },
    {
      path: '/auth',
      name: 'Kakao',
      component: () => import('@/components/user/snsLogin/Kakao.vue'),
    },
    {
      path:'/member/find',
      name:"FindIdPw",
      component: () => import('@/views/user/FindIdPw.vue'),
      redirect: '/member/find/findId',
      children: [     
        {
          path: 'findId',
          name: 'FindId',
          component: () => import('@/components/FindId.vue'),
        },
        {
          path:'findPw',
          name:"FindPw",
          component: () => import('@/components/FindPw.vue')
        },
      ]
    },
  ],
});
