package com.jpkh.cnpc.activity.entity;

import java.util.List;

public class ShotEntity {

    /**
     * code : 200
     * msg : success
     * data : {"delList":[{"id":"9cdfb86632554873808eb1ff9f8a4b4c","station":"5245.51354.5##","wellDepth":null,"explosiveDepth":null,"explosiveWeight":null,"xxxCoordinate":null,"yyyCoordinate":null,"lat":"30.19561826","lng":"106.1711145","nearestImei":null,"updateMode":"0","userId":"0001_02","geom":"","name":"","fileId":"","delFlag":"","nearestDistance":null,"version":1}],"addlist":[{"id":"9cdfb86632554873808eb1ff9f8a4b4c","station":"5245.51354.5##","wellDepth":null,"explosiveDepth":null,"explosiveWeight":null,"xxxCoordinate":null,"yyyCoordinate":null,"lat":"30.19561826","lng":"106.1711145","nearestImei":null,"updateMode":"0","userId":"0001_02","geom":"","name":"","fileId":"","delFlag":"","nearestDistance":null,"version":1}],"updateList":[{"id":"9cdfb86632554873808eb1ff9f8a4b4c","station":"5245.51354.5##","wellDepth":null,"explosiveDepth":null,"explosiveWeight":null,"xxxCoordinate":null,"yyyCoordinate":null,"lat":"30.19561826","lng":"106.1711145","nearestImei":null,"updateMode":"0","userId":"0001_02","geom":"","name":"","fileId":"","delFlag":"","nearestDistance":null,"version":1}]}
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
        private List<DelListBean> delList;
        private List<AddlistBean> addList;
        private List<UpdateListBean> updateList;

        public List<DelListBean> getDelList() {
            return delList;
        }

        public void setDelList(List<DelListBean> delList) {
            this.delList = delList;
        }

        public List<AddlistBean> getAddlist() {
            return addList;
        }

        public void setAddlist(List<AddlistBean> addlist) {
            this.addList = addlist;
        }

        public List<UpdateListBean> getUpdateList() {
            return updateList;
        }

        public void setUpdateList(List<UpdateListBean> updateList) {
            this.updateList = updateList;
        }

        public static class DelListBean {
            /**
             * id : 9cdfb86632554873808eb1ff9f8a4b4c
             * station : 5245.51354.5##
             * wellDepth : null
             * explosiveDepth : null
             * explosiveWeight : null
             * xxxCoordinate : null
             * yyyCoordinate : null
             * lat : 30.19561826
             * lng : 106.1711145
             * nearestImei : null
             * updateMode : 0
             * userId : 0001_02
             * geom :
             * name :
             * fileId :
             * delFlag :
             * nearestDistance : null
             * version : 1
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
            private int version;

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

            public int getVersion() {
                return version;
            }

            public void setVersion(int version) {
                this.version = version;
            }
        }

        public static class AddlistBean {
            /**
             * id : 9cdfb86632554873808eb1ff9f8a4b4c
             * station : 5245.51354.5##
             * wellDepth : null
             * explosiveDepth : null
             * explosiveWeight : null
             * xxxCoordinate : null
             * yyyCoordinate : null
             * lat : 30.19561826
             * lng : 106.1711145
             * nearestImei : null
             * updateMode : 0
             * userId : 0001_02
             * geom :
             * name :
             * fileId :
             * delFlag :
             * nearestDistance : null
             * version : 1
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
            private int version;

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

            public int getVersion() {
                return version;
            }

            public void setVersion(int version) {
                this.version = version;
            }

            @Override
            public String toString() {
                return "AddlistBean{" +
                        "id='" + id + '\'' +
                        ", station='" + station + '\'' +
                        ", wellDepth='" + wellDepth + '\'' +
                        ", explosiveDepth='" + explosiveDepth + '\'' +
                        ", explosiveWeight='" + explosiveWeight + '\'' +
                        ", xxxCoordinate='" + xxxCoordinate + '\'' +
                        ", yyyCoordinate='" + yyyCoordinate + '\'' +
                        ", lat='" + lat + '\'' +
                        ", lng='" + lng + '\'' +
                        ", nearestImei='" + nearestImei + '\'' +
                        ", updateMode='" + updateMode + '\'' +
                        ", userId='" + userId + '\'' +
                        ", geom='" + geom + '\'' +
                        ", name='" + name + '\'' +
                        ", fileId='" + fileId + '\'' +
                        ", delFlag='" + delFlag + '\'' +
                        ", nearestDistance='" + nearestDistance + '\'' +
                        ", version=" + version +
                        '}';
            }
        }

        public static class UpdateListBean {
            /**
             * id : 9cdfb86632554873808eb1ff9f8a4b4c
             * station : 5245.51354.5##
             * wellDepth : null
             * explosiveDepth : null
             * explosiveWeight : null
             * xxxCoordinate : null
             * yyyCoordinate : null
             * lat : 30.19561826
             * lng : 106.1711145
             * nearestImei : null
             * updateMode : 0
             * userId : 0001_02
             * geom :
             * name :
             * fileId :
             * delFlag :
             * nearestDistance : null
             * version : 1
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
            private int version;

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

            public int getVersion() {
                return version;
            }

            public void setVersion(int version) {
                this.version = version;
            }
        }
    }
}
