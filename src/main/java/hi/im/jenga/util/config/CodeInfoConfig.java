package hi.im.jenga.util.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import hi.im.jenga.util.status_code.AuthStatusCode;
import hi.im.jenga.util.EnumManager;
import hi.im.jenga.util.EnumValue;
import hi.im.jenga.util.status_code.BlockStatusCode;
import hi.im.jenga.util.status_code.FileStatusCode;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;

public class CodeInfoConfig implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            List<EnumValue> authCode = EnumManager.getEnumValue(AuthStatusCode.class);
            List<EnumValue> fileCode = EnumManager.getEnumValue(FileStatusCode.class);
            List<EnumValue> blockCode = EnumManager.getEnumValue(BlockStatusCode.class);


            sce.getServletContext().setAttribute("authStatusCode", mapper.writeValueAsString(authCode));
            sce.getServletContext().setAttribute("fileStatusCode", mapper.writeValueAsString(fileCode));
            sce.getServletContext().setAttribute("blockStatusCode", mapper.writeValueAsString(blockCode));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
