package com.inn.alstom.wrapper;

import lombok.Data;

@Data
public class UserWrapper  {

    private Integer id;
    private String name;
    private String email;
    private String contactNumber;
    private String status;
    private String department;
    public UserWrapper(Integer id,String name,String email,String contactNumber,String status,String department){
        this.id = id;
        this.name = name;
        this.email = email;
        this.contactNumber = contactNumber;
        this.status = status;
        this.department = department;

    }

}
