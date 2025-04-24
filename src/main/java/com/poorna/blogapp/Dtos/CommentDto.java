package com.poorna.blogapp.Dtos;

import com.poorna.blogapp.Entities.BlogContents;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {


    private Long id;


    private String comment;


    private BlogContents blogcomment;

}
