package hi.im.jenga.board.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface FileIO {

    public abstract File getFile(final String path, String fileName);
    public abstract File getUploadFile(final String path, final String name);


}
