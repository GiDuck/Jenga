package hi.im.jenga.member.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.*;

@Component
public class UtilFile {

    @Value("#{data['file.path']}")
    private String PATH;
    private static final Logger logger = LoggerFactory.getLogger(UtilFile.class);
    String fileName = "";


    // 프로젝트 내 지정된 경로에 파일을 저장하는 메소드
// DB에는 업로드 된 전체 경로명으로만 지정되기 때문에 (업로드 한 파일 자체는 경로에 저장됨)
// fileUpload() 메소드에서 전체 경로를 리턴받아 DB에 경로 그대로 저장
    public String fileUpload(MultipartHttpServletRequest request, MultipartFile uploadFile) {

        String fileName = "";

        OutputStream out = null; // 찾아보기
        PrintWriter printWriter = null; // 찾아보기

        try {
            fileName = uploadFile.getOriginalFilename();

            if(fileName.equals("")){
                logger.info(": : : UtilFile 빈 파일이 들어왔습니다. 이름을 공백으로 return ");
                return "";
            }

            byte[] bytes = uploadFile.getBytes();
            //path = getSaveLocation(request);
            /*path = "Y:\\go\\Jenga\\profiles\\";*/

            logger.info("UtilFile fileUpload fileName : " + fileName);
            logger.info("UtilFile fileUpload path : " + PATH);

            File file = new File(PATH); // 찾아보기

//          파일명이 중복 && 공백일 경우
            if (fileName != null && !fileName.equals("")) {
                if (file.exists()) {
//                  파일명 앞에 업로드 시간 초 단위로 붙여 파일명 중복을 방지
                    fileName = System.currentTimeMillis() + "_" + fileName;

                    file = new File(PATH + fileName);
                }
            }

            logger.info("UtilFile fileUpload final fileName : " + fileName);
            logger.info("UtilFile fileUpload final path : " + PATH);

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
        return fileName;
//        return path + fileName;
    }

//  업로드 파일 저장 경로 얻는 메소드
//  업로드한 파일의 경로가 도메인 별로 달라야 했기 때문에 도메인의 형을 비교하여 파일 저장 경로를 다르게 지정
    private String getSaveLocation(MultipartHttpServletRequest request) {

        String uploadPath = request.getSession().getServletContext().getRealPath("/");
        String attachPath = "resources\\profiles\\";

        logger.info("UtilFile getSaveLocation path : " + uploadPath + attachPath);

        return uploadPath + attachPath;

    }

}
