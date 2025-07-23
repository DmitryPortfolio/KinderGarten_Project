/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.model;

/**
 *
 * @author Пользователь
 */
public class ModelTeacher {
    private int ID;
    private String FIO;
     private String Telefon; 
      private String Adres; 
      private String Specialization; 

    public ModelTeacher(int ID, String FIO, String Telefon, String Adres, String Specialization) {
        this.ID = ID;
        this.FIO = FIO;
        this.Telefon = Telefon;
        this.Adres = Adres;
        this.Specialization = Specialization;
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

    public String getTelefon() {
        return Telefon;
    }

    public void setTelefon(String Telefon) {
        this.Telefon = Telefon;
    }

    public String getAdres() {
        return Adres;
    }

    public void setAdres(String Adres) {
        this.Adres = Adres;
    }

    public String getSpecialization() {
        return Specialization;
    }

    public void setSpecialization(String Specialization) {
        this.Specialization = Specialization;
    }
}      