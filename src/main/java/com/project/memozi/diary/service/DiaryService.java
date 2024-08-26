package com.project.memozi.diary.service;

import com.project.memozi.diary.dto.DiaryRequestDto;
import com.project.memozi.diary.dto.DiaryResponseDto;
import com.project.memozi.diary.entity.Diary;
import com.project.memozi.diary.repository.DiaryRepository;
import com.project.memozi.kakao.entity.Member;
import com.project.memozi.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public DiaryResponseDto addDiary(List<MultipartFile> images, DiaryRequestDto diaryRequestDto, Member member) throws IOException{
        Diary diary = new Diary(diaryRequestDto, member);
        if (!diary.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
        List<String> imagesUrls = new ArrayList<>();
        if(images !=null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                String imagesUrl = s3Uploader.upload(image, "image");
                imagesUrls.add(imagesUrl);
            }
        }
        diary.addImages(imagesUrls);
        diaryRepository.save(diary);
        return new DiaryResponseDto(diary);
    }

    @Transactional(readOnly = true)
    public List<DiaryResponseDto> getDiary (Member member){
        List<Diary>diaries = diaryRepository.findAllByOrderByCreatedAtDesc();
        List<DiaryResponseDto> diaryResponseDto = new ArrayList<>();
        for (Diary diary : diaries){
            diaryResponseDto.add(new DiaryResponseDto(diary));
        }
        return diaryResponseDto;
    }

    @Transactional(readOnly = true)
    public DiaryResponseDto getDetailDiary (Long diaryId, Member member){
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(()-> new IllegalArgumentException("해당 다이어리가 없습니다"));

        if(!diary.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("권한이 없습니다");
        }

        return new DiaryResponseDto(diary);
    }


}
