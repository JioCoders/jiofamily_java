package com.jiocoders.java.jiofamily.networkmodel.response;

import com.jiocoders.java.jiofamily.base.ResponseBase;
import com.jiocoders.java.jiofamily.networkmodel.response.data.CommonData;

import java.util.ArrayList;
import java.util.List;

public class ResponseDeptList extends ResponseBase {

    List<CommonData> departmentList = new ArrayList<>();

    public List<CommonData> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<CommonData> departmentList) {
        this.departmentList = departmentList;
    }
}
