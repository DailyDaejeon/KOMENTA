package com.komenta.be.service;

import com.komenta.be.model.comment.CommentInfoDTO;
import com.komenta.be.model.comment.VodEpisodeCommentDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    SqlSession sqlSession;
    @Override
    public int insertComment(CommentInfoDTO comment_info) {
        return 0;
    }

    @Override
    public List<VodEpisodeCommentDTO> getVodEpisodeComment(int ve_id) {
        return null;
    }
}
