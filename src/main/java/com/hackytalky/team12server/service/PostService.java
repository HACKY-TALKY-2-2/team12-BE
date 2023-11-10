package com.hackytalky.team12server.service;

import com.hackytalky.team12server.domain.Coordination;
import com.hackytalky.team12server.dto.PostDto;
import com.hackytalky.team12server.dto.RecommendedPostDto;
import com.hackytalky.team12server.dto.Route;
import com.hackytalky.team12server.dto.UserDto;
import com.hackytalky.team12server.entity.Post;
import com.hackytalky.team12server.entity.Review;
import com.hackytalky.team12server.entity.ReviewFeedback;
import com.hackytalky.team12server.entity.User;
import com.hackytalky.team12server.repository.PostRepository;
import com.hackytalky.team12server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final RouteService routeService;

    public PostDto.Details findPostDetails(Long postId, Double lat, Double lon) {
        Post post = postRepository.findById(postId)
                .orElseThrow();
        Set<String> set = new HashSet<>();
        AtomicReference<Integer> gradeSum = new AtomicReference<>(0);
        User user = post.getUser();
        user.getReviews().forEach(review -> {
                    fillSet(review, set);
                    gradeSum.updateAndGet(v -> v + review.getGrade());
                });
        UserDto.Details userInfo = UserDto.Details.of(user,
                set.stream().toList(),
                gradeSum.get() / user.getReviews().size());
        PostDto.TimeRange timeRange = PostDto.TimeRange.fromPost(post);


        //todo : 경로 만들기
        Coordination start = Coordination.builder()
                .lat(post.getDepartLat())
                .lon(post.getDepartLon())
                .build();
        Coordination viewer = Coordination.builder()
                .lat(lat)
                .lon(lon)
                .build();
        Coordination poster = Coordination.builder()
                .lat(post.getDestiLat())
                .lon(post.getDestiLon())
                .build();

        Route route = routeService.findRoute(start, viewer, poster);
        return PostDto.Details.of(post, userInfo, route);
    }

    private void fillSet(Review review, Set<String> set) {
        for (ReviewFeedback reviewFeedback : review.getReviewFeedbacks()) {
            set.add(reviewFeedback.getFeedback());
        }
    }

    public Long savePost(PostDto.saveRequest saveRequest) {
        User user = userRepository.findById(saveRequest.getUserId())
                .orElseThrow();
        Post post = saveRequest.toPostEntity();
        post.setUser(user);

        return postRepository.save(post).getPostId();
    }

    public List<RecommendedPostDto> findRecommendPosts(Long userId) {
        List<Post> posts = postRepository.findByCompletedFalse();
        return posts.stream()
                .map(post -> {
                    User user = post.getUser();
                    UserDto.Info userInfo = UserDto.Info.fromUser(user);
                    PostDto.TimeRange timeRange = PostDto.TimeRange.fromPost(post);
                    return RecommendedPostDto.of(post, userInfo, timeRange);
                })
                .toList();
    }
}
