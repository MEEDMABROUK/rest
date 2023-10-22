package com.inn.alstom.POJO;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
@NamedQuery(name="Product.getAllProduct",query="select new com.inn.alstom.wrapper.ProductWrapper(p.id,p.dtr,p.inverse,p.description,p.status,p.category.id,p.category.name) from Product p ")
@NamedQuery(name="Product.updateProductStatus",query = "update Product p set p.status=:status where p.id=:id")
@NamedQuery(name="Product.getProductByCategory",query="select new com.inn.alstom.wrapper.ProductWrapper(p.id,p.dtr) from Product p where p.category.id=:id and p.status='true'")
@NamedQuery(name="Product.getProductById",query="select  new com.inn.alstom.wrapper.ProductWrapper(p.id,p.dtr,p.inverse,p.description) from Product p where p.id=:id")
@Data
@Entity
@DynamicUpdate
@DynamicInsert
 @Table(name="product")
public class Product implements Serializable {
    public static final Long serialVersionUid = 123456L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;
    @Column(name="dtr")
    private String dtr;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="category_fk",nullable = false)
    private Category category;
    @Column(name="description")
    private String description;
    @Column(name="inverse")
    private String inverse;
    @Column(name="status")
    private String status;


    public void setId(int id) {
        this.id=id;
    }

    public void setStatus(String status) {
        this.status=status;
    }

    public void setCategory(Category category) {
        this.category=category;
    }

    public void setDtr(String dtr) {
        this.dtr=dtr;
    }

    public void setDescription(String description) {
        this.description=description;
    }

    public void setInverse(String inverse) {
        this.inverse=inverse;
    }
}
