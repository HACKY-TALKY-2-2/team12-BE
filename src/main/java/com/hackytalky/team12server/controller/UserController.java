package com.hackytalky.team12server.controller;

import com.hackytalky.team12server.dto.RecommendedPostDto;
import com.hackytalky.team12server.dto.ReviewDto;
import com.hackytalky.team12server.dto.Route;
import com.hackytalky.team12server.dto.UserDto;
import com.hackytalky.team12server.service.PostService;
import com.hackytalky.team12server.service.ReviewService;
import com.hackytalky.team12server.service.TaxichatService;
import com.hackytalky.team12server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ReviewService reviewService;
    private final PostService postService;
    private final TaxichatService taxichatService;

    @PostMapping
    public ResponseEntity register(@RequestBody UserDto.RegisterRequest registerRequest) {
        System.out.println(registerRequest);
        Long userId = userService.register(registerRequest);
        return new ResponseEntity(Map.of("userId", userId), HttpStatus.CREATED);
    }

    @PostMapping("/{userId}/review")
    public ResponseEntity saveReview(@PathVariable Long userId,
                                     @RequestBody ReviewDto.saveRequest saveRequest) {
        Long reviewId = reviewService.saveReview(saveRequest, userId);

        return new ResponseEntity(Map.of("reviewId", reviewId), HttpStatus.CREATED);
    }

    @PostMapping("/{userId}/recommended-post")
    public List<RecommendedPostDto> getPosts(@PathVariable Long userId,
                                             @RequestBody Route.postRequest postRequest) {
        return postService.findRecommendPosts(userId);
    }

    @GetMapping("/{userId}/recommended-post")
    public List<RecommendedPostDto> getPosts(@PathVariable Long userId) {
        return postService.findRecommendPosts(userId);
    }

    @GetMapping("/{userId}/taxichat")
    public List<UserDto.TaxichatList> getTaxichatList(@PathVariable Long userId, @RequestParam Boolean requested) {
        return taxichatService.getTaxichats(userId, requested);
    }
}
