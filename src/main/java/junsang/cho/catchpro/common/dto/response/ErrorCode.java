package junsang.cho.catchpro.common.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    COMMON_SYSTEM_ERROR("일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요."), // 장애 상황
    COMMON_INVALID_PARAMETER("요청한 값이 올바르지 않습니다."),
    COMMON_BAD_CREDENTIAL("자격증명에 실패하였습니다"),
    COMMON_NO_SUCH_ELEMENT("조회결과를 찾을 수 없습니다."),
    COMMON_ILLEGAL_STATUS("잘못된 상태값입니다."),
    COMMON_DUPLICATE_ENTRY("이미 존재하는 데이터 입니다."),
    COMMON_ACCESS_DENIED("접근이 거부되었습니다."),
    COMMON_CONNECT_TIMED_OUT("접속 시간 초과(TIME_OUT)"),
    NOT_FOUND_AGENT("지정된 운영점이 없습니다."),
    COMMON_ILLEGAL_AUTHORITY("권한에 맞지 않는 요청입니다."),
    NOT_INVALID_PASSWORD("비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 이상의 비밀번호여야 합니다."),

    BATCH_SCHEDULER_FAILED("배치파일 실행중 오류가 발생하였습니다.");

    private final String errorMsg;

    public String getErrorMsg(Object... arg) {
        return String.format(errorMsg, arg);
    }
}