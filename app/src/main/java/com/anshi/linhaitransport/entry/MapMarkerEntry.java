package com.anshi.linhaitransport.entry;

import java.util.List;

public class MapMarkerEntry {
    /**
     * msg : 操作成功
     * code : 0
     * data : [{"searchValue":null,"createBy":"admin","createTime":"2019-12-13 15:07:37","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":"017ddd622487466c98d435012aac06a4","roadName":"二富线","roadManager":"炎","roadInspector":"之","roadMaintenance":"炎","buildTime":"2019-05-19 00:00:00","latitude":"41.9117144667564","longitude":"121.96203052369398","delFlag":"0","remarks":null,"text1":null,"text2":null},{"searchValue":null,"createBy":"admin","createTime":"2019-12-13 11:28:52","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":"0cf236a1b474499a8957c19d3e925134","roadName":"X711(凌海大道)","roadManager":"张三","roadInspector":"李四","roadMaintenance":"王五","buildTime":"1997-08-12 00:00:00","latitude":"41.02386592199798","longitude":"121.3743949912868","delFlag":"0","remarks":null,"text1":null,"text2":null},{"searchValue":null,"createBy":"admin","createTime":"2019-12-13 14:17:03","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":"40be12793f6148a18bbf0dd8ad2ad667","roadName":"G1(京哈高速)","roadManager":"张晓","roadInspector":"张亮","roadMaintenance":"张天","buildTime":"2019-08-12 00:00:00","latitude":"41.08001089404269","longitude":"121.31489123430588","delFlag":"0","remarks":null,"text1":null,"text2":null},{"searchValue":null,"createBy":"admin","createTime":"2019-12-13 15:03:28","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":"8ac8508b4fcf4acc8dc038809d7b993e","roadName":"S308(大锦线)","roadManager":"正","roadInspector":"品","roadMaintenance":"路","buildTime":"2019-05-13 00:00:00","latitude":"41.206269056376186","longitude":"121.59889950434051","delFlag":"0","remarks":null,"text1":null,"text2":null},{"searchValue":null,"createBy":"admin","createTime":"2019-12-13 14:24:22","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":"9f4c2860103449e18bc6ca20d0614981","roadName":"X711(凌海大道)","roadManager":"王哈哈","roadInspector":"王嘻嘻","roadMaintenance":"王嘿嘿","buildTime":"2019-08-12 00:00:00","latitude":"41.038464450429416","longitude":"121.37382007576042","delFlag":"0","remarks":null,"text1":null,"text2":null},{"searchValue":null,"createBy":"admin","createTime":"2019-12-13 15:01:35","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":"b3898560d9824038ab5cf00cb2968f0b","roadName":"兴闫线","roadManager":"方","roadInspector":"晃","roadMaintenance":"慌","buildTime":"2018-05-03 00:00:00","latitude":"41.30286131399296","longitude":"121.2930444443032","delFlag":"0","remarks":null,"text1":null,"text2":null},{"searchValue":null,"createBy":"admin","createTime":"2019-12-13 11:13:12","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":"c69e421e8a4f4b9b9712459126efcc3e","roadName":"千一路","roadManager":"王某","roadInspector":"李某","roadMaintenance":"赵某","buildTime":"2019-02-11 00:00:00","latitude":"41.013632763192966","longitude":"121.51941743281765","delFlag":"0","remarks":"测试","text1":null,"text2":null},{"searchValue":null,"createBy":"admin","createTime":"2019-12-13 14:48:25","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":"d7782c4d44e94ac0928d3eb87eb6d98a","roadName":"环城路","roadManager":"高成","roadInspector":"高强","roadMaintenance":"高未","buildTime":"2019-08-12 00:00:00","latitude":"41.16728773578716","longitude":"121.35240447240255","delFlag":"0","remarks":null,"text1":null,"text2":null},{"searchValue":null,"createBy":"admin","createTime":"2019-12-13 14:21:36","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":"da3781cd63a649d395b03eda0cd1b78d","roadName":"G102(京哈线)","roadManager":"李哈哈","roadInspector":"李嘻嘻","roadMaintenance":"李嘿嘿","buildTime":"2019-03-02 00:00:00","latitude":"41.26664930591855","longitude":"121.52024387388683","delFlag":"0","remarks":null,"text1":null,"text2":null},{"searchValue":null,"createBy":"admin","createTime":"2019-12-13 14:19:29","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":"f8a7a0c3f6a0416baeff488e24aad9d4","roadName":"G2512(阜锦高速)","roadManager":"张呵呵","roadInspector":"张嘿嘿","roadMaintenance":"张嘻嘻","buildTime":"2019-05-01 00:00:00","latitude":"41.269631598509214","longitude":"121.30094953279101","delFlag":"0","remarks":null,"text1":null,"text2":null}]
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
         * createTime : 2019-12-13 15:07:37
         * updateBy : null
         * updateTime : null
         * remark : null
         * params : {}
         * id : 017ddd622487466c98d435012aac06a4
         * roadName : 二富线
         * roadManager : 炎
         * roadInspector : 之
         * roadMaintenance : 炎
         * buildTime : 2019-05-19 00:00:00
         * latitude : 41.9117144667564
         * longitude : 121.96203052369398
         * delFlag : 0
         * remarks : null
         * text1 : null
         * text2 : null
         */

        private Object searchValue;
        private String createBy;
        private String createTime;
        private Object updateBy;
        private Object updateTime;
        private Object remark;
        private ParamsBean params;
        private String id;
        private String roadName;
        private String roadManager;
        private String roadInspector;
        private String roadMaintenance;
        private String buildTime;
        private String latitude;
        private String longitude;
        private String delFlag;
        private Object remarks;
        private Object text1;
        private Object text2;

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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRoadName() {
            return roadName;
        }

        public void setRoadName(String roadName) {
            this.roadName = roadName;
        }

        public String getRoadManager() {
            return roadManager;
        }

        public void setRoadManager(String roadManager) {
            this.roadManager = roadManager;
        }

        public String getRoadInspector() {
            return roadInspector;
        }

        public void setRoadInspector(String roadInspector) {
            this.roadInspector = roadInspector;
        }

        public String getRoadMaintenance() {
            return roadMaintenance;
        }

        public void setRoadMaintenance(String roadMaintenance) {
            this.roadMaintenance = roadMaintenance;
        }

        public String getBuildTime() {
            return buildTime;
        }

        public void setBuildTime(String buildTime) {
            this.buildTime = buildTime;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getDelFlag() {
            return delFlag;
        }

        public void setDelFlag(String delFlag) {
            this.delFlag = delFlag;
        }

        public Object getRemarks() {
            return remarks;
        }

        public void setRemarks(Object remarks) {
            this.remarks = remarks;
        }

        public Object getText1() {
            return text1;
        }

        public void setText1(Object text1) {
            this.text1 = text1;
        }

        public Object getText2() {
            return text2;
        }

        public void setText2(Object text2) {
            this.text2 = text2;
        }

        public static class ParamsBean {
        }
    }
}
