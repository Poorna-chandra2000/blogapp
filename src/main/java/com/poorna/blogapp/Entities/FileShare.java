package com.poorna.blogapp.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "FileSharingEntity")
public class FileShare {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;


    String sender;

    String reciever;

    String fileUrl;
}
