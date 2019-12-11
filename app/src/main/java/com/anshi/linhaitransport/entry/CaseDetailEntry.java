package com.anshi.linhaitransport.entry;

import java.util.List;

public class CaseDetailEntry {

    /**
     * msg : 查询成功
     * code : 0
     * data : {"casedata":{"searchValue":null,"createBy":"3","createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"id":4,"caseTitle":"赵家屯一线道路损坏","caseType":"1","description":"路面不平，中间凸起，两旁有坑","areaId":4,"createDate":"2019-11-29 14:51:45","caseState":"1","address":null,"longitude":null,"latitude":null,"filepaths":"/profile/lhjtapp/1575615669209.jpg"},"disposedata":{"searchValue":null,"createBy":"3","createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"id":1,"caseId":4,"disposeType":"4","disposeOpinion":"已联系相关部门解决","deptId":null,"fileName":null,"filePath":null,"createDate":"2019-11-29 16:51:09"},"case_dispose":[{"searchValue":null,"createBy":"3","createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"id":1,"caseId":4,"disposeType":"4","disposeOpinion":"已联系相关部门解决","deptId":null,"fileName":null,"filePath":null,"createDate":"2019-11-29 16:51:09"}]}
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
         * casedata : {"searchValue":null,"createBy":"3","createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"id":4,"caseTitle":"赵家屯一线道路损坏","caseType":"1","description":"路面不平，中间凸起，两旁有坑","areaId":4,"createDate":"2019-11-29 14:51:45","caseState":"1","address":null,"longitude":null,"latitude":null,"filepaths":"/profile/lhjtapp/1575615669209.jpg"}
         * disposedata : {"searchValue":null,"createBy":"3","createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"id":1,"caseId":4,"disposeType":"4","disposeOpinion":"已联系相关部门解决","deptId":null,"fileName":null,"filePath":null,"createDate":"2019-11-29 16:51:09"}
         * case_dispose : [{"searchValue":null,"createBy":"3","createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"id":1,"caseId":4,"disposeType":"4","disposeOpinion":"已联系相关部门解决","deptId":null,"fileName":null,"filePath":null,"createDate":"2019-11-29 16:51:09"}]
         */

        private CasedataBean casedata;
        private DisposedataBean disposedata;
        private List<CaseDisposeBean> case_dispose;

        public CasedataBean getCasedata() {
            return casedata;
        }

        public void setCasedata(CasedataBean casedata) {
            this.casedata = casedata;
        }

        public DisposedataBean getDisposedata() {
            return disposedata;
        }

        public void setDisposedata(DisposedataBean disposedata) {
            this.disposedata = disposedata;
        }

        public List<CaseDisposeBean> getCase_dispose() {
            return case_dispose;
        }

        public void setCase_dispose(List<CaseDisposeBean> case_dispose) {
            this.case_dispose = case_dispose;
        }

        public static class CasedataBean {
            /**
             * searchValue : null
             * createBy : 3
             * createTime : null
             * updateBy : null
             * updateTime : null
             * remark : null
             * params : {}
             * id : 4
             * caseTitle : 赵家屯一线道路损坏
             * caseType : 1
             * description : 路面不平，中间凸起，两旁有坑
             * areaId : 4
             * createDate : 2019-11-29 14:51:45
             * caseState : 1
             * address : null
             * longitude : null
             * latitude : null
             * filepaths : /profile/lhjtapp/1575615669209.jpg
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
            private Object address;
            private Object longitude;
            private Object latitude;
            private String filepaths;

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

            public Object getAddress() {
                return address;
            }

            public void setAddress(Object address) {
                this.address = address;
            }

            public Object getLongitude() {
                return longitude;
            }

            public void setLongitude(Object longitude) {
                this.longitude = longitude;
            }

            public Object getLatitude() {
                return latitude;
            }

            public void setLatitude(Object latitude) {
                this.latitude = latitude;
            }

            public String getFilepaths() {
                return filepaths;
            }

            public void setFilepaths(String filepaths) {
                this.filepaths = filepaths;
            }

            public static class ParamsBean {
            }
        }

        public static class DisposedataBean {
            /**
             * searchValue : null
             * createBy : 3
             * createTime : null
             * updateBy : null
             * updateTime : null
             * remark : null
             * params : {}
             * id : 1
             * caseId : 4
             * disposeType : 4
             * disposeOpinion : 已联系相关部门解决
             * deptId : null
             * fileName : null
             * filePath : null
             * createDate : 2019-11-29 16:51:09
             */

