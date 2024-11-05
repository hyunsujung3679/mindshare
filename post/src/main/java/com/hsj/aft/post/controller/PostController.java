package com.hsj.aft.post.controller;

import com.hsj.aft.common.dto.CommonResponse;
import com.hsj.aft.post.dto.PostDto;
import com.hsj.aft.post.dto.request.InsertPostReq;
import com.hsj.aft.post.dto.request.UpdatePostReq;
import com.hsj.aft.post.dto.response.*;
import com.hsj.aft.post.service.PostService;
import com.hsj.aft.user.config.security.*;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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

        InsertPostRes response = new InsertPostRes();
        response.setPost(postDto);

        return ResponseEntity.ok(CommonResponse.success(response));
    }

    /**
     *
     * @param keyword
     * @param type (title : 제목, content : 내용, insertId : 작성자, modifyId : 수정자 )
     * @param page
     * @param size
     * @param sortBy (postNo : 게시글번호, viewCount : 조회수, insertDate : 등록일자, modifyDATE : 수정일자)
     * @param direction
     * @return
     */
    @GetMapping
    public ResponseEntity<CommonResponse> selectPostList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "postNo") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        if (size > 100) {
            size = 100;
        }

        if (size <= 0) {
            size = 1;
        }

        if (page < 0) {
            page = 0;
        }

        List<String> allowedFields = Arrays.asList("postNo", "viewCount", "insertDate", "modifyDate");
        if (!allowedFields.contains(sortBy)) {
            sortBy = "postNo";
        }

        Sort.Direction sortDirection = null;
        try {
            sortDirection = Sort.Direction.fromString(direction.toUpperCase());
        } catch (IllegalArgumentException e) {
            sortDirection = Sort.Direction.DESC;
        }

        Sort sort = Sort.by(sortDirection, sortBy);

        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<PostDto> postPage = postService.selectPostList(keyword, type, pageRequest);

        SelectPostListRes response = new SelectPostListRes();
        response.setTotal(postPage.getTotalElements());
        response.setTotalPages(postPage.getTotalPages());
        response.setCurrentPage(page);
        response.setPostList(postPage.getContent());

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
