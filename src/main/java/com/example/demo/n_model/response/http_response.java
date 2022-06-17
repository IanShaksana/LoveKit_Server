package com.example.demo.n_model.response;

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.*;

@Setter
@Getter

public class http_response {

    private List<?> data;
    private String message;
    private String status;
    private HttpStatus statuscode;

    public void setFail(){
        this.data = null;
        this.message = "Gagal";
        this.statuscode = HttpStatus.BAD_REQUEST;
        this.status = statuscode.toString();
    }

    public void setFailNull(){
        this.data = null;
        this.message = "Gagal";
        this.statuscode = HttpStatus.NOT_FOUND;
        this.status = statuscode.toString();
    }

    public void setSuccess(){
        this.data = null;
        this.message = "Sukses";
        this.statuscode = HttpStatus.OK;
        this.status = statuscode.toString();
    }

    public void setSuccessWithData(List<?> input_data){
        this.data = input_data;
        this.message = "Sukses";
        this.statuscode = HttpStatus.OK;
        this.status = statuscode.toString();
    }

    
}