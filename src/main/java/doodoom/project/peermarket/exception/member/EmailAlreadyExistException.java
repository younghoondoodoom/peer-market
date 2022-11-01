package doodoom.project.peermarket.exception.member;

public class EmailAlreadyExistException extends RuntimeException {
    public EmailAlreadyExistException() {
        super("이메일이 중복 되었습니다.");
    }
}
