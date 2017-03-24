/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foodmadeeasy.generic;

import java.util.ArrayList;

/**
 *
 * @author cmallinson
 */
public class RecipeIngredient {
    
    private int recipeId = 0;
    private String recipeName = "default";
    private ArrayList<Integer> ingredientIds = new ArrayList();
    private ArrayList<String> quantities = new ArrayList();
    private ArrayList<String> ingredientNames = new ArrayList();
    
    public RecipeIngredient(String recipeName){
        this.recipeName = recipeName;
    }
    
    public RecipeIngredient(String recipeName, String ingredientName, String quantity){
        this.recipeName = recipeName;
        this.ingredientNames.add(ingredientName);
        this.quantities.add(quantity);
    }
    
    public RecipeIngredient(int recipeId, int ingredientId, String quantity){
        this.recipeId = recipeId;
        this.ingredientIds.add(ingredientId);
        this.quantities.add(quantity);
    }
    
    //add an ingredient to the list of ingredients for the recipe
    public void addIngredient(String ingredientName, String quantity){
        this.ingredientNames.add(ingredientName);
        this.quantities.add(quantity);
    }
    
    //add an ingredient id to the list of ingredients for the recipe
    public void addIngredient(int ingredientId, String quantity){
        this.ingredientIds.add(ingredientId);
        this.quantities.add(quantity);
    }

    //getters and setters

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public ArrayList<Integer> getIngredientIds() {
        return ingredientIds;
    }

    public void setIngredientIds(ArrayList<Integer> ingredientIds) {
        this.ingredientIds = ingredientIds;
    }

    public ArrayList<String> getQuantities() {
        return quantities;
    }

    public void setQuantities(ArrayList<String> quantities) {
        this.quantities = quantities;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public ArrayList<String> getIngredientNames() {
        return ingredientNames;
    }

    public void setIngredientNames(ArrayList<String> ingredientNames) {
        this.ingredientNames = ingredientNames;
    }
    
}
