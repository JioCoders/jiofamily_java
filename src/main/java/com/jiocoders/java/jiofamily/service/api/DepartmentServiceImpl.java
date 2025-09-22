package com.jiocoders.java.jiofamily.service.api;

import com.jiocoders.java.jiofamily.base.ResponseBase;
import com.jiocoders.java.jiofamily.entity.Department;
import com.jiocoders.java.jiofamily.entity.User;
import com.jiocoders.java.jiofamily.networkmodel.request.RequestDeptAdd;
import com.jiocoders.java.jiofamily.networkmodel.request.RequestDeptUpdate;
import com.jiocoders.java.jiofamily.networkmodel.response.data.CommonData;
import com.jiocoders.java.jiofamily.networkmodel.response.ResponseCommonDetail;
import com.jiocoders.java.jiofamily.networkmodel.response.ResponseDeptList;
import com.jiocoders.java.jiofamily.repository.AdminRepository;
import com.jiocoders.java.jiofamily.repository.DepartmentRepository;
import com.jiocoders.java.jiofamily.security.AuthScope;
import com.jiocoders.java.jiofamily.security.JwtTokenProvider;
import com.jiocoders.java.jiofamily.utils.ApiConstant;
import com.jiocoders.java.jiofamily.utils.StrConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.jiocoders.java.jiofamily.utils.ApiConstant.*;
import static com.jiocoders.java.jiofamily.utils.Common.checkNotNull;
import static com.jiocoders.java.jiofamily.utils.StrConstant.UN_AUTHORISE_ACCESS;

@Service(DEPARTMENT_SERVICE)
public class DepartmentServiceImpl implements DepartmentService {
    private static final Logger logger = LoggerFactory.getLogger(DepartmentService.class);

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    DepartmentRepository deptRepository;
    @Autowired
    private AdminRepository adminRepository;

