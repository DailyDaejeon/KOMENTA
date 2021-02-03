package com.komenta.be.service;

import com.komenta.be.mapper.VodMapper;
import com.komenta.be.model.vod.VodEpisodeAllDTO;
import com.komenta.be.model.vod.VodEpisodeDTO;
import com.komenta.be.model.vod.VodHistoryDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VodServiceImpl implements VodService{

    @Autowired
    SqlSession sqlSession;

    @Override
    public List<VodHistoryDTO> selectHistoryById(int u_id) {

        return sqlSession.getMapper(VodMapper.class).selectHistoryById(u_id);
    }

    @Override
    public VodEpisodeAllDTO selectEpisodeById(int ve_id) {
        return sqlSession.getMapper(VodMapper.class).selectEpisodeById(ve_id);
    }

    @Override
    public List<VodEpisodeDTO> getVodListRecent() {
        return sqlSession.getMapper(VodMapper.class).getVodListRecent();
    }

    @Override
    public List<VodEpisodeDTO> getVodListComment() {
        return sqlSession.getMapper(VodMapper.class).getVodListComment();
    }

    @Override
    public List<VodEpisodeDTO> getVodListWatching() {
        return sqlSession.getMapper(VodMapper.class).getVodListWatching();
    }
}
