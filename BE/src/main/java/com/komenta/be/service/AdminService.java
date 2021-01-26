package com.komenta.be.service;

import com.komenta.be.model.member.MemberDTO;
import com.komenta.be.model.vod.VodDTO;
import com.komenta.be.model.vod.VodEpisodeDTO;

import java.util.List;

public interface AdminService {

    public List<MemberDTO> selectAllMember();
    public int updateMember(String u_email);
    public int deleteMember(String u_email);

    public int registVod(VodDTO vod);
    public List<VodDTO> selectAllVod();
    public int updateVod(VodDTO vod);
    public int deleteVod(int v_id);

    public int uploadEpisode(VodEpisodeDTO episode);
    public List<VodEpisodeDTO> selectEpisode(int v_id);
    public int updateEpisode(VodEpisodeDTO episode);
    public int deleteEpisode(int ve_id);

}
