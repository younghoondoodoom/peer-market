package doodoom.project.peermarket.exception.member;

public class MemberStatusStopException extends RuntimeException {
    public MemberStatusStopException() {
        super("정지된 회원입니다.");
    }
}
