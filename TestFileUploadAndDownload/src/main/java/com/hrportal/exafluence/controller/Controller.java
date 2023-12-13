package com.hrportal.exafluence.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hrportal.exafluence.FileRepository;
import com.hrportal.exafluence.document.FileModel;

@RestController
@CrossOrigin(origins = {"*"})
public class Controller 
{
	@Autowired
	private FileRepository fileRepository;
	
	@PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Save the file content to MongoDB
            FileModel fileDocument = new FileModel();
            fileDocument.setFileName(file.getOriginalFilename());
            fileDocument.setFile(file.getBytes());
            // Set other fields as needed
            fileDocument = fileRepository.save(fileDocument);

            return ResponseEntity.status(HttpStatus.CREATED).body(fileDocument.getFileName());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file");
        }
    }
	
	@GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String id) {
        Optional<FileModel> optionalFileDocument = fileRepository.findById(id);
        if (optionalFileDocument.isPresent()) {
            FileModel fileDocument = optionalFileDocument.get();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(fileDocument.getFileName()).build());
            return new ResponseEntity<>(fileDocument.getFile(), headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
