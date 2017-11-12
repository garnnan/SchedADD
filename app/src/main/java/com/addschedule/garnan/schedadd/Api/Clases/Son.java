package com.addschedule.garnan.schedadd.Api.Clases;

/**
 * Created by garnan on 11/11/2017.
 */

public class Son {

    private int id;
    private String name,lastName,bithday,gender,code,cellphone;
    private int parentId;
    private int schedules [];

    public Son(int id, String name, String lastName, String bithday, String gender, String code, String cellphone, int parentId, int[] schedules) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.bithday = bithday;
        this.gender = gender;
        this.code = code;
        this.cellphone = cellphone;
        this.parentId = parentId;
        this.schedules = schedules;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBithday() {
        return bithday;
    }

    public void setBithday(String bithday) {
        this.bithday = bithday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int[] getSchedules() {
        return schedules;
    }

    public void setSchedules(int[] schedules) {
        this.schedules = schedules;
    }
}
