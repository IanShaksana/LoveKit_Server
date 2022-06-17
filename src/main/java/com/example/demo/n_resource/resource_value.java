package com.example.demo.n_resource;

import lombok.*;

@Getter
public class resource_value {
    
    private String geojson_path_local;
    private String geojson_path_DO = "/root/geojson/";

    private String address_local = "localhost:8081/";
    private String address_DO = "206.189.32.21:8081/";

    private String profile_path_local = "D:\\profile_pic\\";
    private String profile_path_DO = "/root/profile_pic/";

    private String prove_path_local = "D:\\prove\\";
    private String prove_path_DO = "/root/prove/";
}
