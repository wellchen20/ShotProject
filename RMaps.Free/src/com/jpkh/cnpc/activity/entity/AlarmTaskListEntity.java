package com.jpkh.cnpc.activity.entity;

import java.io.Serializable;
import java.util.List;

public class AlarmTaskListEntity implements Serializable {
    /**
     * code : 200
     * msg : success
     * data : [{"id":41,"appImei":"00000000","alarmId":"1be71353fac44f15a2939f662483454c",
     * "status":0,"sendTime":"2020-08-06 16:13:14",
     * "alarm":{"id":"1be71353fac44f15a2939f662483454c","imei":"868120241058962",
     * "deviceName":"GT370-58962","alarmType":"19","alarmName":"拆卸报警","lat":"38.331204",
     * "lng":"111.896284","alarmTime":"2020-08-06 16:13:13",
     * "address":"山西省忻州市静乐县S313(忻黑线),鱼崖底村东南302米",
     * "readStatus":"","handleStatus":"","handleBy":"","handleTime":"",
     * "remark":"","createTime":"","hasPush":"","station":"","station_lat":"","station_lng":""}}]
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
         * id : 41
         * appImei : 00000000
         * alarmId : 1be71353fac44f15a2939f662483454c
         * status : 0
         * sendTime : 2020-08-06 16:13:14
         * alarm : {"id":"1be71353fac44f15a2939f662483454c","imei":"868120241058962","deviceName":"GT370-58962","alarmType":"19","alarmName":"拆卸报警","lat":"38.331204","lng":"111.896284","alarmTime":"2020-08-06 16:13:13","address":"山西省忻州市静乐县S313(忻黑线),鱼崖底村东南302米","readStatus":"","handleStatus":"","handleBy":"","handleTime":"","remark":"","createTime":"","hasPush":"","station":"","station_lat":"","station_lng":""}
         */

        private int id;
        private String appImei;
        private String alarmId;
        private int status;
        private String sendTime;
        private AlarmBean alarm;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAppImei() {
            return appImei;
        }

        public void setAppImei(String appImei) {
            this.appImei = appImei;
        }

        public String getAlarmId() {
            return alarmId;
        }

        public void setAlarmId(String alarmId) {
            this.alarmId = alarmId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getSendTime() {
            return sendTime;
        }

        public void setSendTime(String sendTime) {
            this.sendTime = sendTime;
        }

        public AlarmBean getAlarm() {
            return alarm;
        }

        public void setAlarm(AlarmBean alarm) {
            this.alarm = alarm;
        }

        public static class AlarmBean {
            /**
             * id : 1be71353fac44f15a2939f662483454c
             * imei : 868120241058962
             * deviceName : GT370-58962
             * alarmType : 19
             * alarmName : 拆卸报警
             * lat : 38.331204
             * lng : 111.896284
             * alarmTime : 2020-08-06 16:13:13
             * address : 山西省忻州市静乐县S313(忻黑线),鱼崖底村东南302米
             * readStatus :
             * handleStatus :
             * handleBy :
             * handleTime :
             * remark :
             * createTime :
             * hasPush :
             * station :
             * station_lat :
             * station_lng :
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
            private String handleBy;
            private String handleTime;
            private String remark;
            private String createTime;
            private String hasPush;
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

            public String getHandleBy() {
                return handleBy;
            }

            public void setHandleBy(String handleBy) {
                this.handleBy = handleBy;
            }

            public String getHandleTime() {
                return handleTime;
            }

            public void setHandleTime(String handleTime) {
                this.handleTime = handleTime;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getHasPush() {
                return hasPush;
            }

            public void setHasPush(String hasPush) {
                this.hasPush = hasPush;
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
}
