package com.robert.maps.applib;

import org.andnav.osm.util.GeoPoint;

import java.io.Serializable;

/**
 * Created by Administrator on 2019/3/15.
 */

public class AlarmPoint implements Serializable{
    GeoPoint point;
    private int id;
    private String ids;
    private String imei;
    private String deviceName;
    private String alarmType;
    private String alarmName;
    private String alarmTime;
    private String address;
    private String readStatus;
    private String handleStatus;
    private String createTime;
    private String station;
    private String station_lat;
    private String station_lng;
    private String dis;

    public AlarmPoint() {
    }

    public AlarmPoint(GeoPoint point, int id, String imei, String deviceName, String alarmType, String alarmName, String alarmTime, String address, String readStatus, String handleStatus, String createTime, String station, String station_lat, String station_lng,String dis,String ids) {
        this.point = point;
        this.id = id;
        this.imei = imei;
        this.deviceName = deviceName;
        this.alarmType = alarmType;
        this.alarmName = alarmName;
        this.alarmTime = alarmTime;
        this.address = address;
        this.readStatus = readStatus;
        this.handleStatus = handleStatus;
        this.createTime = createTime;
        this.station = station;
        this.station_lat = station_lat;
        this.station_lng = station_lng;
        this.dis = dis;
        this.ids = ids;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }

    public String getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(String handleStatus) {
        this.handleStatus = handleStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getStation_lat() {
        return station_lat;
    }

    public void setStation_lat(String station_lat) {
        this.station_lat = station_lat;
    }

    public String getStation_lng() {
        return station_lng;
    }

    public void setStation_lng(String station_lng) {
        this.station_lng = station_lng;
    }

    public GeoPoint getPoint() {
        return point;
    }

    public void setPoint(GeoPoint point) {
        this.point = point;
    }

    public String getDis() {
        return dis;
    }

    public void setDis(String dis) {
        this.dis = dis;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }
}