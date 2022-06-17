package com.example.demo.n_model.response;

import lombok.*;

@Getter
@Setter

public class http_response_gps {

    public http_response_gps() {

    }

    private String s;
    private String sm;

    public void setMati() {
        this.s = "0";
    }

    public void setMatiMaster() {
        this.sm = "0";
        this.s = "0";
    }

    public void setHidup() {
        this.s = "1";
    }

    public void setHidupMaster() {
        this.s = "1";
        this.sm = "1";
    }

    public void setBadrequest() {
        this.s = "99";
        this.sm = "99";
    }

}