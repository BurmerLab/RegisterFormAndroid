package com.example.michal.pojo;

public enum TypeWork {
            FLEXIBLE(1), STANDARD(2), ERROR_TYPE(999);

    private int statusCode;

    private TypeWork(int status){
        statusCode = status;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
