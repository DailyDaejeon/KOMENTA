<template>
  <div>
    <h4 class="page-title popular-vod-sort">{{ message }} VOD</h4>
    <div class="selectTab">
      <router-link :to="{name:'VodPopular'}"  class="text-color">
        <ion-chip @click="changeTitle('인기순')" outline color="primary">
          <ion-label>인기순</ion-label>
        </ion-chip>
      </router-link>
      <router-link :to="{name:'VodComment'}" class="text-color">
      <ion-chip @click="changeTitle('댓글순')" outline color="success">
        <ion-label>댓글순</ion-label>
      </ion-chip>
      </router-link>
      <router-link :to="{name:'VodRecent'}" class="text-color">
        <ion-chip @click="changeTitle('최신순')" outline color="danger">
          <ion-label>최신순</ion-label>
        </ion-chip>
      </router-link>
    </div>
    <router-view></router-view>
  </div>
</template>
<script>
export default {
  data(){
    return {
      message : '인기순',
    }
  },
  methods: {
    changeTitle(msg){
        this.message = msg;
    }
  },
}
</script>
<style>
  ion-chip {
    margin: 0.2rem;
    padding: 0.5rem;
  }

  ion-label {
    color: black;
    font-weight: 500;
    font-size: 1.1rem;
  }

  .selectTab {
    z-index: 999;
    position: relative;
    display: inline-block;
  }

  .text-color {
    font-family: 'Black Han Sans', sans-serif !important; 
  }
</style>

