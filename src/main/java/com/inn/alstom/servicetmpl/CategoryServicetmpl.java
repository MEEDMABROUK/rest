package com.inn.alstom.servicetmpl;

import com.google.common.base.Strings;
import com.inn.alstom.JWT.JwtFilter;
import com.inn.alstom.POJO.Category;
import com.inn.alstom.constents.AlstomConstents;
import com.inn.alstom.dao.CategoryDao;
import com.inn.alstom.service.CategoryService;
import com.inn.alstom.utils.AlstomUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CategoryServicetmpl  implements CategoryService {
    @Autowired
    CategoryDao categoryDao;

    @Autowired
    JwtFilter jwtFilter;
    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()){
               if(validateCategoryMap(requestMap,false)){
                  categoryDao.save(getCategoryFromMap(requestMap,false));
                  return AlstomUtils.getResponseEntity("Category added successfully",HttpStatus.OK);
               }
            }else {
                return AlstomUtils.getResponseEntity(AlstomConstents.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return AlstomUtils.getResponseEntity(AlstomConstents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try{
           if(!Strings.isNullOrEmpty(filterValue) &&  filterValue.equalsIgnoreCase("true")){
               log.info("Inside if");
               return new ResponseEntity<List<Category>>(categoryDao.getAllCategory(),HttpStatus.OK);
           }
           return new ResponseEntity<>(categoryDao.findAll(),HttpStatus.OK);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<List<Category>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()){
                if(validateCategoryMap(requestMap,true)){
                   Optional optional = categoryDao.findById(Integer.parseInt(requestMap.get("id")));
                   if(optional.isPresent()){
                       categoryDao.save(getCategoryFromMap(requestMap,true));
                       return AlstomUtils.getResponseEntity("Category updated successfully",HttpStatus.OK);
                   }else{
                      return AlstomUtils.getResponseEntity("Category id doesn't exist",HttpStatus.OK);
                   }
                }
                return  AlstomUtils.getResponseEntity(AlstomConstents.INVALID_DATA,HttpStatus.BAD_REQUEST);
            }else{
                return AlstomUtils.getResponseEntity(AlstomConstents.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return AlstomUtils.getResponseEntity(AlstomConstents.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
        if(requestMap.containsKey("name")){
            if(requestMap.containsKey("id") && validateId){
                return true;
            }else if(!validateId){
               return true;
            }
        }
        return false;
    }
    private Category getCategoryFromMap(Map<String,String>requestMap,Boolean  isAdd){
        Category category=new Category();
        if (isAdd) {
            category.setId(Integer.parseInt(requestMap.get("id")));
        }
        category.setName(requestMap.get("name"));
        return category;
    }
}
