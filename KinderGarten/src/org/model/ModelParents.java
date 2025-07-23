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
public class ModelParents {
  private int ID;
  private String FIO;
  private String  Telefon;
  private String  Ardres;
  private String  Passport;
  private String  Id_Children;

    public ModelParents(int ID, String FIO, String Telefon, String Ardres, String Passport, String Id_Children) {
        this.ID = ID;
        this.FIO = FIO;
        this.Telefon = Telefon;
        this.Ardres = Ardres;
        this.Passport = Passport;
        this.Id_Children = Id_Children;
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

    public String getArdres() {
        return Ardres;
    }

    public void setArdres(String Ardres) {
        this.Ardres = Ardres;
    }

    public String getPassport() {
        return Passport;
    }

    public void setPassport(String Passport) {
        this.Passport = Passport;
    }

    public String getId_Children() {
        return Id_Children;
    }

    public void setId_Children(String Id_Children) {
        this.Id_Children = Id_Children;
    }
}
