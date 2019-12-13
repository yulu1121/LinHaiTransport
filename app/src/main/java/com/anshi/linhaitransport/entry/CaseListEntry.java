package com.anshi.linhaitransport.entry;

import java.util.List;

public class CaseListEntry {

    /**
     * msg : 查询成功
     * code : 0
     * data : [{"area_name":"翠岩镇","type_name":"路面损坏","case_title":"追日路坏了","description":"追日路红绿灯坏","files":"/profile/lhjtapp/1575615669209.jpg","id":7,"create_date":"2019-12-06 15:02:04"},{"area_name":"凌海","type_name":"路面损坏","case_title":"追日路坏了","description":"追日路部分路面损坏","files":"/profile/lhjtapp/1575615611995.jpg","id":6,"create_date":"2019-12-06 15:01:10"},{"area_name":"凌海","type_name":"路面损坏","case_title":"路坏了","description":"有一段路坏了","files":"/profile/lhjtapp/1575614139197.jpg,/profile/lhjtapp/1575614150685.jpg","id":5,"create_date":"2019-12-06 14:37:28"}]
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
         * area_name : 翠岩镇
         * type_name : 路面损坏
         * case_title : 追日路坏了
         * description : 追日路红绿灯坏
         * files : /profile/lhjtapp/1575615669209.jpg
         * id : 7
         * create_date : 2019-12-06 15:02:04
         */

        private String area_name;
        private String type_name;
        private String case_title;
        private String description;
        private String files;
        private int id;
        private String create_date;
        private String address;
        private String case_state;
        private String dispose_type;
        private int case_id;
        private String dispose_state;
        private String patrol_type;
        public String getArea_name() {
            return area_name;
        }

        public void setArea_name(String area_name) {
            this.area_name = area_name;
        }

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }

        public String getCase_title() {
            return case_title;
        }

        public void setCase_title(String case_title) {
            this.case_title = case_title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getFiles() {
            return files;
        }

        public void setFiles(String files) {
            this.files = files;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCreate_date() {
            return create_date;
        }

        public void setCreate_date(String create_date) {
            this.create_date = create_date;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCase_state() {
            return case_state;
        }

        public void setCase_state(String case_state) {
            this.case_state = case_state;
        }

        public String getDispose_type() {
            return dispose_type;
        }

        public void setDispose_type(String dispose_type) {
            this.dispose_type = dispose_type;
        }

        public int getCase_id() {
            return case_id;
        }

        public void setCase_id(int case_id) {
            this.case_id = case_id;
        }

        public String getDispose_state() {
            return dispose_state;
        }

        public void setDispose_state(String dispose_state) {
            this.dispose_state = dispose_state;
        }


        public String getPatrol_type() {
            return patrol_type;
        }

        public void setPatrol_type(String patrol_type) {
            this.patrol_type = patrol_type;
        }
    }
}
