package com.example.demo.project_michelle.repo;
import com.example.demo.project_michelle.table.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
/*
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
*/
public interface rep_task extends JpaRepository<task,String> {
    List<task> findByUCreatedBy(String uCreatedBy);
    List<task> findByIdRelationship(String idRelationship);
    List<task> findByIdRelationshipAndStatus(String idRelationship, Integer status);

}
