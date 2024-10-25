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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<CommonResponse> insertPost(@RequestBody InsertPostReq post) {

        InsertPostRes response = new InsertPostRes();
        response.setPost(postService.insertPost(post));

        return new ResponseEntity<>(CommonResponse.success(response), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<CommonResponse> selectPostList() {

        List<PostDto> postList = postService.selectPostList();

        SelectPostListRes response = new SelectPostListRes();
        response.setTotal(postList.size());
        response.setPostList(postList);

        return new ResponseEntity<>(CommonResponse.success(response), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<CommonResponse> searchPostList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String type) {

        List<PostDto> postList = postService.searchPostList(keyword, type);

        SearchPostListRes response = new SearchPostListRes();
        response.setTotal(postList.size());
        response.setPostList(postList);

        return new ResponseEntity<>(CommonResponse.success(response), HttpStatus.OK);
    }

    @GetMapping("/{postNo}")
    public ResponseEntity<CommonResponse> selectPost(@PathVariable Integer postNo) {

        Post post = postService.selectPost(postNo);

        SelectPostRes response = new SelectPostRes();
        response.setPost(PostDto.from(post));

        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @PatchMapping("/{postNo}")
    public ResponseEntity<CommonResponse> updatePost(@PathVariable Integer postNo, @RequestBody UpdatePostReq post,  @AuthenticationPrincipal CustomUserDetails userDetails) {

        PostDto updatedPost = postService.updatePost(postNo, post, userDetails.getUserNo());

        UpdatePostRes response = new UpdatePostRes();
        response.setPost(updatedPost);

        return new ResponseEntity<>(CommonResponse.success(response), HttpStatus.OK);
    }

    @DeleteMapping("/{postNo}")
    public ResponseEntity<CommonResponse> deletePost(@PathVariable Integer postNo, @AuthenticationPrincipal CustomUserDetails userDetails) {

        PostDto deletedPost = postService.deletePost(postNo, userDetails.getUserNo());

        DeletePostRes response = new DeletePostRes();
        response.setPost(deletedPost);

        return new ResponseEntity<>(CommonResponse.success(response), HttpStatus.OK);
    }


}
