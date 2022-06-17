package com.example.demo.project_michelle.table;

import java.util.Date;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import lombok.*;

@Entity
@Getter
@Setter

public class task {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 50)
    private String id;

    @Column(length = 50)
    private String idRelationship;
    @Column(length = 50)
    private String title;
    @Column(length = 50)
    private String description;
    // 0 review
    // 1 no
    // 2 gausah
    // 3 selesai
    // 4 failed gausah
    @Column(length = 11)
    private Integer status;
    @Column(length = 11)
    private Integer nilai;
    @Column(length = 11)
    private Integer repeatable;
    @Column(length = 11)
    private Integer rep;
    @Column(length = 11)
    private Integer completion;
    @Column(length = 11)
    private Integer reviewed;
    @Column(length = 100)
    private String prove;
    // 1 word, 2 touch, 3 quality, 4 gift, 5 service, 0 notype
    @Column(length = 11)
    private Integer type;
    

    @Column(length = 40)
    private String createdby;
    @Column(columnDefinition = "DATETIME")
    private Date createdat = new Date();
    @Column(length = 40)
    private String lastModifiedby;
    @Column(columnDefinition = "DATETIME")
    private Date lastmodifiedat;
    @Column(length = 11)
    private Integer version;
    @Column(columnDefinition = "DATETIME")
    private Date deletedat;
    
}