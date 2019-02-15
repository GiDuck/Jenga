package hi.im.jenga.util.email;
import hi.im.jenga.util.HTMLManager;
import java.io.File;
import java.io.FileNotFoundException;

public abstract class EmailForm {

    private File htmlFile;
    private String fileContent;
    private HTMLManager htmlManager;

    public EmailForm() {

        htmlManager = new HTMLManager();
    }

    protected void setDate(String fullFilePath) throws FileNotFoundException{
        System.out.println("filepath");
        System.out.println(fullFilePath);
        htmlFile = new File(fullFilePath);
        if(!htmlFile.exists()){
            throw new FileNotFoundException();
        }
        getFileContent();
    }

    private final void getFileContent(){

        fileContent = htmlManager.getHtmlTextFromFile(htmlFile);

    }

    protected String injectHtmlById(String tagId, String text){
        fileContent = htmlManager.injectTextById(tagId, text, fileContent);
        return fileContent;

    }

    protected final String getHtml(){
        return fileContent;

    };

    public abstract void test();



}
