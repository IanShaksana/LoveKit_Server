package com.example.demo.project_michelle.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

import com.example.demo.n_model.image_model;
import com.example.demo.n_model.response.http_response;
import com.example.demo.n_resource.resource_value;
import com.example.demo.project_michelle.repo.*;
import com.example.demo.project_michelle.table.*;
import com.google.gson.Gson;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/michelle/")
public class task_cont {
    @Autowired
    private rep_profile rep_profile;
    // @Autowired
    // private rep_relationship rep_relationship;
    @Autowired
    private rep_task rep_task;

    // done
    @PostMapping(path = "create_task")
    public ResponseEntity<http_response> create_task(@RequestBody String message) {

        http_response resp = new http_response();
        try {
            System.out.println(message);
            Gson gson = new Gson();
            task newTask = gson.fromJson(message, task.class);
            rep_task.save(newTask);
            resp.setSuccess();
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setFail();
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }
    }

    // done
    @GetMapping(path = "get_task")
    public ResponseEntity<http_response> get_task_personal(@RequestHeader String id_login) {

        http_response resp = new http_response();
        try {
            System.out.println("get task :" + id_login);
            List<task> newTask = rep_task.findByIdRelationship(id_login);
            List<task> task_want_to_send = new ArrayList<>();
            for (task task : newTask) {
                if (task.getStatus() != 3 && task.getStatus() != 4) {
                    task_want_to_send.add(task);
                }
            }
            for (task task : task_want_to_send) {
                System.out.println(task.getStatus());
            }
            resp.setSuccessWithData(task_want_to_send);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setFail();
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }
    }

    // testing & done kurang yg direviewed
    @PostMapping(path = "complete_task")
    public ResponseEntity<http_response> complete_task(@RequestBody String message) {
        http_response resp = new http_response();
        // onesignal warn = new onesignal();
        System.out.println(message);
        try {
            JSONObject JObject = new JSONObject(message);
            task newTask = rep_task.findById(JObject.getString("id")).get();
            profile currentProfile = rep_profile.findById(
                    rep_task.findByIdRelationship(JObject.getString("idRelationship")).get(0).getIdRelationship())
                    .get();
            if (newTask.getRepeatable() == 1) {
                // repeatable task

                if (newTask.getReviewed() == 1) {
                    // reviewed
                    if (newTask.getRep() == newTask.getCompletion()) {
                        newTask.setStatus(0);
                        // warn
                        // warn.sendMessageToUser("need review", JObject.getString("partner"));
                        // upload bukti
                        newTask.setProve("");
                    } else {
                        newTask.setCompletion(newTask.getCompletion() + 1);
                        // upload bukti (optional)
                    }

                } else {
                    // done
                    // non reviewed
                    int future_value = newTask.getCompletion() + 1;
                    if (newTask.getRep() == future_value) {
                        newTask.setStatus(3);
                        newTask.setNilai(newTask.getRep());
                        newTask.setCompletion(future_value);
                        newTask.setDeletedat(new Date());
                        Integer future_hearttank = currentProfile.getHearttank() + (future_value * 5);
                        if (future_hearttank > 100) {
                            currentProfile.setHearttank(100);
                        } else {
                            currentProfile.setHearttank(currentProfile.getHearttank() + (future_value * 5));
                        }
                    } else {
                        newTask.setCompletion(newTask.getCompletion() + 1);
                    }
                }

            } else {
                // single task
                if (newTask.getReviewed() == 1) {
                    // reviewed
                    newTask.setStatus(0);
                    // warn
                    // warn.sendMessageToUser("commitment need to be reviewed",
                    // JObject.getString("partner"));
                    // upload bukti
                    newTask.setProve("");

                } else {
                    // done
                    // non reviewed
                    newTask.setStatus(3);
                    newTask.setNilai(1);
                    newTask.setDeletedat(new Date());
                    Integer future_hearttank = currentProfile.getHearttank() + (newTask.getNilai() * 5);
                    if (future_hearttank > 100) {
                        currentProfile.setHearttank(100);
                    } else {
                        currentProfile.setHearttank(currentProfile.getHearttank() + (newTask.getNilai() * 5));
                    }
                }

            }

            rep_task.save(newTask);
            resp.setSuccess();
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setFail();
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }
    }

