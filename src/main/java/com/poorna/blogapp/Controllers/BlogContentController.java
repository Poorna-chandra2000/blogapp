package com.poorna.blogapp.Controllers;

import com.poorna.blogapp.Dtos.BlogContentDto;
import com.poorna.blogapp.Services.BlogContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blog/childBlog")
public class BlogContentController {

    private final BlogContentService blogContentService;

//    @PostMapping("/contents/{parentid}")
//    ResponseEntity<BlogContentDto> createContent(@PathVariable(name = "id") Long id,
//                                                        @RequestBody BlogContentDto blogContentDto){
//
//
//        return ResponseEntity.ok(blogContentService.createContent(id,blogContentDto));
//    }

    @PostMapping("/contents/{parentid}")
    ResponseEntity<BlogContentDto> createContent(@PathVariable(name = "parentid") Long id,
                                                 @RequestBody BlogContentDto blogContentDto) {
        return ResponseEntity.ok(blogContentService.createContent(id, blogContentDto));
    }


    @PutMapping("/updateimage/{id}")
    public ResponseEntity<String> uploadimage(@PathVariable Long id,
                                              @RequestPart("image") List<MultipartFile> image) throws IOException {


        String url = blogContentService.saveListsImg(id,image);
        return ResponseEntity.ok(url);

    }

    @GetMapping("getContent")
    public ResponseEntity<List<BlogContentDto>> getAll(){

        return ResponseEntity.ok(blogContentService.getAll());
    }

}
