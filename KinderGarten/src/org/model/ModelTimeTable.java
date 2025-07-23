/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.model;

import java.util.Date;

/**
 *
 * @author Пользователь
 */
public class ModelTimeTable {
     private int ID;
    private String Date;
    private String Time;
    private String Id_Lessons;
    private String Id_Group;
    private String Id_Classroom;
    private String Id_Teacher;

    public ModelTimeTable(int ID, String Date, String Time, String Id_Lessons, String Id_Group, String Id_Classroom, String Id_Teacher) {
        this.ID = ID;
        this.Date = Date;
        this.Time = Time;
        this.Id_Lessons = Id_Lessons;
        this.Id_Group = Id_Group;
        this.Id_Classroom = Id_Classroom;
        this.Id_Teacher = Id_Teacher;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String Time) {
        this.Time = Time;
    }

    public String getId_Lessons() {
        return Id_Lessons;
    }

    public void setId_Lessons(String Id_Lessons) {
        this.Id_Lessons = Id_Lessons;
    }

    public String getId_Group() {
        return Id_Group;
    }

    public void setId_Group(String Id_Group) {
        this.Id_Group = Id_Group;
    }

    public String getId_Classroom() {
        return Id_Classroom;
    }

    public void setId_Classroom(String Id_Classroom) {
        this.Id_Classroom = Id_Classroom;
    }

    public String getId_Teacher() {
        return Id_Teacher;
    }

    public void setId_Teacher(String Id_Teacher) {
        this.Id_Teacher = Id_Teacher;
    }

}