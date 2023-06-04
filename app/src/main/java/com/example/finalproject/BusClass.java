package com.example.finalproject;

import java.util.List;

public class BusClass {
    private String busNo;
    private String driverNo;
    private List<String> routeList;
    private boolean isApproved;

    public BusClass() {
        // Default constructor required for Firestore
    }


    public BusClass(String busNo, String driverNo, List<String> routeList, boolean isApproved) {
        this.busNo = busNo;
        this.driverNo = driverNo;
        this.routeList = routeList;
        this.isApproved = isApproved;
    }

    public String getBusNo() {
        return busNo;
    }

    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }

    public String getDriverNo() {
        return driverNo;
    }

    public void setDriverNo(String driverNo) {
        this.driverNo = driverNo;
    }

    public List<String> getRouteList() {
        return routeList;
    }

    public void setRouteList(List<String> routeList) {
        this.routeList = routeList;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

}
