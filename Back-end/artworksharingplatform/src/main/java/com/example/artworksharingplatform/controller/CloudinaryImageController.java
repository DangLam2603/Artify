package com.example.artworksharingplatform.controller;

import java.util.Map;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.artworksharingplatform.service.CloudinaryService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping
public class CloudinaryImageController {
	
	@Autowired
	private CloudinaryService cloudinaryService;

	@PostMapping("api/auth/upload")
	public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file){
		Map data = cloudinaryService.upload(file);
		String url = data.get("url").toString();
		return  ResponseEntity.ok(url);
	}

	public String test(){
		return "hello";
	}
}
