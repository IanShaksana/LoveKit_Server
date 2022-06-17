package com.example.demo.project_michelle.table;

import java.util.Date;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import lombok.*;

@Entity
@Getter
@Setter

public class profile {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 50)
    private String id;

    @Column(length = 50)
    private String password;
    @Column(length = 50)
    private String email;
    @Column(columnDefinition = "LONGTEXT")
    private String detail;
    @Column(columnDefinition = "LONGTEXT")
    private String testDetail;
    @Column(length = 11)
    private Integer status;
    @Column(length = 100)
    private String propic;
    @Column(length = 50)
    private String onesignalid;
    @Column(length = 50)
    private Integer hearttank;
    

    @Column(length = 40)
    private String createdby;
    @Column(columnDefinition = "DATETIME")
    private Date createdat;
    @Column(length = 40)
    private String lastModifiedby;
    @Column(columnDefinition = "DATETIME")
    private Date lastmodifiedat;
    @Column(length = 11)
    private Integer version;
    @Column(columnDefinition = "DATETIME")
    private Date deletedat;
    
}