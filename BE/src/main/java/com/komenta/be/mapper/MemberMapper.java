package com.komenta.be.mapper;

import com.komenta.be.model.member.MemberDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

    MemberDTO getPw(String userid);
}
