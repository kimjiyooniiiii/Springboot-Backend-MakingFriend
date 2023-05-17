package com.knucapstone.rudoori.common.expection;

public class NonSelfException extends RuntimeException {
    public NonSelfException() {
        super("자신의 것만 수행 가능합니다.");
    }

    public NonSelfException(String message) {
        super(message);
    }
}