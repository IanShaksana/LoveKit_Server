package com.example.demo.n_model.response;

import lombok.*;

@Getter
@Setter

public class resp {

	public resp(String text, String status, String ID){
		this.text = text;
		this.status = status;
		this.ID = ID;		
	}

    private String text; 
	private String status;
	private String ID; 

}