<template>
  <div class="myPage">
    <!-- 회원 정보 관리 -->
    <user-info @showUserInfoForm="showUserInfoForm"></user-info>
    <Modal v-if="showUserInfoModal" class="myInfo-modi-modal">
        <div slot="header">
          <h3 class="findIdPw__title">내 정보 수정</h3>
          <span id="closeModalBtn" @click="closeUserInfoModal">
            <i class="fa fa-times" aria-hidden="true"></i>
          </span>
          <hr>                                                                                                      
        </div>
        <div slot="body" class="modal-body-form">
          <form @submit.prevent="modifyUserInfo">
            <div class="profile-form">
              <div class="img">
                <v-img v-if="showProfile" :src="showProfile" width="100%"/>
              </div>
              <div class="file-input">
                <input id="profile" ref="imageInput" type="file" hidden @change="onChangeImages" :v-model="userProfilePic" accept="image/jpeg,image/jpg">
                <v-btn type="button" @click="onClickImageUpload" class="btn-upload">
                  <font-awesome-icon :icon="['fas', 'upload']" :style="{ color: '#ffffff' }"/>
                  upload
                </v-btn>
              </div>
            </div>
            <div class="rowHR"></div>
            <div class="user-modi-form">
              <div class="modi-form">
                <div class="modi-form-uemail">
                  <label for="modi-u-email">아이디</label>
                  <input type="text" id="modi-u-email" class="form-control form-control-lg" v-model="userId" disabled>
                </div>
                <div>
                  <!-- 비밀번호 8자 이상 -->
                  <label for="modi-u-password">비밀번호</label>
                  <input type="password" id="modi-u-password" class="form-control form-control-lg" v-model="userPassword"><br>
                  <p class="warning-form warning-signup">
                    <span class="warning-text" v-if="!isPasswordValid">
                      password를 8자 이상 입력하세요.
                    </span>
                  </p>
                </div>
                <div>
                  <!-- 비밀번호 일치 여부 확인 -->
                  <label for="modi-u-pwConfirm">비밀번호 확인</label>
                  <input type="password" id="modi-u-pwConfirm" class="form-control form-control-lg" v-model="confirmPW"><br>
                  <p class="warning-form warning-signup">
                    <span class="warning-text" v-if="!isPasswordConfirmValid">
                      password가 일치하지 않습니다.
                    </span>
                  </p>
                </div>
                <div class="modi-form-nickname">
                  <!-- 닉네임 중복 여부 확인 -->
                  <label for="modi-u-nickname">닉네임</label>
                  <input type="text" id="modi-u-nickname" class="form-control form-control-lg" v-model="userNickName">
                  <div class="icon-inline-block icon-form">
                    <!-- 중복 닉네임이 아닐 때 표출 -->
                    <p class="icon-inline-block" v-show="!isUserNickNameEmpty && !nickCheck">
                      <font-awesome-icon class="fw-icon fwCheck" :icon="['fas', 'check' ]" />
                    </p>
                    <!-- 중복 닉네임일 때 표출 -->
                    <p class="icon-inline-block" v-show="!isUserNickNameEmpty && nickCheck">
                      <font-awesome-icon class="fw-icon fwTimes" :icon="['fas', 'times' ]" />
                    </p>
                  </div>
                </div>
                <div class="modi-form-phoneNum">
                  <!-- 변경하기 클릭 시,  -->
                  <label for="modi-u-phone">휴대폰 번호</label>
                  <input type="text" id="modi-u-phone" class="form-control form-control-lg" v-model="userPhoneNumber" :disabled="phoneChange">
                  <button class="phoneNumChange" @click.stop.prevent="changePhoneNumber" v-text="phoneText"></button>
                </div>
              </div>
            </div>
            <div class="userModi-btn">
              <p @click="unSubscribe">회원탈퇴</p>
              <input type="submit" value="수정완료">
            </div>
          </form>
        </div>
    </Modal>

    <div v-if="userIsAdmin == 1">
      <admin-page></admin-page>
    </div>
    <div v-else>
      <!-- 내가 단 댓글 관리 -->
      <my-comment></my-comment>

      <!-- 시청 VOD 관리 -->
      <watched-vod :getUserId="uId"></watched-vod>

      <!-- 팔로우 관리 -->
      <follow :getUserId="uId"></follow>

      <!-- 언팔로우 관리 -->
      <un-follow :getUserId="uId"></un-follow>

      <!-- 멤버십 관리 -->
      <membership-setting :userInfo="this.userInfo"></membership-setting>
    </div>
  </div>
