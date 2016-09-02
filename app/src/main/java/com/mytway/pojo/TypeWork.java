package com.mytway.pojo;

public enum TypeWork {
            FLEXIBLE_TYPE(1), STANDARD_TYPE(2), ERROR_TYPE(999);

    private int statusCode;

    TypeWork(int status){
        statusCode = status;
    }

    TypeWork() {
    }

    public int getStatusCode() {
        return statusCode;
    }

    public static TypeWork obtainTypeWork(int status){
        if(status==1){
            return TypeWork.FLEXIBLE_TYPE;
        }else if(status == 2){
            return TypeWork.STANDARD_TYPE;
        }else{
            return TypeWork.ERROR_TYPE;
        }
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
