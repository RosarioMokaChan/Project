/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foodmadeeasy.connection;

import foodmadeeasy.generic.Ingredient;
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
 * @author chris
 */
public class ConnectionHandlerIngredient {
    //url for connecting to the database
    private static final String URL = "jdbc:postgresql://localhost:5432/";
    private final String dbUsername = "postgres";
    private final String dbPassword = "postgres";
    
    private Ingredient ingredient;
    
    public Ingredient getIngredientDetails(int id){
        String sql = "SELECT * FROM public.\"Ingredients\" WHERE ingredient_id='" + id + "'";
        
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
            
            //populate ingredient with returned values
            //(while the resultset has another value to read, continue)
            while (rs.next() != false) {
                ingredient = new Ingredient(id, rs.getString("ingredient_name"));
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
        return ingredient;
    }
    
    public ArrayList<Ingredient> getAllIngredients(){
        ArrayList<Ingredient> ingredients = new ArrayList();
        String sql = "SELECT * FROM public.\"Ingredients\"";
        
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
            
            //populate ingredient with returned values
            //(while the resultset has another value to read, continue)
            while (rs.next() != false) {
                ingredients.add(new Ingredient(rs.getInt("ingredient_id"), rs.getString("ingredient_name")));
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
        return ingredients;
    }
    
    public boolean addIngredient(Ingredient ingredient){
        boolean flag = false;
        String sql = "INSERT INTO public.\"Ingredients\" (ingredient_name) VALUES "
                        + "('" + ingredient.getName() + "')";
        //initialise connection
        Connection con = null;
        Statement st = null;
        //try and connect to database
        try{
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(URL, dbUsername, dbPassword);
            st = con.createStatement();
            st.execute(sql);
            flag = true;
        }catch(SQLException | ClassNotFoundException ex){
            Logger lgr = Logger.getLogger(ConnectionHandlerAdmin.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            //finally, close connections
            try {
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
    
    public boolean editIngredient(Ingredient ingredient){
        boolean flag = false;
        String sql = "UPDATE public.\"Ingredients\" SET ingredient_name = '" + ingredient.getName() + "' " + "WHERE ingredient_id = '" + ingredient.getId() + "'";
        //initialise connection
        Connection con = null;
        Statement st = null;
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
    
    public boolean deleteIngredient(int id){
        String sql = "DELETE FROM public.\"Ingredients\" WHERE ingredient_id='" + id + "'";
        boolean flag = false;
        //initialise connection
        Connection con = null;
        Statement st = null;
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
