package ru.valerii.NauJava.exception;

public class ApiError {
    private String message;

    private ApiError(String message) {
        this.message = message;
    }

    public static ApiError create(Throwable e) {
        return new ApiError(e.getMessage());
    }

    public static ApiError create(String message) {
        return new ApiError(message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}