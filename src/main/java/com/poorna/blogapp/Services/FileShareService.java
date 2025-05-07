package com.poorna.blogapp.Services;

import com.poorna.blogapp.Dtos.FileShareDto;
import com.poorna.blogapp.Entities.FileShare;
import com.poorna.blogapp.Repositories.FileShareRepo;
import com.poorna.blogapp.SecurityApp.SecuriyRepository.UserRepository;
import com.poorna.blogapp.SecurityApp.User;
import com.poorna.blogapp.WebSocketsPrivate.NotificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileShareService {

    private final FileService fileService;//local service custom interface or gcs i.e google cloud service
    @Value("${project.image}")//template literals ${}
    private  String foldername;


    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final FileShareRepo fileShareRepo;

    private final SimpMessagingTemplate messagingTemplate;

    //for websockets
   // private final SimpMessagingTemplate messagingTemplate;


//    public Object shareFile(String email, MultipartFile file) throws IOException {
//
//       String fileUrl= fileService.uploadFile(foldername,file);
//
//        User currentSenderOrUser= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        String recieverEmail=userRepository.findByEmail(email).map(User::getEmail)//u can use lambda or Classname::Entity
//                .orElseThrow(()->new RuntimeException("not found"));//y used map because or else throw works properly on lists
//        //if you dont need such complex just Use Optional
//
//       // FileShare fileShare=new FileShare(null, currentSenderOrUser.getEmail(), recieverEmail,fileUrl);///directly give input as Constructor or use Builder pattern or individually set
//      //or use Builder pattern to keep things clean ,firstly annote builder in entity
//        FileShare fileShare=FileShare.builder()
//                .fileUrl(fileUrl)
//                .sender(currentSenderOrUser.getEmail())
//                .reciever(recieverEmail)
//                .build();
//
//        fileShareRepo.save(fileShare);
//
//        // Prepare notification
//        NotificationDTO notification = new NotificationDTO(
//                "File received from " + currentSenderOrUser.getEmail(),
//                currentSenderOrUser.getEmail()
//        );
//
//
//      log.info(notification+" printed");
//      log.info(recieverEmail);
//
//     // Send to specific user’s queue
//        messagingTemplate.convertAndSendToUser(recieverEmail, "/queue/notifications", notification);
//      return "Shared Successfully";
//    }

    public Object shareFile(String email, MultipartFile file) throws IOException {

        String currentEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User currentSenderOrUser = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String receiverEmail = userRepository.findByEmail(email)
                .map(User::getEmail)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        String fileUrl = fileService.uploadFile(foldername, file);

        FileShare fileShare = FileShare.builder()
                .fileUrl(fileUrl)
                .sender(currentSenderOrUser.getEmail())
                .reciever(receiverEmail)
                .build();

        fileShareRepo.save(fileShare);

        NotificationDTO notification = new NotificationDTO(
                "File received from " + currentSenderOrUser.getEmail(),
                receiverEmail
        );

        log.info(receiverEmail+" is the Reciever");

        messagingTemplate.convertAndSendToUser(
                receiverEmail,
                "/queue/notifications",
                notification
        );


        return "Shared Successfully";
    }


    //current user is the reciever and recieving files
    public List<FileShareDto> recieveFile() {

        User currectReciver=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<FileShare> recievedfiles=fileShareRepo.findByReciever(currectReciver.getEmail());

         return recievedfiles.stream()
                 .map(r->modelMapper.map(r,FileShareDto.class))
                 .collect(Collectors.toList());
    }
}
