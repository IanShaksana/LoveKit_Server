package com.example.demo.project_michelle.table;

import java.util.Date;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import lombok.*;

@Entity
@Getter
@Setter

public class relationship {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 50)
    private String id;
    @Column(length = 50)
    private String pasangan1;
    @Column(length = 50)
    private String nama1;
    @Column(length = 50)
    private String pasangan2;
    @Column(length = 50)
    private String nama2;
    // 0 pengajuan
    // 1 ditolak
    // 2 diterima
    // 3 selesai
    @Column(length = 50)
    private Integer status;

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