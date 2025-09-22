package com.jiocoders.java.jiofamily.service.api;

import com.jiocoders.java.jiofamily.base.ResponseBase;
import com.jiocoders.java.jiofamily.controller.AdminController;
import com.jiocoders.java.jiofamily.entity.*;
import com.jiocoders.java.jiofamily.networkmodel.request.*;
import com.jiocoders.java.jiofamily.networkmodel.response.*;
import com.jiocoders.java.jiofamily.networkmodel.response.data.*;
import com.jiocoders.java.jiofamily.repository.*;
import com.jiocoders.java.jiofamily.security.AuthScope;
import com.jiocoders.java.jiofamily.security.JwtTokenProvider;
import com.jiocoders.java.jiofamily.utils.ApiConstant;
import com.jiocoders.java.jiofamily.utils.Common;
import com.jiocoders.java.jiofamily.utils.StrConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jiocoders.java.jiofamily.utils.ApiConstant.*;
import static com.jiocoders.java.jiofamily.utils.Common.checkNotNull;
import static com.jiocoders.java.jiofamily.utils.Common.regexEmail;
import static com.jiocoders.java.jiofamily.utils.StrConstant.INVALID_EMAIL;
import static com.jiocoders.java.jiofamily.utils.StrConstant.UN_AUTHORISE_ACCESS;

@Service(ADMIN_SERVICE)
public class AdminServiceImpl implements AdminService {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    LocationRepository locationRepository;

    // Auto-wiring the CacheManager within your service
    // @Autowired
    // private CacheManager cacheManager;

    // Method responsible for refreshing the cache
    // @Override
    // public void refreshCache(String cacheName) {
    //     Cache cache = cacheManager.getCache(cacheName);
    //     if (cache != null) {
    //         cache.clear(); // Clears all entries from the cache, effectively refreshing it
    //     }
    // }

    @Override
    public ResponseEntity<ResponseBase> addUser(RequestAddUser request, String token) {
        ResponseBase responseBase = new ResponseBase();
        if (!checkNotNull(token)) {
            responseBase.setMessage(StrConstant.TOKEN_NOT_FOUND);
            responseBase.setStatus(INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.UNAUTHORIZED);
        }

        // CHECK ADMIN USER
        String bearer = jwtTokenProvider.resolveToken(token);
        if (jwtTokenProvider.validateToken(bearer, AuthScope.USER)
                || !jwtTokenProvider.validateToken(bearer, AuthScope.ADMIN)) {
            responseBase.setMessage(UN_AUTHORISE_ACCESS);
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
        String errorString = null;
        if (!checkNotNull(request.getFirstName())) {
            errorString = "First Name";
        }
        if (!checkNotNull(request.getPassword()) || request.getPassword().length() < 6) {
            if (errorString == null) {
                errorString = "Password";
            } else {
                errorString = errorString + ",Password";
            }
        }
        if (!checkNotNull(request.getEmail())) {
            if (errorString == null) {
                errorString = "Email";
            } else {
                errorString = errorString + ",Email";
            }
        }

        Pattern pattern = Pattern.compile(regexEmail);
        Matcher matcher = pattern.matcher(request.getEmail());
        if (!matcher.matches()) {
            if (errorString == null) {
                errorString = "Email";
            } else {
                errorString = errorString + ",Invalid Email";
            }
        }
        if (!checkNotNull(request.getPassword())) {
            if (errorString == null) {
                errorString = "Password";
            } else {
                errorString = errorString + ",Password";
            }
        }
        if (!checkNotNull(request.getEmployeeId())) {
            if (errorString == null) {
                errorString = "Employee id";
            } else {
                errorString = errorString + ",Employee id";
            }
        }
        if (request.getDepartmentId() == 0) {
            if (errorString == null) {
                errorString = "Department Id";
            } else {
                errorString = errorString + ",Department Id";
            }
        }
        if (request.getUserLocationId() == 0) {
            if (errorString == null) {
                errorString = "Location Id";
            } else {
                errorString = errorString + ",Location Id";
            }
        }
        if (errorString != null) {
            responseBase.setMessage(StrConstant.PLEASE_PROVIDE_MANDATORY_FIELD + ": " + errorString);
            responseBase.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.OK);
        }

        User user = adminRepository.findByEmailId(request.getEmail());
        if (user == null) {
            user = new User();
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setMobileNo(request.getMobile());
            user.setEmailId(request.getEmail());
            user.setPassword(request.getPassword());
            user.setBio("");
            user.setDob(request.getDob());
            user.setGender(request.getGender());
            user.setCompanyId(loginUser.getCompanyId());
            user.setCompanyId(1);
            user.setUserLocation(request.getUserLocationId());
            user.setIsAdmin(false);
            user.setIsSuperAdmin(false);
            user.setActive(true);
            user.setCreatedAt(System.currentTimeMillis());

            String otpString = String.valueOf(Common.generateOTP());
            user.setOtp(otpString);

            adminRepository.save(user);

            responseBase.setMessage(StrConstant.INSERT_SUCCESS);
            responseBase.setStatus(SUCCESS_CODE);
        } else {
            responseBase.setMessage(StrConstant.EMAIL_ALREADY_EXISTS);
            responseBase.setStatus(ApiConstant.INVALID_REQUEST_CODE);
        }
        return new ResponseEntity<>(responseBase, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseBase> updateUser(RequestUpdateUser request, String token) {
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
            responseBase.setMessage(UN_AUTHORISE_ACCESS);
            responseBase.setStatus(INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.OK);
        }

        int loginUserId = Integer.parseInt(jwtTokenProvider.getUsername(bearer));
        logger.info("loginUserId=>" + loginUserId);
        User user = adminRepository.findById(request.getUserId()).get();
        if (user == null || request == null) {
            responseBase.setMessage("User not found!");
            responseBase.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.OK);
        }
        if (!user.isActive()) {
            responseBase.setMessage("User is not active to update!");
            responseBase.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.OK);
        }

