package com.jpkh.cnpc.activity.entity;

public class StationInfoEntity {
    /**
     * code : 200
     * msg : success
     * data : {"id":"239293ad793a4a8aafb2c4becbf7f7af","station":"53071305","wellDepth":"","explosiveDepth":null,"explosiveWeight":null,
     * "xxxCoordinate":null,"yyyCoordinate":null,"lat":"28.79176325","lng":"105.1020114","nearestImei":null,"updateMode":"0","userId":"0001_00010001",
     * "geom":null,"name":null,"fileId":null,"delFlag":null,"nearestDistance":null,"version":null}
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
         * id : 239293ad793a4a8aafb2c4becbf7f7af
         * station : 53071305
         * wellDepth : 
         * explosiveDepth : null
         * explosiveWeight : null
         * xxxCoordinate : null
         * yyyCoordinate : null
         * lat : 28.79176325
         * lng : 105.1020114
         * nearestImei : null
         * updateMode : 0
         * userId : 0001_00010001
         * geom : null
         * name : null
         * fileId : null
         * delFlag : null
         * nearestDistance : null
         * version : null
         */

        private String id;
        private String station;
        private String wellDepth;
        private String explosiveDepth;
        private String explosiveWeight;
        private String xxxCoordinate;
        private String yyyCoordinate;
        private String lat;
        private String lng;
        private String nearestImei;
        private String updateMode;
        private String userId;
        private String geom;
        private String name;
        private String fileId;
        private String delFlag;
        private String nearestDistance;
        private String version;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStation() {
            return station;
        }

        public void setStation(String station) {
            this.station = station;
        }

        public String getWellDepth() {
            return wellDepth;
        }

        public void setWellDepth(String wellDepth) {
            this.wellDepth = wellDepth;
        }

        public String getExplosiveDepth() {
            return explosiveDepth;
        }

        public void setExplosiveDepth(String explosiveDepth) {
            this.explosiveDepth = explosiveDepth;
        }

        public String getExplosiveWeight() {
            return explosiveWeight;
        }

        public void setExplosiveWeight(String explosiveWeight) {
            this.explosiveWeight = explosiveWeight;
        }

        public String getXxxCoordinate() {
            return xxxCoordinate;
        }

        public void setXxxCoordinate(String xxxCoordinate) {
            this.xxxCoordinate = xxxCoordinate;
        }

        public String getYyyCoordinate() {
            return yyyCoordinate;
        }

        public void setYyyCoordinate(String yyyCoordinate) {
            this.yyyCoordinate = yyyCoordinate;
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

        public String getNearestImei() {
            return nearestImei;
        }

        public void setNearestImei(String nearestImei) {
            this.nearestImei = nearestImei;
        }

        public String getUpdateMode() {
            return updateMode;
        }

        public void setUpdateMode(String updateMode) {
            this.updateMode = updateMode;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getGeom() {
            return geom;
        }

        public void setGeom(String geom) {
            this.geom = geom;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFileId() {
            return fileId;
        }

        public void setFileId(String fileId) {
            this.fileId = fileId;
        }

        public String getDelFlag() {
            return delFlag;
        }

        public void setDelFlag(String delFlag) {
            this.delFlag = delFlag;
        }

        public String getNearestDistance() {
            return nearestDistance;
        }

        public void setNearestDistance(String nearestDistance) {
            this.nearestDistance = nearestDistance;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }
}
