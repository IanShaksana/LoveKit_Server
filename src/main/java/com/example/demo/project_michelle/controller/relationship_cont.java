package com.example.demo.project_michelle.controller;

import java.util.*;

import com.example.demo.n_model.response.http_response;
import com.example.demo.onesignal.onesignal;
import com.example.demo.project_michelle.repo.*;
import com.example.demo.project_michelle.table.*;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/michelle/")
public class relationship_cont {

    @Autowired
    private rep_profile rep_profile;
    @Autowired
    private rep_relationship rep_relationship;
    // @Autowired
    // private rep_task rep_task;

    // done
    @GetMapping(path = "get_relation")
    public ResponseEntity<http_response> get_relation(@RequestHeader String id_login) {

        http_response resp = new http_response();
        try {
            List<relationship> newRelation1 = rep_relationship.findByPasangan1AndStatus(id_login, 2);
            List<relationship> newRelation2 = rep_relationship.findByPasangan2AndStatus(id_login, 2);
            if (!newRelation1.isEmpty()) {
                resp.setSuccessWithData(newRelation1);
                return new ResponseEntity<>(resp, HttpStatus.OK);
            } else if (!newRelation2.isEmpty()) {
                resp.setSuccessWithData(newRelation2);
                return new ResponseEntity<>(resp, HttpStatus.OK);
            } else {
                resp.setFailNull();
                return new ResponseEntity<>(resp, HttpStatus.OK);
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.setFail();
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }
    }

    // done
    @GetMapping(path = "get_relation_pending")
    public ResponseEntity<http_response> get_relation_pending(@RequestHeader String id_login) {

        http_response resp = new http_response();
        System.out.println("masuk " + id_login);
        try {
            List<relationship> newRelation = rep_relationship.findByPasangan2AndStatus(id_login, 0);
            if (!newRelation.isEmpty()) {
                System.out.println("not empty");
                resp.setSuccessWithData(newRelation);
                return new ResponseEntity<>(resp, HttpStatus.OK);
            } else {
                System.out.println("empty");
                resp.setFailNull();
                resp.setMessage("Tidak Ada Partner yang Tertunda");
                return new ResponseEntity<>(resp, HttpStatus.OK);
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.setFail();
            resp.setMessage("bad request");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }
    }

    // done
    @PostMapping(path = "add_relation")
    public ResponseEntity<http_response> add_relation(@RequestBody String message) {

        http_response resp = new http_response();
        try {

            JSONObject JObject = new JSONObject(message);
            System.out.println(message);
            String pasangan1 = JObject.getString("pasangan1");
            String pasangan2 = rep_profile.findByEmail(JObject.getString("pasangan2")).getId();

            // check if ada user e
            if (rep_profile.findByEmail(JObject.getString("pasangan2")) == null) {
                resp.setFailNull();
                resp.setMessage("User Tidak Ditemukan");
                return new ResponseEntity<>(resp, HttpStatus.OK);
            }

            // check if user e ws nduwe relation
            if (rep_relationship.findByPasangan2AndStatus(pasangan2, 2) == null) {
                resp.setFailNull();
                resp.setMessage("User Sudah Punya Partner");
                return new ResponseEntity<>(resp, HttpStatus.OK);
            }

            // check if invitation has been send
            if (rep_relationship.findByPasangan1AndPasangan2AndStatus(pasangan1, pasangan2, 0).isEmpty()) {
                relationship newRelation = new relationship();
                newRelation.setPasangan1(pasangan1);
                newRelation.setPasangan2(pasangan2);

                profile profile1 = rep_profile.findById(pasangan1).get();
                profile profile2 = rep_profile.findById(pasangan2).get();

                JSONObject JObject1 = new JSONObject(profile1.getDetail());
                JSONObject JObject2 = new JSONObject(profile2.getDetail());

                newRelation.setNama1(JObject1.getString("name"));
                newRelation.setNama2(JObject2.getString("name"));

                newRelation.setStatus(0);
                rep_relationship.save(newRelation);
                resp.setSuccess();
                resp.setMessage("Undangan telah dikirim ke: " + JObject.getString("pasangan2"));

                // one signal warn
                onesignal warn = new onesignal();
                warn.sendMessageToUser(newRelation.getNama1()+ " Ingin Menjadi Partnermu",
                        rep_profile.findByEmail(JObject.getString("pasangan2")).getOnesignalid());

                return new ResponseEntity<>(resp, HttpStatus.OK);
            } else {
                resp.setFail();
                resp.setMessage("Undanganmu Telah Dikirim");
                return new ResponseEntity<>(resp, HttpStatus.OK);
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.setFail();
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

    }

    // done
    @PostMapping(path = "accept_relation")
    public ResponseEntity<http_response> accept_relation(@RequestBody String message) {
        http_response resp = new http_response();
        System.out.println(message);
        try {
            JSONObject JObject = new JSONObject(message);
            relationship newRelation = rep_relationship.findById(JObject.getString("id")).get();
            // isa reject isa ndak

            onesignal warn = new onesignal();
            profile sender = rep_profile.findById(newRelation.getPasangan1()).get();
            if (JObject.getInt("status") == 1) {
                warn.sendMessageToUser("Ajakan Berpartner Kamu Telah Ditolak Oleh: "+newRelation.getNama2(),
                        sender.getOnesignalid());
            }

            if (JObject.getInt("status") == 2) {
                warn.sendMessageToUser("Ajakan Berpartner Kamu Telah Diterima Oleh: "+newRelation.getNama2(),
                        sender.getOnesignalid());
            }

            newRelation.setStatus(JObject.getInt("status"));
            rep_relationship.save(newRelation);
            resp.setSuccess();
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setFail();
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }
    }

    // done
    @PostMapping(path = "del_relation")
    public ResponseEntity<http_response> del_relation(@RequestBody String message) {
        http_response resp = new http_response();
        System.out.println(message);
        try {

            JSONObject JObject = new JSONObject(message);

            List<relationship> newRelation1 = rep_relationship.findByPasangan1AndStatus(JObject.getString("id"), 2);
            List<relationship> newRelation2 = rep_relationship.findByPasangan2AndStatus(JObject.getString("id"), 2);
            if (!newRelation1.isEmpty()) {
                relationship del_relation = newRelation1.get(0);
                del_relation.setStatus(3);
                rep_relationship.save(del_relation);
                resp.setSuccess();
                resp.setMessage("Partner Sukses Dihapus");
                return new ResponseEntity<>(resp, HttpStatus.OK);
            } else if (!newRelation2.isEmpty()) {
                relationship del_relation = newRelation2.get(0);
                del_relation.setStatus(3);
                rep_relationship.save(del_relation);
                resp.setSuccess();
                resp.setMessage("Partner Sukses Dihapus");
                return new ResponseEntity<>(resp, HttpStatus.OK);
            } else {
                resp.setFailNull();
                resp.setMessage("Partner Tidak Ditemukan");
                return new ResponseEntity<>(resp, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setFail();
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }
    }

}