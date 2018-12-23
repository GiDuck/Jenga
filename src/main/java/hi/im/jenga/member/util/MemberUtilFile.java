package hi.im.jenga.member.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.*;

@Component
public class MemberUtilFile {

    @Value("#{data['image.profile_path']}")
    private String PROFILE_PATH;
    @Value("#{data['image.profile_absolute_path']}")
    private String PROFILE_ABSOLUTE_PATH;

    private static final Logger logger = LoggerFactory.getLogger(MemberUtilFile.class);
    String fileName = "";

    /*
    * 프로젝트 내 지정된 경로에 파일을 저장하는 메소드
    * DB에는 업로드 된 전체 경로명으로만 지정되기 때문에 (업로드 한 파일 자체는 경로에 저장됨)
    * fileUpload() 메소드에서 전체 경로를 리턴받아 DB에 경로 그대로 저장
    */
    public String fileUpload(MultipartFile uploadFile) {

        String fileName = "";

        OutputStream out = null; // 찾아보기
        PrintWriter printWriter = null; // 찾아보기

        try {
            fileName = uploadFile.getOriginalFilename();


//          파일이름이 ""면 (파일을 올리지 않았으면 ""로 들어옴)
            if(fileName.equals("")){
                logger.info(": : : UtilFile 빈 파일이 들어왔습니다. 이름을 공백으로 반환");
                return "";
            }

            byte[] bytes = uploadFile.getBytes();


            logger.info("UtilFile fileUpload fileName : " + fileName);
            logger.info("UtilFile fileUpload path : " + PROFILE_PATH);

            File file = new File(PROFILE_ABSOLUTE_PATH, fileName);

//          파일명이 중복 && 공백일 경우
            if (fileName != null && !fileName.equals("")) {
                if (file.exists()) {
//                  파일명 앞에 업로드 시간 초 단위로 붙여 파일명 중복을 방지
                    fileName = System.currentTimeMillis() + "_" + fileName;

                    file = new File(PROFILE_ABSOLUTE_PATH + fileName);
                }
            }

            logger.info("UtilFile fileUpload final fileName : " + fileName);
            logger.info("UtilFile fileUpload final path : " + PROFILE_PATH);

            logger.info(file.getPath());
            String filePath = file.getPath();
            PROFILE_PATH = filePath.replace(PROFILE_ABSOLUTE_PATH,"");
            PROFILE_PATH = PROFILE_PATH.replace("\\", "/");

            out = new FileOutputStream(file);

            logger.info("UtilFile fileUpload out : " + out);

            out.write(bytes);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                if(out != null){
                    out.close();
                }
                if(printWriter != null){
                    printWriter.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        logger.info(PROFILE_PATH);
        return PROFILE_PATH;
    }

}
