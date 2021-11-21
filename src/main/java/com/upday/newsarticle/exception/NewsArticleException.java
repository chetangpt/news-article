package com.upday.newsarticle.exception;

public class NewsArticleException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String errCode;
    private final String errMsg;
    private static final String exceptionErrorCode = "9999";

    public NewsArticleException(String errCode, String errMsg) {
        super(errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public NewsArticleException(Exception e) {
        super(e);
        this.errMsg = e.getMessage();
        this.errCode = exceptionErrorCode;
    }

    public String getErrCode() {
        return errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

}
