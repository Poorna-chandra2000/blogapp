package com.poorna.blogapp.Controllers;

import com.poorna.blogapp.Dtos.FileShareDto;
import com.poorna.blogapp.SecurityApp.SecuriyRepository.UserRepository;
import com.poorna.blogapp.Services.FileShareService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Share")
public class FileSharingController {

    private final FileShareService fileShareService;



    @PostMapping("/send")//io exception is must
    ResponseEntity<?> shareFile(@RequestParam(name = "Recievername") String email,@RequestPart(name = "file") MultipartFile file) throws IOException {

        return new ResponseEntity<>(fileShareService.shareFile(email,file), HttpStatus.CREATED);

    }

    @GetMapping("/RecieveFile")
    ResponseEntity<List<FileShareDto>> recieveFile(){

        return ResponseEntity.ok(fileShareService.recieveFile());
    }

}
