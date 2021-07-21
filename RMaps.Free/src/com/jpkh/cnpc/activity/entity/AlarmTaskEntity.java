package com.jpkh.cnpc.activity.entity;

import java.io.Serializable;

public class AlarmTaskEntity implements Serializable {
    /**
     * alarm : {"address":"山西省忻州市静乐县S313(忻黑线),鱼崖底村东306米","alarmName":"震动报警","alarmTime":"2020-08-05 18:10:44","alarmType":"3","deviceName":"GT370-58350","id":"ec9fb14cb11244b784fec648bad208c4","imei":"868120241058350","lat":"38.331373","lng":"111.896373"}
     * alarmId : ec9fb14cb11244b784fec648bad208c4
     * appImei : 00000000
     * id : 11
     * sendTime : 1596622245000
     * status : 0
     */

    private AlarmBean alarm;
    private String alarmId;
    private String appImei;
    private int id;
    private long sendTime;
    private int status;

    public AlarmBean getAlarm() {
        return alarm;
    }

    public void setAlarm(AlarmBean alarm) {
        this.alarm = alarm;
    }

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public String getAppImei() {
        return appImei;
    }

    public void setAppImei(String appImei) {
        this.appImei = appImei;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class AlarmBean implements Serializable{
        /**
         * address : 山西省忻州市静乐县S313(忻黑线),鱼崖底村东306米
         * alarmName : 震动报警
         * alarmTime : 2020-08-05 18:10:44
         * alarmType : 3
         * deviceName : GT370-58350
         * id : ec9fb14cb11244b784fec648bad208c4
         * imei : 868120241058350
         * lat : 38.331373
         * lng : 111.896373
         */

        private String address;
        private String alarmName;
        private String alarmTime;
        private String alarmType;
        private String deviceName;
        private String id;
        private String imei;
        private String lat;
        private String lng;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAlarmName() {
            return alarmName;
        }

        public void setAlarmName(String alarmName) {
            this.alarmName = alarmName;
        }

        public String getAlarmTime() {
            return alarmTime;
        }

        public void setAlarmTime(String alarmTime) {
            this.alarmTime = alarmTime;
        }

        public String getAlarmType() {
            return alarmType;
        }

        public void setAlarmType(String alarmType) {
            this.alarmType = alarmType;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }
    }
}
