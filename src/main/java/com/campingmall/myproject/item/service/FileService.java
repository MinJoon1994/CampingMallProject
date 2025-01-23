package com.campingmall.myproject.item.service;

public interface FileService {
    
    //1.파일 업로드 -> 파일을 생성
    public String uploadFile(String uploadPath, String originalFileName,byte[] fileData) throws Exception;
    
    
    //2.업로드 된 파일 삭제
    public void deleteFile(String filePath) throws Exception;

}
