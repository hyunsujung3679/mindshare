package com.hsj.aft.post.controller;

import com.hsj.aft.common.dto.CommonResponse;
import com.hsj.aft.domain.entity.Post;
import com.hsj.aft.post.dto.PostDto;
import com.hsj.aft.post.dto.request.InsertPostReq;
import com.hsj.aft.post.dto.request.UpdatePostReq;
import com.hsj.aft.post.dto.response.*;
import com.hsj.aft.post.service.PostService;
import com.hsj.aft.user.config.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * 게시판 등록
     * @param post
     * @param userDetails
     * @return
     */
    @PostMapping
    public ResponseEntity<CommonResponse> insertPost(
            @RequestBody InsertPostReq post,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        PostDto postDto = postService.insertPost(post, userDetails.getUserNo());
        postDto.setInsertId(userDetails.getUsername());
        postDto.setModifyId(userDetails.getUsername());

        InsertPostRes response = new InsertPostRes();
        response.setPost(postDto);

        return ResponseEntity.ok(CommonResponse.success(response));
    }

    /**
     * 게시판 목록 조회
     * @return
     */
    @GetMapping
    public ResponseEntity<CommonResponse> selectPostList() {
        List<PostDto> postList = postService.selectPostList();

        SelectPostListRes response = new SelectPostListRes();
        response.setTotal(postList.size());
        response.setPostList(postList);

        return ResponseEntity.ok(CommonResponse.success(response));
    }

    /**
     * 게시판 검색
     * @param keyword
     * @param type (title:제목, content:내용, insertId:작성자, modifyId:수정자)
     * @return
     */
    @GetMapping("/search")
    public ResponseEntity<CommonResponse> selectPostListBySearch(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String type) {
        List<PostDto> postList = postService.selectPostList(keyword, type);

        SearchPostListRes response = new SearchPostListRes();
        response.setTotal(postList.size());
        response.setPostList(postList);

        return ResponseEntity.ok(CommonResponse.success(response));
    }

    /**
     * 게시판 상세 조회
     * @param postNo
     * @return
     */
    @GetMapping("/{postNo}")
    public ResponseEntity<CommonResponse> selectPost(@PathVariable Integer postNo) {
        PostDto post = postService.selectPost(postNo);

        SelectPostRes response = new SelectPostRes();
        response.setPost(post);

        return ResponseEntity.ok(CommonResponse.success(response));
    }

    /**
     * 게시판 수정
     * @param postNo
     * @param post
     * @param userDetails
     * @return
     */
    @PatchMapping("/{postNo}")
    public ResponseEntity<CommonResponse> updatePost(
            @PathVariable Integer postNo,
            @RequestBody UpdatePostReq post,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        PostDto updatedPost = postService.updatePost(postNo, post, userDetails.getUserNo());

        UpdatePostRes response = new UpdatePostRes();
        response.setPost(updatedPost);

        return ResponseEntity.ok(CommonResponse.success(response));
    }

    /**
     * 게시판 삭제
     * @param postNo
     * @param userDetails
     * @return
     */
    @DeleteMapping("/{postNo}")
    public ResponseEntity<CommonResponse> deletePost(
            @PathVariable Integer postNo,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        PostDto deletedPost = postService.deletePost(postNo, userDetails.getUserNo());

        DeletePostRes response = new DeletePostRes();
        response.setPost(deletedPost);

        return ResponseEntity.ok(CommonResponse.success(response));
    }

}
