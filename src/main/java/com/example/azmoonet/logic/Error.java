package com.example.azmoonet.logic;

public class Error {
    int errorCode;
    String errorMassage;
    public Error(int errorCode,String errorMassage){
        this.errorCode = errorCode;
        this.errorMassage = errorMassage;
    }
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorMassage(String errorMassage) {
        this.errorMassage = errorMassage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMassage() {
        return errorMassage;
    }
}
