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
public class AdminUser {
    
    private String username = "default";
    private byte[] password = "default".getBytes();
    private byte[] key = "25415".getBytes();
    
    public AdminUser(String username, byte[] password, byte[] key){
        this.username = username;
        this.password = password;
        this.key = key;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }
    
    
    
}
