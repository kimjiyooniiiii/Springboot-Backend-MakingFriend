package com.knucapstone.rudoori.common.expection;

public class SelfException extends RuntimeException{
    public SelfException() {
        super("자신의 것은 수행 불가능합니다.");
    }

    public SelfException(String message) {
        super(message);
    }
}