    /**
     * Method to return the list of all the departments in the system.This is an
     * implementation but use pagination in the real world.
     *
     * @param token
     * @return list of Departments
     */
    @Override
    public ResponseEntity<ResponseDeptList> getDeptList(String token) {
        ResponseDeptList response = new ResponseDeptList();
        if (!checkNotNull(token)) {
            response.setMessage(StrConstant.TOKEN_NOT_FOUND);
            response.setStatus(INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        // CHECK ADMIN USER
        String bearer = jwtTokenProvider.resolveToken(token);
        if (!jwtTokenProvider.validateToken(bearer, AuthScope.USER)
                && !jwtTokenProvider.validateToken(bearer, AuthScope.ADMIN)) {
            response.setMessage(StrConstant.UN_AUTHORISE_ACCESS);
            response.setStatus(INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        // CHECK VALID USER ID
        int loginUserId = Integer.parseInt(jwtTokenProvider.getUsername(bearer));
        logger.info("UserId={}", loginUserId);
        User user = adminRepository.findById(loginUserId).get();
        if (user == null) {
            response.setMessage("User not found!");
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        if (!user.isActive()) {
            response.setMessage("User is not active to update!");
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        List<Department> departments = deptRepository.findAll();
        List<CommonData> dataList = new ArrayList<>();
        for (Department dept : departments) {
            CommonData d = new CommonData();
            d.setId(dept.getDepartmentId());
            d.setName(dept.getDepartmentName());
            dataList.add(d);
        }
        response.setDepartmentList(dataList);
        response.setMessage(StrConstant.SUCCESS);
        response.setStatus(ApiConstant.SUCCESS_CODE);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Create a department based on the data sent to the service class.
     *
     * @param deptRequest
     * @param token
     * @return DTO representation of the department
     */
    @Override
    public ResponseEntity<ResponseBase> addDept(RequestDeptAdd deptRequest, String token) {
        ResponseBase responseBase = new ResponseBase();
        if (!checkNotNull(token)) {
            responseBase.setMessage(StrConstant.TOKEN_NOT_FOUND);
            responseBase.setStatus(INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.OK);
        }

        // CHECK ADMIN USER
        String bearer = jwtTokenProvider.resolveToken(token);
        if (jwtTokenProvider.validateToken(bearer, AuthScope.USER)
                || !jwtTokenProvider.validateToken(bearer, AuthScope.ADMIN)) {
            responseBase.setMessage(StrConstant.UN_AUTHORISE_ACCESS);
            responseBase.setStatus(INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.OK);
        }

        // CHECK VALIDATION
        int loginUserId = Integer.parseInt(jwtTokenProvider.getUsername(bearer));
        User loginUser = adminRepository.findById(loginUserId).get();
        if (loginUser.getCompanyId() == 0) {
            responseBase.setMessage(UN_AUTHORISE_ACCESS);
            responseBase.setStatus(INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.OK);
        }

        if (!checkNotNull(deptRequest.getDepartmentName()) || deptRequest.getDepartmentName().length() < 2) {
            responseBase.setMessage(StrConstant.PLEASE_PROVIDE_MANDATORY_FIELD + ": Department name");
            responseBase.setStatus(INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.OK);
        }
        List<Department> repoResult = deptRepository.findAll();
        boolean isExist = false;
        for (Department o : repoResult) {
            if (o.getDepartmentName().equalsIgnoreCase(deptRequest.getDepartmentName())) {
                isExist = true;
                break;
            }
        }
        if (isExist) {
            responseBase.setMessage(deptRequest.getDepartmentName() + " - department is already exists!");
            responseBase.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.OK);
        }
        Department department = new Department();
        department.setDepartmentName(deptRequest.getDepartmentName());
        department.setCompanyId(loginUser.getCompanyId());
        department.setActive(true);
        deptRepository.save(department);

        responseBase.setMessage(StrConstant.INSERT_SUCCESS);
        responseBase.setStatus(SUCCESS_CODE);

        return new ResponseEntity<>(responseBase, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseBase> updateDept(RequestDeptUpdate request, String token) {
        ResponseBase responseBase = new ResponseBase();
        if (!checkNotNull(token)) {
            responseBase.setMessage(StrConstant.TOKEN_NOT_FOUND);
            responseBase.setStatus(INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.OK);
        }

        // CHECK ADMIN USER
        String bearer = jwtTokenProvider.resolveToken(token);
        if (jwtTokenProvider.validateToken(bearer, AuthScope.USER)
                || !jwtTokenProvider.validateToken(bearer, AuthScope.ADMIN)) {
            responseBase.setMessage(StrConstant.UN_AUTHORISE_ACCESS);
            responseBase.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.OK);
        }

        // CHECK VALIDATION
        // int userId = Integer.parseInt(jwtTokenProvider.getUsername(bearer));
        if (!checkNotNull(request.getDepartmentName())) {
            responseBase.setMessage(StrConstant.ITEM_NOT_FOUND_UPDATE + " : department name");
            responseBase.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.OK);
        }
        Department repoDept = deptRepository.findById(request.getDepartmentId()).get();
        if (repoDept == null) {
            responseBase.setMessage(StrConstant.ITEM_NOT_FOUND_UPDATE);
            responseBase.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.OK);
        }
        if (repoDept.getDepartmentName().equals(request.getDepartmentName())) {
            responseBase.setMessage(StrConstant.DUPLICATE_ENTRY);
            responseBase.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.OK);
        }
        repoDept.setDepartmentName(request.getDepartmentName());

        deptRepository.saveAndFlush(repoDept);

        responseBase.setMessage(StrConstant.INSERT_UPDATE);
        responseBase.setStatus(ApiConstant.SUCCESS_CODE);
        return new ResponseEntity<>(responseBase, HttpStatus.OK);
    }

    /**
     * Get detail of department based on the department ID.
     * based on the entity (passing JPA entity class as method parameter)
     *
     * @param id
     * @param token
     * @return boolean flag showing the request status
     */
    @Override
    public ResponseEntity<ResponseCommonDetail> detailDept(int id, String token) {
        ResponseCommonDetail response = new ResponseCommonDetail();
        if (!checkNotNull(token)) {
            response.setMessage(StrConstant.TOKEN_NOT_FOUND);
            response.setStatus(INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        // CHECK ADMIN USER
        String bearer = jwtTokenProvider.resolveToken(token);
        if (!jwtTokenProvider.validateToken(bearer, AuthScope.USER)
                && !jwtTokenProvider.validateToken(bearer, AuthScope.ADMIN)) {
            response.setMessage(StrConstant.UN_AUTHORISE_ACCESS);
            response.setStatus(INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        // CHECK VALID USER ID
        int loginUserId = Integer.parseInt(jwtTokenProvider.getUsername(bearer));
        logger.info("UserId={}", loginUserId);
        User user = adminRepository.findById(loginUserId).get();
        if (user == null) {
            response.setMessage("User not found!");
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        if (!user.isActive()) {
            response.setMessage("User is not active to update!");
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Department dept = deptRepository.findByDepartmentId(id);
        if (dept == null) {
            response.setMessage("Department details are not found");
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        CommonData c = new CommonData();
        c.setId(dept.getDepartmentId());
        c.setName(dept.getDepartmentName());
        response.setData(c);
        response.setMessage(StrConstant.SUCCESS);
        response.setStatus(ApiConstant.SUCCESS_CODE);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Delete department based on the option ID.We can also use other option to
     * delete department
     * based on the entity (passing JPA entity class as method parameter)
     *
     * @param deptId
     * @param token
     * @return boolean flag showing the request status
     */
    @Override
    public ResponseEntity<ResponseBase> removeDept(String deptId, String token) {
        ResponseBase responseBase = new ResponseBase();
        if (!checkNotNull(token)) {
            responseBase.setMessage(StrConstant.TOKEN_NOT_FOUND);
            responseBase.setStatus(INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.OK);
        }

        // CHECK ADMIN USER
        String bearer = jwtTokenProvider.resolveToken(token);
        if (jwtTokenProvider.validateToken(bearer, AuthScope.USER)
                || !jwtTokenProvider.validateToken(bearer, AuthScope.ADMIN)) {
            responseBase.setMessage(StrConstant.UN_AUTHORISE_ACCESS);
            responseBase.setStatus(INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.OK);
        }

        // CHECK VALIDATION
        // int userId = Integer.parseInt(jwtTokenProvider.getUsername(bearer));
        if (!checkNotNull(deptId) || deptId.length() < 10) {
            responseBase.setMessage(StrConstant.PLEASE_PROVIDE_MANDATORY_FIELD + ": Department id");
            responseBase.setStatus(INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.OK);
        }

        deptRepository.removeDepartment(deptId);
        responseBase.setMessage(StrConstant.INSERT_SUCCESS);
        responseBase.setStatus(SUCCESS_CODE);

        return new ResponseEntity<>(responseBase, HttpStatus.OK);
    }
}
