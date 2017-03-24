/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foodmadeeasy.generic;

/**
 *
 * @author chris
 */
public class Recipe {
    
    private int id = 0;
    private String name = "default";
    private String imageurl = "default";
    private int difficulty = 0;
    private String desc = "default";
    private String method = "default";
    private String type = "default";
    private int time = 0;
    
    //constructor without id-used for adding recipes
    public Recipe(String name, String url, int difficulty, String desc, String method, String type, int time){
        this.name = name;
        this.imageurl = url;
        this.difficulty = difficulty;
        this.desc = desc;
        this.time = time;
        this.method = method;
        this.type = type;
    }
    
    //constructor with id-used for getting recipes
    public Recipe(int id, String name, String url, int difficulty, String desc, String method, String type, int time){
        this.id = id;
        this.name = name;
        this.imageurl = url;
        this.difficulty = difficulty;
        this.desc = desc;
        this.time = time;
        this.method = method;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
    
}
