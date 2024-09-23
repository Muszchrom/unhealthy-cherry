package com.example.demo;

import org.springframework.http.MediaType;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.exceptions.InvalidFileExtensionException;
import com.example.demo.exceptions.ServerErrorException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileProcessingService {
  
  // SINCE WE"RE IN A FUCKING DOCKER CONTAINER, WE ARE WORKING ON ITS PATHS
  // FUCKING DUMBASS
  private final String basePath = "/files/";

  private MultipartFile multipartFile;
  private String fileHash;
  private String fileExtension;

  public FileProcessingService() {}

  public FileProcessingService(MultipartFile multipartFile) {
    setupFileProcessingService(multipartFile);
  }

  public String getBasePath() {
    return this.basePath;
  }

  public String getFileHash() {
    return this.fileHash;
  }

  public String getFileExtension() {
    return this.fileExtension;
  }

  public MultipartFile getMultipartFile() {
    return this.multipartFile;
  }

  public void setMultipartFile(MultipartFile multipartFile) {
    setupFileProcessingService(multipartFile);
  }


  private void setupFileProcessingService(MultipartFile multipartFile) {
    this.multipartFile = multipartFile;
    try {
      this.fileHash = createHashBasedOnFile();
    } catch(NoSuchAlgorithmException | IOException ex) {
      throw new ServerErrorException(" while processing the file.");
    } 
    this.fileExtension = getExtension();
  }

  private String createHashBasedOnFile() throws NoSuchAlgorithmException, IOException {
    MessageDigest digest;

    digest = MessageDigest.getInstance("SHA-256");
    InputStream inputStream = this.multipartFile.getInputStream();
    byte[] buffer = new byte[8192];
    int bytesRead;

    while ((bytesRead = inputStream.read(buffer)) != -1) {
      digest.update(buffer, 0, bytesRead);
    }

    byte[] hashedBytes = digest.digest();
    return String.format("%064x", new BigInteger(1, hashedBytes));
  };

  private String getExtension() throws InvalidFileExtensionException{
    // Verift extension
    // Verify file size
    // Verify magic numbers

    String contentType = this.multipartFile.getContentType();
    String ext = "INVALID";

    // Verify mime type
    switch (contentType) {
      case "image/jpeg":
        ext = "jpg";
        break;

      case "image/png":
        ext = "png";
        break;

      case "image/webp":
        ext = "webp";
        break;

      case "image/bmp":
        ext = "bmp";
        break;

      case "image/avif":
        ext = "avif";
        break;

      case "image/gif":
        ext = "gif";
        break;

      case "image/tiff":
        ext = "tiff";
        break;

      default:
        ext = "INVALID";
        break;
    }

    if (ext.equals("INVALID")) {
      throw new InvalidFileExtensionException(this.fileHash);
    }
    return ext;
  }


  public String uploadFile() {
    // check if file exists
    File dir = new File(this.basePath + this.fileHash + "." + this.fileExtension);
    if (dir.exists()) {
      return "EXISTS";
    }

    // Save file
    Path path = Path.of(this.basePath + this.fileHash + "." + this.fileExtension);
    try {
      Files.copy(this.multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
      return "CREATED";
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    return "FAILED";
  }

  public String uploadFile(String oldFileNameWithExtension) {
    File dir = new File(this.basePath + this.fileHash + "." + this.fileExtension);
    if (dir.exists()) {
      return "EXISTS";
    }

    Path oldFilePath = Path.of(this.basePath + oldFileNameWithExtension);
    Path path = Path.of(this.basePath + this.fileHash + "." + this.fileExtension);
    try {
      Files.copy(this.multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
      Files.delete(oldFilePath);
      return "CREATED";
    } catch (IOException e) {
      System.out.println(e.getMessage());
      return "FAILED";
    }
  }

  public void deleteFile(String fileNameWithExtension) {
    Path path = Path.of(this.basePath + fileNameWithExtension);
    try {
      Files.delete(path);
    } catch (IOException ex) {
      throw new ServerErrorException(" while deleting the file.");
    }
  }

  public List<String> fileList() {
    File dir = new File(basePath);
    File[] files = dir.listFiles();
    return files != null ? Arrays.stream(files).map(File::getName).collect(Collectors.toList()) : null;
  }

  public final class ResourceWithContentType {
    private Resource resource;
    private MediaType mediaType;
    
    public ResourceWithContentType(Resource resource, MediaType mediaType) {
      this.resource = resource;
      this.mediaType = mediaType;
    }

    public Resource getResource() {
      return this.resource;
    }

    public MediaType getMediaType() {
      return this.mediaType;
    }
  }

  public ResourceWithContentType downloadFile(String fileName) {
    Path filePath = Path.of(basePath + fileName);

    try {
      Resource resource = new UrlResource(filePath.toUri());
      String contentType = Files.probeContentType(filePath);
      MediaType mediaType = MediaType.parseMediaType(
        contentType == null ? "application/octet-stream" : contentType
      ); 

      return new ResourceWithContentType(resource, mediaType);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }
}