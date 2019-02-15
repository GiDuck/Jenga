package hi.im.jenga.util.email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("tempPasswordEmailForm")
public class TempPasswordEmailForm extends EmailForm {

    @Value("#{props['temp.password']}")
    private String filePath;

/*    public TempPasswordEmailForm() {
        super();
        try {
            setDate(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void test(){
        System.out.println("*********************testing... *********************");
        System.out.println(filePath);
    }

}
