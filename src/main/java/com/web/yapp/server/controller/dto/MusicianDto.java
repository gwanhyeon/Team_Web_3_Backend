package com.web.yapp.server.controller.dto;

import com.web.yapp.server.domain.BaseTimeEntity;
import com.web.yapp.server.domain.Musician;
import com.web.yapp.server.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MusicianDto {
    private Long id;
    private User userId;
    @NotEmpty(message = "뮤지션 경력은 필수입니다.")
    private String career;
    @NotEmpty(message = "뮤지션 이름은 필수입니다.")
    private String nickNm;
    @NotEmpty(message = "뮤지션 소개는 필수입니다.")
    private String introduction;

    private String cellPhone;
    private String portFolioLink;
    private String snsNm;
    private Long snsType;
    private String workStage01;
    private String workStage02;
    private String workStage03;
    private String qstnAns01;
    private String qstnAns02;
    private String qstnAns03;
    private String qstnAns04;
    private Long styPrc01;
    private Long styPrc02;
    private Long styPrc03;
    private String styExpln01;
    private String styExpln02;
    private String styExpln03;
    private String profileUrl;
    private Long bookmarkCount;

    private AtmosphereDto atmosphereList;
    private GenreDto genreList;
    private InstrumentDto instrumentList;
    private SpecialDto specialList;
    private ThemeDto themeList;
    private List<MultipartFile> multipartFiles;
   /* private List<MultipartFile> portfolioFile;
    private List<MultipartFile> mainSongFile;
    private List<MultipartFile> subSongFile;*/



    public MusicianDto(Musician Entity){
        this.id = Entity.getId();
        this.userId = Entity.getUserId();
        this.career = Entity.getCareer();
        this.nickNm = Entity.getNickNm();
        this.introduction = Entity.getIntroduction();
        this.cellPhone = Entity.getCellPhone();
        this.portFolioLink = Entity.getPortFolioLink();
        this.snsNm = Entity.getSnsNm();
        this.snsType = Entity.getSnsType();
        this.workStage01 = Entity.getWorkStage01();
        this.workStage02 = Entity.getWorkStage02();
        this.workStage03 = Entity.getWorkStage03();
        this.qstnAns01 = Entity.getQstnAns01();
        this.qstnAns02 = Entity.getQstnAns02();
        this.qstnAns03 = Entity.getQstnAns03();
        this.qstnAns04 = Entity.getQstnAns04();
        this.styPrc01 = Entity.getStyPrc01();
        this.styPrc02 = Entity.getStyPrc02();
        this.styPrc03 = Entity.getStyPrc03();
        this.styExpln01 = Entity.getStyExpln01();
        this.styExpln02 = Entity.getStyExpln02();
        this.styExpln03 = Entity.getStyExpln03();
        this.profileUrl = Entity.getProfileUrl();
        this.bookmarkCount = Entity.getBookmarkCount();

    }

    public Musician toEntity(){
        return Musician.builder()
                .id(id)
                .userId(userId)
                .career(career)
                .nickNm(nickNm)
                .introduction(introduction)
                .cellPhone(cellPhone)
                .portFolioLink(portFolioLink)
                .snsNm(snsNm)
                .snsType(snsType)
                .workStage01(workStage01)
                .workStage02(workStage02)
                .workStage03(workStage03)
                .qstnAns01(qstnAns01)
                .qstnAns02(qstnAns02)
                .qstnAns03(qstnAns03)
                .qstnAns04(qstnAns04)
                .styPrc01(styPrc01)
                .styPrc02(styPrc02)
                .styPrc03(styPrc03)
                .styExpln01(styExpln01)
                .styExpln02(styExpln02)
                .styExpln03(styExpln03)
                .profileUrl(profileUrl)
                .bookmarkCount(0L) //O으로 초기 설정
                .build();
    }

}