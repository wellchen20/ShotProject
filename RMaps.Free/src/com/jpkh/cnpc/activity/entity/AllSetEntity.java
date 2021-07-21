package com.jpkh.cnpc.activity.entity;

import java.io.Serializable;
import java.util.List;

public class AllSetEntity implements Serializable {

    /**
     * code : 200
     * msg : success
     * data : {"total":57,"list":[{"imei":"6010184469","deviceName":"6010184469","icon":null,"status":"2","deviceType":"3G","lat":"29.337206","lng":"104.837146","station":null,"userId":"0001_00010001","orgId":"C102","staLat":null,"staLng":null,"deviceState":null},{"imei":"6010201693","deviceName":"6010201693","icon":null,"status":"1","deviceType":"3G","lat":"39.276515","lng":"116.099297","station":null,"userId":"0001_00010001","orgId":null,"staLat":null,"staLng":null,"deviceState":null},{"imei":"6010201701","deviceName":"6010201701","icon":null,"status":"1","deviceType":"3G","lat":"39.276622","lng":"116.099368","station":null,"userId":"0001_00010001","orgId":null,"staLat":null,"staLng":null,"deviceState":null},{"imei":"6010201686","deviceName":"6010201686","icon":null,"status":"1","deviceType":"3G","lat":"39.276768","lng":"116.099404","station":null,"userId":"0001_00010001","orgId":null,"staLat":null,"staLng":null,"deviceState":null},{"imei":"6010201689","deviceName":"6010201689","icon":null,"status":"1","deviceType":"3G","lat":"39.276657","lng":"116.099413","station":null,"userId":"0001_00010001","orgId":null,"staLat":null,"staLng":null,"deviceState":null},{"imei":"6010201684","deviceName":"6010201684","icon":null,"status":"1","deviceType":"3G","lat":"39.259151","lng":"116.081502","station":null,"userId":"0001_00010001","orgId":null,"staLat":null,"staLng":null,"deviceState":null},{"imei":"6010201687","deviceName":"6010201687","icon":null,"status":"1","deviceType":"3G","lat":"39.275800","lng":"116.097715","station":null,"userId":"0001_00010001","orgId":null,"staLat":null,"staLng":null,"deviceState":null},{"imei":"6010201694","deviceName":"6010201694","icon":null,"status":"1","deviceType":"3G","lat":"39.276671","lng":"116.099377","station":null,"userId":"0001_00010001","orgId":null,"staLat":null,"staLng":null,"deviceState":null},{"imei":"6010201683","deviceName":"6010201683","icon":null,"status":"1","deviceType":"3G","lat":"39.276622","lng":"116.099635","station":null,"userId":"0001_00010001","orgId":null,"staLat":null,"staLng":null,"deviceState":null},{"imei":"6010201688","deviceName":"6010201688","icon":null,"status":"1","deviceType":"3G","lat":"39.263124","lng":"116.081626","station":null,"userId":"0001_00010001","orgId":null,"staLat":null,"staLng":null,"deviceState":null}],"pageNum":1,"pageSize":10,"size":10,"startRow":1,"endRow":10,"pages":6,"prePage":0,"nextPage":2,"isFirstPage":true,"isLastPage":false,"hasPreviousPage":false,"hasNextPage":true,"navigatePages":8,"navigatepageNums":[1,2,3,4,5,6],"navigateFirstPage":1,"navigateLastPage":6}
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

    public static class DataBean implements Serializable{
        /**
         * total : 57
         * list : [{"imei":"6010184469","deviceName":"6010184469","icon":null,"status":"2","deviceType":"3G","lat":"29.337206","lng":"104.837146","station":null,"userId":"0001_00010001","orgId":"C102","staLat":null,"staLng":null,"deviceState":null},{"imei":"6010201693","deviceName":"6010201693","icon":null,"status":"1","deviceType":"3G","lat":"39.276515","lng":"116.099297","station":null,"userId":"0001_00010001","orgId":null,"staLat":null,"staLng":null,"deviceState":null},{"imei":"6010201701","deviceName":"6010201701","icon":null,"status":"1","deviceType":"3G","lat":"39.276622","lng":"116.099368","station":null,"userId":"0001_00010001","orgId":null,"staLat":null,"staLng":null,"deviceState":null},{"imei":"6010201686","deviceName":"6010201686","icon":null,"status":"1","deviceType":"3G","lat":"39.276768","lng":"116.099404","station":null,"userId":"0001_00010001","orgId":null,"staLat":null,"staLng":null,"deviceState":null},{"imei":"6010201689","deviceName":"6010201689","icon":null,"status":"1","deviceType":"3G","lat":"39.276657","lng":"116.099413","station":null,"userId":"0001_00010001","orgId":null,"staLat":null,"staLng":null,"deviceState":null},{"imei":"6010201684","deviceName":"6010201684","icon":null,"status":"1","deviceType":"3G","lat":"39.259151","lng":"116.081502","station":null,"userId":"0001_00010001","orgId":null,"staLat":null,"staLng":null,"deviceState":null},{"imei":"6010201687","deviceName":"6010201687","icon":null,"status":"1","deviceType":"3G","lat":"39.275800","lng":"116.097715","station":null,"userId":"0001_00010001","orgId":null,"staLat":null,"staLng":null,"deviceState":null},{"imei":"6010201694","deviceName":"6010201694","icon":null,"status":"1","deviceType":"3G","lat":"39.276671","lng":"116.099377","station":null,"userId":"0001_00010001","orgId":null,"staLat":null,"staLng":null,"deviceState":null},{"imei":"6010201683","deviceName":"6010201683","icon":null,"status":"1","deviceType":"3G","lat":"39.276622","lng":"116.099635","station":null,"userId":"0001_00010001","orgId":null,"staLat":null,"staLng":null,"deviceState":null},{"imei":"6010201688","deviceName":"6010201688","icon":null,"status":"1","deviceType":"3G","lat":"39.263124","lng":"116.081626","station":null,"userId":"0001_00010001","orgId":null,"staLat":null,"staLng":null,"deviceState":null}]
         * pageNum : 1
         * pageSize : 10
         * size : 10
         * startRow : 1
         * endRow : 10
         * pages : 6
         * prePage : 0
         * nextPage : 2
         * isFirstPage : true
         * isLastPage : false
         * hasPreviousPage : false
         * hasNextPage : true
         * navigatePages : 8
         * navigatepageNums : [1,2,3,4,5,6]
         * navigateFirstPage : 1
         * navigateLastPage : 6
         */

