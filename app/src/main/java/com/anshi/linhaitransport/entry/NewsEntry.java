package com.anshi.linhaitransport.entry;

import java.util.List;

public class NewsEntry {

    /**
     * msg : 查询成功
     * code : 0
     * data : [{"searchValue":null,"createBy":"admin","createTime":"2019-12-05 15:11:38","updateBy":"","updateTime":null,"remark":null,"params":{},"noticeId":4,"noticeTitle":"法治兴则国兴，法治强则国强","noticeType":"2","status":"0","logoImg":"/profile/upload/2019/12/05/998a03711cf9b3486b007056bef486a4.jpg"}]
     */

    private String msg;
    private int code;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * searchValue : null
         * createBy : admin
         * createTime : 2019-12-05 15:11:38
         * updateBy :
         * updateTime : null
         * remark : null
         * params : {}
         * noticeId : 4
         * noticeTitle : 法治兴则国兴，法治强则国强
         * noticeType : 2
         * status : 0
         * logoImg : /profile/upload/2019/12/05/998a03711cf9b3486b007056bef486a4.jpg
         */

        private Object searchValue;
        private String createBy;
        private String createTime;
        private String updateBy;
        private Object updateTime;
        private Object remark;
        private ParamsBean params;
        private int noticeId;
        private String noticeTitle;
        private String noticeType;
        private String status;
        private String logoImg;

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

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(String updateBy) {
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

        public int getNoticeId() {
            return noticeId;
        }

        public void setNoticeId(int noticeId) {
            this.noticeId = noticeId;
        }

        public String getNoticeTitle() {
            return noticeTitle;
        }

        public void setNoticeTitle(String noticeTitle) {
            this.noticeTitle = noticeTitle;
        }

        public String getNoticeType() {
            return noticeType;
        }

        public void setNoticeType(String noticeType) {
            this.noticeType = noticeType;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLogoImg() {
            return logoImg;
        }

        public void setLogoImg(String logoImg) {
            this.logoImg = logoImg;
        }

        public static class ParamsBean {
        }
    }
}