    // done
    @PostMapping(path = "review_task")
    public ResponseEntity<http_response> review_task(@RequestBody String message) {

        http_response resp = new http_response();
        try {
            JSONObject JObject = new JSONObject(message);
            task newTask = rep_task.findById(JObject.getString("id")).get();
            profile currentProfile = rep_profile.findById(newTask.getIdRelationship()).get();

            if (JObject.getInt("status") == 1) {

                if (JObject.getInt("repeatable") == 1) {
                    // repeatable

                    if (JObject.getInt("reviewed") == 1) {
                        // reviewed
                        newTask.setStatus(JObject.getInt("status"));
                        newTask.setCompletion(0);
                        newTask.setProve(null);
                    } else {
                        // non reviewed

                    }

                } else {

                    // single task

                    if (JObject.getInt("reviewed") == 1) {
                        // reviewed
                        newTask.setStatus(JObject.getInt("status"));
                        newTask.setCompletion(0);
                        newTask.setProve(null);
                    } else {
                        // non reviewed

                    }

                }
            }

            if (JObject.getInt("status") == 3) {
                newTask.setStatus(JObject.getInt("status"));
                newTask.setNilai(JObject.getInt("nilai"));
                newTask.setDeletedat(new Date());
                Integer future_value = currentProfile.getHearttank() + (newTask.getNilai() * 5);
                if (future_value > 100) {
                    currentProfile.setHearttank(100);
                } else {
                    currentProfile.setHearttank(currentProfile.getHearttank() + (newTask.getNilai() * 5));
                }
            }

            rep_task.save(newTask);
            resp.setSuccess();
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setFail();
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }
    }

    // done
    @PostMapping(path = "prove")
    public ResponseEntity<http_response> post_prove_pic(@RequestBody String message) {

        http_response resp = new http_response();
        try {
            System.out.println(message);
            JSONObject JObject = new JSONObject(message);
            resource_value res = new resource_value();
            // onesignal warn = new onesignal();

            String folder_pic = JObject.getString("id");
            String fileLoc = "";
            File currDir = new File(res.getProve_path_DO() + folder_pic);
            if (currDir.exists()) {
                String path = currDir.getAbsolutePath();
                fileLoc = path + "\\prove.jpg";
                byte[] imageByte = Base64.decodeBase64(JObject.getString("encodedimage"));

                FileOutputStream outputStream = new FileOutputStream(fileLoc);
                outputStream.write(imageByte);
                outputStream.close();
                resp.setSuccess();
            } else {
                if (currDir.mkdirs()) {
                    String path = currDir.getAbsolutePath();
                    fileLoc = path + "\\prove.jpg";
                    byte[] imageByte = Base64.decodeBase64(JObject.getString("encodedimage"));

                    FileOutputStream outputStream = new FileOutputStream(fileLoc);
                    outputStream.write(imageByte);
                    outputStream.close();
                    resp.setSuccess();
                } else {
                    resp.setFail();
                }
            }

            // warn.sendMessageToUser("commitment need to be reviewed",
            // JObject.getString("partner"));

            task currentTask = rep_task.findById(JObject.getString("id")).get();
            currentTask.setProve(fileLoc);
            currentTask.setCompletion(currentTask.getRep());
            currentTask.setStatus(0);
            rep_task.save(currentTask);
            return new ResponseEntity<>(resp, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            resp.setFail();
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }
    }

    // done
    @GetMapping(path = "prove")
    public ResponseEntity<http_response> get_prove_pic(@RequestHeader String id_login) {
        http_response resp = new http_response();
        System.out.println(id_login);
        try {
            task currentTask = rep_task.findById(id_login).get();
            if (currentTask.getProve() == null) {
                resp.setFailNull();
            } else {
                File currDir = new File(currentTask.getProve());
                FileInputStream fileInputStreamReader = new FileInputStream(currDir);
                byte[] bytes = new byte[(int) currDir.length()];
                fileInputStreamReader.read(bytes);
                fileInputStreamReader.close();

                String encodedImage = new String(Base64.encodeBase64(bytes), "UTF-8");
                List<image_model> data = new ArrayList<>();

                data.add(new image_model(encodedImage));
                resp.setSuccessWithData(data);
            }

            return new ResponseEntity<>(resp, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            resp.setFail();
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }
    }

    // done
    @GetMapping(path = "task_record")
    public ResponseEntity<http_response> see_record(@RequestHeader String id_login) {

        http_response resp = new http_response();
        try {
            // filter yg success dan failed
            List<task> newTask = rep_task.findByIdRelationshipAndStatus(id_login, 3);
            resp.setSuccessWithData(newTask);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setFail();
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

    }

}