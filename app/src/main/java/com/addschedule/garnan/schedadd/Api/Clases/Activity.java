package com.addschedule.garnan.schedadd.Api.Clases;

/**
 * Created by garnan on 06/11/2017.
 */

public class Activity {

    int id;
    String name;
    String descripcion;
    String estado;
    String steps;
    String image;
    String date;
    int duracion;
    int scheduleid;
    int parentid;

    public Activity(int id, String name, String descripcion, String estado, String steps, String image, String date, int duracion, int scheduleid, int parentid) {
        this.id = id;
        this.name = name;
        this.descripcion = descripcion;
        this.estado = estado;
        this.steps = steps;
        this.image = image;
        this.date = date;
        this.duracion = duracion;
        this.scheduleid = scheduleid;
        this.parentid = parentid;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public int getScheduleid() {
        return scheduleid;
    }

    public void setScheduleid(int scheduleid) {
        this.scheduleid = scheduleid;
    }

    public int getParentid() {
        return parentid;
    }

    public void setParentid(int parentid) {
        this.parentid = parentid;
    }
}
