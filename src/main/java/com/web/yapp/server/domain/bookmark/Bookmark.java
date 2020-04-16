package com.web.yapp.server.domain.bookmark;

import com.web.yapp.server.domain.musician.Musician;
import com.web.yapp.server.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Table(name = "TUNA_BOOKMARK")
@Entity
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOKMARK_ID")
    private Long id;

    @OneToOne
    @JoinColumn(name = "MUSICIAN_ID")
    private Musician musician;

    @ManyToOne // 북마크(n) -> 유저(1)
    @JoinColumn(name = "USER_ID")
    private User user;

}