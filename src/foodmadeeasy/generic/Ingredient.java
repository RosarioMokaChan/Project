/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foodmadeeasy.generic;

/**
 *
 * @author cmallinson
 */
public class Ingredient {
    
    private int id = 0;
    private String name = "default";
    
    public Ingredient(String name){
        this.name = name;
    }
    
    public Ingredient(int id, String name){
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
}
