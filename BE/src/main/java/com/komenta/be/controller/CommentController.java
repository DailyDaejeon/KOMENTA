package com.komenta.be.controller;

import com.komenta.be.model.comment.CommentInfoDTO;
import com.komenta.be.model.comment.VodEpisodeCommentDTO;
import com.komenta.be.model.member.AuthPhoneDTO;
import com.komenta.be.model.member.MemberDTO;
import com.komenta.be.service.CommentService;
import com.komenta.be.service.JwtService;
import com.komenta.be.service.MemberService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

//거기서 댓글 등록하면 댓글 입력
//그 회차의 모든 댓글 리스트


@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value ="/comment")
public class CommentController {

    @Autowired
    CommentService cservice;


    @ApiOperation(value = "댓글 입력", notes = "댓글을 입력하면 해당 댓글을 DB에 저장하고 이를 반환")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "comment_info", value = "c_contents(댓글 내용), u_playtime(등록 시간), u_id(회원 아이디), ve_id(vod 회차 아이디)", dataType = "CommentInfoDTO", required = true)
    })
    @PostMapping("/insert")
    public int insertComment(@RequestBody CommentInfoDTO comment_info){
        return cservice.insertComment(comment_info);
    }




    @ApiOperation(value = "회차별 댓글 정보", notes = "해당 회차의 모든 댓글 정보를 반환")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ve_id", value = "회차 아이디", dataType = "int",required = true)
    })
    @GetMapping("/ve_c_list/{ve_id}")
    public List<VodEpisodeCommentDTO> getVodEpisodeComment(@PathVariable("ve_id") int ve_id){
        return cservice.getVodEpisodeComment(ve_id);
    }

}
