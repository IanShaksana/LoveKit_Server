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
public interface rep_relationship extends JpaRepository<relationship,String> {
    List<relationship> findByPasangan1(String pasangan1);
    List<relationship> findByPasangan2(String pasangan2);
    List<relationship> findByPasangan1AndStatus(String pasangan1, Integer status);
    List<relationship> findByPasangan2AndStatus(String pasangan2, Integer status);
    List<relationship> findByPasangan1AndPasangan2AndStatus(String pasangan1, String pasangan2, Integer status);
}