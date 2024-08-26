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
}
