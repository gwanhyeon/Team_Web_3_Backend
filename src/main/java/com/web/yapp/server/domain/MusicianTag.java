package com.web.yapp.server.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Table(name = "TUNA_MU_TAG_MAP")
@Entity
public class MusicianTag extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MU_TAG_MAP_ID")
    private Long id;

    @ManyToOne // 뮤지션 (1) : 태그 (N)
    @JoinColumn(name = "MUSICIAN_ID")
    private Musician musician;

    @ManyToOne // 태그 (1) : 뮤지션(N)
    @JoinColumn(name = "TAG_ID")
    private Tag tag;

    @Column(name = "MU_TAG_MAP_RPRSN") // 대표 : 1 , 일반 : 0
    private int represent;

    @Column(name = "MU_TAG_NM")
    private String muTagNM;

    @Column(name = "MU_TAG_CATEGORY_NM")
    private String categoryNM;

    @Builder
    public MusicianTag(Musician musician, Tag tag, int represent, String muTagNM, String categoryNM){
        this.musician = musician;
        this.tag = tag;
        this.represent = represent;
        this.muTagNM = muTagNM;
        this.categoryNM = categoryNM;
    }
}
