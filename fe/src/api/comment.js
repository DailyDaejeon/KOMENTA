import { setInterceptors } from './config/interceptors'

const instance = setInterceptors()

//실시간 베스트 댓글 유저 랭킹
function fetchBestComment() {
    return instance.get('comment/comment_rank');
}

//댓글입력
function commentInsert(commentInfo){
    return instance.post('comment/insert', commentInfo);
}

//회차별 댓글 정보
function fetchEpiComment(veId) {
    return instance.get(`comment/ve_c_list/${veId}`);
}

// 회원이 단 모든 댓글 조회
function fetchUserComment(uId){
    return instance.get('comment/comment_list',{
        params: {
            u_id : uId
        }
    })
}



// 댓글 좋아요 추가/ 취소
function userlikeComment(cInfo) {
    return instance.post('comment/comment_good_cancel', cInfo)
}

// 댓글 삭제 기능
function removeComment(cId) {
    return instance.delete('comment/comment_delete', cId)
}

export {
    fetchBestComment,
    commentInsert,
    fetchEpiComment,
    fetchUserComment,
    userlikeComment,
    removeComment
}