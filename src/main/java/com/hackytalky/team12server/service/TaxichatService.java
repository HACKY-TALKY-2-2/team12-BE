package com.hackytalky.team12server.service;

import com.hackytalky.team12server.dto.UserDto;
import com.hackytalky.team12server.entity.Post;
import com.hackytalky.team12server.entity.Taxichat;
import com.hackytalky.team12server.entity.User;
import com.hackytalky.team12server.repository.PostRepository;
import com.hackytalky.team12server.repository.TaxichatRepository;
import com.hackytalky.team12server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaxichatService {
    private final TaxichatRepository taxichatRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Long sendTaxichat(Long postId, Long userId) {
        Taxichat taxichat = Taxichat.builder()
                .post(postRepository.findById(postId).orElseThrow())
                .user(userRepository.findById(userId).orElseThrow())
                .accepted(0)
                .userCompleted(0)
                .recieverCompleted(0)
                .build();

        return taxichatRepository.save(taxichat).getTaxichatId();
    }

    @Transactional
    public void deleteTaxichat(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow();
        User user = userRepository.findById(userId)
                .orElseThrow();
        taxichatRepository.deleteByPostAndUser(post, user);
    }

    public void acceptTaxichat(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow();
        User user = userRepository.findById(userId)
                .orElseThrow();

        List<Taxichat> taxichats = taxichatRepository.findByPostAndUser(post, user);
        taxichats.forEach(taxichat -> {
            taxichat.setAccepted(1);
            taxichatRepository.save(taxichat);
        });
    }

    public List<UserDto.TaxichatList> getTaxichats(Long userId, Boolean requested) {
        User user = userRepository.findById(userId).orElseThrow();
        List<UserDto.TaxichatList> result = new ArrayList<>();

        if (requested) { // taxichat에 있는 userId가 상대
            List<Taxichat> taxichats = taxichatRepository.findByUser(user);
            taxichats.forEach(taxichat -> {
                User opposite = taxichat.getUser();
                Post post = taxichat.getPost();
                result.add(UserDto.TaxichatList.builder()
                        .userId(opposite.getUserId())
                        .postId(post.getPostId())
                        .nickname(opposite.getNickname())
                        .destinationAddress(opposite.getResidenceAddress())
                        .departureAddress(opposite.getWorkplaceAddress())
                        .type(reverse(post.getType()))
                        .completed(post.getCompleted())
                        .build());
            });
        }
        else {
            List<Taxichat> taxichats = new ArrayList<>();
            user.getPosts()
                    .forEach(post -> taxichats.addAll(post.getTaxichats()));

            taxichats.forEach(taxichat -> {
                Post post = taxichat.getPost();
                User opposite = post.getUser();
                result.add(UserDto.TaxichatList.of(opposite, post));
            });
        }

        return result;
    }

    private static String reverse(String type) {
        if (type.equals("MENTOR")) {
            return "MENTEE";
        }
        return "MENTOR";
    }

    public void completeTaxichat(Long userId, Long taxichatId) {
        Taxichat taxichat = taxichatRepository.findById(taxichatId)
                .orElseThrow();
        if (userId == taxichat.getUser().getUserId()) {
            taxichat.setUserCompleted(1);
            taxichatRepository.save(taxichat);
            return;
        }
        taxichat.setRecieverCompleted(1);
        taxichatRepository.save(taxichat);
    }
}
