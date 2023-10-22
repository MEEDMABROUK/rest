package com.inn.alstom.servicetmpl;

import com.inn.alstom.JWT.JwtFilter;
import com.inn.alstom.POJO.Category;
import com.inn.alstom.POJO.Product;
import com.inn.alstom.constents.AlstomConstents;
import com.inn.alstom.dao.ProductDao;
import com.inn.alstom.service.ProductService;
import com.inn.alstom.utils.AlstomUtils;
import com.inn.alstom.wrapper.ProductWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductServicetmpl implements ProductService {
    @Autowired
    ProductDao productDao;
    @Autowired
    JwtFilter jwtFilter;
    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()){
                if(validateProductMap(requestMap,false)){
                    productDao.save(getProductFromMap(requestMap,false));
                    return AlstomUtils.getResponseEntity("Product added successfully",HttpStatus.OK);
                }
                return AlstomUtils.getResponseEntity(AlstomConstents.INVALID_DATA,HttpStatus.BAD_REQUEST);
            }else{
                return AlstomUtils.getResponseEntity(AlstomConstents.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return AlstomUtils.getResponseEntity(AlstomConstents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }



    private boolean validateProductMap(Map<String,String> requestMap,boolean validateId){
        if(requestMap.containsKey("dtr")){
            if(requestMap.containsKey("id") && validateId){
                return true;
            }
            else if (!validateId){
                return true;
            }
        }
        return false;
    }
    private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {
        Category category= new Category();
        Product product = new Product();
        category.setId(Integer.parseInt(requestMap.get("categoryId")));
        if(isAdd){
            product.setId(Integer.parseInt(requestMap.get("id")));
        }
        else{
            product.setStatus("true");
        }
        product.setCategory(category);
        product.setDtr(requestMap.get("dtr"));
        product.setDescription(requestMap.get("description"));
        product.setInverse(requestMap.get("inverse"));
        return product;
    }
    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
        try{
            return new ResponseEntity<>(productDao.getAllProduct(),HttpStatus.OK);
        }catch(Exception ex){
           ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()){
                if(validateProductMap(requestMap,true)){
                    Optional<Product> optional = productDao.findById(Integer.parseInt(requestMap.get("id")));
                    if(optional.isPresent()){
                        Product product=getProductFromMap(requestMap,true);
                        product.setStatus(optional.get().getStatus());
                        productDao.save(product);
                        return AlstomUtils.getResponseEntity("Product updated successfully",HttpStatus.OK);
                    }
                    else{
                        return AlstomUtils.getResponseEntity("Product id doesn't exist",HttpStatus.OK);
                    }
                }else{
                    AlstomUtils.getResponseEntity(AlstomConstents.INVALID_DATA,HttpStatus.BAD_REQUEST);
                }
            }else{
                return AlstomUtils.getResponseEntity(AlstomConstents.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return AlstomUtils.getResponseEntity(AlstomConstents.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        try{
            if(jwtFilter.isAdmin()){
                Optional optional = productDao.findById(id);
                if(optional.isPresent()){
                    productDao.deleteById(id);
                    return AlstomUtils.getResponseEntity("Product deleted successfully",HttpStatus.OK);
                }
                return AlstomUtils.getResponseEntity("Product id doesn't exist",HttpStatus.OK);
            }else{
                return AlstomUtils.getResponseEntity(AlstomConstents.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return AlstomUtils.getResponseEntity(AlstomConstents.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()){
                Optional optional =productDao.findById(Integer.parseInt(requestMap.get("id")));
                if(optional.isPresent()){
                    productDao.updateProductStatus(requestMap.get("status"),Integer.parseInt(requestMap.get("id")));
                    return AlstomUtils.getResponseEntity("Product status updated successfully",HttpStatus.OK);
                }
                 return AlstomUtils.getResponseEntity("Product id doesn't exist",HttpStatus.OK);

            }else{
                return AlstomUtils.getResponseEntity(AlstomConstents.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return AlstomUtils.getResponseEntity(AlstomConstents.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getByCategory(Integer id) {
        try{
            return new ResponseEntity<>(productDao.getProductByCategory(id),HttpStatus.OK);
        }catch(Exception ex){

        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ProductWrapper> getProductById(Integer id) {
        try{
            return new ResponseEntity<>(productDao.getProductById(id),HttpStatus.OK);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ProductWrapper(),HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
