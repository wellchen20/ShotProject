package com.jpkh.cnpc.activity.entity;

import java.util.List;

public class FirstEntity {
    /**
     * code : 200
     * msg : success
     * data : [{"id":"36a0d3abc9834901877d70b41b22938a","imei":"868120236388309","deviceName":"GT370-88309","alarmType":"3","alarmName":"震动报警","lat":"29.982323450043662","lng":"106.27323413311","alarmTime":"2020-05-26 13:40:03","address":"重庆市合川区白塔路,文峰塔公园西南115米","readStatus":"0","handleStatus":"0","createTime":"2020-05-26 13:40:03","station":null,"station_lat":null,"station_lng":null}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 36a0d3abc9834901877d70b41b22938a
         * imei : 868120236388309
         * deviceName : GT370-88309
         * alarmType : 3
         * alarmName : 震动报警
         * lat : 29.982323450043662
         * lng : 106.27323413311
         * alarmTime : 2020-05-26 13:40:03
         * address : 重庆市合川区白塔路,文峰塔公园西南115米
         * readStatus : 0
         * handleStatus : 0
         * createTime : 2020-05-26 13:40:03
         * station : null
         * station_lat : null
         * station_lng : null
         */

        private String id;
        private String imei;
        private String deviceName;
        private String alarmType;
        private String alarmName;
        private String lat;
        private String lng;
        private String alarmTime;
        private String address;
        private String readStatus;
        private String handleStatus;
        private String createTime;
        private String station;
        private String station_lat;
        private String station_lng;

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

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getAlarmType() {
            return alarmType;
        }

        public void setAlarmType(String alarmType) {
            this.alarmType = alarmType;
        }

        public String getAlarmName() {
            return alarmName;
        }

        public void setAlarmName(String alarmName) {
            this.alarmName = alarmName;
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

        public String getAlarmTime() {
            return alarmTime;
        }

        public void setAlarmTime(String alarmTime) {
            this.alarmTime = alarmTime;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getReadStatus() {
            return readStatus;
        }

        public void setReadStatus(String readStatus) {
            this.readStatus = readStatus;
        }

        public String getHandleStatus() {
            return handleStatus;
        }

        public void setHandleStatus(String handleStatus) {
            this.handleStatus = handleStatus;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getStation() {
            return station;
        }

        public void setStation(String station) {
            this.station = station;
        }

        public String getStation_lat() {
            return station_lat;
        }

        public void setStation_lat(String station_lat) {
            this.station_lat = station_lat;
        }

        public String getStation_lng() {
            return station_lng;
        }

        public void setStation_lng(String station_lng) {
            this.station_lng = station_lng;
        }
    }
}
