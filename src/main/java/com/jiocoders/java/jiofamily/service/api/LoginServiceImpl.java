package com.jiocoders.java.jiofamily.service.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jiocoders.java.jiofamily.entity.Location;
import com.jiocoders.java.jiofamily.entity.User;
import com.jiocoders.java.jiofamily.networkmodel.request.RequestLogin;
import com.jiocoders.java.jiofamily.networkmodel.response.ResponseLogin;
import com.jiocoders.java.jiofamily.networkmodel.response.data.LocationData;
import com.jiocoders.java.jiofamily.networkmodel.response.data.LoginData;
import com.jiocoders.java.jiofamily.repository.LocationRepository;
import com.jiocoders.java.jiofamily.repository.LoginRepository;
import com.jiocoders.java.jiofamily.security.AuthScope;
import com.jiocoders.java.jiofamily.security.JwtTokenProvider;
import com.jiocoders.java.jiofamily.service.other.SendOTPService;
import com.jiocoders.java.jiofamily.utils.ApiConstant;
import static com.jiocoders.java.jiofamily.utils.ApiConstant.LOGIN_SERVICE;
import static com.jiocoders.java.jiofamily.utils.Common.checkNotNull;
import static com.jiocoders.java.jiofamily.utils.Common.regexEmail;
import com.jiocoders.java.jiofamily.utils.StrConstant;

@Service(LOGIN_SERVICE)
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    SendOTPService sendOTPService;
    @Autowired
    private LocationRepository locationRepository;

    @Override
    public ResponseEntity<ResponseLogin> loginUser(RequestLogin requestLogin) {
        ResponseLogin responseLogin = new ResponseLogin();
        if (!checkNotNull(requestLogin.getEmailId()) || !checkNotNull(requestLogin.getPassword())
                || requestLogin.getPassword().length() < 6) {
            responseLogin.setMessage(StrConstant.PLEASE_PROVIDE_MANDATORY_FIELD);
            responseLogin.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseLogin, HttpStatus.OK);
        }
        Pattern pattern = Pattern.compile(regexEmail);
        Matcher matcher = pattern.matcher(requestLogin.getEmailId());
        if (!matcher.matches()) {
            responseLogin.setMessage(StrConstant.INVALID_EMAIL);
            responseLogin.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseLogin, HttpStatus.OK);
        }

        User user = loginRepository.findFirstByEmailIdAndPassword(requestLogin.getEmailId(),
                requestLogin.getPassword());
        if (user != null) {
            LoginData userData = new LoginData();
            userData.setUserId(user.getId());
            userData.setEmailId(user.getEmailId());
            userData.setMobileNo(user.getMobileNo());
            userData.setFirstName(user.getFirstName());
            userData.setLastName(user.getLastName());
            userData.setGender(user.getGender());
            userData.setDob(user.getDob());
            Location repoLocation = locationRepository.findByLocationId(user.getUserLocation());
            LocationData locationData = new LocationData();
            if (repoLocation != null) {
                locationData.setId(repoLocation.getLocationId());
                locationData.setName(repoLocation.getLocationName());
                locationData.setLatitude(repoLocation.getLatitude());
                locationData.setLongitude(repoLocation.getLongitude());
                userData.setUserLocationData(locationData);
            } else {
                userData.setUserLocationData(null);
            }
            if (user.getIsAdmin()) {
                userData.setToken(jwtTokenProvider.createToken(String.valueOf(user.getId()), AuthScope.ADMIN));
            } else if (user.getIsSuperAdmin()) {
                userData.setToken(jwtTokenProvider.createToken(String.valueOf(user.getId()), AuthScope.SUPER_ADMIN));
            } else {
                userData.setToken(jwtTokenProvider.createToken(String.valueOf(user.getId()), AuthScope.USER));
            }

            // String otpString = String.valueOf(Common.generateOTP());
            // emailService.sendSMS(user.getMobileNo(), otpString);
            // emailService.sendEmailOTP(user.getEmailId(), otpString);
            // user.setOtp(otpString);

            responseLogin.setUserData(userData);
            responseLogin.setMessage(StrConstant.SUCCESS);
            responseLogin.setStatus(ApiConstant.SUCCESS_CODE);
        } else {
            responseLogin.setMessage(StrConstant.PLEASE_PROVIDE_VALID_EMAIL_PASSWORD);
            responseLogin.setStatus(ApiConstant.INVALID_REQUEST_CODE);
        }
        return new ResponseEntity<>(responseLogin, HttpStatus.OK);
    }
}
