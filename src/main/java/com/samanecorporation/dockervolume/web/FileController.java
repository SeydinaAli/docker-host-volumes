package com.samanecorporation.dockervolume.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("create")
public class FileController {

    private String directory_name ;
    private RestTemplate template;

    public FileController(@Value(("${app.data.folder}"))String directory_name, RestTemplate template) {
        this.directory_name = directory_name;
        this.template = template;
    }

    @GetMapping("/{fileName}")
    public String create(@PathVariable("fileName") String fileName) throws IOException {
        createFile(fileName);
        return "File created";
    }

    private void createFile(String fileName) throws IOException {
        Path newFilePath = Paths.get(directory_name + fileName + ".txt");
        Files.createFile(newFilePath);
    }

    @GetMapping("/getCours")
    public List<String> getCours() throws Exception{
        String url = "http://COURS-SERVICE/cours/all";
        
        ResponseEntity<String[]> responseEntity = template.getForEntity(url, String[].class);
        var coursList = responseEntity.getBody();
        HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();
        if(statusCode.equals(HttpStatus.OK)){
            this.createFile("cours-recu");
        }else {
            throw new RuntimeException("Erreur");
        }

        return Arrays.stream(coursList).toList();
    }
}
