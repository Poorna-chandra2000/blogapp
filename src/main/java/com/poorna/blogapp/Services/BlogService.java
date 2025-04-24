package com.poorna.blogapp.Services;


import com.poorna.blogapp.Dtos.BlogDto;
import com.poorna.blogapp.Entities.Blog;
import com.poorna.blogapp.GoogleCloudStorageService.GCSService;
import com.poorna.blogapp.Repositories.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;
    private final ModelMapper modelMapper;
    private final FileService fileService;
    private final GCSService gcsService;

    @Value("${project.image}")
    private String imagePath;

    //upgrade 1 blog can have multiple images use list<String> images
    //add each url to list

    //next deletion of just images separately


    public BlogDto saveBlog(BlogDto blogDto, MultipartFile image) throws IOException {
   // Upload the image and store it in server

        String imgpathurl = fileService.uploadFile(imagePath, image);

        // Create the URL for the uploaded image
       String imageUrl = "/images/" + imgpathurl;

        Blog blog = modelMapper.map(blogDto, Blog.class);
        blog.setImgurl(imageUrl);

        return modelMapper.map(blogRepository.save(blog), BlogDto.class);
    }


//     // uncomment this and comment the local upload code
//    //for cloud storage service
//    public BlogDto saveBlog(BlogDto blogDto, MultipartFile image) throws IOException {
//        // Upload the image and store it in server
//        //for cloud storage
//        String imageUrl = gcsService.uploadFile(image);
//
//        // Create the URL for the uploaded image
//        //String imageUrl = "/images/" + imgpathurl;
//
//        Blog blog = modelMapper.map(blogDto, Blog.class);
//        blog.setImgurl(imageUrl);
//
//
//        return modelMapper.map(blogRepository.save(blog), BlogDto.class);
//    }



    public String saveimg(Long id, MultipartFile image) throws IOException {
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Blog not found with id: {}" + id));
        String imgpathurl = fileService.uploadFile(imagePath, image);
        blog.setImgurl(imgpathurl);
        blogRepository.save(blog);
        return imgpathurl;
    }

    public void deleteBlog(Long id) {
        Blog blog = blogRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Blog not found with id: " + id));

        // Delete the image file from the server
        //just removes images which i appened while saving in database
        if(!fileService.deleteFileFromLocalServer(blog.getImgurl().substring("/images/".length()))){
            throw new RuntimeException("file not deleted");
        }

        // Delete the blog entry from the database
        blogRepository.delete(blog);
    }




    public List<BlogDto> getAllBlog() {

            List<Blog> blogs=blogRepository.findAll();
            List<BlogDto> blogDtos=new ArrayList<>();
            for(Blog blog:blogs){
                BlogDto blogDto=modelMapper.map(blog,BlogDto.class);
                blogDtos.add(blogDto);
            }
            return  blogDtos;
    }


}
