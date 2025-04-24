package com.poorna.blogapp.Entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comments {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 100)
    @Lob
    private String comment;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "BlogContentId")
    private BlogContents blogcomment;

}