        private int total;
        private int pageNum;
        private int pageSize;
        private int size;
        private int startRow;
        private int endRow;
        private int pages;
        private int prePage;
        private int nextPage;
        private boolean isFirstPage;
        private boolean isLastPage;
        private boolean hasPreviousPage;
        private boolean hasNextPage;
        private int navigatePages;
        private int navigateFirstPage;
        private int navigateLastPage;
        private List<ListBean> list;
        private List<Integer> navigatepageNums;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getStartRow() {
            return startRow;
        }

        public void setStartRow(int startRow) {
            this.startRow = startRow;
        }

        public int getEndRow() {
            return endRow;
        }

        public void setEndRow(int endRow) {
            this.endRow = endRow;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public int getPrePage() {
            return prePage;
        }

        public void setPrePage(int prePage) {
            this.prePage = prePage;
        }

        public int getNextPage() {
            return nextPage;
        }

        public void setNextPage(int nextPage) {
            this.nextPage = nextPage;
        }

        public boolean isIsFirstPage() {
            return isFirstPage;
        }

        public void setIsFirstPage(boolean isFirstPage) {
            this.isFirstPage = isFirstPage;
        }

        public boolean isIsLastPage() {
            return isLastPage;
        }

        public void setIsLastPage(boolean isLastPage) {
            this.isLastPage = isLastPage;
        }

        public boolean isHasPreviousPage() {
            return hasPreviousPage;
        }

        public void setHasPreviousPage(boolean hasPreviousPage) {
            this.hasPreviousPage = hasPreviousPage;
        }

        public boolean isHasNextPage() {
            return hasNextPage;
        }

        public void setHasNextPage(boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
        }

        public int getNavigatePages() {
            return navigatePages;
        }

        public void setNavigatePages(int navigatePages) {
            this.navigatePages = navigatePages;
        }

        public int getNavigateFirstPage() {
            return navigateFirstPage;
        }

        public void setNavigateFirstPage(int navigateFirstPage) {
            this.navigateFirstPage = navigateFirstPage;
        }

        public int getNavigateLastPage() {
            return navigateLastPage;
        }

        public void setNavigateLastPage(int navigateLastPage) {
            this.navigateLastPage = navigateLastPage;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public List<Integer> getNavigatepageNums() {
            return navigatepageNums;
        }

        public void setNavigatepageNums(List<Integer> navigatepageNums) {
            this.navigatepageNums = navigatepageNums;
        }

        public static class ListBean implements Serializable{
            /**
             * imei : 6010184469
             * deviceName : 6010184469
             * icon : null
             * status : 2
             * deviceType : 3G
             * lat : 29.337206
             * lng : 104.837146
             * station : null
             * userId : 0001_00010001
             * orgId : C102
             * staLat : null
             * staLng : null
             * deviceState : null
             */

            private String imei;
            private String deviceName;
            private String icon;
            private String status;
            private String deviceType;
            private String lat;
            private String lng;
            private String station;
            private String userId;
            private String orgId;
            private String staLat;
            private String staLng;
            private String deviceState;

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

            public Object getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getDeviceType() {
                return deviceType;
            }

            public void setDeviceType(String deviceType) {
                this.deviceType = deviceType;
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

            public Object getStation() {
                return station;
            }

            public void setStation(String station) {
                this.station = station;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getOrgId() {
                return orgId;
            }

            public void setOrgId(String orgId) {
                this.orgId = orgId;
            }

            public Object getStaLat() {
                return staLat;
            }

            public void setStaLat(String staLat) {
                this.staLat = staLat;
            }

            public Object getStaLng() {
                return staLng;
            }

            public void setStaLng(String staLng) {
                this.staLng = staLng;
            }

            public Object getDeviceState() {
                return deviceState;
            }

            public void setDeviceState(String deviceState) {
                this.deviceState = deviceState;
            }
        }
    }
}
