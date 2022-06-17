package com.example.demo.project_michelle.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

import com.mashape.unirest.http.HttpResponse;
// import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import com.example.demo.n_model.image_model;
import com.example.demo.n_model.response.http_response;
import com.example.demo.n_resource.resource_value;
import com.example.demo.onesignal.onesignal;
import com.example.demo.project_michelle.repo.*;
import com.example.demo.project_michelle.table.*;
import com.google.gson.Gson;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(path = "/api/michelle/")
public class main_controller {

    @Autowired
    private rep_profile rep_profile;
    // @Autowired
    // private rep_relationship rep_relationship;
    @Autowired
    private rep_task rep_task;

    @PostMapping(path = "email")
    public ResponseEntity<http_response> coba_email(/*@RequestBody String message*/) {

        http_response resp = new http_response();
        try {

            String response = sendSimpleMessage();
            String verif = verif();
            System.out.println(response);
            System.out.println(verif);
            resp.setSuccess();
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setFail();
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }
    }

    // done
    @PostMapping(path = "login")
    public ResponseEntity<http_response> Login(@RequestBody String message) {

        http_response resp = new http_response();
        try {
            JSONObject JObject = new JSONObject(message);
            String credential = JObject.getString("email");
            String cPassword = JObject.getString("password");
            profile login_profile_1 = rep_profile.findByEmail(credential);
            if (login_profile_1 != null) {
                if (cPassword.equals(login_profile_1.getPassword())) {

                    if (login_profile_1.getStatus() == 0) {
                        login_profile_1.setStatus(1);
                        login_profile_1.setOnesignalid(JObject.getString("onesignalid"));
                        rep_profile.save(login_profile_1);
                        List<profile> list_dftr = new ArrayList<>();
                        list_dftr.add(login_profile_1);
                        resp.setSuccessWithData(list_dftr);
                        return new ResponseEntity<>(resp, HttpStatus.OK);
                    } else {
                        resp.setFail();
                        resp.setMessage("Sudah Login Sebelumnya");
                        return new ResponseEntity<>(resp, HttpStatus.OK);
                    }
                } else {
                    resp.setFailNull();
                    resp.setMessage("Email & Password Salah");
                    return new ResponseEntity<>(resp, HttpStatus.OK);
                }
            } else {
                resp.setFailNull();
                resp.setMessage("User Tidak Ditemukan");
                return new ResponseEntity<>(resp, HttpStatus.OK);
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.setFail();
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }
    }

    // done
    @PostMapping(path = "logout")
    public ResponseEntity<http_response> Logout(@RequestBody String message) {

        http_response resp = new http_response();
        try {
            JSONObject JObject = new JSONObject(message);
            String dEmail = JObject.getString("email");
            String cPassword = JObject.getString("password");
            profile wasd = rep_profile.findByEmail(dEmail);

            if (wasd != null) {
                if (cPassword.equals(wasd.getPassword())) {
                    if (wasd.getStatus() == 1) {
                        wasd.setStatus(0);
                        List<profile> list_dftr = new ArrayList<>();
                        list_dftr.add(wasd);
                        rep_profile.save(wasd);

                        resp.setSuccess();
                        return new ResponseEntity<>(resp, HttpStatus.OK);
                    } else {
                        resp.setFail();
                        resp.setMessage("Already Logout");
                        return new ResponseEntity<>(resp, HttpStatus.OK);
                    }

                } else {
                    resp.setFail();
                    return new ResponseEntity<>(resp, HttpStatus.NOT_FOUND);
                }
            } else {
                resp.setFailNull();
                return new ResponseEntity<>(resp, HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.setFail();
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }
    }

    // done
    @PostMapping(path = "register")
    public ResponseEntity<http_response> Register(@RequestBody String message) {

        http_response resp = new http_response();
        try {
            JSONObject JObject = new JSONObject(message);
            String email = JObject.getString("email");
            if (rep_profile.findByEmail(email) == null) {
                Gson gson = new Gson();
                profile new_profile = new profile();
                new_profile = gson.fromJson(message, profile.class);
                new_profile.setStatus(0);
                new_profile.setHearttank(0);
                rep_profile.save(new_profile);
                resp.setSuccess();
                return new ResponseEntity<>(resp, HttpStatus.OK);
            } else {
                resp.setFail();
                resp.setMessage("Email Sudah Teregistrasi");
                return new ResponseEntity<>(resp, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setFail();
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }
    }

    // belum, core
    @PostMapping(path = "answer")
    public ResponseEntity<http_response> answer(@RequestBody String message) {
        http_response resp = new http_response();
        try {
            System.out.println(message);
            Gson gson = new Gson();
            profile updateProfile = gson.fromJson(message, profile.class);
            rep_profile.save(updateProfile);
            resp.setSuccess();
            return new ResponseEntity<>(resp, resp.getStatuscode());
        } catch (Exception e) {
            e.printStackTrace();
            resp.setFail();
            return new ResponseEntity<>(resp, resp.getStatuscode());
        }

    }

    // done
    @GetMapping(path = "profile")
    public ResponseEntity<http_response> profile(@RequestHeader String id_login) {

        http_response resp = new http_response();
        System.out.println(id_login);
        try {
            System.out.println(id_login);
            List<profile> updatedProfile = new ArrayList<>();
            updatedProfile.add(rep_profile.findById(id_login).get());
            // do something
            resp.setSuccessWithData(updatedProfile);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setFail();
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }
    }

    // done
    @PostMapping(path = "edit_profile")
    public ResponseEntity<http_response> edit_profile(@RequestBody String message) {

        http_response resp = new http_response();
        try {
            JSONObject JObject = new JSONObject(message);
            profile currentProfile = rep_profile.findById(JObject.getString("id")).get();
            currentProfile.setDetail(JObject.getString("detail"));
            rep_profile.save(currentProfile);
            // do something
            resp.setSuccess();
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setFail();
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

    }

    // done
    @PostMapping(path = "change_password")
    public ResponseEntity<http_response> change_password(@RequestBody String message) {

        http_response resp = new http_response();
        try {
            JSONObject JObject = new JSONObject(message);
            profile currentProfile = rep_profile.findById(JObject.getString("id")).get();
            currentProfile.setPassword((JObject.getString("newPass")));
            rep_profile.save(currentProfile);
            resp.setSuccess();
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setFail();
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

    }

    // done
    @PostMapping(path = "profile_pic")
    public ResponseEntity<http_response> post_profile_pic(@RequestBody String message) {

        http_response resp = new http_response();
        try {
            JSONObject JObject = new JSONObject(message);
            resource_value res = new resource_value();

            String folder_pic = JObject.getString("id");
            profile current_profile = rep_profile.findById(folder_pic).get();
            JSONObject detail = new JSONObject(current_profile.getDetail());
            String fileLoc = "";
            File currDir = new File(res.getProfile_path_DO() + folder_pic);
            if (currDir.exists()) {
                String path = currDir.getAbsolutePath();
                fileLoc = path + "\\" + detail.getString("name") + ".jpg";
                byte[] imageByte = Base64.decodeBase64(JObject.getString("encodedimage"));

                FileOutputStream outputStream = new FileOutputStream(fileLoc);
                outputStream.write(imageByte);
                outputStream.close();
                resp.setSuccess();
            } else {
                if (currDir.mkdirs()) {
                    String path = currDir.getAbsolutePath();
                    fileLoc = path + "\\" + detail.getString("name") + ".jpg";
                    byte[] imageByte = Base64.decodeBase64(JObject.getString("encodedimage"));

                    FileOutputStream outputStream = new FileOutputStream(fileLoc);
                    outputStream.write(imageByte);
                    outputStream.close();
                    resp.setSuccess();
                } else {
                    resp.setFail();
                }
            }
            profile currentProfile = rep_profile.findById(JObject.getString("id")).get();
            currentProfile.setPropic(fileLoc);
            rep_profile.save(currentProfile);
            return new ResponseEntity<>(resp, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            resp.setFail();
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }
    }

    // done
    @GetMapping(path = "profile_pic")
    public ResponseEntity<http_response> get_profile_pic(@RequestHeader String id_login) {
        http_response resp = new http_response();
        try {
            profile currentProfile = rep_profile.findById(id_login).get();
            File currDir = new File(currentProfile.getPropic());
            FileInputStream fileInputStreamReader = new FileInputStream(currDir);
            byte[] bytes = new byte[(int) currDir.length()];
            fileInputStreamReader.read(bytes);
            fileInputStreamReader.close();

            String encodedImage = new String(Base64.encodeBase64(bytes), "UTF-8");
            List<image_model> data = new ArrayList<>();

            data.add(new image_model(encodedImage));
            resp.setSuccessWithData(data);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setFail();
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }
    }

    @PostMapping(value = "cek_notif")
    public void postMethodName() {
        List<task> task_unfiltered = rep_task.findAll();
        List<task> task_filtered = new ArrayList<>();

        for (task task : task_unfiltered) {
            if (task.getStatus() == 1) {
                task_filtered.add(task);
            }
        }

        onesignal warn = new onesignal();

        for (task task : task_filtered) {
            profile currentProfile = rep_profile.findById(task.getIdRelationship()).get();
            warn.sendMessageToUser("Yuk, jangan lupa kerjakan komitmenmu: " + task.getTitle(),
                    currentProfile.getOnesignalid());
        }
    }


    public static String sendSimpleMessage() throws UnirestException {
        String YOUR_DOMAIN_NAME = "dev.segen.id";
        String API_KEY = "6036ea9e7691fce7528d81b5e1dc436e-a2b91229-963bb7f1";
		HttpResponse<String> request = Unirest.post("https://api.mailgun.net/v3/" + YOUR_DOMAIN_NAME + "/messages")
			.basicAuth("api", API_KEY)
			.queryString("from", "Ian <ian@segen.id>")
			.queryString("to", "adrhighland@gmail.com")
			.queryString("subject", "hello")
			.queryString("text", "testing")
			.asString();
		return request.getBody();
    }
    
    public static String verif() throws UnirestException {
        // String YOUR_DOMAIN_NAME = "dev.segen.id";
        String API_KEY = "6036ea9e7691fce7528d81b5e1dc436e-a2b91229-963bb7f1";
		HttpResponse<String> request = Unirest.get("https://api.mailgun.net/v4/address/validate")
			.basicAuth("api", API_KEY)
			.queryString("address", "adrhighland@mailgun.com")
			.asString();
		return request.getBody();
	}
}