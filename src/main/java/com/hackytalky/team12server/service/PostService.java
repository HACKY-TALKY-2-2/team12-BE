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

    public List<RecommendedPostDto> findAllPosts(Long userId) {
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

    public List<RecommendedPostDto> findRecommendedPosts(Long userId, Route.postRequest postRequest) {
        List<Post> posts = postRepository.findByCompletedFalse();
        posts = posts.stream()
                .filter(post -> checkDistance(
                        postRequest.getDepartureLat(),
                        postRequest.getDepartureLon(),
                        post.getDepartLat(),
                        post.getDepartLon())
                )
                .filter(post -> checkDirection(
                        postRequest.getDepartureLat(),
                        postRequest.getDepartureLon(),
                        postRequest.getDestinationLat(),
                        postRequest.getDestinationLon(),
                        post.getDepartLat(),
                        post.getDepartLon(),
                        post.getDestiLat(),
                        post.getDestiLon()
                ))
                .toList();


        return posts.stream()
                .map(post -> {
                    User user = post.getUser();
                    UserDto.Info userInfo = UserDto.Info.fromUser(user);
                    PostDto.TimeRange timeRange = PostDto.TimeRange.fromPost(post);
                    return RecommendedPostDto.of(post, userInfo, timeRange);
                })
                .toList();
    }

    private boolean checkDirection(Double lat1, Double lon1, Double lat2, Double lon2,
                                   Double lat3, Double lon3, Double lat4, Double lon4) {
        return calculateAngle(lat1, lon1, lat2, lon2, lat3, lon3, lat4, lon4) > 60;
    }

    private static double calculateAngle(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        double slope1 = (y2 - y1) / (x2 - x1);
        double slope2 = (y4 - y3) / (x4 - x3);

        double slopeDifference = Math.atan((slope2 - slope1) / (1 + slope1 * slope2));

        double angle = Math.toDegrees(slopeDifference);

        if (angle < 0) {
            angle += 180;
        }

        return angle;
    }


    private static boolean checkDistance(double lat1, double lon1, double lat2, double lon2) {
        double distance = calculateDistance(lat1, lon1, lat2, lon2);
        return distance < 0.5d;
    }

    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;

        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        double dlon = lon2Rad - lon1Rad;
        double dlat = lat2Rad - lat1Rad;
        double a = Math.sin(dlat / 2) * Math.sin(dlat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(dlon / 2) * Math.sin(dlon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}
