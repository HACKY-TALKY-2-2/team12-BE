package com.hackytalky.team12server.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Taxichat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long taxichatId;

    @ColumnDefault("0")
    private Integer userCompleted;

    @ColumnDefault("0")
    private Integer recieverCompleted;

    @ColumnDefault("0")
    private Integer accepted;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "postId", nullable = false)
    private Post post;
}
