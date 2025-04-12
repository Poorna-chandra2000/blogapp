package com.poorna.blogapp.Controllers;

import com.poorna.blogapp.Dtos.BlogDto;
import com.poorna.blogapp.GoogleCloudStorageService.GCSService;
import com.poorna.blogapp.Services.BlogService;
import com.poorna.blogapp.Services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blog")
public class BlogController {

   private final BlogService blogService;

    private final GCSService gcsService;

    private final FileService fileService;

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


    @GetMapping("/localServerfiles/download")
    public ResponseEntity<Resource> download(@RequestParam String fileName) throws MalformedURLException {
        Resource resource = fileService.downloadFile(fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


    ///////////////////gclod============================
    //deletion of file from the cloud
    @DeleteMapping("/delete-file/{filename}")
    public ResponseEntity<String> deleteFile(@PathVariable String filename) {
        try {
            boolean deleted = gcsService.deleteFile(filename);
            return deleted ?
                    ResponseEntity.ok("File deleted successfully.") :
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting file: " + e.getMessage());
        }
    }


//--->read the comment //pathvariable cannot handle strings with spaces so use Requestparam to send file name
//    @GetMapping("/download/{objectName}")
//    public ResponseEntity<Resource> downloadFile(@PathVariable String objectName) throws IOException {
//        Resource file = gcsService.downloadFileAsResource(objectName);
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + objectName + "\"")
//                .body(file);
//    }

    //download from google cloud storage
    //object name is file name here
    @GetMapping("/files/download")
    public ResponseEntity<Resource> downloadFileRequestparam(@RequestParam String objectName) throws IOException {
        Resource file = gcsService.downloadFileAsResource(objectName);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + objectName + "\"")
                .body(file);
    }

}
