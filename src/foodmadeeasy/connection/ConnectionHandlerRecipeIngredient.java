/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foodmadeeasy.connection;

import foodmadeeasy.generic.FullRecipeDetails;
import foodmadeeasy.generic.Ingredient;
import foodmadeeasy.generic.Recipe;
import foodmadeeasy.generic.RecipeIngredient;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cmallinson
 */
public class ConnectionHandlerRecipeIngredient {
    //url for connecting to the database
    private static final String URL = "jdbc:postgresql://localhost:5432/";
    private final String dbUsername = "postgres";
    private final String dbPassword = "postgres";
    
    private ArrayList<RecipeIngredient> recipeIngredients;
    
    public ArrayList<FullRecipeDetails> getAllRecipesIngredients(){
        //database table to draw data from
        String sql = "SELECT * FROM public.\"recipes_ingredients\"";
        
        
        //update method for getting all details of a recipe
        //database structure is..
        //recipeName - primary key (gets the recipe from the recipe table)
        //ingredientName
        //http://stackoverflow.com/questions/15847173/concatenate-multiple-result-rows-of-one-column-into-one-group-by-another-column
        //turn the mutliple rows from ingredient into one column in recipeIngredient,
        //try using website linked and string_agg()
        //try at home, might work
        //String sql2 = "SELECT recipeName, string_agg(ingredients, ', ') AS recipe_ingredients FROM public.\"RecipeIngredients\" GROUP BY recipeName";

        
        //initialise connection
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        //try and connect to database
        try{
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(URL, dbUsername, dbPassword);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            //initialise list
            recipeIngredients = new ArrayList<>();
            //boolean to see if a recipe already exists
            boolean added;
            //populate arraylist with returned values
            //(while the resultset has another value to read, continue)
            while (rs.next() != false) {
                added = false;
                //if there are no entries yet, add to the list
                if(recipeIngredients.isEmpty()){
                    recipeIngredients.add(new RecipeIngredient(rs.getInt("recipe_id"), rs.getInt("ingredient_id"), rs.getString("quantity")));
                }else{
                    //get details of recipe and linked ingredients from database
                    //loop through existing recipeIngredients entries
                    for(int i = 0; i < recipeIngredients.size(); i++){
                        //if the recipeId already exists, add the new ingredient to the existing recipe
                        if(recipeIngredients.get(i).getRecipeId() == rs.getInt("recipe_id")){
                            recipeIngredients.get(i).addIngredient(rs.getInt("ingredient_id"), rs.getString("quantity"));
                            added = true;
                            break;
                        }
                    }
                    //if the current recipe had an ingredient added to it, do not make another recipe entry
                    if(!added) recipeIngredients.add(new RecipeIngredient(rs.getInt("recipe_id"), rs.getInt("ingredient_id"), rs.getString("quantity")));
                }
            }
            
        }catch(SQLException | ClassNotFoundException ex){
            Logger lgr = Logger.getLogger(ConnectionHandlerAdmin.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            //finally, close connections
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(ConnectionHandlerAdmin.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        
        //connection handlers for getting details of recipes and ingredients
        ConnectionHandlerRecipe recipeConn = new ConnectionHandlerRecipe();
        ConnectionHandlerIngredient ingredientConn = new ConnectionHandlerIngredient();
        
        //temp store recipes and ingredients
        ArrayList<Recipe> recipes = new ArrayList();;
        ArrayList<Ingredient> ingredients;
        ArrayList<FullRecipeDetails> completeRecipes = new ArrayList();
        //try and get details of ingredients and recipes, 
        try{
            //loop through every recipe
            if(recipeIngredients != null){//if something went wrong with getting the recipes, return empty list
                for(int i = 0; i < recipeIngredients.size(); i++){
                    //get details of current recipe
                    recipes.add(recipeConn.getRecipeDetails(recipeIngredients.get(i).getRecipeId()));
                    ingredients = new ArrayList();
                    for(int j = 0; j < recipeIngredients.get(i).getIngredientIds().size(); j++){
                        //get details of every ingredient for current recipe
                        ingredients.add(ingredientConn.getIngredientDetails(recipeIngredients.get(i).getIngredientIds().get(j))); 
                    }
                    //store details of recipe and required ingredients together
                    completeRecipes.add(new FullRecipeDetails(recipes.get(i).getId(), recipes.get(i).getName(), 
                            recipes.get(i).getImageurl(), recipes.get(i).getDifficulty(), recipes.get(i).getDesc(), recipes.get(i).getTime(), 
                            recipes.get(i).getMethod(), recipes.get(i).getType(), ingredients, recipeIngredients)); 
                }
            }else{recipeIngredients = new ArrayList();}
        }catch(NullPointerException ex){
                Logger lgr = Logger.getLogger(ConnectionHandlerAdmin.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        //return every recipe with required ingredients
        return completeRecipes;
    }
    
    /**
     * Function to add a new recipe with linked ingredients to the database
     * @param recipe FullRecipeDetails object
     * @return Boolean flag, true if successful, false otherwise
     */
    public boolean addRecipe(FullRecipeDetails recipe){
        boolean flag = false;
        int recipeId = 0;
        ArrayList<Integer> ingredientIds = new ArrayList();
        //check to see if the recipe already exists
        String sql = "SELECT 1 FROM public.\"Recipes\" WHERE recipe_name = '" + recipe.getRecipeName() + "' limit 1";
        
        //initialise connection
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        //try and connect to database
        try{
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(URL, dbUsername, dbPassword);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            //checking to see if a recipe with that name already exists
            //if so then return saying false
            if(rs.next()) return flag;
            //if the recipe didnt already exist then insert into table
            sql = "INSERT INTO public.\"Recipes\" (recipe_name, image_url, difficulty, short_description, method, category, \"time\") VALUES "
                + "('" + recipe.getRecipeName() + "', '" + recipe.getImageurl() + "', '" + recipe.getDifficulty() + "', '" + recipe.getDescription() + "', '" + recipe.getMethod() + "', '" + recipe.getType() + "', '" + recipe.getTime() + "') "
                + "RETURNING recipe_id";
            rs = st.executeQuery(sql);
            while(rs.next() != false){
                //the recipe_id generated by inserting into the table
                //used when creating new entries in the recipes_ingredients table
                recipeId = rs.getInt("recipe_id");
            }
            
            //insert all ingredients for inserted recipe
            for(int i = 0; i < recipe.getIngredients().size(); i++){
                sql = "SELECT * FROM public.\"Ingredients\" WHERE ingredient_name = '" + recipe.getIngredients().get(i).getName() + "'";
                rs = st.executeQuery(sql);
                //if the ingredient already exists in the table, get its id and stores it,
                //otherwise insert into table and get id
                if(rs.next()){
                    ingredientIds.add(rs.getInt("ingredient_id"));
                }else{
                    //prepare to insert ingredients now
                    sql = "INSERT INTO public.\"Ingredients\" (ingredient_name) VALUES "
                        + "('" + recipe.getIngredients().get(i).getName() + "') "
                        + "RETURNING ingredient_id";
                    rs = st.executeQuery(sql);
                    while(rs.next() != false){
                        ingredientIds.add(rs.getInt("ingredient_id"));
                    }
                }
            }
            
            //insert all ingredients for inserted recipe
            for(int i = 0; i < recipe.getIngredients().size(); i++){
                //if the combination of recipeid and ingredient id already exists,
                //don't create a new row for it as it already exists
                sql = "SELECT * FROM public.\"recipes_ingredients\" WHERE recipe_id = '" + recipeId + "' AND ingredient_id = '" + ingredientIds.get(i) + "'";
                rs = st.executeQuery(sql);
                if(!rs.next()){
                    //prepare to insert ingredients now
                    sql = "INSERT INTO public.\"recipes_ingredients\" (recipe_id, ingredient_id, quantity) VALUES "
                        + "('" + recipeId + "', '" + ingredientIds.get(i) + "', '" + recipe.getRecipeIngredients().get(0).getQuantities().get(i) + "') "
                        + "RETURNING recipe_id";
                    rs = st.executeQuery(sql);
                }
            }
            flag = true;
        }catch(SQLException | ClassNotFoundException ex){
            Logger lgr = Logger.getLogger(ConnectionHandlerAdmin.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            //finally, close connections
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(ConnectionHandlerAdmin.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return flag;
    }
    
    
    /**
     * Function to edit a full recipe in the database
     * Pass through a full recipe and it will update all necessary tables
     * Ingredients no longer will not be deleted, but new ones will be added
     * @param recipe - FullRecipeDetails (with id) used to update the table
     * @return Boolean true if successful, false otherwise
     */
    public boolean editRecipe(FullRecipeDetails recipe){
        boolean flag = false;
        ArrayList<Integer> ingredientIds = new ArrayList();
        //update the values in the database with the values passed through when the id's are equal
        String sql = "UPDATE public.\"Recipes\" SET recipe_name = '" + recipe.getRecipeName() + "', image_url = '" + recipe.getImageurl() + "', "
                + "difficulty = '" + recipe.getDifficulty() + "', short_description = '" + recipe.getDescription() + "', method = '" + recipe.getMethod() + "', "
                + "category = '" + recipe.getType() + "', time = '" + recipe.getTime() + "' "
                + "WHERE recipe_id = '" + recipe.getRecipeId() + "'";
        
        //initialise connection
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        //try and connect to database
        try{
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(URL, dbUsername, dbPassword);
            st = con.createStatement();
            int up = st.executeUpdate(sql);
            
            //once the recipe row has been updated, time to update the other tables
            //with ingredients, we will keep existing ingredients in the table, but 
            //any new ingredients will be added
            
            //insert all ingredients for recipe
            for(int i = 0; i < recipe.getIngredients().size(); i++){
                sql = "SELECT * FROM public.\"Ingredients\" WHERE ingredient_name = '" + recipe.getIngredients().get(i).getName() + "'";
                rs = st.executeQuery(sql);
                //if the ingredient already exists in the table, get its id and stores it,
                //otherwise insert into table and get id
                if(rs.next()){
                    ingredientIds.add(rs.getInt("ingredient_id"));
                }else{
                    //prepare to insert ingredients now
                    sql = "INSERT INTO public.\"Ingredients\" (ingredient_name) VALUES "
                        + "('" + recipe.getIngredients().get(i).getName() + "') "
                        + "RETURNING ingredient_id";
                    rs = st.executeQuery(sql);
                    while(rs.next() != false){
                        ingredientIds.add(rs.getInt("ingredient_id"));
                    }
                }
            }
            
            //update linking table with new linked ingredients
            //the viable options I considered for this are:
            //-search for ingredients linked in the database but not in arraylist
            //-delete all linkings and just add new ingredients (I opted for this one)
            
            //therefore method is to delete all linked ingredients to this recipe,
            //then re-add them with the updated ingredient list. This means adding extra ingredients 
            //and removing ingredients are both handled in one go

            sql = "DELETE FROM public.\"recipes_ingredients\" WHERE recipe_id = '" + recipe.getRecipeId() + "'";
            System.out.println("id:"+recipe.getRecipeId());
            st.executeUpdate(sql);
            
            //insert all ingredients for inserted recipe
            for(int i = 0; i < recipe.getIngredients().size(); i++){
                //if the combination of recipeid and ingredient id already exists,
                //don't create a new row for it as it already exists
                sql = "SELECT * FROM public.\"recipes_ingredients\" WHERE recipe_id = '" + recipe.getRecipeId() + "' AND ingredient_id = '" + ingredientIds.get(i) + "'";
                rs = st.executeQuery(sql);
                if(!rs.next()){
                    //prepare to insert ingredients now
                    sql = "INSERT INTO public.\"recipes_ingredients\" (recipe_id, ingredient_id, quantity) VALUES "
                        + "('" + recipe.getRecipeId() + "', '" + ingredientIds.get(i) + "', '" + recipe.getRecipeIngredients().get(0).getQuantities().get(i) + "') "
                        + "RETURNING recipe_id";
                    rs = st.executeQuery(sql);
                }
            }
            flag = true;
        }catch(SQLException | ClassNotFoundException ex){
            Logger lgr = Logger.getLogger(ConnectionHandlerAdmin.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            //finally, close connections
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(ConnectionHandlerAdmin.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return flag;
    }
    
    
    public Boolean deleteRecipe(int id){
        String sql = "DELETE FROM public.\"Recipes\" WHERE recipe_id='" + id + "'";
        Boolean flag = false;
        //initialise connection
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        //try and connect to database
        try{
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(URL, dbUsername, dbPassword);
            st = con.createStatement();
            st.executeUpdate(sql);
            flag = true;
        }catch(SQLException | ClassNotFoundException ex){
            Logger lgr = Logger.getLogger(ConnectionHandlerAdmin.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            //finally, close connections
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(ConnectionHandlerAdmin.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return flag;
    }
    
    
}
