package hi.im.jenga.util.email;
import hi.im.jenga.util.HTMLManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

public abstract class EmailForm {

    private File htmlFile;
    private String fileContent;
    private HTMLManager htmlManager;

    public EmailForm() {

        htmlManager = new HTMLManager();
    }

    protected void setData(String fullFilePath) throws FileNotFoundException{
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

    public abstract void setUp(Map<String ,Object> paramContainer);



}
