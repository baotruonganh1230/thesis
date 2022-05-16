package com.example.thesis.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GoogleDriveService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleDriveService.class);

    @Value("${google.service_account_email}")
    private String serviceAccountEmail;

    @Value("${aws.server.key_path}")
    private String serverKeyPath;

//    @Value("${local.key_path}")
//    private String localKeyPath;

    @Value("${google.application_name}")
    private String applicationName;

    @Value("${google.service_account_key}")
    private String serviceAccountKey;

    @Value("${google.folder_id}")
    private String folderID;

    public Drive getDriveService() {
        Drive service = null;
        try {
            java.io.File key = new java.io.File(serverKeyPath + this.serviceAccountKey);
            HttpTransport httpTransport = new NetHttpTransport();
            JacksonFactory jsonFactory = new JacksonFactory();

            GoogleCredential credential = new GoogleCredential.Builder().setTransport(httpTransport)
                    .setJsonFactory(jsonFactory).setServiceAccountId(serviceAccountEmail)
                    .setServiceAccountScopes(Collections.singleton(DriveScopes.DRIVE))
                    .setServiceAccountPrivateKeyFromP12File(key).build();
            System.out.println("Create credentials succeed!!!!!");
            service = new Drive.Builder(httpTransport, jsonFactory, credential).setApplicationName(applicationName)
                    .setHttpRequestInitializer(credential).build();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());

        }

        return service;

    }

    public File upLoadFile(String fileName, String filePath, String mimeType) {
        File file = new File();
        try {
            java.io.File fileUpload = new java.io.File(filePath);
            com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
            fileMetadata.setMimeType(mimeType);
            fileMetadata.setName(fileName);
            fileMetadata.setParents(Collections.singletonList(folderID));
            com.google.api.client.http.FileContent fileContent = new FileContent(mimeType, fileUpload);
            Drive.Files files = getDriveService().files();
            Drive.Files.Create create = files.create(fileMetadata, fileContent);
            Drive.Files.Create create1 = create.setFields("webContentLink");
            file = create1.execute();
//            file = getDriveService().files().create(fileMetadata, fileContent)
//                    .setFields("webContentLink").execute();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return file;
    }

}
