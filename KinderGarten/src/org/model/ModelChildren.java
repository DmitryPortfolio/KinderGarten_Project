/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.model;

import java.util.Date;

public class ModelChildren {

    private int ID;
    private String FIO;
    private String Birthday;
    private String Date_receipt;
    private String Date_departure;
    private String Id_Groups;
    private String BirthdayCertificate;
    private String retirement;
    private String Status;

    public ModelChildren(int ID, String FIO, String Birthday, String Date_receipt, String Date_departure, String Id_Groups, String BirthdayCertificate, String retirement, String Status) {
        this.ID = ID;
        this.FIO = FIO;
        this.Birthday = Birthday;
        this.Date_receipt = Date_receipt;
        this.Date_departure = Date_departure;
        this.Id_Groups = Id_Groups;
        this.BirthdayCertificate = BirthdayCertificate;
        this.retirement = retirement;
        this.Status = Status;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFIO() {
        return FIO;
    }

    public void setFIO(String FIO) {
        this.FIO = FIO;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String Birthday) {
        this.Birthday = Birthday;
    }

    public String getDate_receipt() {
        return Date_receipt;
    }

    public void setDate_receipt(String Date_receipt) {
        this.Date_receipt = Date_receipt;
    }

    public String getDate_departure() {
        return Date_departure;
    }

    public void setDate_departure(String Date_departure) {
        this.Date_departure = Date_departure;
    }

    public String getId_Groups() {
        return Id_Groups;
    }

    public void setId_Groups(String Id_Groups) {
        this.Id_Groups = Id_Groups;
    }

    public String getBirthdayCertificate() {
        return BirthdayCertificate;
    }

    public void setBirthdayCertificate(String BirthdayCertificate) {
        this.BirthdayCertificate = BirthdayCertificate;
    }

    public String getRetirement() {
        return retirement;
    }

    public void setRetirement(String retirement) {
        this.retirement = retirement;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    
}