package com.addschedule.garnan.schedadd.Api.Clases;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by garnan on 05/11/2017.
 */

public class User {

    private int id;
    private String username,first_name,last_name,email;
    private int [] sons;

    public User(int id, String username, String first_name, String last_name, String email, int[] sons) {
        this.id = id;
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.sons = sons;
    }

    public  User() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int[] getSons() {
        return sons;
    }

    public void setSons(int[] sons) {
        this.sons = sons;
    }

    public static User Users(String json)
    {
        Gson gson = new Gson();
        Type type = new TypeToken<User>(){}.getType();
        return gson.fromJson(json,type);
    }
}
