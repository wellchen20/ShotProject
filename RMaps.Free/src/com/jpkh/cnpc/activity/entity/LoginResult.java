package com.jpkh.cnpc.activity.entity;

public class LoginResult {
    /**
     * code : 200
     * msg : success
     * data : {"msg":"success","code":"0","tokenId":"7ed5da96cd274dec8d5c5db3d2e515ce","accessToken":"233e74dc010c8914b71fccf917d3b0d4"}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * msg : success
         * code : 0
         * tokenId : 7ed5da96cd274dec8d5c5db3d2e515ce
         * accessToken : 233e74dc010c8914b71fccf917d3b0d4
         */

        private String msg;
        private String code;
        private String tokenId;
        private String accessToken;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getTokenId() {
            return tokenId;
        }

        public void setTokenId(String tokenId) {
            this.tokenId = tokenId;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }
    }
}
