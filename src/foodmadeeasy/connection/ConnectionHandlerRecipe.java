/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foodmadeeasy.connection;

import foodmadeeasy.generic.Recipe;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author chris
 */
public class ConnectionHandlerRecipe {
    //url for connecting to the database
    private static final String URL = "jdbc:postgresql://localhost:5432/";
    private final String dbUsername = "postgres";
    private final String dbPassword = "postgres";
    
    private Recipe recipe;
    
    /**
     * Function to get every recipe from the database
     * @param id
     * @return 
     */
    public Recipe getRecipeDetails(int id){
        String sql = "SELECT * FROM public.\"Recipes\" WHERE recipe_id='" + id + "'";
        
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
            
            //populate recipe with returned values
            //(while the resultset has another value to read, continue)
            while (rs.next() != false) {
                recipe = new Recipe(id, rs.getString("recipe_name"), rs.getString("image_url"), rs.getInt("difficulty"), rs.getString("short_description"), rs.getString("method"), rs.getString("category"), rs.getInt("time"));
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
        return recipe;
    }
    
}
