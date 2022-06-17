package com.example.demo.project_michelle.repo;
import com.example.demo.project_michelle.table.profile;
import org.springframework.data.jpa.repository.JpaRepository;

/*
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
*/
public interface rep_profile extends JpaRepository<profile,String> {
    profile findByEmail(String email);
}