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
public class SearchForMatchingRecipes {
    
    /**
     * Function to return recipes which can be made with the input ingredients -2 ingredients
     * For example, if 10 ingredients are entered and a recipe uses 3 of them + two
     * that have not been entered, then the user only requires 2 extra ingredients
     * (values gained from initial investigations)
     * @param ingredientList ArraryList of ingredients entered by the user
     * @param recipes ArrayList of recipes - contains every recipe from the database
     * @param exact Boolean, search for exact matches if true, otherwise return with close matches
     * @return filteredRecipes - ArrayList of recipes with which most of their ingredients are in ingredientList
     */
    public static ArrayList<FullRecipeDetails> searchViaIngredients(ArrayList<Ingredient> ingredientList, ArrayList<FullRecipeDetails> recipes, boolean exact){
        ArrayList<FullRecipeDetails> filteredRecipes = new ArrayList();
        //loop through every recipe
        for(int i = 0; i < recipes.size(); i++){
            int matching = 0;
            //for every ingredient to search for, check if each recipe contians it
            for(int j = 0; j < ingredientList.size(); j++){
                //for every ingredient in each recipe, check if it is what is being searched for
                for(int k = 0; k < recipes.get(i).getIngredients().size(); k++){
                    if(ingredientList.get(j).getName().equalsIgnoreCase(recipes.get(i).getIngredients().get(k).getName())){
                        matching++;
                    }
                }
            }
            //if the recipe contains some of the ingredients entered then it is
            //suitable to be returned.
            //e.g. if 2/3 required ingredients are entered then it is suitable, 
            //1/3 is not
            //(between a specified threshold)
            if(!exact){
                if(matching <= recipes.get(i).getIngredients().size() && matching >= recipes.get(i).getIngredients().size()-2){
                    filteredRecipes.add(recipes.get(i));
                }
            }else{//otherwise if exact matches are required, only add recipes which contain the exact ingredients listed
                if(matching == recipes.get(i).getIngredients().size()){
                    filteredRecipes.add(recipes.get(i));
                }
            }
        }
        return filteredRecipes;
    }
    
}
