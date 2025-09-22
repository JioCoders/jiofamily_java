package com.jiocoders.java.jiofamily.networkmodel.response;

import com.jiocoders.java.jiofamily.base.ResponseBase;
import com.jiocoders.java.jiofamily.networkmodel.response.data.CommonData;

public class ResponseCommonDetail extends ResponseBase {

    CommonData data = new CommonData();

    public CommonData getData() {
        return data;
    }

    public void setData(CommonData data) {
        this.data = data;
    }
}
