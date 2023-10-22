package com.inn.alstom.wrapper;

import lombok.Data;

@Data
public class ProductWrapper {
    Integer id;
    String dtr;
    String inverse;
    String description;
    String status;
    Integer categoryId;
    String categoryName;
    public ProductWrapper(){
    }
    public ProductWrapper(Integer id,String dtr,String inverse,String description,String status,Integer categoryId,String categoryName){
        this.id=id;
        this.dtr=dtr;
        this.inverse=inverse;
        this.description=description;
        this.status=status;
        this.categoryId=categoryId;
        this.categoryName=categoryName;
    }

    public ProductWrapper(Integer id ,String dtr){
       this.id=id;
       this.dtr=dtr;
    }
    public ProductWrapper(Integer id,String dtr,String inverse,String description ){
        this.id=id;
        this.dtr=dtr;
        this.inverse=inverse;
        this.description=description;
    }




}
