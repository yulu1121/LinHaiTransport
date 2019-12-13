package com.anshi.linhaitransport.entry;

public class UploadEntry {
    /**
     * msg : 操作成功
     * code : 0
     * data : {"searchValue":null,"createBy":"3","createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"id":15,"caseTitle":"1111","caseType":"1","description":"1111","areaId":4,"createDate":"2019-12-12 11:56:10","caseState":"1","address":"11111111111","longitude":"1111","latitude":"1111","filepaths":"11111111111111111111111111","disposeState":null,"patrolType":"2"}
     */

    private String msg;
    private int code;
    private DataBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * searchValue : null
         * createBy : 3
         * createTime : null
         * updateBy : null
         * updateTime : null
         * remark : null
         * params : {}
         * id : 15
         * caseTitle : 1111
         * caseType : 1
         * description : 1111
         * areaId : 4
         * createDate : 2019-12-12 11:56:10
         * caseState : 1
         * address : 11111111111
         * longitude : 1111
         * latitude : 1111
         * filepaths : 11111111111111111111111111
         * disposeState : null
         * patrolType : 2
         */

        private Object searchValue;
        private String createBy;
        private Object createTime;
        private Object updateBy;
        private Object updateTime;
        private Object remark;
        private ParamsBean params;
        private int id;
        private String caseTitle;
        private String caseType;
        private String description;
        private int areaId;
        private String createDate;
        private String caseState;
        private String address;
        private String longitude;
        private String latitude;
        private String filepaths;
        private Object disposeState;
        private String patrolType;

        public Object getSearchValue() {
            return searchValue;
        }

        public void setSearchValue(Object searchValue) {
            this.searchValue = searchValue;
        }

        public String getCreateBy() {
            return createBy;
        }

        public void setCreateBy(String createBy) {
            this.createBy = createBy;
        }

        public Object getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Object createTime) {
            this.createTime = createTime;
        }

        public Object getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(Object updateBy) {
            this.updateBy = updateBy;
        }

        public Object getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Object updateTime) {
            this.updateTime = updateTime;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public ParamsBean getParams() {
            return params;
        }

        public void setParams(ParamsBean params) {
            this.params = params;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCaseTitle() {
            return caseTitle;
        }

        public void setCaseTitle(String caseTitle) {
            this.caseTitle = caseTitle;
        }

        public String getCaseType() {
            return caseType;
        }

        public void setCaseType(String caseType) {
            this.caseType = caseType;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getAreaId() {
            return areaId;
        }

        public void setAreaId(int areaId) {
            this.areaId = areaId;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getCaseState() {
            return caseState;
        }

        public void setCaseState(String caseState) {
            this.caseState = caseState;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getFilepaths() {
            return filepaths;
        }

        public void setFilepaths(String filepaths) {
            this.filepaths = filepaths;
        }

        public Object getDisposeState() {
            return disposeState;
        }

        public void setDisposeState(Object disposeState) {
            this.disposeState = disposeState;
        }

        public String getPatrolType() {
            return patrolType;
        }

        public void setPatrolType(String patrolType) {
            this.patrolType = patrolType;
        }

        public static class ParamsBean {
        }
    }
}
