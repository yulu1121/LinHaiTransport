package com.anshi.linhaitransport.entry;

import java.util.List;

public class LoginEntry {

    /**
     * msg : 操作成功
     * code : 0
     * data : {"searchValue":null,"createBy":null,"createTime":"2019-12-04 14:41:24","updateBy":null,"updateTime":null,"remark":null,"params":{},"userId":4,"deptId":109,"parentId":null,"roleId":null,"loginName":"zhangsan","userName":"张三","email":"abc@qq.com","phonenumber":"13345678987","sex":"0","avatar":"","password":"9281d47be958bfd1f7e02bb87cc6e61e","salt":"8cdcc6","status":"0","delFlag":"0","loginIp":"192.168.0.46","loginDate":"2019-12-04 17:14:55","dept":{"searchValue":null,"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"deptId":109,"parentId":108,"ancestors":null,"deptName":"村域路长","orderNum":"1","leader":"","phone":null,"email":null,"status":"0","delFlag":null,"parentName":null},"roles":[],"roleIds":null,"postIds":null,"areaId":1,"admin":false}
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
         * createBy : null
         * createTime : 2019-12-04 14:41:24
         * updateBy : null
         * updateTime : null
         * remark : null
         * params : {}
         * userId : 4
         * deptId : 109
         * parentId : null
         * roleId : null
         * loginName : zhangsan
         * userName : 张三
         * email : abc@qq.com
         * phonenumber : 13345678987
         * sex : 0
         * avatar :
         * password : 9281d47be958bfd1f7e02bb87cc6e61e
         * salt : 8cdcc6
         * status : 0
         * delFlag : 0
         * loginIp : 192.168.0.46
         * loginDate : 2019-12-04 17:14:55
         * dept : {"searchValue":null,"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"deptId":109,"parentId":108,"ancestors":null,"deptName":"村域路长","orderNum":"1","leader":"","phone":null,"email":null,"status":"0","delFlag":null,"parentName":null}
         * roles : []
         * roleIds : null
         * postIds : null
         * areaId : 1
         * admin : false
         */

        private Object searchValue;
        private Object createBy;
        private String createTime;
        private Object updateBy;
        private Object updateTime;
        private Object remark;
        private ParamsBean params;
        private int userId;
        private int deptId;
        private Object parentId;
        private Object roleId;
        private String loginName;
        private String userName;
        private String email;
        private String phonenumber;
        private String sex;
        private String avatar;
        private String password;
        private String salt;
        private String status;
        private String delFlag;
        private String loginIp;
        private String loginDate;
        private DeptBean dept;
        private Object roleIds;
        private Object postIds;
        private int areaId;
        private boolean admin;
        private List<?> roles;

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

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getDeptId() {
            return deptId;
        }

        public void setDeptId(int deptId) {
            this.deptId = deptId;
        }

        public Object getParentId() {
            return parentId;
        }

        public void setParentId(Object parentId) {
            this.parentId = parentId;
        }

        public Object getRoleId() {
            return roleId;
        }

        public void setRoleId(Object roleId) {
            this.roleId = roleId;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhonenumber() {
            return phonenumber;
        }

        public void setPhonenumber(String phonenumber) {
            this.phonenumber = phonenumber;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getSalt() {
            return salt;
        }

        public void setSalt(String salt) {
            this.salt = salt;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDelFlag() {
            return delFlag;
        }

        public void setDelFlag(String delFlag) {
            this.delFlag = delFlag;
        }

        public String getLoginIp() {
            return loginIp;
        }

        public void setLoginIp(String loginIp) {
            this.loginIp = loginIp;
        }

        public String getLoginDate() {
            return loginDate;
        }

        public void setLoginDate(String loginDate) {
            this.loginDate = loginDate;
        }

        public DeptBean getDept() {
            return dept;
        }

        public void setDept(DeptBean dept) {
            this.dept = dept;
        }

        public Object getRoleIds() {
            return roleIds;
        }

        public void setRoleIds(Object roleIds) {
            this.roleIds = roleIds;
        }

        public Object getPostIds() {
            return postIds;
        }

        public void setPostIds(Object postIds) {
            this.postIds = postIds;
        }

        public int getAreaId() {
            return areaId;
        }

        public void setAreaId(int areaId) {
            this.areaId = areaId;
        }

        public boolean isAdmin() {
            return admin;
        }

        public void setAdmin(boolean admin) {
            this.admin = admin;
        }

        public List<?> getRoles() {
            return roles;
        }

        public void setRoles(List<?> roles) {
            this.roles = roles;
        }

        public static class ParamsBean {
        }

        public static class DeptBean {
            /**
             * searchValue : null
             * createBy : null
             * createTime : null
             * updateBy : null
             * updateTime : null
             * remark : null
             * params : {}
             * deptId : 109
             * parentId : 108
             * ancestors : null
             * deptName : 村域路长
             * orderNum : 1
             * leader :
             * phone : null
             * email : null
             * status : 0
             * delFlag : null
             * parentName : null
             */

            private Object searchValue;
            private Object createBy;
            private Object createTime;
            private Object updateBy;
            private Object updateTime;
            private Object remark;
            private ParamsBeanX params;
            private int deptId;
            private int parentId;
            private Object ancestors;
            private String deptName;
            private String orderNum;
            private String leader;
            private Object phone;
            private Object email;
            private String status;
            private Object delFlag;
            private Object parentName;

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

            public ParamsBeanX getParams() {
                return params;
            }

            public void setParams(ParamsBeanX params) {
                this.params = params;
            }

            public int getDeptId() {
                return deptId;
            }

            public void setDeptId(int deptId) {
                this.deptId = deptId;
            }

            public int getParentId() {
                return parentId;
            }

            public void setParentId(int parentId) {
                this.parentId = parentId;
            }

            public Object getAncestors() {
                return ancestors;
            }

            public void setAncestors(Object ancestors) {
                this.ancestors = ancestors;
            }

            public String getDeptName() {
                return deptName;
            }

            public void setDeptName(String deptName) {
                this.deptName = deptName;
            }

            public String getOrderNum() {
                return orderNum;
            }

            public void setOrderNum(String orderNum) {
                this.orderNum = orderNum;
            }

            public String getLeader() {
                return leader;
            }

            public void setLeader(String leader) {
                this.leader = leader;
            }

            public Object getPhone() {
                return phone;
            }

            public void setPhone(Object phone) {
                this.phone = phone;
            }

            public Object getEmail() {
                return email;
            }

            public void setEmail(Object email) {
                this.email = email;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public Object getDelFlag() {
                return delFlag;
            }

            public void setDelFlag(Object delFlag) {
                this.delFlag = delFlag;
            }

            public Object getParentName() {
                return parentName;
            }

            public void setParentName(Object parentName) {
                this.parentName = parentName;
            }

            public static class ParamsBeanX {
            }
        }
    }
}
