package com.poorna.blogapp.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadService implements FileService{

    @Value("${project.image}")//template literals ${}
    private  String foldername;

    @Override
    public String uploadFile(String path, MultipartFile file) throws IOException {
        // Use imagePath instead of the passed `path`
        path = foldername;

        //get file names of currrent or original file
        String originalFileName=file.getOriginalFilename();
        //Generate a unique file name we random uui
        String randomId= UUID.randomUUID().toString();
        //example:mat.jpg --> 1234 --> 1234.jpg
        String fileName=randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));//this preserves original extension
        String filePath=path + File.separator + fileName;//we also need to know the file path

        //check if path exist and create
        File folder=new File(path);
        if(!folder.exists()) folder.mkdir();


        //upload to server
        Files.copy(file.getInputStream(), Paths.get(filePath));

        //returing file name
        return fileName;
    }

   public Boolean deleteFileFromLocalServer(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            File file = new File(foldername + File.separator + filePath);
            if (file.exists()) {
                return   file.delete();

            }
        }
        return false;
    }

    // ✅ Download file as Resource
    public Resource downloadFile(String fileName) throws MalformedURLException {
        Path path = Paths.get(foldername).resolve(fileName).normalize();
        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists()) {
            throw new RuntimeException("File not found: " + fileName);
        }

        return resource;
    }
}
