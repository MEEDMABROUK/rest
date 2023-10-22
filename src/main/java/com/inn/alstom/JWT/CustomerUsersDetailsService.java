package com.inn.alstom.JWT;

import com.inn.alstom.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Logger;
@Slf4j
@Service
public class CustomerUsersDetailsService implements UserDetailsService {
    private static final Logger log = Logger.getLogger(JwtUtil.class.getName());
    @Autowired
    UserDao userDao;
    private com.inn.alstom.POJO.User userDetail;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername {}"+username);
        userDetail = userDao.findByEmailId(username);
        if(!Objects.isNull(userDetail))
            return new User(userDetail.getEmail(),userDetail.getPassword(),new ArrayList<>());
        else
            throw new UsernameNotFoundException("User not found");
    }
    public com.inn.alstom.POJO.User getUserDetail(){
        com.inn.alstom.POJO.User user = userDetail;
        user.setPassword(null);
        return userDetail;
    }
}
