package doodoom.project.peermarket.exception.file;

public class FileLoadException extends RuntimeException {
    public FileLoadException() {
        super("파일 업로드에 실패 하였습니다.");
    }
}
