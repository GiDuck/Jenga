package hi.im.jenga.board.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 파일 읽어오기
 * <p>
 * FileReader reader = new FileReader([파일경로]) : [파일 경로]에 있는 파일의 내용을 읽어온다.			/ 파일 문자 입력 라이브러리
 * BufferedReader buffer = new BUfferedReader(reader) : [파일 경로]에 있는 파일의 내용을 읽어온다.		/ 버퍼를 이용한 입력 라이브러리
 * buffer.readLine() : BufferedReader에 저장된 내용을 String형으로 한 줄씩 읽어온다. "\n", "\r"을 만날때 까지 읽어온다.
 * - readLine() 메소드를 사용하면서 문자 읽기를 더 효율적으로 할 수 있게 됐다.
 * - 기존의 read() 메소드로 한 문자씩 읽어오는 것보다 한줄씩 읽어서 처리하기에 더 간편하다.
 */


public class FileIOUtil {
    private BufferedOutputStream bos;
    private BufferedInputStream ios;
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
            ios = new BufferedInputStream(new FileInputStream(file));
            buf = new byte[(int)file.length()];
            ios.read(buf);

        }catch (IOException e){
            e.printStackTrace();
        }

        return buf;

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

        file = new File(folder.getPath(), fileName);


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


}
