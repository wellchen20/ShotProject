package com.jpkh.cnpc.activity.entity;

import java.io.Serializable;
import java.util.List;

public class StationPageEntity implements Serializable {
    /**
     * code : 200
     * msg : success
     * data : {"total":17,"list":[{"id":"6e8170f29efb4c2aacd2cd5a9f772faa","station":"62972134","lat":"40.16186111","lng":"116.2306389","userId":"0001_02","version":0},{"id":"6eb6918c89d243129fc211ba18a025b0","station":"63202110","lat":"40.16151389","lng":"116.2326917","userId":"0001_02","version":0},{"id":"db069812ba2f4a208ffb9596e6a7d540","station":"63202108","lat":"40.16148889","lng":"116.2321444","userId":"0001_02","version":0},{"id":"46d87770b4f64b218481d0ff63e8d1bf","station":"63092119","lat":"40.16204167","lng":"116.2325944","userId":"0001_02","version":0}],"pageNum":1,"pageSize":4,"size":4,"startRow":1,"endRow":4,"pages":5,"prePage":0,"nextPage":2,"isFirstPage":true,"isLastPage":false,"hasPreviousPage":false,"hasNextPage":true,"navigatePages":8,"navigatepageNums":[1,2,3,4,5],"navigateFirstPage":1,"navigateLastPage":5}
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
         * total : 17
         * list : [{"id":"6e8170f29efb4c2aacd2cd5a9f772faa","station":"62972134","lat":"40.16186111","lng":"116.2306389","userId":"0001_02","version":0},{"id":"6eb6918c89d243129fc211ba18a025b0","station":"63202110","lat":"40.16151389","lng":"116.2326917","userId":"0001_02","version":0},{"id":"db069812ba2f4a208ffb9596e6a7d540","station":"63202108","lat":"40.16148889","lng":"116.2321444","userId":"0001_02","version":0},{"id":"46d87770b4f64b218481d0ff63e8d1bf","station":"63092119","lat":"40.16204167","lng":"116.2325944","userId":"0001_02","version":0}]
         * pageNum : 1
         * pageSize : 4
         * size : 4
         * startRow : 1
         * endRow : 4
         * pages : 5
         * prePage : 0
         * nextPage : 2
         * isFirstPage : true
         * isLastPage : false
         * hasPreviousPage : false
         * hasNextPage : true
         * navigatePages : 8
         * navigatepageNums : [1,2,3,4,5]
         * navigateFirstPage : 1
         * navigateLastPage : 5
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
             * id : 6e8170f29efb4c2aacd2cd5a9f772faa
             * station : 62972134
             * lat : 40.16186111
             * lng : 116.2306389
             * userId : 0001_02
             * version : 0
             */

            private String id;
            private String station;
            private String lat;
            private String lng;
            private String userId;
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

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
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
