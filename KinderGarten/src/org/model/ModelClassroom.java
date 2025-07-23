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
public class ModelClassroom {
     private int ID;
     private String  Name_room;
     private String namber_room;

    public ModelClassroom(int ID, String Name_room, String namber_room) {
        this.ID = ID;
        this.Name_room = Name_room;
        this.namber_room = namber_room;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName_room() {
        return Name_room;
    }

    public void setName_room(String Name_room) {
        this.Name_room = Name_room;
    }

    public String getNamber_room() {
        return namber_room;
    }

    public void setNamber_room(String namber_room) {
        this.namber_room = namber_room;
    }
}