        Pattern pattern = Pattern.compile(regexEmail);
        Matcher matcher = pattern.matcher(request.getEmailId());
        if (!matcher.matches()) {
            responseBase.setMessage(INVALID_EMAIL);
            responseBase.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.OK);
        }

        User repoUser = adminRepository.findByEmailId(request.getEmailId());
        if (repoUser == null) {
            responseBase.setMessage("Email id not found");
            responseBase.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.OK);
        }

        String validationResult = checkEmpUpdateValidation(request);
        if (validationResult != null) {
            responseBase.setMessage("User is not active to update!");
            responseBase.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.OK);
        }
        setEmployeeUpdate(repoUser, request);
        adminRepository.saveAndFlush(repoUser);
        responseBase.setMessage("Updated Successfully");
        responseBase.setStatus(ApiConstant.INVALID_REQUEST_CODE);
        return new ResponseEntity<>(responseBase, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseUserList> getUserList(String token) {
        ResponseUserList response = new ResponseUserList();
        if (!checkNotNull(token)) {
            response.setMessage(StrConstant.TOKEN_NOT_FOUND);
            response.setStatus(INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        // CHECK ADMIN USER
        String bearer = jwtTokenProvider.resolveToken(token);
        if (!jwtTokenProvider.validateToken(bearer, AuthScope.USER)
                && !jwtTokenProvider.validateToken(bearer, AuthScope.ADMIN)) {
            response.setMessage(UN_AUTHORISE_ACCESS);
            response.setStatus(INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        // CHECK VALID USER ID
        int userId = Integer.parseInt(jwtTokenProvider.getUsername(bearer));
        logger.info("UserId=>" + userId);
        User user = adminRepository.findById(userId).get();
        if (user == null) {
            response.setMessage("User not found!");
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        if (!user.isActive()) {
            response.setMessage("User is not active to see the user list record!");
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        logger.info("+++++++++++++Executing query to get users fetch list+++++++++++++");

        List<User> repoList = adminRepository.findAllByIsActive(true);
        List<UserData> userDataList = new ArrayList<>();
        for (User u : repoList) {
            UserData data = new UserData();
            data.setUserId(u.getId());
            data.setFirstName(u.getFirstName());
            data.setLastName(u.getLastName());
            data.setGender(u.getGender());
            data.setEmailId(u.getEmailId());
            data.setMobileNo(u.getMobileNo());
            data.setBio(u.getBio());
            data.setDob(u.getDob());
            data.setActive(u.isActive());
            data.setCreatedAt(u.getCreatedAt());
            Department department = departmentRepository.findByDepartmentId(u.getDepartmentId());
            if (department != null) {
                CommonData d = new CommonData();
                d.setId(department.getDepartmentId());
                d.setName(department.getDepartmentName());
                data.setDepartment(d);
            } else {
                data.setDepartment(null);
            }
            Location location = locationRepository.findByLocationId(u.getUserLocation());
            if (location != null) {
                CommonData loc = new CommonData();
                loc.setId(location.getLocationId());
                loc.setName(location.getLocationName());
                data.setUserLocationData(loc);
            } else {
                data.setUserLocationData(new CommonData());
            }
            userDataList.add(data);
        }
        response.setUserList(userDataList);
        response.setMessage(StrConstant.SUCCESS);
        response.setStatus(ApiConstant.SUCCESS_CODE);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseBase> resetUserPassword(String token, RequestResetPass request) {
        ResponseBase response = new ResponseBase();
        if (!checkNotNull(token)) {
            response.setMessage(StrConstant.TOKEN_NOT_FOUND);
            response.setStatus(INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        // CHECK ADMIN USER
        String bearer = jwtTokenProvider.resolveToken(token);
        if (jwtTokenProvider.validateToken(bearer, AuthScope.USER)
                || !jwtTokenProvider.validateToken(bearer, AuthScope.ADMIN)) {
            response.setMessage(UN_AUTHORISE_ACCESS);
            response.setStatus(INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        // CHECK VALID USER ID
        int userId = Integer.parseInt(jwtTokenProvider.getUsername(bearer));
        logger.info("UserId=>" + userId);
        User user = adminRepository.findById(userId).get();
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

        if (request.getUserId() == 0) {
            response.setMessage("Invalid user!");
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        User emp = adminRepository.findById(request.getUserId()).get();
        if (emp == null) {
            response.setMessage("Employee not found");
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        emp.setPassword(request.getPassword());
        adminRepository.saveAndFlush(emp);
        response.setMessage("Password changed successfully!");
        response.setStatus(ApiConstant.SUCCESS_CODE);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseBase> validateUser(String token, RequestValidateUser request) {
        ResponseBase response = new ResponseBase();
        if (!checkNotNull(token)) {
            response.setMessage(StrConstant.TOKEN_NOT_FOUND);
            response.setStatus(INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        // CHECK ADMIN USER
        String bearer = jwtTokenProvider.resolveToken(token);
        if (jwtTokenProvider.validateToken(bearer, AuthScope.USER)
                || !jwtTokenProvider.validateToken(bearer, AuthScope.ADMIN)) {
            response.setMessage(UN_AUTHORISE_ACCESS);
            response.setStatus(INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        // CHECK VALID USER ID
        int userId = Integer.parseInt(jwtTokenProvider.getUsername(bearer));
        logger.info("UserId=>" + userId);
        User user = adminRepository.findById(userId).get();
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

        if (request.getUserId() == 0) {
            response.setMessage("Invalid user!");
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        User emp = adminRepository.findById(request.getUserId()).get();
        if (emp == null) {
            response.setMessage("Employee not found");
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        if (emp.getOtp().equals(request.getOtp())) {
            emp.setActive(true);
            adminRepository.saveAndFlush(emp);
            response.setMessage("User validated successfully!");
            response.setStatus(ApiConstant.SUCCESS_CODE);
        } else {
            response.setMessage("Otp is not correct, please try again!");
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);

        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseBase> activeUserStatus(String token, RequestActivate request) {
        ResponseBase response = new ResponseBase();
        if (!checkNotNull(token)) {
            response.setMessage(StrConstant.TOKEN_NOT_FOUND);
            response.setStatus(INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        // CHECK ADMIN USER
        String bearer = jwtTokenProvider.resolveToken(token);
        if (jwtTokenProvider.validateToken(bearer, AuthScope.USER)
                || !jwtTokenProvider.validateToken(bearer, AuthScope.ADMIN)) {
            response.setMessage(UN_AUTHORISE_ACCESS);
            response.setStatus(INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        // CHECK VALID USER ID
        int userId = Integer.parseInt(jwtTokenProvider.getUsername(bearer));
        logger.info("UserId=>" + userId);
        User user = adminRepository.findById(userId).get();
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

        if (request.getUserId() == 0) {
            response.setMessage("Invalid user!");
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        User emp = adminRepository.findById(request.getUserId()).get();
        if (emp == null) {
            response.setMessage("Employee not found");
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        emp.setActive(request.isActive());
        adminRepository.saveAndFlush(emp);
        response.setMessage("User removed successfully!");
        response.setStatus(ApiConstant.SUCCESS_CODE);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseUserDetail> detailUser(int id, String token) {
        ResponseUserDetail response = new ResponseUserDetail();
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
        User loginUser = adminRepository.findById(loginUserId).get();
        if (loginUser == null) {
            response.setMessage("User Detail not found!");
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        if (!loginUser.isActive()) {
            response.setMessage("User Detail is not active to update!");
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        User u = adminRepository.findById(id).get();
        UserData data = new UserData();
        data.setUserId(u.getId());
        data.setFirstName(u.getFirstName());
        data.setLastName(u.getLastName());
        data.setGender(u.getGender());
        data.setEmailId(u.getEmailId());
        data.setMobileNo(u.getMobileNo());
        data.setBio(u.getBio());
        data.setDob(u.getDob());
        data.setActive(u.isActive());
        data.setCreatedAt(u.getCreatedAt());
        Department department = departmentRepository.findByDepartmentId(u.getDepartmentId());
        if (department != null) {
            CommonData d = new CommonData();
            d.setId(department.getDepartmentId());
            d.setName(department.getDepartmentName());
            data.setDepartment(d);
        } else {
            data.setDepartment(null);
        }
        Location location = locationRepository.findByLocationId(u.getUserLocation());
        if (location != null) {
            CommonData loc = new CommonData();
            loc.setId(location.getLocationId());
            loc.setName(location.getLocationName());
            data.setUserLocationData(loc);
        } else {
            data.setUserLocationData(new CommonData());
        }
        response.setUserDetail(data);
        response.setMessage(StrConstant.SUCCESS);
        response.setStatus(ApiConstant.SUCCESS_CODE);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    boolean setEmployeeUpdate(User user, RequestUpdateUser request) {
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setMobileNo(request.getMobileNo());
        user.setBio(request.getBio());
        user.setDob(request.getDob());
        user.setEmployeeId(request.getEmployeeId());
        user.setUserLocation(request.getUserLocationId());
        user.setDepartmentId(request.getDepartmentId());
        return true;
    }

    String checkEmpUpdateValidation(RequestUpdateUser request) {
        if (!checkNotNull(request.getFirstName())) {
            return "Name is required";
        }
        if (!checkNotNull(request.getEmailId())) {
            return "Email id is required";
        }
        if (!checkNotNull(request.getMobileNo())) {
            return "Mobile number is required";
        }
        if (!checkNotNull(request.getEmployeeId())) {
            return "Employee id is required";
        }
        if (request.getDepartmentId() == 0) {
            return "Department is required";
        }
        if (request.getUserLocationId() == 0) {
            return "Location is required";
        }
        return null;
    }

    // FileStorageService fileStorageService;
    // @GetMapping("/downloadFile/{fileName:.+}")
    // public ResponseEntity<Resource> downloadFile(@PathVariable String fileName,
    // HttpServletRequest request) {
    // // Load file as Resource
    // Resource resource = fileStorageService.loadFileAsResource(fileName);
    //
    // // Try to determine file's content type
    // String contentType = null;
    // try {
    // contentType =
    // request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
    // } catch (IOException ex) {
    // logger.info("Could not determine file type.");
    // }
    //
    // // Fallback to the default content type if type could not be determined
    // if(contentType == null) {
    // contentType = "application/octet-stream";
    // }
    //
    // return ResponseEntity.ok()
    // .contentType(MediaType.parseMediaType(contentType))
    // .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
    // resource.getFilename() + "\"")
    // .body(resource);
    // }

    // /**
    // * This API is for uploading user profile Image.
    // */
    // @PostMapping(value = "/uploadFile", consumes =
    // {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    // public ResponseEntity<ResponseUploadFile> uploadImage(@RequestParam("file")
    // MultipartFile file,
    // @RequestParam("customerID") String customerID,
    // @RequestParam("uploadType") String uploadType,
    // @RequestHeader("Authorization") String token) {
    // ResponseUploadFile responseUploadFile = new ResponseUploadFile();
    // if (file.isEmpty() || checkNotNull(customerID) || checkNotNull(token)) {
    // responseUploadFile.message = "Please try again";
    // responseUploadFile.status = ApiConstant.INVALID_REQUEST_CODE;
    // return new ResponseEntity<>(responseUploadFile, HttpStatus.OK);
    // }
    // String bearer = jwtTokenProvider.resolveToken(token);
    //
    // if (jwtTokenProvider.validateToken(bearer, AuthScope.USER)) {
    //
    // String storageName = customerID + UUID.randomUUID();
    // storageName = storageName.hashCode() + "";
    //
    // String fileName = fileStorageService.storeFile(file, storageName);
    // responseUploadFile.fileName = fileName;
    // responseUploadFile.fileType = file.getContentType();
    // responseUploadFile.size = file.getSize();
    // String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
    // .path(ApiConstant.DOWNLOAD_FILES)
    // .path(fileName)
    // .toUriString();
    // responseUploadFile.fileDownloadUri = fileDownloadUri;
    //
    // if (uploadType.equalsIgnoreCase("profile")) {
    // Employee user = adminRepository.findById(UUID.fromString(customerID)).get();
    // user.setImage(fileDownloadUri);
    // adminRepository.save(user);
    // }
    // responseUploadFile.message = "PROFILE_IMAGE_UPLOADED";
    // responseUploadFile.status = ApiConstant.SUCCESS_CODE;
    // } else {
    // responseUploadFile.message = "Invalid token!";
    // responseUploadFile.status = BAD_TOKEN_CODE;
    // }
    // return new ResponseEntity<>(responseUploadFile, HttpStatus.OK);
    // }

}
