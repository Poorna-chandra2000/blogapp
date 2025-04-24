package com.poorna.blogapp.Controllers;

import com.poorna.blogapp.Dtos.CommentDto;
import com.poorna.blogapp.Services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("{blogid}")
    ResponseEntity<CommentDto> createComment(@PathVariable(name="blogid") Long bid, @RequestBody CommentDto commentDto){

        return new ResponseEntity<>(commentService.createComment(bid, commentDto),HttpStatus.CREATED);

    }





}
