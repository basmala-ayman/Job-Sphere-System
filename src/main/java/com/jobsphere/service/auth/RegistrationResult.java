package com.jobsphere.service.auth;

public class RegistrationResult {
    private final boolean success;
    private final String msg;
    private final int userId;

    public RegistrationResult(boolean success, String msg, int userId) {
        this.success = success;
        this.msg = msg;
        this.userId = userId;
    }

    public static RegistrationResult success(int userId, String msg) {
        return new RegistrationResult(true, msg, userId);
    }

    public static RegistrationResult fail(String msg) {
        return new RegistrationResult(false, msg, -1);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }

    public int getUserId() {
        return userId;
    }

}
