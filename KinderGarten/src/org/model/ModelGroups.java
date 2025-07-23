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
public class ModelGroups {
            private int ID;
            private String Name_groups;
            private String Year;

    public ModelGroups(int ID, String Name_groups, String Year) {
        this.ID = ID;
        this.Name_groups = Name_groups;
        this.Year = Year;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName_groups() {
        return Name_groups;
    }

    public void setName_groups(String Name_groups) {
        this.Name_groups = Name_groups;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String Year) {
        this.Year = Year;
    }



}