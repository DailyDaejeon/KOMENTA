<template>
    <div class="category">
        <span class="pl-comment category-total-title">전체 카테고리</span>
        <div class="category-select">
            <div class="__select-form">
                <select v-model="selectedGenre" id="mainGenre">
                    <option value="">장르 전체</option>
                    <option v-for="genre in allGenres" :key="genre.g_id" :value="`${genre.g_id}`">{{genre.g_name}}</option>
                </select>
            </div>
            <div class="__select-form">
                <select v-model="selectedGenreDetail" id="subGenre">
                    <option disabled value=""> 세부 장르 선택 </option>
                    <option v-for="genrdetail in subGenres" :key="genrdetail.gd_id" :value="`${genrdetail.gd_id}`">{{genrdetail.gd_name}}</option>
                </select>
            </div>
        </div>
        <hr>
        <!-- 처음 화면 -->
        <div class="all-category" v-if="vodlists.length == 0">
            <div class="category-sub-list">
                <category-paging :vodList="dramaList"></category-paging>
            </div>
            <div class="category-sub-list">
                <category-paging :vodList="entertainList"></category-paging>
            </div>
            <div class="category-sub-list">
                <category-paging :vodList="docuList"></category-paging>
            </div>
            <div class="category-sub-list">
                <category-paging :vodList="sportList"></category-paging>
            </div>
            <div class="category-sub-list">
                <category-paging :vodList="aniList"></category-paging>
            </div>
        </div>
        <!-- select box에서 선택한 뒤 -->
        <div class="category-img-form" v-for='vod in vodlists' :key="vod.v_id" v-else>
            <span class="category-img" @click="goVodDetail(vod.v_id)">
                <img :src="getPoster(vod.v_poster)">
            </span>
            <span class="category-title">{{getTitle(vod.v_title)}}</span>
        </div>
    </div>
</template>

<script>
import {fetchVodList,fetchAllGenre,fetchGenreDetail,fetchMainGenreVod, fetchSubGenreVod, fetchVodDetail} from '@/api/vod'
import CategoryPaging from '@/components/Category/categoryPaging'
export default {
    name: 'Category',
    data() {
        return {
            selectedGenre:'',
            selectedGenreDetail:'',
            allGenres :[],
            vodlists:[],
            subGenres:[],
            dramaList:[],
            entertainList:[],
            docuList:[],
            sportList:[],
            aniList:[]
        };
    },
    created(){
        this.getAllVOD();
        this.getMainGenre();
        //default 모든 VOD 조회
    },
    components: {
        CategoryPaging
    },
    methods: {
        async getAllVOD(){
            const response = await fetchVodList();
            for (let i = 0; i < response.data.length; i++) {
                const contents = response.data[i];
                if(contents.g_name == '드라마') {
                    this.dramaList.push(contents);
                } else if(contents.g_name == '예능') {
                    this.entertainList.push(contents);
                } else if(contents.g_name == '다큐') {
                    this.docuList.push(contents);
                }else if(contents.g_name == '스포츠') {
                    this.sportList.push(contents);
                }else if(contents.g_name == '애니메이션') {
                    this.aniList.push(contents);
                }
            }
        },
        async getMainGenre() {
            const genres = await fetchAllGenre();
            this.allGenres = genres.data
        },
        async getMainGenreDetail(gId) {
            const sub = await fetchGenreDetail(gId);
            this.subGenres = sub.data
            const vod = await fetchMainGenreVod(gId);
            this.vodlists = vod.data
        },
        async getSubGenre(gdId) {
            const vod = await fetchSubGenreVod(gdId);
            this.vodlists = vod.data
        },
        async goVodDetail(vId){
            const res = await fetchVodDetail(vId)
            // VOD의 가장 첫 epi로 보내기
            this.$router.push(`/voddetail/${res.data[0].ve_id}`)
        },
        getPoster(path){
            return `${process.env.VUE_APP_PICTURE}poster/${path}`
        },
        getTitle(title) {
            if(title.length >= 11) return title.substring(0,11)+'...';
            else return title;
        },
    },
    watch:{
        selectedGenre : function(){
            if(this.selectedGenre == "") {
                this.vodlists.splice(0);
                this.subGenres.splice(0);
            }else {
                this.getMainGenreDetail(this.selectedGenre);
            }
        },
        selectedGenreDetail:function(){
            this.getSubGenre(this.selectedGenreDetail)
        }
    },
};
</script>

<style scoped>
label {
    font-weight: bold;
}

</style>