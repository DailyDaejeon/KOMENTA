import { setInterceptors } from './config/interceptors'

const instance = setInterceptors()

//인기 플레이 리스트
function fetchPopularPlayList() {
    return instance.get('');
}

//댓글순 vod 조회
function fetchCommentVOD() {
    return instance.get('');
}

//인기순 vod 조회
function fetchPopularVOD() {
    return instance.get('')
}

//최신순 vod조회
function fetchRecentVOD() {
    return instance.get('')
}

export {
    fetchPopularPlayList,
    fetchCommentVOD,
    fetchPopularVOD,
    fetchRecentVOD
}