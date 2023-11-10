package com.hackytalky.team12server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    private String nickname;

    private Integer age;

    private String image;

    private String category;

    private String job;

    private String company;

    private String phoneNumber;

    private Integer yearOfExperience;

    private String workplaceAddress;

    private String residenceAddress;

    private String personality;

    private String interests;

    private Double workplaceLon;

    private Double workplaceLat;

    private Double residenceLon;

    private Double residenceLat;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    private List<Taxichat> taxichats;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;
}
