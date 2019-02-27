package com.example.app.bloodbank;

/**
 * Created by AkshayeJH on 24/07/17.
 */

public class Messages {

    private String message, type,lat,log;
    private long  time;
    private boolean seen;

    private String from;
    private String gps;

    public Messages(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Messages(String message,String gps,String lat,String log, String type, long time, boolean seen) {
        this.lat =  lat;
        this.log = log ;
        this.message = message;
        this.type = type;
        this.time = time;
        this.seen = seen;
        this.gps = gps;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public Messages(){

    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }
}
