<template>
  <div>
    <!-- asidebar에서 내피드를 클릭했을때 feed/f_id로 오게 함 -->
    <!-- f_id로 정보 받아서 처리 -->
    <div class="container">
      <section class="feed-section">
        <div class="userprofile">
          <div class="userprofile__picbox">
            <div class="userprofile__picbox__content">
              <img :src="getProfile()" />
            </div>
          </div>
          <div class="userprofile__name">{{ feedUserInfo.u_nickname }}</div>
          <template v-if="!showMyRecent() && showFollowBtn">
            <button class="userprofile__button" @click="followUser">
              FOLLOW
            </button>
          </template>
          <template v-else-if="!showMyRecent() && !showFollowBtn">
            <button class="userprofile__button" @click="followUser">
              UNFOLLOW
            </button>
          </template>
        </div>

        <template v-if="showMyRecent()">
          <div class="container__recentSection">
            <!-- 최근 본 VOD div -->
            <h3 class="container__recentSection__recentList">
              Recently Watched
            </h3>
            <button class="container__recentSection__createPlaylistButton" @click="showModalForm" @mouseover="showIntro" @mouseleave="closeIntro">
              <h4 class="container__recentSection__createPlaylistTitle">
                새로운 플레이리스트 만들기
              </h4>
            </button>
            <div class="intro-drag-and-drop" :style="{display:isShowIntro}">
              <div class="triangle"></div>
              <div class="img-box">
                <img :src="getIntro()" alt="">
              </div>
            </div>
          </div>
          <div class="drop-zone" @dragover.prevent @dragenter.prevent>
            <!-- 최근 본 VOD 리스트 중 하나씩 v-for돌림 -->
            <div class="drop-zone__inner">
              <div
                v-for="(vod, index) in myrecentlists.episodeList"
                :key="index"
                class="drag-el"
                draggable
                @dragstart="startDrag($event, vod)"
              >
                <img :src="getVodPoster(vod.v_poster)" width="100%" />
                <div class="vodInfo">
                  <div class="vod-info-form">
                    <p class="vod-info-title" v-html="vodTitleReName(vod.v_title, vod.ve_episode_num)"></p>
                    <p class="vod-info-genre">{{vod.g_name}}/{{vod.gd_name}}</p>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <Modal v-if="showModal" @close="showModal = false">
            <h4 slot="header">
              <div class="modal_title">나만의 플레이 리스트를 만들어보세요</div>
              
              <div @click="showModal = false" class="modal_close_btn">
                <i class="closeModalBtn fa fa-times" aria-hidden="true"> </i>
              </div>
            </h4>
            <p slot="body" @keydown.enter="createPlaylist">
              <label class="modal__playlist" for="playlist_name"
                ><strong>TITLE</strong></label
              >
              <input
                id="playlist_name"
                class="modal-input"
                type="text"
                v-model="plName"
                placeholder="플레이리스트 제목을 적어주세요."
              />
              <br />
              <label class="modal__playlist" for="playlist_context"
                ><strong>CONTEXT</strong></label
              >
              <input
                id="playlist_context"
                class="modal-textarea"
                type="text"
                v-model="plComment"
                placeholder="플레이리스트 내용을 적어주세요."
              />
              <br />
              <button
                class="userprofile__button modal_button"
                @click="createPlaylist"
              >
                SUBMIT
              </button>
            </p>
          </Modal>
          <!-- 플레이리스트 수만큼 drop-zon v-for -->
          <div v-for="(playlist, index) in playlists" :key="index">
            <h3 style="cursor:pointer">
              <span @click="goPlaylsitDetail(playlist[0].pl_id)"
                >{{ playlist[0].pl_name }} </span
              ><span @click="deleteUserPlaylist(playlist[0].pl_id)"
                ><i class="far fa-trash-alt"></i>
              </span>
            </h3>
            <div
              class="drop-zone"
              @drop="onDrop($event, playlist[0].pl_id,index)"
              @dragover.prevent
              @dragenter.prevent
            >
              <!-- 한 플레이리스트의 컨텐츠만큼 v-for(5개씩 보여주면 옆으로 넘기는 식으로 해야될것같음) -->
              <!-- startDrag -1이면  -->
              <div class="drop-zone__inner">
                <div
                  v-for="(vod, index) in playlist"
                  :key="index"
                  class="drag-el"
                  draggable
                >
                  <span v-if="vod.gd_name" @click="goEpiDetail(vod.ve_id)">
                    <img :src="getPlaylistVodPoster(vod.v_poster)"/>
                    <div class="vodInfo">
                      <div class="vod-info-form">
                        <p class="vod-info-title" v-html="vodTitleReName(vod.v_title, vod.ve_episode_num)"></p>
                        <p class="vod-info-genre">{{vod.g_name}}/{{vod.gd_name}}</p>
                        <button class="vod-info-btn">보러가기</button>
                      </div>
                    </div>
                  </span>
                </div>
              </div>
            </div>
          </div>
        </template>

        <template v-else>
          <div v-for="(playlist, index) in playlists" :key="index">
            <h3 style="cursor:pointer">
              <span @click="goPlaylsitDetail(playlist[0].pl_id)">{{
                playlist[0].pl_name
              }}</span>
              <template v-if="isLikePlaylist(playlist[0].pl_id)">
                <span
                  class="playlsit-icon"
                  @click="addlikeUserPlaylist(playlist[0].pl_id)"
                >
                  <font-awesome-icon :icon="['far', 'star']"/>
                </span>
              </template>
              <template v-else>
                <span
                  class="playlsit-icon"
                  @click="cancellikePlaylist(playlist[0].pl_id)"
                >
                  <font-awesome-icon
                    class="playlist__star"
                    :icon="['fas', 'star']"
                  />
                </span>
              </template>
            </h3>
            <div class="drop-zone">
              <!-- 한 플레이리스트의 컨텐츠만큼 v-for(5개씩 보여주면 옆으로 넘기는 식으로 해야될것같음) -->
              <!-- startDrag -1이면  -->
              <div class="drop-zone__inner">
                <div v-for="(vod, index) in playlist" :key="index" class="drag-el">
                  <span v-if="vod.gd_name" @click="goEpiDetail(vod.ve_id)">
                    <img :src="getPlaylistVodPoster(vod.v_poster)"/>
                    <div class="vodInfo">
                      <div class="vod-info-form">
                        <p class="vod-info-title" v-html="vodTitleReName(vod.v_title, vod.ve_episode_num)"></p>
                        <p class="vod-info-genre">{{vod.g_name}}/{{vod.gd_name}}</p>
                        <button class="vod-info-btn">보러가기</button>
                      </div>
                    </div>
                  </span>
                </div>
              </div>
            </div>
          </div>
        </template>
      </section>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import {
  fetchMyInfo,
  fetchRecentPlaylist,
  unlikePlaylist,
  likePlaylist,
  deletePlaylist,
  addReviewPlaylist,
  fetchMyPlaylist,
  addPlaylist,
  modifyfollow,
  addPlaylistVod,
  fetchfollowinglist,
} from '@/api/user';
import Modal from '@/components/common/Modal';

