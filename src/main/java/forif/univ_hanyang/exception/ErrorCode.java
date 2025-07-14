package forif.univ_hanyang.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    
    // 400 Bad Request
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "E001", "입력값이 유효하지 않습니다."),
    TEAM_NOT_FOUND_BY_SEMESTER(HttpStatus.BAD_REQUEST, "E002", "해당 연도, 학기에 해당하는 운영진이 없습니다."),
    TEAM_NOT_FOUND_BY_STUDENT_ID(HttpStatus.BAD_REQUEST, "E003", "해당 학번에 해당하는 운영진이 없습니다."),
    USER_NOT_FOUND_BY_ID(HttpStatus.BAD_REQUEST, "E004", "ID에 해당하는 유저가 없습니다."),
    SEMESTER_INFO_NOT_FOUND(HttpStatus.BAD_REQUEST, "E005", "해당 학기의 정보가 존재하지 않습니다."),
    STUDY_NOT_FOUND(HttpStatus.BAD_REQUEST, "E006", "스터디가 존재하지 않습니다."),
    APPLY_NOT_FOUND(HttpStatus.BAD_REQUEST, "E007", "지원서가 없습니다."),
    STUDY_ALREADY_APPROVED(HttpStatus.BAD_REQUEST, "E008", "이미 승인된 스터디입니다."),
    PRIMARY_STUDY_REQUIRED(HttpStatus.BAD_REQUEST, "E009", "1순위 스터디를 무조건 선택해야 합니다."),
    USER_NOT_APPLIED_TO_STUDY(HttpStatus.BAD_REQUEST, "E010", "해당 스터디에 지원하지 않은 유저입니다."),
    STUDY_APPLICATION_PERIOD_ENDED(HttpStatus.BAD_REQUEST, "E011", "스터디 지원 기간이 아닙니다."),
    
    // 401 Unauthorized
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "E101", "유효하지 않은 토큰입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "E102", "토큰이 만료되었습니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "E103", "권한이 없습니다."),
    
    // 403 Forbidden
    INSUFFICIENT_PERMISSION(HttpStatus.FORBIDDEN, "E201", "권한이 없습니다."),
    NOT_STUDY_MENTOR(HttpStatus.FORBIDDEN, "E202", "해당 스터디의 멘토가 아닙니다."),
    
    // 404 Not Found
    STUDY_NOT_FOUND_404(HttpStatus.NOT_FOUND, "E301", "스터디가 존재하지 않습니다."),
    USER_NOT_FOUND_404(HttpStatus.NOT_FOUND, "E302", "유저를 찾을 수 없습니다."),
    NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND, "E303", "공지사항이 없습니다."),
    SPECIFIC_NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND, "E304", "해당 공지사항이 없습니다."),
    FAQ_NOT_FOUND(HttpStatus.NOT_FOUND, "E305", "FAQ가 없습니다."),
    SPECIFIC_FAQ_NOT_FOUND(HttpStatus.NOT_FOUND, "E306", "해당 FAQ가 없습니다."),
    TECH_BLOG_NOT_FOUND(HttpStatus.NOT_FOUND, "E307", "기술 블로그가 없습니다."),
    SPECIFIC_TECH_BLOG_NOT_FOUND(HttpStatus.NOT_FOUND, "E308", "해당 기술 블로그 글이 없습니다."),
    STUDY_APPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "E309", "해당하는 스터디 신청을 찾을 수 없습니다."),
    FIRST_MENTOR_NOT_FOUND(HttpStatus.NOT_FOUND, "E310", "1순위 멘토를 찾을 수 없습니다."),
    SECOND_MENTOR_NOT_FOUND(HttpStatus.NOT_FOUND, "E311", "2순위 멘토를 찾을 수 없습니다."),
    STUDY_PLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "E312", "스터디 플랜이 없습니다."),
    WEEKLY_PLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "E313", "해당하는 스터디의 주간 계획을 찾을 수 없습니다."),
    
    // 409 Conflict
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "E401", "이미 가입된 사용자입니다."),
    
    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E501", "서버 내부 오류가 발생했습니다.");
    
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
    
    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
    
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
} 