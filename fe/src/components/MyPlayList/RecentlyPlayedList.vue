<template>
  <div>
    <h4>Recently Played</h4>
    <!-- vod 목록 보여줄 전체 폼 -->
    <div class="rPlayList-Form">
      <!-- 시청중인 VOD가 없을 때 -->
      <div v-if="historyList.length == 0">
        <p>시청한 VOD가 없습니다.</p>
      </div>
      <!-- 시청중인 VOD가 있을 때 -->
      <div v-else>
        <!-- 이전 버튼 -->
        <div class="btn-cover displayInlineBlock">
          <button :disabled="pageNum === 0" @click="prevPage" class="page-btn">&lt;</button>
        </div>
        <div v-for="(vod,index) in paginatedData" :key="vod.v_id">
          <!-- vod poster -->
          <div class="vodPoster">
            <img :src="getPoster(index)" @click="goVodDetail(vod.ve_id)">
          </div>
        </div>
        <!-- 다음 버튼 -->
        <div class="btn-cover">
          <button :disabled="pageNum >= pageCount-1" @click="nextPage" class="page-btn">&gt;</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import { fetchRecentPlaylist } from '@/api/user';
export default {
  data() {
    return {
      pageNum:0,
      historyList:[],
      episodeList:[],
      playlists:[],
      rVodInfo:'none',
    }
  },
  created() {
    this.fetchRPlayList();
  },
  props: {
    pageSize: {
      type: Number,
      required: false,
      default: 5
    }
  },
  computed: {
    ...mapState({
        userInfo: state => state.user.userInfo,
      }),
    pageCount() {
      let listLeng = this.historyList.length,
          listSize = this.pageSize,
          page = Math.floor(listLeng / listSize);

      if(listLeng % listSize > 0) page += 1;

      return page;
    },
    paginatedData() {
      const start = this.pageNum * this.pageSize,
            end = start + this.pageSize;

      return this.historyList.slice(start, end);
    }
  },
  methods: {
    goVodDetail(veId){
      this.$router.push(`/voddetail/${veId}`)
    },
    nextPage() {
      this.pageNum += 1;
    },
    prevPage() {
      this.pageNum -= 1;
    },
    getPoster(index) {
      const idx = (index + ((this.pageNum*this.pageSize)));
      const poster = String(this.episodeList[idx].gd_id+'_'+ this.episodeList[idx].v_title);
      return `${process.env.VUE_APP_PICTURE}poster/${poster}`;
    },
    async fetchRPlayList(){
      const response = await fetchRecentPlaylist(this.userInfo.u_id);
      this.historyList = response.data.historyList;
      this.episodeList = response.data.episodeList;
    },
    showOnInfo() {
      if(this.rVodInfo == 'none') {
        this.rVodInfo = 'inline';
      }
    },
  },
}
</script>

<style>

</style>