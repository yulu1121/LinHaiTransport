package com.anshi.linhaitransport.entry;

import java.util.List;

public class DeptEntry {

    /**
     * msg : 查询成功
     * code : 0
     * data : [{"deptName":"县级养护路长","deptId":111},{"deptName":"巡查路长","deptId":112},{"deptName":"警务路长","deptId":113}]
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
         * deptName : 县级养护路长
         * deptId : 111
         */

        private String deptName;
        private int deptId;

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public int getDeptId() {
            return deptId;
        }

        public void setDeptId(int deptId) {
            this.deptId = deptId;
        }
    }
}
