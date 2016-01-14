package com.mytway.pojo;

public enum TypeWork {
            FLEXIBLE_TYPE(1), STANDARD_TYPE(2), ERROR_TYPE(999);

    private int statusCode;

    private TypeWork(int status){
        statusCode = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
