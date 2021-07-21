package com.jpkh.cnpc.activity.entity;

public class SetEntity {
    /**
     * code : 200
     * msg : success
     * data : {"countImei":191,"countTodayAlarm":439,"countTenAlarm":null}
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
         * countImei : 191
         * countTodayAlarm : 439
         * countTenAlarm : null
         */

        private int countImei;
        private int countTodayAlarm;
        private int countTenAlarm;
        private int countAlarmDevice;

        public int getCountImei() {
            return countImei;
        }

        public void setCountImei(int countImei) {
            this.countImei = countImei;
        }

        public int getCountTodayAlarm() {
            return countTodayAlarm;
        }

        public void setCountTodayAlarm(int countTodayAlarm) {
            this.countTodayAlarm = countTodayAlarm;
        }

        public int getCountTenAlarm() {
            return countTenAlarm;
        }

        public void setCountTenAlarm(int countTenAlarm) {
            this.countTenAlarm = countTenAlarm;
        }

        public int getCountAlarmDevice() {
            return countAlarmDevice;
        }

        public void setCountAlarmDevice(int countAlarmDevice) {
            this.countAlarmDevice = countAlarmDevice;
        }
    }

}
