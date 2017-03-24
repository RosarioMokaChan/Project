/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foodmadeeasy.connection;

import foodmadeeasy.generic.AdminUser;
import foodmadeeasy.generic.EncryptDecrypt;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
public class ConnectionHandlerAdmin{
    //url for connecting to the database
    private static final String URL = "jdbc:postgresql://localhost:5432/";
    private final String dbUsername = "postgres";
    private final String dbPassword = "postgres";
    
    //array to store all admins returned from sql call
    private ArrayList<AdminUser> admins;
    
    /**
     * Function to get an admin user from the database
     * @param user String - username to search for - to return all users in table, enter NULL
     * @return ArrayList containing AdminUser types - list of all returned values from table 
     */
    public ArrayList<AdminUser> getAdmin(String user){
        String sql;
        //if null is passed though, return all values in table
        //otherwise only return the values for item searched for
        if(user != null){
            sql = "SELECT * FROM public.\"Admins\" WHERE username='" + user + "'";
        }else{
            sql = "SELECT * FROM public.\"Admins\"";
        }
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
            //initialise list with size of returned rows
            admins = new ArrayList<>();
            //populate arraylist with returned values
            //(while the resultset has another value to read, continue)
            while (rs.next() != false) {
                admins.add(new AdminUser(rs.getString("username"), rs.getBytes("password"), rs.getBytes("key")));
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
        if(admins == null) admins = new ArrayList();//if null, don't return null, it's just polite
        //return list of admins
        return admins;
    }
    
    /**
     * Function to create an admin user in the database
     * @param username - String - chosen username for user
     * @param password - String - chosen password in plain text for the user
     * @return boolean true if created, false otherwise
     */
    public Boolean createAdmin(String username, String password){
        ArrayList<byte[]> encryptedPassword = EncryptDecrypt.encryptPassword(password);
        boolean flag = false;
        //sql doesn't really like putting byte arrays right into the string, have to do it this way
        String sql = "INSERT INTO public.\"Admins\" VALUES ('" + username + "',?,?)";
        //initialise connection
        Connection con = null;
        Statement st = null;
        //try and connect to database
        try{
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(URL, dbUsername, dbPassword);
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setBytes(1, encryptedPassword.get(0));
            pstmt.setBytes(2, encryptedPassword.get(1));
            pstmt.execute();
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
    
    /**
     * Function to edit an admin user in the database
     * @param oldUsername - String - original username for the user in question
     * @param admin - AdminUser - object for the new user details
     * @return boolean true if edited, false otherwise
     */
    public boolean editAdmin(String oldUsername, AdminUser admin){
        boolean flag = false;
        //since the primary key is the username (we don't want duplicate users), grab the old username as the identifier. simple
        String sql = "UPDATE public.\"Admins\" SET username='" + admin.getUsername() + "', password=?, key=? WHERE username = '" + oldUsername + "'";
        //initialise connection
        Connection con = null;
        Statement st = null;
        //try and connect to database
        try{
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(URL, dbUsername, dbPassword);
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setBytes(1, admin.getPassword());
            pstmt.setBytes(2, admin.getKey());
            pstmt.execute();
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
    
    /**
     * Function to delete an admin user from the database
     * "A man needs a name"
     * @param name - String - name of the user to delete
     * @return boolean true if deleted, false otherwise
     */
    public boolean deleteAdmin(String name){
        String sql = "DELETE FROM public.\"Admins\" WHERE username='" + name + "'";
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
