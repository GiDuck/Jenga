package hi.im.jenga.board.util;


import java.io.File;

public class BookmarkFileIO implements FileIO {

    public File getFile(final String path, String fileName) {
        return FileIOUtil.makeFileWithFolder(path, fileName);

    }

    public File getUploadFile(String path, String name) {
        return FileIOUtil.getNormalFile(path, name);
    }
}
