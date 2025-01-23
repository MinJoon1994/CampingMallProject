package com.campingmall.myproject.item.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log4j2
public class FileServiceImpl implements FileService{

    //1.파일 업로드 -> 파일을 생성(file.outputStream)
    @Override
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception {

        //중복없는 난수 발생
        UUID uuid = UUID.randomUUID();

        //1.경로를 제외한 파일 이름 추출
        String fileName = originalFileName.substring(originalFileName.lastIndexOf("\\")+1);

        //2.확장자를 제외한 파일이름 추출
        String fname = fileName.substring(0,fileName.lastIndexOf("."));

        //3.확장자만 추출
        String fext = originalFileName.substring(fileName.lastIndexOf("."));
        
        //4.파일이름 + "_" + 난수 + "확장자"
        //업로드 저장될 파일이름(업로드 파일이름 중복 제거)
        String savedFileName = fname+"_"+uuid.toString()+fext;

        //5.업로드 되었을 경우에 사용되는 경로 + 파일 이름
        String fileUploadFullUrl = uploadPath + "/"+savedFileName;

        //업로드 완료 =? c:/upload/파일이름_난수.확장자;
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData);
        fos.close();

        return savedFileName;
    }
    
    //2. 업로드된 파일 삭제
    @Override
    public void deleteFile(String filePath) throws Exception{
        File deleteFile = new File(filePath);
        if(deleteFile.exists()){
            deleteFile.delete();
        }else{
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
