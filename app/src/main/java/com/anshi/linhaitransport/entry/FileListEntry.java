package com.anshi.linhaitransport.entry;

import java.util.List;

public class FileListEntry {

    /**
     * msg : 查询成功
     * code : 0
     * data : [{"searchValue":null,"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"id":2,"fileTitle":"ddd","fileContent":"dddd","upFileName":"学校测试修改意见表0819.docx","upFilePath":"/profile//2019/12/02/4f5485caea4dbe5d3c6f4573cdd2671b.docx","createDate":"2019-12-02 00:00:00"}]
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
         * createBy : null
         * createTime : null
         * updateBy : null
         * updateTime : null
         * remark : null
         * params : {}
         * id : 2
         * fileTitle : ddd
         * fileContent : dddd
         * upFileName : 学校测试修改意见表0819.docx
         * upFilePath : /profile//2019/12/02/4f5485caea4dbe5d3c6f4573cdd2671b.docx
         * createDate : 2019-12-02 00:00:00
         */

        private Object searchValue;
        private Object createBy;
        private Object createTime;
        private Object updateBy;
        private Object updateTime;
        private Object remark;
        private ParamsBean params;
        private int id;
        private String fileTitle;
        private String fileContent;
        private String upFileName;
        private String upFilePath;
        private String createDate;

        public Object getSearchValue() {
            return searchValue;
        }

        public void setSearchValue(Object searchValue) {
            this.searchValue = searchValue;
        }

        public Object getCreateBy() {
            return createBy;
        }

        public void setCreateBy(Object createBy) {
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

        public String getFileTitle() {
            return fileTitle;
        }

        public void setFileTitle(String fileTitle) {
            this.fileTitle = fileTitle;
        }

        public String getFileContent() {
            return fileContent;
        }

        public void setFileContent(String fileContent) {
            this.fileContent = fileContent;
        }

        public String getUpFileName() {
            return upFileName;
        }

        public void setUpFileName(String upFileName) {
            this.upFileName = upFileName;
        }

        public String getUpFilePath() {
            return upFilePath;
        }

        public void setUpFilePath(String upFilePath) {
            this.upFilePath = upFilePath;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public static class ParamsBean {
        }
    }
}
