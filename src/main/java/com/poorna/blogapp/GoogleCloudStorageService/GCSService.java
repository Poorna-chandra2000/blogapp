package com.poorna.blogapp.GoogleCloudStorageService;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class GCSService {

    @Value("${gcp.bucket.name}")
    private String bucketName;

    @Value("${gcp.credentials.path}")
    private String credentialsPath;

    public String uploadFile(MultipartFile file) throws IOException {
        Storage storage = StorageOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(
                        new ClassPathResource(credentialsPath).getInputStream()))//mention proper path of json bucket file
                .build()
                .getService();
//        GoogleCredentials credentials = GoogleCredentials
//                .fromStream(new ClassPathResource("lively-citizen-450104-t1-314f36139474.json").getInputStream());
        String objectName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        storage.create(blobInfo, file.getBytes());

        // Make file public
        storage.createAcl(blobId, Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

        // Return public URL
        return "https://storage.googleapis.com/" + bucketName + "/" + objectName;
    }

    //file deletion
    public boolean deleteFile(String objectName) throws IOException {
        Storage storage = StorageOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(
                        new ClassPathResource(credentialsPath).getInputStream()))
                .build()
                .getService();

        BlobId blobId = BlobId.of(bucketName, objectName);
        return storage.delete(blobId); // returns true if deletion was successful
    }

//object name is file name..but for a bucket its a object
    public Resource downloadFileAsResource(String objectName) throws IOException {
        Storage storage = StorageOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(
                        new ClassPathResource(credentialsPath).getInputStream()))
                .build()
                .getService();

        Blob blob = storage.get(BlobId.of(bucketName, objectName));
        if (blob == null || !blob.exists()) {
            throw new IOException("File not found: " + objectName);
        }

        byte[] content = blob.getContent();
        return new ByteArrayResource(content);
    }

}
