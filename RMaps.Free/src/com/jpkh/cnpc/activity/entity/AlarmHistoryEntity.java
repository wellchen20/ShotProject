package com.jpkh.cnpc.activity.entity;

import java.io.Serializable;
import java.util.List;

public class AlarmHistoryEntity implements Serializable {

    /**
     * code : 200
     * msg : success
     * data : {"total":121,"list":[{"id":"c86b7fd5a056407d9b6a7770c071f4fc","imei":"6010184196","deviceName":"6010184196",
     * "alarmType":"00100000","alarmName":"拆除报警","lat":"29.020475","lng":"105.464684","alarmTime":"2020-11-19 15:02:20",
     * "address":null,"readStatus":null,"handleStatus":"0","handleBy":null,"handleTime":null,"remark":null,
     * "createTime":"2020-11-19 15:02:21","hasPush":null,"deviceState":null,"handleFrom":"","station":null,
     * "station_lat":null,"station_lng":null,"userId":null,"usernameSys":null,"usernameApp":null}],"pageNum":1,
     * "pageSize":1,"size":1,"startRow":1,"endRow":1,"pages":121,"prePage":0,"nextPage":2,"isFirstPage":true,
     * "isLastPage":false,"hasPreviousPage":false,"hasNextPage":true,"navigatePages":8,"navigatepageNums":[1,2,3,4,5,6,7,8],
     * "navigateFirstPage":1,"navigateLastPage":8}
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
         * total : 121
         * list : [{"id":"c86b7fd5a056407d9b6a7770c071f4fc","imei":"6010184196","deviceName":"6010184196","alarmType":"00100000","alarmName":"拆除报警","lat":"29.020475","lng":"105.464684","alarmTime":"2020-11-19 15:02:20","address":null,"readStatus":null,"handleStatus":"0","handleBy":null,"handleTime":null,"remark":null,"createTime":"2020-11-19 15:02:21","hasPush":null,"deviceState":null,"handleFrom":"","station":null,"station_lat":null,"station_lng":null,"userId":null,"usernameSys":null,"usernameApp":null}]
         * pageNum : 1
         * pageSize : 1
         * size : 1
         * startRow : 1
         * endRow : 1
         * pages : 121
         * prePage : 0
         * nextPage : 2
         * isFirstPage : true
         * isLastPage : false
         * hasPreviousPage : false
         * hasNextPage : true
         * navigatePages : 8
         * navigatepageNums : [1,2,3,4,5,6,7,8]
         * navigateFirstPage : 1
         * navigateLastPage : 8
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

        public static class ListBean {
            /**
             * id : c86b7fd5a056407d9b6a7770c071f4fc
             * imei : 6010184196
             * deviceName : 6010184196
             * alarmType : 00100000
             * alarmName : 拆除报警
             * lat : 29.020475
             * lng : 105.464684
             * alarmTime : 2020-11-19 15:02:20
             * address : null
             * readStatus : null
             * handleStatus : 0
             * handleBy : null
             * handleTime : null
             * remark : null
             * createTime : 2020-11-19 15:02:21
             * hasPush : null
             * deviceState : null
             * handleFrom : 
             * station : null
             * station_lat : null
             * station_lng : null
             * userId : null
             * usernameSys : null
             * usernameApp : null
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
            private String deviceState;
            private String handleFrom;
            private String station;
            private String station_lat;
            private String station_lng;
            private String userId;
            private String usernameSys;
            private String usernameApp;

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

            public String getDeviceState() {
                return deviceState;
            }

            public void setDeviceState(String deviceState) {
                this.deviceState = deviceState;
            }

            public String getHandleFrom() {
                return handleFrom;
            }

            public void setHandleFrom(String handleFrom) {
                this.handleFrom = handleFrom;
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

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getUsernameSys() {
                return usernameSys;
            }

            public void setUsernameSys(String usernameSys) {
                this.usernameSys = usernameSys;
            }

            public String getUsernameApp() {
                return usernameApp;
            }

            public void setUsernameApp(String usernameApp) {
                this.usernameApp = usernameApp;
            }
        }
    }
}
