package com.xiaoqiang.xiaoxin;

/**
 * @author Android-小强
 * @email: 15075818555@163.com
 * @data: on 2020/8/18 17:29
 */
class Data {
    private int code;
    private int httpCode;
    private boolean success;
    private String msg;
    private String errorMsg;
    private String httpMsg;

    @Override
    public String toString() {
        return "Data{\n" +
                "code=" + code +
                ", \nhttpCode=" + httpCode +
                ", \nsuccess=" + success +
                ", \nmsg='" + msg + '\'' +
                ", \nerrorMsg='" + errorMsg + '\'' +
                ", \nhttpMsg='" + httpMsg + '\'' + "" +
                "\n" +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg == null ? "" : msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getErrorMsg() {
        return errorMsg == null ? "" : errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getHttpMsg() {
        return httpMsg == null ? "" : httpMsg;
    }

    public void setHttpMsg(String httpMsg) {
        this.httpMsg = httpMsg;
    }
}
