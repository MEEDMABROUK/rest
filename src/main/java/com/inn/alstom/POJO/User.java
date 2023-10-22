package com.inn.alstom.POJO;


import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;



@Data
@NamedQuery(name="User.findByEmailId",query ="select u from User u where u.email =: email ")
@NamedQuery(name="User.getAllUser",query="select new com.inn.alstom.wrapper.UserWrapper(u.id,u.name,u.email,u.contactNumber,u.status,u.department) from User u where u.role = 'user'")
@NamedQuery(name="User.updateStatus",query="update User u set u.status=:status where u.id=:id ")
@NamedQuery(name="User.getAllAdmin",query="select u.email from User u where u.role = 'admin'")
@Entity
@DynamicUpdate

@Table(name="userrr")
public class User  implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name="name")
    private String name;
    @Column(name="contactNumber")
    private String contactNumber;
    @Column(name="email")
    private String email;
    @Column(name="department")
    private String department;
    @Column(name="password")
    private String password;
    @Column(name="status")
    private String status;
    @Column(name="role")
    private String role;


    public void setEmail(String email) {
        this.email=email;
    }

    public void setName(String name) {
        this.name=name;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber=contactNumber;
    }

    public void setDepartment(String department) {
        this.department=department;
    }

    public void setPassword(String password) {
        this.password=password;
    }


    public void setStatus(String status) {
        this.status=status;
    }

    public void setRole(String role) {
        this.role=role;
    }

    public String getStatus() {
        return status;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
