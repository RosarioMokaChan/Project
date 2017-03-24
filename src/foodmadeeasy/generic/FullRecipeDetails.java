/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foodmadeeasy.generic;

import java.util.ArrayList;

/**
 *
 * @author chris
 */
public class FullRecipeDetails {
    
    private int recipeId = 0;
    private String recipeName = "default";
    private String imageurl = "default";
    private int difficulty = 0;
    private String description = "default";
    private int time = 0;
    private String method = "default";
    private String type = "default";
    
    private ArrayList<Ingredient> ingredients = new ArrayList();
    private ArrayList<RecipeIngredient> recipeIngredients;
    
    public FullRecipeDetails(){
        
    }
    
    public FullRecipeDetails(String name, String url, int difficulty, String desc, int time, String method, String type, ArrayList<Ingredient> ingredients, ArrayList<RecipeIngredient> recipeIngredients){
        this.recipeName = name;
        this.imageurl = url;
        this.difficulty = difficulty;
        this.description = desc;
        this.time = time;
        this.method = method;
        this.type = type;
        this.ingredients = ingredients;
        this.recipeIngredients = recipeIngredients;
    }
    
    public FullRecipeDetails(int id, String name, String url, int difficulty, String desc, int time, String method, String type, ArrayList<Ingredient> ingredients, ArrayList<RecipeIngredient> recipeIngredients){
        this.recipeId = id;
        this.recipeName = name;
        this.imageurl = url;
        this.difficulty = difficulty;
        this.description = desc;
        this.time = time;
        this.method = method;
        this.type = type;
        this.ingredients = ingredients;
        this.recipeIngredients = recipeIngredients;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
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

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<RecipeIngredient> getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(ArrayList<RecipeIngredient> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }
    
}
