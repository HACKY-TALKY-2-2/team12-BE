package com.hackytalky.team12server.repository;

import com.hackytalky.team12server.entity.Post;
import com.hackytalky.team12server.entity.Taxichat;
import com.hackytalky.team12server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaxichatRepository extends JpaRepository<Taxichat, Long> {
    List<Taxichat> deleteByPostAndUser(Post post, User user);

    List<Taxichat> findByPostAndUser(Post post, User user);
    List<Taxichat> findByUser(User user);
}
