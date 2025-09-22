package com.jiocoders.java.jiofamily.service.api;

import com.jiocoders.java.jiofamily.base.ResponseBase;
import com.jiocoders.java.jiofamily.networkmodel.request.RequestDeptAdd;
import com.jiocoders.java.jiofamily.networkmodel.request.RequestDeptUpdate;
import com.jiocoders.java.jiofamily.networkmodel.response.ResponseCommonDetail;
import com.jiocoders.java.jiofamily.networkmodel.response.ResponseDeptList;
import org.springframework.http.ResponseEntity;

public interface DepartmentService {

    ResponseEntity<ResponseDeptList> getDeptList(String token);

    ResponseEntity<ResponseBase> addDept(RequestDeptAdd request, String token);

    ResponseEntity<ResponseBase> updateDept(RequestDeptUpdate request, String token);

    ResponseEntity<ResponseCommonDetail> detailDept(int id, String token);

    ResponseEntity<ResponseBase> removeDept(String optionId, String token);
}
