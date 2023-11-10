package com.hackytalky.team12server.controller;

import com.hackytalky.team12server.dto.IdDto;
import com.hackytalky.team12server.dto.PostDto;
import com.hackytalky.team12server.service.PostService;
import com.hackytalky.team12server.service.TaxichatService;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final TaxichatService taxichatService;

    @PostMapping
    public ResponseEntity savePost(@RequestBody PostDto.saveRequest saveRequest) {
        Long postId = postService.savePost(saveRequest);
        return new ResponseEntity(Map.of("postId", postId), HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public PostDto.Details getPostDetails(@PathVariable Long postId,
                                          @RequestParam Double lat,
                                          @RequestParam Double lon) {
        System.out.println(lat + ", " + lon);
        return postService.findPostDetails(postId, lat, lon);
    }

    @DeleteMapping("/{postId}/taxichat")
    public ResponseEntity deleteTaxichat(@PathVariable Long postId, @RequestParam Long userId) {
        taxichatService.deleteTaxichat(postId, userId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/{postId}/taxichat")
    public ResponseEntity sendTaxichat(@PathVariable Long postId, @RequestBody IdDto userId) {
        Long taxichatId = taxichatService.sendTaxichat(postId, userId.getUserId());
        return new ResponseEntity(Map.of("taxichatId", taxichatId), HttpStatus.CREATED);
    }

    @PutMapping("/{postId}/taxichat")
    public ResponseEntity sendTaxichat(@PathVariable Long postId, @RequestParam Long userid) {
        taxichatService.acceptTaxichat(postId, userid);
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
