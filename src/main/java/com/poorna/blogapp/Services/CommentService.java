package com.poorna.blogapp.Services;


import com.poorna.blogapp.Dtos.CommentDto;
import com.poorna.blogapp.Entities.BlogContents;
import com.poorna.blogapp.Entities.Comments;
import com.poorna.blogapp.Repositories.BlogContentRepo;

import com.poorna.blogapp.Repositories.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.rmi.NotBoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {


    private final CommentRepository commentRepository;
    private final BlogContentRepo blogContentRepo;
    private final ModelMapper modelMapper;


    public CommentDto createComment(Long bid, CommentDto commentDto) {

//        BlogContents blogContents=blogContentRepo.findById(bid).orElse(null);

        if(!blogContentRepo.existsById(bid)){
         throw new RuntimeException("Blog not found");
        }
        Optional<BlogContents> blogContents=blogContentRepo.findById(bid);
        Comments newcomment=modelMapper.map(commentDto,Comments.class);

        blogContents.flatMap(blog->{
            newcomment.setBlogcomment(blog);

            return Optional.of(commentRepository.save(newcomment));

        }).orElseThrow(()->new RuntimeException("Blog not found"));


        return null;
    }


}
