package hi.im.jenga.util.email;
import hi.im.jenga.member.controller.MemberController;
import hi.im.jenga.util.HTMLManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

@Component
public abstract class EmailForm {

    private File htmlFile;
    private String fileContent;
    private HTMLManager htmlManager;
    @Autowired
    private ServletContext servletContext;
    public EmailForm() {

        htmlManager = new HTMLManager();
    }

    protected void setData(String fullFilePath) throws FileNotFoundException{
        System.out.println(servletContext.getRealPath("/"));
        htmlFile = new File(servletContext.getRealPath("/") + fullFilePath);
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
