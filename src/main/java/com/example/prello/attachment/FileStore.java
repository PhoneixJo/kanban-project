package com.example.prello.attachment;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileStore {

    /**
     * 서버 내 파일 저장 경로 반환
     *
     * @return 파일 저장 경로
     */
    public String getDestinationFileUrl() {
        return "C:/Prello";
    }

    /**
     * 단일 파일 저장
     *
     * @param multipartFile 저장할 파일
     * @return 저장 파일명
     * @throws IOException File 로 변환 실패
     */
    public String storeFile(MultipartFile multipartFile) throws IOException {

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);

        File destinationFolder = new File(getDestinationFileUrl());

        destinationFolder.mkdirs();

        multipartFile.transferTo(new File(destinationFolder, storeFileName));
        return storeFileName;
    }

    /**
     * 서버 저장용 이름 생성, 랜덤 UUID
     *
     * @param originalFilename 원본 파일명
     * @return 랜덤 UUID 로 만든 저장용 파일명
     */
    private String createStoreFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = findExt(originalFilename);
        return uuid + "." + ext;
    }

    /**
     * 확장자 추출
     *
     * @param originalFilename 원본 파일명
     * @return 파일 확장자
     */
    public String findExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf('.');
        return originalFilename.substring(pos + 1);
    }
}
