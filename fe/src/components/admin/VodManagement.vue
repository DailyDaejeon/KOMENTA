<template>
  <b-col class="vod-management">
    <h4>전체 VOD</h4>
    <div class="btn-right">
      <router-link class="vodInsertBtn" :to="{name:'VODInsert'}">VOD 추가</router-link>
    </div>
    <table class="table-border-style" style="text-align:center;">
      <tr>
        <td>등록번호</td>
        <td>제목</td>
        <td>종류/장르</td>
        <td>등록일</td>
        <td>담당자</td>
        <td>설정</td>
      </tr>
      <tr v-if="vodList.length == 0">
        <td colspan="6">
          등록된 VOD가 없습니다.
        </td>
      </tr>
      <tr v-for="(vod, index) in paginatedData" :key="index" v-else>
        <td>{{vod.ve_id}}</td>
        <td><p @click="goVod(vod.ve_id)">{{vod.v_title}} {{vod.ve_episode_num}}화</p></td>
        <td>{{vod.g_name}}/{{vod.gd_name}}</td>
        <td>{{vod.ve_upload_date}}</td>
        <td>{{vod.ve_admin}}</td>
        <td>
          <p class="vod-setting vod-delete" @click="deleteVod(vod.v_id)">삭제</p>
        </td>
      </tr>
    </table>
    <div class="admin-btn-cover">
      <button :disabled="pageNum === 0" @click="prevPage" class="page-btn">
        <font-awesome-icon :icon="['fas', 'angle-left']"/>
      </button>
      <span class="page-count">{{pageNum+1}}/{{pageCount}} 페이지 </span>
      <button :disabled="pageNum >= pageCount-1" @click="nextPage" class="page-btn">
        <font-awesome-icon :icon="['fas', 'angle-right']"/>
      </button>
    </div>
  </b-col>
</template>

<script>
import { fetchAllEpi, deleteVOD } from '@/api/vod';
export default {
  data() {
    return {
      pageNum:0,
      vodList:[],
    }
  },
  props: {
    pageSize: {
      type: Number,
      required: false,
      default: 10
    },
  },
  created() {
    this.getVODList();
  },
  methods: {
    nextPage() {
      this.pageNum += 1;
    },
    prevPage() {
      this.pageNum -= 1;
    },
    async getVODList() {
      const response = await fetchAllEpi();
      this.vodList = response.data;
    },
    goVod(veId) {
      this.$router.push(`/voddetail/${veId}`);
    },
    async deleteVod(v_id) {
      const response = await deleteVOD(v_id);
      if(response.data == 1) {
        alert('성공적으로 삭제되었습니다.')
        window.location.reload();
      }
    }
  },
  computed: {
    pageCount() {
      let listLeng = this.vodList.length,
          listSize = this.pageSize,
          page = Math.floor(listLeng / listSize);

      if(listLeng % listSize > 0) page += 1;

      return page;
    },
    paginatedData() {
      const start = this.pageNum * this.pageSize,
            end = start + this.pageSize;

      return this.vodList.slice(start, end);
    }
  }
}
</script>

<style>
  .btn-right {
    text-align: right;
  }
</style>