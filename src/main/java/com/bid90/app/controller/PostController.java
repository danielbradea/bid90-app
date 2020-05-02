package com.bid90.app.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bid90.app.DTO.PostDTO;
import com.bid90.app.DTO.UpdatePostDTO;
import com.bid90.app.exception.CustomException;
import com.bid90.app.model.FileInfo;
import com.bid90.app.model.Image;
import com.bid90.app.model.Post;
import com.bid90.app.service.FileStorageService;
import com.bid90.app.service.ImageService;
import com.bid90.app.service.PostService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping("api/post")
public class PostController {

	@Autowired
	PostService postService;

	@Autowired
	ImageService imageService;

	@Autowired
	FileStorageService fileStorageService;

	@Autowired
	ModelMapper modelMapper;

	@ApiOperation(value = "Delete post", authorizations = { @Authorization(value = "Bearer") })
	@DeleteMapping("/delete")
	public HashMap<String, Object> delete(@RequestParam(required = true, name = "id") Long id) {
		HashMap<String, Object> message = new HashMap<String, Object>();
		Post deletedPost = postService.reade(id);
		if (deletedPost == null) {
			throw new CustomException("Bad id", HttpStatus.BAD_REQUEST);
		}
		deletedPost.getImage().forEach(image ->{
			fileStorageService.removeFile(image.getPath());
		});
		postService.delete(deletedPost);
		message.put("message", "Post with id=" + id + " deleted");
		return message;
	}

	@ApiOperation(value = "Update post", authorizations = { @Authorization(value = "Bearer") })
	@PostMapping("/update")
	public PostDTO update(@RequestBody UpdatePostDTO updatePostDTO, HttpServletRequest request) {
		
		String link = request.getRequestURL().toString().replace(request.getRequestURI(), "/api/file/get/image?name=");
		Post updatePost = modelMapper.map(updatePostDTO, Post.class);
		
		PostDTO updatedPostDTO = modelMapper.map(updatePost, PostDTO.class);
		updatedPostDTO.getImage().forEach((x) -> x.setLink(link));
		return updatedPostDTO;
	}
	
	@ApiOperation(value = "Add image to post", authorizations = { @Authorization(value = "Bearer") })
	@PostMapping(value = "/update/image/add", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public PostDTO updateNewImage(@RequestParam("postId") Long postId,
			@RequestParam("file") MultipartFile files, HttpServletRequest request) {
		
		String link = request.getRequestURL().toString().replace(request.getRequestURI(), "/api/file/get/image?name=");
		Post updatePost = postService.reade(postId);
		
		String fileType = fileStorageService.fileType(files);
		if(fileType.contains("image/png") || fileType.contains("image/jpeg")) {
			Image newImage = new Image();
			FileInfo fileInfo = fileStorageService.storeFile(files, true, "images");
			newImage.setPath(fileInfo.getPath());
			newImage.setName(fileInfo.getName());
			newImage.setSize(fileInfo.getSize());
			updatePost.setImage(newImage);
			}else {
				throw new CustomException("The file is not supported", HttpStatus.BAD_REQUEST);
			}
		
		Post saved = postService.create(updatePost);
		PostDTO newPostDTO = modelMapper.map(saved, PostDTO.class);
		newPostDTO.getImage().forEach((x) -> x.setLink(link));
		return newPostDTO;
	}
	
	@ApiOperation(value = "Delete image from post", authorizations = { @Authorization(value = "Bearer") })
	@DeleteMapping(value = "/update/image/delete")
	public PostDTO updateRemoveImage(@RequestParam("postId") Long postId,@RequestParam(name = "name") String name,  HttpServletRequest request) {
		
		String link = request.getRequestURL().toString().replace(request.getRequestURI(), "/api/file/get/image?name=");
		Post updatePost = postService.reade(postId);
			
		Image imageToRemove = imageService.findByName(name);
		Post post = postService.reade(postId);
		
		if(imageToRemove == null || post == null ) {
			throw new CustomException("Bad post id or image name", HttpStatus.BAD_REQUEST);
		}
		
		if(!post.getImage().contains(imageToRemove)) {
			throw new CustomException("Bad image name", HttpStatus.BAD_REQUEST);
		}
		
		fileStorageService.removeFile(imageToRemove.getPath());
		updatePost.removeImage(imageToRemove);
		
		
		Post saved = postService.create(updatePost);
		PostDTO newPostDTO = modelMapper.map(saved, PostDTO.class);
		newPostDTO.getImage().forEach((x) -> x.setLink(link));
		
		return newPostDTO;
	}
	
	@ApiOperation(value = "Find all posts")
	@GetMapping("/find")
	public List<PostDTO> find(@RequestParam(required = false, name = "id") Long id, HttpServletRequest request) {
		List<Post> listPost = null;
		String link = request.getRequestURL().toString().replace(request.getRequestURI(), "/api/file/get/image?name=");
		if (id == null) {
			listPost = postService.findAll();
		} else {
			Post post = postService.reade(id);
			if (post == null) {
				throw new CustomException("Bad id", HttpStatus.BAD_REQUEST);
			}
			listPost = Arrays.asList(post);
		}

		List<PostDTO> listPostDTO = modelMapper.map(listPost, new TypeToken<List<PostDTO>>() {
		}.getType());
		listPostDTO.forEach((p) -> p.getImage().forEach((i) -> i.setLink(link)));
		return listPostDTO;
	}

	@ApiOperation(value = "All new post; swagger bug. make request with postman", authorizations = { @Authorization(value = "Bearer") })
	@PostMapping(value = "/add", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public PostDTO add(@RequestParam("title") String title, @RequestParam("description") String description,
			@RequestParam("files") List<MultipartFile> files, HttpServletRequest request) throws IOException {
		
		if(files.size() > 5) {
			throw new CustomException("Tou can oly 5 images to upload", HttpStatus.BAD_REQUEST);
		}
		
		String link = request.getRequestURL().toString().replace(request.getRequestURI(), "/api/file/get/image?name=");
		Post newPost = new Post();
		newPost.setTitle(title);
		newPost.setDescription(description);
		
		for (MultipartFile mf : files) {
			String fileType = fileStorageService.fileType(mf);
			if(fileType.isEmpty()) {
				continue;
			}
			if(fileType.contains("image/png") || fileType.contains("image/jpeg")) {
			Image newImage = new Image();
			FileInfo fileInfo = fileStorageService.storeFile(mf, true, "images");
			newImage.setPath(fileInfo.getPath());
			newImage.setName(fileInfo.getName());
			newImage.setSize(fileInfo.getSize());
			newPost.setImage(newImage);
			}else {
				throw new CustomException("The file is not supported", HttpStatus.BAD_REQUEST);
			}
		}

		Post saved = postService.create(newPost);
		PostDTO newPostDTO = modelMapper.map(saved, PostDTO.class);
		newPostDTO.getImage().forEach((x) -> x.setLink(link));
		return newPostDTO;

	}

}
