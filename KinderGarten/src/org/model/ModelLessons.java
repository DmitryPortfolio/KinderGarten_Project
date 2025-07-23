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
public class ModelLessons {
     private int ID;
    private String Name_Lessons;

    public ModelLessons(int ID, String Name_Lessons) {
        this.ID = ID;
        this.Name_Lessons = Name_Lessons;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName_Lessons() {
        return Name_Lessons;
    }

    public void setName_Lessons(String Name_Lessons) {
        this.Name_Lessons = Name_Lessons;
    }
    
}
