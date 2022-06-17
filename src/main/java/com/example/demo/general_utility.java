package com.example.demo;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class general_utility {

    public Date addMonth(String x, int y) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(x);

            Calendar wad = Calendar.getInstance();
            wad.setTime(date);
            wad.add(Calendar.MONTH, y);
            date = wad.getTime();
            // System.out.println(formatter.format(date));
            // return formatter.format(date);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Date dateFormat(Date element) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(DateToString(element));

            Calendar wad = Calendar.getInstance();
            wad.setTime(date);
            date = wad.getTime();
            // System.out.println(formatter.format(date));
            // return formatter.format(date);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Date StringToDate(String x) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(x);

            Calendar wad = Calendar.getInstance();
            wad.setTime(date);
            date = wad.getTime();
            // System.out.println(formatter.format(date));
            // return formatter.format(date);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String DateToString(Date element) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar wad = Calendar.getInstance();
        wad.setTime(element);
        element = wad.getTime();
        // System.out.println(formatter.format(date));
        return formatter.format(element);
    }

    public String DateToString2(Date element) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar wad = Calendar.getInstance();
        wad.setTime(element);
        element = wad.getTime();
        // System.out.println(formatter.format(date));
        return formatter.format(element);
    }

    public String timeDiff(Date element1, Date element2) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

        String date1 = formatter.format(element1);
        String date2 = formatter.format(element2);
        System.out.println(date1 + ' ' + date2);
        long wasd = 0;
        try {
            wasd = formatter.parse(date1).getTime() - formatter.parse(date2).getTime();
            long diffSec = wasd / 1000;
            long min = diffSec / 60;
            long sec = diffSec % 60;
            return min + " menit " + sec + " detik.";
        } catch (ParseException e) {
            return "";
        }
    }

    public Date set_time(Integer hour, Integer minute, Integer second) {
        try {

            Calendar wad = Calendar.getInstance();
            wad.setTime(new Date());
            wad.set(Calendar.HOUR_OF_DAY, hour);
            wad.set(Calendar.MINUTE, minute);
            wad.set(Calendar.SECOND, second);
            Date date = wad.getTime();
            System.out.println(date);
            // return formatter.format(date);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * private void setBunga() {
     * 
     * }
     */

    public double round(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.FLOOR);

        Long i = (long) Math.ceil(bd.doubleValue());

        i = ((i + 999) / 1000) * 1000;
        // System.out.println(i);
        // System.out.println(i.doubleValue());

        return i.doubleValue();
        // return bd.doubleValue();
    }

    public String enkrip_pass_OLD(String pass) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(pass.getBytes("utf8"));
            String sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
            return sha1;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public String enkrip_pass(String pass) {
        try {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            System.out.println(pass);
            return bCryptPasswordEncoder.encode(pass).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public Integer daydiff(Date date1, Date date2) {
        long diff = date1.getTime() - date2.getTime();
        if (diff < 0) {
            diff = diff * (-1);
        }
        int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
        // System.out.println(diffDays);
        return diffDays;
    }

    public Integer daydiff2(Date date1, Date date2) {
        LocalDate d1 = LocalDate.parse(date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),
                DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate d2 = LocalDate.parse(date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),
                DateTimeFormatter.ISO_LOCAL_DATE);
        Duration diff = Duration.between(d1.atStartOfDay(), d2.atStartOfDay());
        long diffDays = diff.toDays();
        // System.out.println(diffDays);
        return (int) diffDays;
    }

    public String sendMailgun(JSONObject JObject, String RESTurl) throws Exception {

        StringBuilder result = new StringBuilder();
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        try {
            // Create data to send to server

            // Initialize and config request, then connect to server
            // String urlpath = "http://127.0.0.1:8081/"+RESTurl;
            String urlpath = "http://206.189.32.21:8081/" + RESTurl;
            URL url = new URL(urlpath);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true); // enable output (body data)
            urlConnection.setRequestProperty("Content-Type", "application/json"); // set header
            urlConnection.setRequestProperty("Authorization", "Bearer");
            urlConnection.connect();

            // Write data into server
            OutputStream outputStream = urlConnection.getOutputStream();
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(JObject.toString());
            bufferedWriter.flush();

            InputStream inputStream = urlConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line).append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        }
        return result.toString();
    }

}