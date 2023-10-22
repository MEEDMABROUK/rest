package com.inn.alstom.dao;

import com.inn.alstom.POJO.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.*;
import java.util.List;


public interface CategoryDao extends JpaRepository<Category,Integer> {
    List<Category> getAllCategory();



}
