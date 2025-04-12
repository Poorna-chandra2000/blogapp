package com.poorna.blogapp.Dtos;

import com.poorna.blogapp.Entities.Blog;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogContentDto {

    private Long Id;

    private String title;

    private List<String> imageUrl;


    private String Content;

    private Blog blog;

}
