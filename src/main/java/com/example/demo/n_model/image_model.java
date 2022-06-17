package com.example.demo.n_model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class image_model {
    private String encodedimage;

    public image_model(String encodedimage) {
        this.encodedimage = encodedimage;
    }
    
    
}