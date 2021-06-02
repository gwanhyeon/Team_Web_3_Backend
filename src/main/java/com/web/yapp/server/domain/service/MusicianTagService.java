package com.web.yapp.server.domain.service;
import com.web.yapp.server.controller.dto.TagDto;
import com.web.yapp.server.domain.Musician;
import com.web.yapp.server.domain.MusicianTag;
import com.web.yapp.server.domain.Tag;
import com.web.yapp.server.domain.repository.MusicianRepository;
import com.web.yapp.server.domain.repository.MusicianTagRepository;
import com.web.yapp.server.domain.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)     /* 전체 서비스를 Readonly로 처리 단, 회원가입의 경우만 트랜잭션 처리 */
@RequiredArgsConstructor
public class MusicianTagService {
    private final MusicianRepository musicianRepository;
    private final MusicianTagRepository musicianTagRepository;
    private final TagRepository tagRepository;

    /**
     * 태그등록
     * @param tagList
     * @param musician
     * @param categoryNM
     */
    public void saveMusicianTag(List<String> tagList, Musician musician, String categoryNM){
        for(int i=0;i<tagList.size();i++){
            String tagNM = tagList.get(i);
            Tag tag = tagRepository.findTagByTagNM(tagNM);
            MusicianTag musicianTag;

            musicianTag = MusicianTag.builder()
                    .musician(musician)
                    .tag(tag)
                    .represent(0)
                    .muTagNM(tagNM)
                    .categoryNM(categoryNM)
                    .build();

            if(i==0 && !(categoryNM.equals("작업")))  { //대표태그, 작업태그는 대표태그 X
                musicianTag = MusicianTag.builder()
                        .musician(musician)
                        .tag(tag)
                        .represent(1)
                        .muTagNM(tagNM)
                        .categoryNM(categoryNM)
                        .build();
            }
            musicianTagRepository.save(musicianTag);
        }
    }

    /**
     * 대표태그 조회
     * @param musicianId
     * @return
     */
    public List<String> findRPTagByMusician(Long musicianId){
        List<Tag> RPTags = musicianTagRepository.findRPTagByMusician(musicianId);
        List<String> RPTagNMList = new LinkedList<String>();

        for (Tag tag: RPTags
        ) {
            RPTagNMList.add(tag.getTagNM());
        }
        return RPTagNMList;
    }

    /**
     * 태그리스트로 뮤지션 조회
     * 선택한 태그를 모두 가진 뮤지션 조회
     * 큐레이션, 탐색에 필요
     * @param tagList
     * @return
     */
    public List<Musician> findMusicianByTags(List<String> tagList, String categoryNM){
        Map<Musician, Integer> map = new HashMap<Musician, Integer>();
        List<Musician> result = new LinkedList<>();
        int max = 0;

        for (int i=0;i<tagList.size();i++){
            String tagNM = tagList.get(i);
            log.info("MusicianTagService findMusicianByTags 태그명 : "+ tagList.get(i));
            if(tagNM.equals("선택안함") || tagNM.equals("제한없음") || tagNM.equals("")) {
                result = musicianRepository.findMusicianByNew();
                return result;
            }
            Tag tag = tagRepository.findTagByTagNM(tagNM);
            if (tag == null) break;

            Long tagId = tag.getId();
            List<Musician> musicians = musicianTagRepository.findMusicianByTag(tagId);

            if(musicians == null) break;
            for (Musician musician : musicians) {
                System.out.println(tagNM +", musicianId " + musician.getId() +"tagId :"+tag.getId());
                int value = map.containsKey(musician) ? map.get(musician)+1 : 1 ;
                map.put(musician,value);
                max = Math.max(value,max);
            }
        }

        Tag noSelectTag = tagRepository.findTagByTagNM("선택안함");
        Long noSelectTagId = noSelectTag.getId();
        List<Musician> noSelectMuisicians = musicianTagRepository.findNoOptionMusicianByTag(noSelectTagId, categoryNM);

        Tag noLimitTag = tagRepository.findTagByTagNM("제한없음");
        Long noLimitTagId = noLimitTag.getId();
        List<Musician> noLimitMuisicians = musicianTagRepository.findNoOptionMusicianByTag(noLimitTagId, categoryNM);

        for (Musician musician : noSelectMuisicians) {
            map.put(musician, 0);
        }

        for (Musician musician : noLimitMuisicians) {
            map.put(musician, 0);
        }
        for(Map.Entry<Musician, Integer> elem : map.entrySet()){
            if(elem.getValue() == max || elem.getValue() == 0){
                result.add(elem.getKey());
            }
        }
        return result;
    }

    /**
     * 작업태그 조회
     * @param musicianId
     * @return
     */
    public List<String> findSpclNoteTagByMusician(Long musicianId){
        List<Tag> tags = musicianTagRepository.findSpclNoteTagByMusician(musicianId);
        List<String> tagNMList = new LinkedList<String>();
        for (Tag tag:tags
        ) {
            tagNMList.add(tag.getTagNM());
        }
        return tagNMList;
    }

    /**
     * 뮤지션이 가진 태그 조회
     * 카테고리별로 태그 담아서 return
     * @param musicianId
     * @return
     */
    public Map<String, Object> findTagByMusician(Long musicianId){
        List<TagDto> tags = musicianTagRepository.findTagByMusician(musicianId).stream()
                .map(TagDto::new)
                .collect(Collectors.toList());
        Map<String,Object> map = new HashMap<>();
        List<String> themeList = new LinkedList<>();
        List<String> genreList = new LinkedList<>();
        List<String> atmoList = new LinkedList<>();
        List<String> instruList = new LinkedList<>();

        for (TagDto t:tags) {
            switch (t.getCategoryNM()){
                case "테마":
                    themeList.add(t.getTagNM());
                    break;
                case "장르":
                    genreList.add(t.getTagNM());
                    break;
                case "분위기":
                    atmoList.add(t.getTagNM());
                    break;
                default:
                    instruList.add(t.getTagNM());
                    break;
            }
        }
        map.put("theme",themeList);
        map.put("genre",genreList);
        map.put("atmo",atmoList);
        map.put("instru",instruList);
        return map;
    }
}