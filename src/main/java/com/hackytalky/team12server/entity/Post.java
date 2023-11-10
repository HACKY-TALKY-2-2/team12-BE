package com.hackytalky.team12server.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long postId;

    @ColumnDefault("0")
    private Integer completed;

    private String departureAddress;

    private String destinationAddress;

    private LocalDateTime start;

    private LocalDateTime end;

    private String type;

    private String title;
    private String transformation;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    private Double departLon;

    private Double departLat;

    private Double destiLon;

    private Double destiLat;

    @OneToMany(mappedBy = "post")
    private List<Taxichat> taxichats;
}
