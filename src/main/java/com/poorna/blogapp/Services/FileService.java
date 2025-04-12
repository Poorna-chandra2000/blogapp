package com.poorna.blogapp.Services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public interface FileService {

    String uploadFile(String path, MultipartFile file) throws IOException;
    public Boolean deleteFileFromLocalServer(String filePath) ;

    Resource downloadFile(String fileName) throws MalformedURLException;
}
