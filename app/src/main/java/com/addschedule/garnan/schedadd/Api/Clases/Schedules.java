package com.addschedule.garnan.schedadd.Api.Clases;

/**
 * Created by garnan on 05/11/2017.
 */

public class Schedules {

    private int id,sonId;
    private String name;
    private int [] activities;

    public Schedules(int id, int sonId, String name, int[] activities) {
        this.id = id;
        this.sonId = sonId;
        this.name = name;
        this.activities = activities;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSonId() {
        return sonId;
    }

    public void setSonId(int sonId) {
        this.sonId = sonId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getActivities() {
        return activities;
    }

    public void setActivities(int[] activities) {
        this.activities = activities;
    }
}
