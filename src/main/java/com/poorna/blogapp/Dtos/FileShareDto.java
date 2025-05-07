package com.poorna.blogapp.Dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileShareDto {


    String sender;

    String reciever;

    String fileUrl;
}
