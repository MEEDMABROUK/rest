package com.inn.alstom.servicetmpl;

import com.inn.alstom.dao.CategoryDao;
import com.inn.alstom.dao.ProductDao;
import com.inn.alstom.dao.UserDao;
import com.inn.alstom.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServicetmpl implements DashboardService {
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    ProductDao productDao;
    @Autowired
    UserDao userDao;

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        Map<String,Object> map = new HashMap<>();
        map.put("user",userDao.count());
        map.put("category",categoryDao.count());
        map.put("product",productDao.count());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