</template>

<script>
import UserInfo from '@/components/user/myPage/UserInfo'
import MyComment from '@/components/user/myPage/MyComment';
import WatchedVod from '@/components/user/myPage/WatchedVOD'
import Follow from '@/components/user/myPage/Follow.vue';
import UnFollow from '@/components/user/myPage/UnFollow.vue';
import MembershipSetting from '@/components/user/myPage/MembershipSetting.vue';
import AdminPage from '@/components/user/myPage/adminPage.vue';
import Modal from '@/components/common/Modal';

import { validatePassword } from '@/utils/validations';
import { deleteMyInfo, uploadProfile, dupNickNameChk } from '@/api/user';
import { mapState } from 'vuex';


export default {
  components:{
    UserInfo,
    MyComment,
    WatchedVod,
    Follow,
    UnFollow,
    MembershipSetting,
    AdminPage,
    Modal,
  },
  data() {
    return {
      uId:'',
      userId:'',
      userPassword:'',
      confirmPW:'',
      userNickName:'',
      userPhoneNumber:'',
      userProfilePic:'',
      profilePicFile:'',
      userIsAdmin:0,
      modiForm:'none',
      showUserInfoModal:false,
      phoneChange:true,
      phoneText:'변경하기',
      showProfile:'',
      nickCheck:false,
    }
  },
  created() {
    this.getUserInfo();
    this.getUserProfile(this.userInfo.u_profile_pic)
  },
  computed: {
    ...mapState({
      userInfo: state => state.user.userInfo,
    }),
    isUserNickNameEmpty() {
      if(!this.userNickName) {
        return true;
      }
      return false;
    },
    isNickNameDuplicaionCheck() {
      const result = 'true';
      if(result === 'true') {
        return true;
      }
      return false;
    },
    isPasswordValid() {
      if (!this.userPassword) {
        return true;
      }
      return validatePassword(this.userPassword);
    },
    isPasswordConfirmValid() {
      if (!this.confirmPW) {
        return true;
      } else if (this.userPassword != this.confirmPW) {
        return false;
      }
      return true;
    },

  },
  watch: {
    userNickName : function () {
      if(this.userInfo.u_nickname != this.userNickName){
        this.isDupNickNameCheck()
      }
    }
  },
  methods: {
    async isDupNickNameCheck() {
      try{
        const result = await dupNickNameChk(this.userNickName);
        this.nickCheck = result.data;
      }catch(err) {
        console.log(err);
      }
      return false;
      
    },
    getUserProfile(profile){
      const picName = profile.split('.')
      this.showProfile = `${process.env.VUE_APP_PICTURE}profile/${picName[0]}`;
    },
    closeUserInfoModal() {
      this.showUserInfoModal = false;
    },
    showUserInfoForm() {
      this.showUserInfoModal = true;
    },
    getUserInfo() {
      this.uId = this.userInfo.u_id;
      this.userId = this.userInfo.u_email;
      this.userNickName = this.userInfo.u_nickname;
      this.userPhoneNumber = this.userInfo.u_phone_number;
      this.userProfilePic = this.userInfo.u_profile_pic;
      this.userIsAdmin = this.userInfo.is_admin;
    },
    modifyUser() {
      this.modiForm = 'block';
    },
    changePhoneNumber() {
      if(this.phoneChange) {
        this.phoneChange = false;
        this.phoneText = '변경완료';
      }else {
        this.phoneChange = true;
        this.phoneText = '변경하기';
      }
    },
    onClickImageUpload() {
      this.$refs.imageInput.click();
    },
    onChangeImages(e) {
      this.userProfilePic = this.$refs.imageInput.files[0].name;
      this.profilePicFile = this.$refs.imageInput.files[0];
      this.showProfile =  URL.createObjectURL(e.target.files[0]);
    },
    async modifyUserInfo(){
      if(!this.userPassword) {
        this.$swal({
        text: "비밀번호를 입력하세요.",
        customClass: {
          container: 'swal2-container'
        },
        icon: 'info',
        timer: 1300,
        showConfirmButton: false,
      })
        return;
      }
      if(!this.confirmPW) {
        this.$swal({
          customClass: {
          container: 'swal2-container'
        },
        text: "비밀번호를 확인하세요.",
        icon: 'info',
        timer: 1300,
        showConfirmButton: false,
      })
        return;
      }
      if(!this.userNickName) {
        this.$swal({
        text: "닉네임을 설정해주세요.",
        customClass: {
          container: 'swal2-container'
        },
        icon: 'info',
        timer: 1300,
        showConfirmButton: false,
      })
        return;
      }
      if(!this.userPhoneNumber) {
        this.$swal({
        text: "휴대폰 번호를 입력하세요",
        customClass: {
          container: 'swal2-container'
        },
        icon: 'info',
        timer: 1300,
        showConfirmButton: false,
      })
        return;
      }
      try {
        if(this.profilePicFile) {
          let profilePic = new FormData();
          let pic = this.userInfo.u_id+this.userInfo.u_nickname+"_프로필";
          let picDB = String(pic + '.jpg');
          this.userProfilePic = picDB;
          profilePic.append("profile", this.profilePicFile, String(pic+'.jpg'))
          await uploadProfile(profilePic)
          .then(() => {
          })
          .catch(() => {
            this.$swal({
              text: "프로필 사진을 업로드 하던 중 오류가 발생했습니다.",
              customClass: {
                container: 'swal2-container'
              },
              icon: 'warning',
              timer: 1300,
              showConfirmButton: false,
            })
            return false;
          });
        }
        const userData = {
          u_id:this.uId,
          u_email:this.userId,
          u_pw: this.userPassword,
          u_nickname : this.userNickName,
          u_phone_number : this.userPhoneNumber,
          u_profile_pic : this.userProfilePic
        };
        await this.$store.dispatch('MODIFY',userData)
        this.closeUserInfoModal();
          this.$swal({
          customClass: {
        container: 'swal2-container'
        },
          text: '내정보 수정을 완료했습니다.',
          icon: 'success',
          timer: 1300,
          showConfirmButton: false,

      }).then(()=>{
        this.userId = this.userInfo.u_email
        this.userNickName = this.userInfo.u_nickname
        this.u_phone_number = this.userInfo.u_phone_number
              
        window.location.reload();
        
      })
      }catch(err) {
        console.log(err);
      }
    },
    unSubscribe(){
        this.$swal({
        customClass: {
          container: 'swal2-container'
        },
        icon:'warning',
        title:"회원 탈퇴",
        text: "탈퇴를 계속 진행하시겠습니까?",
        showCancelButton: true,
        confirmButtonText: '탈퇴',
        confirmButtonColor: "#fc3c44",
        cancelButtonText: '취소',
      }).then((result) => {
        if(result.value) {
          this.$swal({
            text: 'Komenta를 이용해주셔서 감사합니다. 정상적으로 탈퇴처리 되었습니다.',
            icon: 'success',
            customClass: {
          container: 'swal2-container'
        },
            timer: 1300,
            showConfirmButton: false,
          })
            deleteMyInfo(this.uId).then(()=>{
              sessionStorage.clear()
            })
            window.location.href='/';
        } 
      })
     },
  },
}
</script>

<style>
  h4 {
    font-weight: 700;
    margin-top: 2rem;
  }

</style>