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
public class ModelAttendance {
    private int ID;
    private String Id_Children;
     private String Date;
      private String Status;

    public ModelAttendance(int ID, String Id_Children, String Date, String Status) {
        this.ID = ID;
        this.Id_Children = Id_Children;
        this.Date = Date;
        this.Status = Status;
    }

    public ModelAttendance() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getId_Children() {
        return Id_Children;
    }

    public void setId_Children(String Id_Children) {
        this.Id_Children = Id_Children;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }
 
}