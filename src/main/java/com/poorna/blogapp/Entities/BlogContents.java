package com.poorna.blogapp.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogContents {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    private String title;

    private List<String> imageUrl;

    @Lob
    private String content;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="ParentBlogId")
    private Blog blog;

}