export default {
  components: {
    Modal,
  },
  data() {
    return {
      myFollowing: [],
      feedUserInfo: {},
      myrecentlists: {},
      playlists: [],
      showModal: false,
      showFollow: false,
      showRecent: false,
      dragVod: {},
      plName: '',
      plComment: '',
      epiComment: '',
      selectedId: [],
      showFollowBtn: true,
      isShowIntro: 'none',
    };
  },
  created() {
    this.getFeedInfo();
    this.$store.dispatch('FETCH_LIKEPLAYLIST', this.userInfo.u_id);
    this.getFollowinglist();
    this.getUserPlayList();
    this.getRecentPlayList();
  },
  computed: {
    ...mapState({
      userInfo: (state) => state.user.userInfo,
      myFollowingList: (state) => state.user.myFollowingList,
      likePlaylist: (state) => state.user.likePlaylist,
    }),
  },
  methods: {
    getProfile() {
      const profile = this.feedUserInfo.u_profile_pic.split('.')[0];
      return `${process.env.VUE_APP_PICTURE}profile/${profile}`;
    },
    // drag and drop 메소드
    // drag 시작한 vod
    startDrag: (evt, vod) => {
      evt.dataTransfer.dropEffect = 'move';
      evt.dataTransfer.effectAllowed = 'move';
      evt.dataTransfer.setData('vodID', vod.ve_id);
    },
    // vod를 끌어다가 놓는 플레이리스트
    onDrop(evt, plId,index) {
      const vodID = evt.dataTransfer.getData('vodID');
      const vod = this.myrecentlists.historyList.find(
        (vod) => vod.ve_id == vodID
      );
      for (let i = 0; i < this.playlists[index].length; i++) {
        const playVod = this.playlists[index][i];
        if ( vod.ve_id == playVod.ve_id) {
           this.$swal({
          text: '이미 플레이리스트에 있는 VOD EPISODE 입니다.',
          customClass: {
          container: 'swal2-container'
          },
          icon: 'info',
          timer: 1300,
          showConfirmButton: false,
        })
          return
        } 
      }
      this.dragIndex = this.myrecentlists.historyList.indexOf(vod, 0);
      const epiInfo = {
        vh_id: vod.vh_id,
        pl_id: plId,
      };
      addPlaylistVod(epiInfo).then(()=>{
        this.getUserPlayList();
      });
    },
    showModalForm() {
      this.showModal = true;
    },
    showMyRecent() {
      if (this.userInfo.u_id == this.feedUserInfo.u_id) {
        return true;
      }
      return false;
    },
    showIntro(){
      this.isShowIntro = 'block';
    },
    closeIntro(){
      this.isShowIntro = 'none';
    },
    getIntro(){
      return require('@/assets/images/dragAndDrop.gif');
    },
    async getFollowinglist() {
      const response = await fetchfollowinglist(this.userInfo.u_id);
      this.myFollowing = response.data;
      for (let index = 0; index < this.myFollowing.length; index++) {
        const element = this.myFollowing[index];
        if (element.f_id === Number(this.$route.params.id)) {
          this.showFollowBtn = false;
          return;
        }
      }
      this.showFollowBtn = true;
      return;
    },
    //팔로우하는 로직 추가 구현
    followUser() {
      const your_id = this.$route.params.id;
      const my_id = this.userInfo.u_id;
      const bothId = { u_id: my_id, f_id: your_id };
      modifyfollow(bothId);
      this.$store.dispatch('FETCH_FOLLOWING', this.userInfo.u_id);
      this.showFollowBtn = !this.showFollowBtn;
    },

    async addComment(plId) {
      try {
        const commentInfo = {
          plc_id: plId,
          vh_comment: this.epiComment,
        };
        await addReviewPlaylist(commentInfo);
      } catch {
        console.log('플레이리스트 comment생성 실패');
      }
    },
    async createPlaylist() {
      const playlistinfo = {
        pl_name: this.plName,
        pl_comment: this.plComment,
      };
      try {
        await addPlaylist(playlistinfo);
        this.showModal = false;
        this.getUserPlayList();
        this.$store.dispatch('FETCH_MYPLAYLIST', this.userInfo.u_id);
        this.$swal({
          customClass: {
          container: 'swal2-container'
        },
        text: '플레이리스트를 생성 했습니다.',
        icon: 'success',
        timer: 1300,
        showConfirmButton: false,
      })
        this.plName = '';
        this.plComment = '';
      } catch {
        alert('플레이리스트 생성 실패');
      }
    },
    goPlaylsitDetail(plId) {
      this.$router.push(`/playlist/${plId}`);
    },
    goEpiDetail(veId) {
      this.$router.push(`/voddetail/${veId}`);
    },
    async getRecentPlayList() {
      try {
        // userId를 같이 안보내주고 토큰으로??
        const res = await fetchRecentPlaylist();
        this.myrecentlists = res.data;
      } catch {
        console.log('최신vod에러');
      }
    },
    async getUserPlayList() {
      const userId = this.$route.params.id;
      try {
        const res = await fetchMyPlaylist(userId);
        this.playlists = res.data;
      } catch {
        console.log('playlist목록에러');
      }
    },
    isLikePlaylist(plId) {
      for (let i = 0; i < this.likePlaylist.length; i++) {
        const playlist = this.likePlaylist[i];
        if (playlist[0].pl_id == plId) {
          return false;
        }
      }
      return true;
    },
    async addlikeUserPlaylist(plId) {
      await likePlaylist({ pl_id: plId });
      this.$store.dispatch('FETCH_LIKEPLAYLIST', this.userInfo.u_id);
      this.$swal({
        customClass: {
          container: 'swal2-container'
        },
        text: '플레이리스트 좋아요를 했습니다.',
        icon: 'success',
        timer: 1300,
        showConfirmButton: false,
      })
    },
    async cancellikePlaylist(plId) {
      await unlikePlaylist({ pl_id: plId });
      this.$store.dispatch('FETCH_LIKEPLAYLIST', this.userInfo.u_id);
      this.$swal({
        customClass: {
          container: 'swal2-container'
        },
        text: '플레이리스트 좋아요를 취소했습니다.',
        icon: 'error',
        timer: 1300,
        showConfirmButton: false,
      })
    },
    deleteUserPlaylist(plId) {
      this.$swal({
        text: '플레이리스트를 삭제하시겠습니까?',
        icon:'error',
        showCancelButton: true,
        confirmButtonText: '삭제',
        cancelButtonText: '취소',
        confirmButtonColor: "#fc3c44",
        customClass: {
          container: 'swal2-container'
        },
      }).then((result) => {
        if(result.value) {
          this.$swal({
            customClass: {
          container: 'swal2-container'
        },
            text: '플레이리스트를 삭제했습니다.',
            icon: 'success',
            timer: 1300,
            showConfirmButton: false,
          })
          deletePlaylist(plId).then(()=>{
            this.$store.dispatch('FETCH_MYPLAYLIST', this.userInfo.u_id);
            this.getUserPlayList();
          })
        } else {
          this.$swal({
            customClass: {
          container: 'swal2-container'
        },
            text: '플레이리스트를 삭제를 취소했습니다.',
            icon: 'info',
            timer: 1300,
            showConfirmButton: false,
          })
        }
      })
      
    },
    async getFeedInfo() {
      try {
        const feedUserId = this.$route.params.id;
        const response = await fetchMyInfo(feedUserId);
        this.feedUserInfo = response.data;
      } catch {
        console.log('피드목록 에러');
      }
    },
    getPlaylistVodPoster(poster) {
      return `${process.env.VUE_APP_PICTURE}poster/${poster}`;
    },
    getVodPoster(poster) {
      return `${process.env.VUE_APP_PICTURE}poster/${poster}`;
    },
    vodTitleReName(title, epi_num) {
      let name = String(title + ' ' + epi_num + '화');
      let rename = '';
      for (let i = 0; i < name.length; i++) {
        if (i % 10 == 0) {
          rename += `<br>` + name.charAt(i);
        } else {
          rename += name.charAt(i);
        }
      }
      return rename;
    },
  },
};
</script>
