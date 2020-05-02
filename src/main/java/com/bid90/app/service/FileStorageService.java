package com.bid90.app.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bid90.app.model.FileInfo;



@Service
public class FileStorageService {

	@Value("${DOWNLOAD_PATH}")
	String pathDownloadFile;
	
	public FileInfo storeFile(MultipartFile file,Boolean randomName,String folderName) {
		FileInfo fileInfo= new FileInfo();
        Path pathFile = null;
        String fileDotExtension = file.getOriginalFilename();
        String fileName ;
        String extension;
     
        
        if(fileDotExtension.lastIndexOf(".") > -1) {
        	extension = fileDotExtension.substring(fileDotExtension.lastIndexOf(".")) ;
        	fileName = fileDotExtension.substring(0, fileDotExtension.lastIndexOf("."));
        }else {
        	extension = null;
        	fileName = fileDotExtension;
        }
        if(randomName) {
        	fileName = random(17);
        }
       
        
		if(pathDownloadFile.equals("/")) {
			pathFile = Paths.get("download/"+folderName+"/"+fileName+extension).toAbsolutePath();
		}else {
			pathFile = Paths.get(pathDownloadFile+folderName+"/"+fileName+extension);
		}
		 fileInfo.setName(fileName);
	     fileInfo.setType(file.getContentType());
	     fileInfo.setPath(pathFile.toString());
	     fileInfo.setSize(file.getSize());
	        
		try {
			if(Files.notExists(pathFile)) {
				Files.createDirectories(pathFile);
			}
			Files.copy(file.getInputStream(), pathFile,StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        return fileInfo;
    }
	public void removeFile(String path) {
		
		File file = new File(path); 
		file.delete();
		
	}
	public String fileType(MultipartFile file) {
		if(file.getContentType() != null) {
			return file.getContentType();
		}
		return "";
	}
    public String random(Integer length) {
    	char[][] pairs = { { 'a', 'z' }, { '0', '9' }, { 'A', 'Z' } };
		RandomStringGenerator passwordGen = new RandomStringGenerator.Builder().withinRange(pairs).build();
		String password = passwordGen.generate(length);
		return password;
    }

    
}
