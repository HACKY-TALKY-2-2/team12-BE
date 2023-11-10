package com.hackytalky.team12server.service;

import com.hackytalky.team12server.S3Uploader;
import com.hackytalky.team12server.entity.Image;
import com.hackytalky.team12server.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final S3Uploader s3Uploader;

    public String saveImage(MultipartFile multipartFile, Image image) throws IOException {
        String url = s3Uploader.upload(multipartFile, "testDir");
        image.setImageUrl(url);
        imageRepository.save(image);

        return url;
    }
}
