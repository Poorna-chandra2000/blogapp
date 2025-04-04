package com.rahul.rahulapp.Controllers;

import com.rahul.rahulapp.Dtos.BlogDto;
import com.rahul.rahulapp.Services.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blog")
public class BlogController {

   private final BlogService blogService;

   @GetMapping("/getall")
   ResponseEntity<List<BlogDto>> getAllBlog(){
       return ResponseEntity.ok(blogService.getAllBlog());
   }

    @PostMapping("/upload")
    public ResponseEntity<BlogDto> uploadProduct(
            @RequestPart("blogDto") BlogDto blogDto,
            @RequestPart("image") MultipartFile image) throws IOException {


            BlogDto postedbBlog = blogService.saveBlog(blogDto, image);
            return ResponseEntity.ok(postedbBlog);

    }

    @PutMapping("/updateimage/{id}")
    public ResponseEntity<String> uploadimage(@PathVariable Long id,
            @RequestPart("image") MultipartFile image) throws IOException {


        String url = blogService.saveimg(id,image);
        return ResponseEntity.ok(url);

    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBlog(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return ResponseEntity.ok("Blog deleted successfully along with the image.");
    }
}
