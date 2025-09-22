package com.jiocoders.java.jiofamily.controller;

import com.jiocoders.java.jiofamily.base.ResponseBase;
import com.jiocoders.java.jiofamily.networkmodel.request.RequestDeptAdd;
import com.jiocoders.java.jiofamily.networkmodel.request.RequestDeptUpdate;
import com.jiocoders.java.jiofamily.networkmodel.response.ResponseCommonDetail;
import com.jiocoders.java.jiofamily.networkmodel.response.ResponseDeptList;
import com.jiocoders.java.jiofamily.service.api.DepartmentService;
import com.jiocoders.java.jiofamily.utils.ApiConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

import static com.jiocoders.java.jiofamily.utils.ApiConstant.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/dept")
@CrossOrigin(origins = "*")
public class DepartmentController {
    private static Logger logger = LoggerFactory.getLogger(DepartmentController.class);

    @Resource(name = DEPARTMENT_SERVICE)
    private DepartmentService departmentService;

    /**
     * <p>
     * Get all option data in the system.For production system want to use
     * pagination.
     * </p>
     * removed this feature
     *
     * @param token
     * @return ResponseOptionList List<CustomerData>
     */
    @PostMapping(ApiConstant.DEPARTMENT_LIST)
    public ResponseEntity<ResponseDeptList> getDeptList(@RequestHeader(AUTHORIZATION) String token) {
        logger.info("check--------------------------data from -------db");
        return departmentService.getDeptList(token);
    }

    /**
     * Post request to create department information in the system.
     *
     * @param departmentRequest
     * @return
     */
    @PostMapping(value = ApiConstant.DEPARTMENT_ADD, consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
    public ResponseEntity<ResponseBase> addDept(@RequestBody RequestDeptAdd departmentRequest,
            @RequestHeader(AUTHORIZATION) String token) {
        return departmentService.addDept(departmentRequest, token);
    }

    /**
     * Post request to update department information in the system.
     *
     * @param request
     * @return
     */
    @PostMapping(DEPARTMENT_UPDATE)
    public ResponseEntity<ResponseBase> updateDept(@RequestBody RequestDeptUpdate request,
            @RequestHeader(AUTHORIZATION) String token) {
        return departmentService.updateDept(request, token);
    }

    /**
     * Post request to get department details information in the system.
     *
     * @param request
     * @return
     */
    @PostMapping(DETAILS)
    public ResponseEntity<ResponseCommonDetail> detailDept(@RequestBody int id,
            @RequestHeader(AUTHORIZATION) String token) {
        return departmentService.detailDept(id, token);
    }
    // /**
    // * <p>Deactivate(remove) a department from the system based on the ID. The
    // method mapping is like the getDepartment with difference of
    // *
    // * @param departmentId
    // * @param token
    // * @return
    // * @PostMapping and @GetMapping</p>
    // */
    // @PostMapping(ApiConstant.DEPARTMENT_REMOVE)
    // public ResponseEntity<ResponseBase> removeDept(@RequestBody String optionId,
    // @RequestHeader(AUTHORIZATION) String token) {
    // return departmentService.removeDept(optionId, token);
    // }

}
