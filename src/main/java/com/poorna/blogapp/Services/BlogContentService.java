package com.poorna.blogapp.Services;

import com.poorna.blogapp.Dtos.BlogContentDto;
import com.poorna.blogapp.Entities.Blog;
import com.poorna.blogapp.Entities.BlogContents;
import com.poorna.blogapp.Repositories.BlogContentRepo;
import com.poorna.blogapp.Repositories.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogContentService {

    private final ModelMapper modelMapper;
    private final BlogContentRepo blogContentRepo;
    private final BlogRepository blogRepository;

    private final FileService fileService;

//    public BlogContentDto createContent(Long id, BlogContentDto blogContentDto) {
//
//        Blog foreignkey=blogRepository.findById(id).orElse(null);
//        BlogContents blogContents =modelMapper.map(blogContentDto,BlogContents.class);
//        blogContents.setBlog(foreignkey);
//
////        BlogContents savedentity=blogContentRepo.save(blogContents);
////        return modelMapper.map(savedentity,BlogContentDto.class);
//
//        BlogContentDto blogContentDto1=modelMapper.map(blogContentRepo.save(blogContents),BlogContentDto.class);
//
//        return blogContentDto1;
//
//        //direct way
//        //return modelMapper.map(blogContentRepo.save(blogContents),BlogContentDto.class);
//
////        int a=b+c;
////
////        return b+c;
//
//    }


    public BlogContentDto createContent(Long id, BlogContentDto blogContentDto) {

        Blog foreignkey=blogRepository.findById(id).orElse(null);
        BlogContents blogContents =modelMapper.map(blogContentDto,BlogContents.class);
        blogContents.setBlog(foreignkey);


        BlogContentDto blogContentDto1=modelMapper.map(blogContentRepo.save(blogContents),BlogContentDto.class);

        return blogContentDto1;

    }

    public String saveListsImg(Long id, List<MultipartFile> image) {

        //if using optional<> use flatmap incase of setting and getting
        //donot use streams.map for setting and getting not good practice
        Optional<BlogContents> blogContents=blogContentRepo.findById(id);


        blogContents.flatMap(content->{//if using optional<> use flatmap incase of setting and getting
            // Initialize list if null
            if(content.getImageUrl()==null){
                content.setImageUrl(new ArrayList<>());
            }

            image.forEach(img->{
                //loop though images save it get the url set it in listof images of blog content
                //i.e update it
                try {
                    String imgUrl=fileService.uploadFile(null,img);
                    content.getImageUrl().add(imgUrl);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });


            return Optional.of( blogContentRepo.save(content));

        }).orElseThrow(()->new RuntimeException("Upload failed"));


      return "Images uploaded Successfully";
    }


    public List<BlogContentDto> getAll() {

        return blogContentRepo
                .findAll()
                .stream().map(blog->modelMapper.map(blog,BlogContentDto.class))
                .collect(Collectors.toList());
    }
}
