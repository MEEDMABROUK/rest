package com.inn.alstom.dao;

import com.inn.alstom.POJO.User;
import com.inn.alstom.wrapper.UserWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface UserDao extends JpaRepository<User,Integer> {

   User findByEmailId(@Param("email") String email);
   List<UserWrapper> getAllUser();
   List<String> getAllAdmin();
   @Transactional
   @Modifying
   Integer updateStatus(@Param("Status") String status,@Param("id") Integer id);
   //Je vais pas ecrire une méthode pour celle-ci car automatiquement une requete est generee si findBy....
   User findByEmail(String  email);

}
