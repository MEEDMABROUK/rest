package com.inn.alstom.servicetmpl;
import com.google.common.base.Strings;
import com.inn.alstom.JWT.CustomerUsersDetailsService;
import com.inn.alstom.JWT.JwtFilter;
import com.inn.alstom.JWT.JwtUtil;
import com.inn.alstom.POJO.User;
import com.inn.alstom.constents.AlstomConstents;
import com.inn.alstom.dao.UserDao;
import com.inn.alstom.service.UserService;
import com.inn.alstom.utils.AlstomUtils;
import com.inn.alstom.utils.EmailUtils;
import com.inn.alstom.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserServicetmpl implements UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    CustomerUsersDetailsService customerUsersDetailsService;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    JwtFilter jwtFilter;
    @Autowired
    EmailUtils emailUtils;
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signup {}",requestMap);
        try {
            if (validateSignUpMap(requestMap)) {
                User user = userDao.findByEmailId(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    userDao.save(getUserFromMap(requestMap));
                    return AlstomUtils.getResponseEntity("Successfully registered", HttpStatus.OK);
                } else {
                    return AlstomUtils.getResponseEntity("Email already exists.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return AlstomUtils.getResponseEntity(AlstomConstents.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return AlstomUtils.getResponseEntity(AlstomConstents.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);


    }



    private boolean validateSignUpMap(Map<String,String> requestMap ){
        if(requestMap.containsKey("name") && requestMap.containsKey("contactNumber") && requestMap.containsKey("email") && requestMap.containsKey("password") && requestMap.containsKey("department")){
            return true;
        }
        return false;
    }
    private User getUserFromMap(Map<String,String> requestMap){
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setDepartment(requestMap.get("department"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }
    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login");
        try{
            Authentication auth = authenticationManager.authenticate(

                    new UsernamePasswordAuthenticationToken(requestMap.get("email"),requestMap.get("password"))

            );
            log.info("try authentication");
            if(auth.isAuthenticated()){
                if(customerUsersDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")){
                    return new ResponseEntity<>("{\"token\":\"" + jwtUtil.generateToken(customerUsersDetailsService.getUserDetail().getEmail(),customerUsersDetailsService.getUserDetail().getRole()) + "\"}" , HttpStatus.OK);

                }
                else{
                    return new ResponseEntity<String>("{\"message\":\""+"Wait for admin approval"+"\"}",HttpStatus.BAD_REQUEST);
                }
            }
        }catch(Exception ex){
            log.error("{}",ex);
        }

        return  new ResponseEntity<String>("{\"message\":\""+"Bad Credentials"+"\"}",HttpStatus.BAD_REQUEST);
    }


    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try{
            if(jwtFilter.isAdmin()){
                return new ResponseEntity<>(userDao.getAllUser(),HttpStatus.OK);
            }else{
                new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isUser()){
                Optional<User> optional = userDao.findById(Integer.parseInt(requestMap.get("id")));
                if(optional.isPresent()){
                    userDao.updateStatus(requestMap.get("status"),Integer.parseInt(requestMap.get("id")));
                    sendMailToAllAdmin(requestMap.get("status"),optional.get().getEmail(),userDao.getAllAdmin());
                    return AlstomUtils.getResponseEntity("User Status updated successfully",HttpStatus.OK);
                }
                else{
                    return AlstomUtils.getResponseEntity("User id doesn't exist",HttpStatus.OK);
                }
            }
            else{
                return AlstomUtils.getResponseEntity(AlstomConstents.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return AlstomUtils.getResponseEntity(AlstomConstents.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void sendMailToAllAdmin(String status,String user,List<String> allAdmin){
        allAdmin.remove(jwtFilter.getCurrentUser());
        if(status != null && status.equalsIgnoreCase("true")){
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),"Account approved","USER:- "+user+"\n is approved by \n ADMIN:-"+jwtFilter.getCurrentUser(),allAdmin);
        }
        else{
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),"Account disabled","USER:- "+user+"\n is disabled by \n ADMIN:-"+jwtFilter.getCurrentUser(),allAdmin);
        }
    }

    @Override
    public ResponseEntity<String> checkToken() {
        return AlstomUtils.getResponseEntity("true",HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try{
           User userObj= userDao.findByEmail(jwtFilter.getCurrentUser());
            if(!userObj.equals(null)){
                if(userObj.getPassword().equals(requestMap.get("oldPassword"))){
                    userObj.setPassword(requestMap.get("newPassword"));
                    userDao.save(userObj);
                    return AlstomUtils.getResponseEntity("Password updated successfully",HttpStatus.OK);
                }
                return AlstomUtils.getResponseEntity("Incorrect old password",HttpStatus.BAD_REQUEST);
            }
            return AlstomUtils.getResponseEntity(AlstomConstents.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return AlstomUtils.getResponseEntity(AlstomConstents.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try{
            User user=userDao.findByEmail(requestMap.get("email"));
            if(!Objects.isNull(user)&& !Strings.isNullOrEmpty(user.getEmail()))
               emailUtils.forgotMail(user.getEmail(),"Credentials by Alstom Management system",user.getPassword());
            return AlstomUtils.getResponseEntity("Check your mail for credentials",HttpStatus.OK);

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return AlstomUtils.getResponseEntity(AlstomConstents.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
