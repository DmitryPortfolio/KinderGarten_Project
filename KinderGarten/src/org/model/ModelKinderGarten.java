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
public class ModelKinderGarten {
    private int ID;
    private String Name;
    private String Adres;
     private String Telefon;
      private String Director;

    public ModelKinderGarten(int ID, String Name, String Adres, String Telefon, String Director) {
        this.ID = ID;
        this.Name = Name;
        this.Adres = Adres;
        this.Telefon = Telefon;
        this.Director = Director;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getAdres() {
        return Adres;
    }

    public void setAdres(String Adres) {
        this.Adres = Adres;
    }

    public String getTelefon() {
        return Telefon;
    }

    public void setTelefon(String Telefon) {
        this.Telefon = Telefon;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String Director) {
        this.Director = Director;
    }
      
      
}
