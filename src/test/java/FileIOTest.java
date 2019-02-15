
import hi.im.jenga.board.util.FileIOUtil;
import hi.im.jenga.board.util.FileType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileIOTest {

    private static MultipartFile imageFile;
    private static MultipartFile bookmarkFile;
    public static final String TEST_PATH = "D:/testFiles";
    public static final String TEST_UPLOADED_PATH = "D:/testFile/" + new SimpleDateFormat("YY/MM/dd").format(new Date());

    public static final String IMAGE_NAME = "rubber.jpg";
    public static final String IMAGE_PATH = TEST_PATH + "/ "+ IMAGE_NAME;

    public static final String BOOKMARK_NAME = "bookmarks.html";
    public static final String BOOKMARK_PATH = TEST_PATH+ "/ "+ BOOKMARK_NAME;

    public String nameParser(final String oldName){

        String[] tokens = oldName.split("\\.");
        String newName = "";
        for(int i = 0; i< tokens.length ; ++i){
            if(i == tokens.length - 1) break;
            newName += tokens[i];
        }

        return newName;

    }

    @Before
    public void setUp(){
        byte[] imageContent = null;
        byte[] bookmarkContent = null;
        Path imagePath = Paths.get(IMAGE_PATH);
        Path bookmarkPath = Paths.get(BOOKMARK_PATH);
        try{

        imageContent = Files.readAllBytes(imagePath);
        bookmarkContent = Files.readAllBytes(bookmarkPath);

        }catch (IOException e){
            e.printStackTrace();
        }



        imageFile = new MockMultipartFile(nameParser(IMAGE_NAME), IMAGE_NAME, "image/jpg", imageContent);
        bookmarkFile = new MockMultipartFile(nameParser(BOOKMARK_NAME), BOOKMARK_NAME, "text/plain", bookmarkContent);

    }


    @Ignore
    @Test
    public void testInputImage(){

        System.out.println(TEST_UPLOADED_PATH);
        FileIOUtil fileIOUtil = new FileIOUtil(FileType.IMAGE);
        fileIOUtil.fileUpload(imageFile, TEST_PATH);

    }

    @Ignore
    @Test
    public void testOutputImage(){
        FileIOUtil fileIOUtil = new FileIOUtil(FileType.IMAGE);
        byte[] imageByteArr =  fileIOUtil.getFileToBynary(IMAGE_NAME, TEST_UPLOADED_PATH);
        Assert.assertNotNull("돌아온 데이터가 null임", imageByteArr);



    }


    @Ignore
    @Test
    public void testInputBookmark(){

        FileIOUtil fileIOUtil = new FileIOUtil(FileType.BOOKMARK);
        fileIOUtil.fileUpload(bookmarkFile, TEST_PATH);


    }




    @Ignore
    @Test
    public void testOutputBookmark(){

        FileIOUtil fileIOUtil = new FileIOUtil(FileType.BOOKMARK);
        byte[] bookmarkArr =  fileIOUtil.getFileToBynary(BOOKMARK_NAME, "D:/testFile/2019/02/12");
        System.out.println(new String(bookmarkArr));
        Assert.assertNotNull("돌아온 데이터가 null임", bookmarkArr);


    }

}
