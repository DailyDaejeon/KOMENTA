<template>
  <div>
    <template v-if="commentsList.length">
    <div v-for="(comment,index) in commentsList" :key="comment.c_id">
      <div class="comment__rank">{{index+1}} </div>
      <div class="comment__commentbox">
      <span class="comment__username" @click="goFeed(comment.u_id)">{{comment.u_nickname}}</span> 
      <template v-if="itsMe(comment.u_id)"><span class="comment__block" @click="DeleteComment(comment.c_id)">삭제</span></template>
      <template v-else-if="isBlockUser(comment.u_id)">
      <span @click="blockUser(comment.u_id)" class="comment__block">
      차단취소</span>
      </template>
      <template v-else>
      <span @click="blockUser(comment.u_id)" class="comment__block">
      차단하기</span>
      </template>
     <br>
      <div class="comment__best">BEST</div>
      <span class="comment__time" @click="goCommentTime(timeToSec(comment.c_playtime))"> {{comment.c_playtime}} </span> 
      <template v-if="isBlockUser(comment.u_id)">
      <span class="comment__block__content">차단한 댓글입니다. </span> <br>
      </template>
      <template v-else>
      <span>{{ comment.c_contents}} </span> <br>
      </template>
      <span class="comment__uploadtime"> {{comment.c_upload_time}} </span>
      <span @click="commentLike(index,comment)"><font-awesome-icon :icon="['fas', 'thumbs-up']" :id="`like-btn-${comment.c_id}`" :class="[comment.is_like_comment ? 'commet__like' :' comment__unlike' ]" style="cursor:pointer"/><span :id="`like-cnt-${comment.c_id}`">{{ comment.comment_good_count }}</span> </span>
      </div>
      <hr>
    </div>
  </template>
  <template v-else>
    <h2>댓글이 없습니다! 댓글을 남겨주세요!</h2>
  </template>
  </div>

</template>

<script>
import {userlikeComment,fetchEpiComment, removeComment} from '@/api/comment'
import {modifyunfollow} from '@/api/user'

import {mapState} from 'vuex'
export default {
  data(){
    return {
      commentsList:[],

    }
  },
  computed:{
    ...mapState({
      userInfo: state => state.user.userInfo,
      myUnFollowingList: state => state.user.myUnFollowingList,
    }),
  },
  created() {
    this.getEpiComment();
  },
  methods : {
    itsMe(uId) {
          if (this.userInfo.u_id == uId) {
              return true
          }
          return false
    },
    DeleteComment(cId){
      this.$swal({
        text: '댓글을 삭제하시겠습니까?',
        icon:'error',
         customClass: {
          container: 'swal2-container'
        },
        showCancelButton: true,
        confirmButtonText: '삭제',
        confirmButtonColor: "#fc3c44",
        cancelButtonText: '취소',
      }).then((result) => {
        if(result.value) {
          this.$swal({
            text: '댓글을 삭제했습니다.',
            icon: 'success',
            timer: 1300,
            showConfirmButton: false,
          })
           removeComment(cId).then(()=>{
            this.getEpiComment();
          })
        } else {
          this.$swal({
            text: '댓글을 삭제를 취소했습니다.',
            icon: 'info',
            timer: 1300,
            showConfirmButton: false,
          })
        }
      })
    },
    isBlockUser(uId){
      for (let i = 0; i < this.myUnFollowingList.length; i++) {
        const unfollowuser = this.myUnFollowingList[i];
        if (unfollowuser.f_id == uId) {
          return true
        }
      }
      return false
    },
    async blockUser(uId) {
      const blockInfo = {
        u_id : this.userInfo.u_id,
        uf_id : uId
      }
      await modifyunfollow(blockInfo)
      this.$store.dispatch('FETCH_UNFOLLOWING',this.userInfo.u_id)
    },
    goFeed(uId){
      this.$router.push(`/feed/${uId}`)
    },
    async getEpiComment() {
      try {
          const epiId = this.$route.params.id; 
        const res = await fetchEpiComment(epiId)
        this.commentsList = res.data.sort(function (a,b) {
              return parseFloat(a.comment_good_count) > parseFloat(b.comment_good_count) ? -1 : parseFloat(a.comment_good_count) < parseFloat(b.comment_good_count) ? 1:0;
          }).slice(0,10)
      } catch {
        console.log('epicomment 에러!!')
      }
    },
    goCommentTime(time){
      this.$emit('goCommentTime',time)
    },
    timeToSec(time){
      let splitTime = time.split(':')
      let changeTime = Number(splitTime[splitTime.length-1])
      for (let i = splitTime.length-2; i >= 0; i--) {
          let element = Number(splitTime[i]);
          changeTime += element*(60**(splitTime.length-i-1))
      }
      return changeTime
    },
    async commentLike(index,comment){
      const likeBtn = document.getElementById(`like-btn-${comment.c_id}`)
      this.commentsList[index].is_like_comment = !this.commentsList[index].is_like_comment

      likeBtn.classList.toggle('comment__like')
      likeBtn.classList.toggle('comment__unlike')
      const commentInfo = {
        c_id : comment.c_id,
        u_id : this.userInfo.u_id
      }
      await userlikeComment(commentInfo)
      this.getEpiComment();
      this.$emit('commentLike',comment.c_id)
    }    
  }
}
</script>

<style scoped>

</style>