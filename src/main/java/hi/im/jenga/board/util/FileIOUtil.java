package hi.im.jenga.board.util;

import hi.im.jenga.util.FileType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;



public class FileIOUtil {
    private BufferedOutputStream bos;
    private BufferedReader bir;
    private BufferedInputStream bis;
    private FileIO fileIO;
    private FileType fileType;

    private static final Logger logger = LoggerFactory.getLogger(FileIOUtil.class);

    public FileIOUtil(FileType fileType) {
        super();
        this.fileType = fileType;

        if (fileType == FileType.IMAGE) {
            fileIO = new ImageFileIO();
        } else if (fileType == FileType.BOOKMARK) {
            fileIO = new BookmarkFileIO();
        }
    }


    public String fileUpload(final MultipartFile uploadFile, final String path) {

        String fileName = null;
        File file = null;
        try {

            if (uploadFile == null) {
                throw new NullPointerException();
            }

            fileName = uploadFile.getOriginalFilename();

            if (fileName == null || fileName.trim().length() < 1) {
                throw new FileNotFoundException();
            }

            file = fileIO.getFile(path, fileName);

            byte[] bytes = uploadFile.getBytes();
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(bytes);

            return file.getPath();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;

    }

    private File getFile(final String fileName, final String filePath) {

        File file = fileIO.getUploadFile(fileName, filePath);

        return file;

    }

    public byte[] getFileToBynary(final String fileName, final String filePath) {
        byte[] buf = null;
        try {

            File file = getFile(fileName, filePath);
            bis = new BufferedInputStream(new FileInputStream(file));
            buf = new byte[1024];
            while(bis.read(buf) != -1){
                bis.read(buf);
            }

        }catch (IOException e){
            e.printStackTrace();
        }

        return buf;

    }


    public String getFileToChar(final String fileName, final String filePath){

        StringBuffer strBuf = new StringBuffer();
        try {

            File file = getFile(fileName, filePath);
            bir = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
            String str = null;
            while((str = bir.readLine()) != null){

                strBuf.append(str);

            }

        }catch (IOException e){
            e.printStackTrace();
        }

        return strBuf.toString();



    }


    public static File makeFolder(String path) {

        File folder = new File(path, getFolder());

        if (!folder.exists()) {
            folder.mkdirs();
        }

        return folder;

    }

    public static File makeFileWithFolder(final String path, String fileName) {

        File folder = FileIOUtil.makeFolder(path);
        File file = new File(folder, fileName);

        if (file.exists()) {
            fileName = System.currentTimeMillis() + "_" + fileName;
            return new File(path, fileName);
        }

        file = new File(folder.getAbsolutePath(), fileName);


        return file;

    }

    public static File getNormalFile(final String path, final String fileName) {

        return new File(fileName, path);


    }



    //날짜별 폴더에 담음 중복방지
    public static String getFolder() {

        String str = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        return str.replace("-", File.separator);
    }


    public static String getUploadedFilePath(MultipartFile uploadFile, String path, String replceToken, FileType fileType){

        String uploadFilePath;
        if (uploadFile != null) {
            uploadFilePath = new FileIOUtil(fileType).fileUpload(uploadFile, path);
            if(uploadFilePath!= null && uploadFilePath.contains("\\")){
                uploadFilePath = uploadFilePath.replace("\\", "/");
                uploadFilePath = uploadFilePath.replace(replceToken, "");
            }
        } else {
            uploadFilePath = "";
        }
        return uploadFilePath;

    }

}