            private Object searchValue;
            private String createBy;
            private Object createTime;
            private Object updateBy;
            private Object updateTime;
            private Object remark;
            private ParamsBeanX params;
            private int id;
            private int caseId;
            private String disposeType;
            private String disposeOpinion;
            private Object deptId;
            private Object fileName;
            private String filePath;
            private String createDate;

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

            public ParamsBeanX getParams() {
                return params;
            }

            public void setParams(ParamsBeanX params) {
                this.params = params;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getCaseId() {
                return caseId;
            }

            public void setCaseId(int caseId) {
                this.caseId = caseId;
            }

            public String getDisposeType() {
                return disposeType;
            }

            public void setDisposeType(String disposeType) {
                this.disposeType = disposeType;
            }

            public String getDisposeOpinion() {
                return disposeOpinion;
            }

            public void setDisposeOpinion(String disposeOpinion) {
                this.disposeOpinion = disposeOpinion;
            }

            public Object getDeptId() {
                return deptId;
            }

            public void setDeptId(Object deptId) {
                this.deptId = deptId;
            }

            public Object getFileName() {
                return fileName;
            }

            public void setFileName(Object fileName) {
                this.fileName = fileName;
            }

            public String getFilePath() {
                return filePath;
            }

            public void setFilePath(String filePath) {
                this.filePath = filePath;
            }

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public static class ParamsBeanX {
            }
        }

        public static class CaseDisposeBean {
            /**
             * searchValue : null
             * createBy : 3
             * createTime : null
             * updateBy : null
             * updateTime : null
             * remark : null
             * params : {}
             * id : 1
             * caseId : 4
             * disposeType : 4
             * disposeOpinion : 已联系相关部门解决
             * deptId : null
             * fileName : null
             * filePath : null
             * createDate : 2019-11-29 16:51:09
             */

            private Object searchValue;
            private String createBy;
            private Object createTime;
            private Object updateBy;
            private Object updateTime;
            private Object remark;
            private ParamsBeanXX params;
            private int id;
            private int caseId;
            private String disposeType;
            private String disposeOpinion;
            private Object deptId;
            private Object fileName;
            private Object filePath;
            private String createDate;

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

            public ParamsBeanXX getParams() {
                return params;
            }

            public void setParams(ParamsBeanXX params) {
                this.params = params;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getCaseId() {
                return caseId;
            }

            public void setCaseId(int caseId) {
                this.caseId = caseId;
            }

            public String getDisposeType() {
                return disposeType;
            }

            public void setDisposeType(String disposeType) {
                this.disposeType = disposeType;
            }

            public String getDisposeOpinion() {
                return disposeOpinion;
            }

            public void setDisposeOpinion(String disposeOpinion) {
                this.disposeOpinion = disposeOpinion;
            }

            public Object getDeptId() {
                return deptId;
            }

            public void setDeptId(Object deptId) {
                this.deptId = deptId;
            }

            public Object getFileName() {
                return fileName;
            }

            public void setFileName(Object fileName) {
                this.fileName = fileName;
            }

            public Object getFilePath() {
                return filePath;
            }

            public void setFilePath(Object filePath) {
                this.filePath = filePath;
            }

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public static class ParamsBeanXX {
            }
        }
    }
}
