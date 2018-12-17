package hi.im.jenga.board.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class BoardUtilFile {

    @Value("#{data['image.block_path']}")
    private String BLOCK_IMAGE_PATH;
    @Value("#{data['block.path']}")
    private String BLOCK_PATH;


    private String BLOCK_FINAL_PATH;
    private static final Logger logger = LoggerFactory.getLogger(BoardUtilFile.class);
    String fileName = "";
    File file;

    /**
    * 프로젝트 내 지정된 경로에 파일을 저장하는 메소드
    * DB에는 업로드 된 전체 경로명으로만 지정되기 때문에 (업로드 한 파일 자체는 경로에 저장됨)
    *
    * fileUpload() / block으로 들어오면 메소드에서 전체 경로를 리턴받아 DB에 경로 그대로 저장
    *              / image로 들어오면 파일이름만 리턴받고 저장
    */
    public String fileUpload(MultipartFile uploadFile, String type) {

        String fileName = "";
        OutputStream out = null;
        PrintWriter printWriter = null;

        try {

            fileName = uploadFile.getOriginalFilename();

            if(type.equals("image")) {
//          파일이름이 ""면 (파일을 올리지 않았으면 ""로 들어옴)
                if (fileName.equals("")) {
                    logger.info(": : : BoardUtilFile 빈 이미지파일이 들어왔습니다. 이름을 공백으로 반환");
                    return "";
                }

                File uploadPath = new File(BLOCK_IMAGE_PATH, getFolder());

                if(uploadPath.exists() == false){
                    logger.info("yyyy/MM/dd 폴더를 생성");
                    uploadPath.mkdirs();
                    logger.info("생성완료");
                }

                logger.info("BoardUtilFile fileUpload fileName : " + fileName);
                logger.info("BoardUtilFile fileUpload path : " + BLOCK_IMAGE_PATH);


                file = new File(uploadPath, fileName);


//          파일명이 중복 && 공백일 경우
                if (fileName != null && !fileName.equals("")) {
                    if (file.exists()) {
//                  파일명 앞에 업로드 시간 초 단위로 붙여 파일명 중복을 방지
                        fileName = System.currentTimeMillis() + "_" + fileName;

                        file = new File(uploadPath, fileName);
                    }
                }

                logger.info("BoardUtilFile fileUpload final fileName : " + fileName);
                logger.info("BoardUtilFile fileUpload final path : " + BLOCK_IMAGE_PATH);

                BLOCK_IMAGE_PATH = uploadPath.getPath()+"\\"+fileName;
            }





//          block path로 들어오면
            else {
                File uploadPath = new File(BLOCK_PATH, getFolder());    // 이 경로에가서 /년/월/일  폴더를 만듬
                // yyyy/MM/dd 폴더를 만듬
                if(uploadPath.exists() == false){
                    uploadPath.mkdirs();
                }

                logger.info("BoardUtilFile fileUpload fileName : " + fileName);
                logger.info("요기요기");
                logger.info("BoardUtilFile fileUpload path : " +uploadPath.getName());  // 04
                logger.info("BoardUtilFile fileUpload path : " +uploadPath.getAbsolutePath()+"\\"+fileName);
                logger.info("BoardUtilFile fileUpload path : " +uploadPath.getPath()+"\\"+fileName);

                BLOCK_FINAL_PATH = uploadPath.getPath()+"\\"+fileName;

//                file = new File(BLOCK_FINAL_PATH, fileName);
                file = new File(uploadPath, fileName);

            }




            logger.info("file direcroty "+file.isDirectory());


            byte[] bytes = uploadFile.getBytes();
            //path = getSaveLocation(request);
            /*path = "Y:\\go\\Jenga\\profiles\\";*/


            out = new FileOutputStream(file);

            logger.info("BoardUtilFile fileUpload out : " + out);

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

        if(type.equals("image")) {
            logger.info("image입니다");
            logger.info(BLOCK_IMAGE_PATH);
            return BLOCK_IMAGE_PATH;
        }
        logger.info("그냥입니다");
        return BLOCK_FINAL_PATH;
//        return BLOCK_PATH + fileName;
    }

//  업로드 파일 저장 경로 얻는 메소드
//  업로드한 파일의 경로가 도메인 별로 달라야 했기 때문에 도메인의 형을 비교하여 파일 저장 경로를 다르게 지정
//    private String getSaveLocation(MultipartHttpServletRequest request) {
//
//        String uploadPath = request.getSession().getServletContext().getRealPath("/");
//        String attachPath = "resources\\profiles\\";
//
//        logger.info("UtilFile getSaveLocation path : " + uploadPath + attachPath);
//
//        return uploadPath + attachPath;
//
//    }

    //날짜별 폴더에 담음 중복방지
    private String getFolder(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date = new Date();

        String str = sdf.format(date);

        return str.replace("-",File.separator);
    }

}
