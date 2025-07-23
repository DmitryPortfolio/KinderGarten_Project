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
public class ModelMenu {
      private int ID;
     private String Date;
      private String Type;
       private String Food;
        private String Number_grams;
     private String Callories;

    public ModelMenu(int ID, String Date, String Type, String Food, String Number_grams, String Callories) {
        this.ID = ID;
        this.Date = Date;
        this.Type = Type;
        this.Food = Food;
        this.Number_grams = Number_grams;
        this.Callories = Callories;
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

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getFood() {
        return Food;
    }

    public void setFood(String Food) {
        this.Food = Food;
    }

    public String getNumber_grams() {
        return Number_grams;
    }

    public void setNumber_grams(String Number_grams) {
        this.Number_grams = Number_grams;
    }

    public String getCallories() {
        return Callories;
    }

    public void setCallories(String Callories) {
        this.Callories = Callories;
    }

}   