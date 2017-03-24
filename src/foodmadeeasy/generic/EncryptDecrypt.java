/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foodmadeeasy.generic;

import java.security.SecureRandom;
import java.util.ArrayList;

/**
 *
 * @author chris
 */
public class EncryptDecrypt {
    
    /**
     * Function to encrypt a plaintext password using one time pad encryption
     * A key is randomly generated and bitwise XOR is used to apply encryption
     * @param plainPassword String - password to be encrypted
     * @return ArrayList - contains byte array for encrypted password and key
     */
    public static ArrayList<byte[]> encryptPassword(String plainPassword){
        //convert string password to byte array
        final byte[] input = plainPassword.getBytes();
        //byte array to hold result of algorithm
        final byte[] output = new byte[input.length];
        //create random key with same length as password
        final byte[] key = new byte[input.length];
        new SecureRandom().nextBytes(key);
        //perform bitwise XOR on each byte in array
        for(int i = 0; i < input.length; i++){
            output[i] = (byte) (input[i] ^ key[i]);
        }
        //create list of password and corresponding key (same key is required to
        //decrypt the data)
        ArrayList<byte[]> passKey = new ArrayList();
        passKey.add(output);
        passKey.add(key);
        return passKey;
    }
    
    /**
     * Function to decrypt encrypted password using a key. Key provided must
     * be the same key that was returned when encrypting the password
     * @param input byte array - encrypted password
     * @param key byte array - key returned when encrypting the password
     * @return String of decrypted password
     */
    public static String decryptPassword(byte[] input, byte[] key){
        //create byte array to store output of decryption
        final byte[] output = new byte[input.length];
        //perform bitwise XOR on each byte in array
        for(int i = 0; i < input.length; i++){
            output[i] = (byte) (input[i] ^ key[i]);
        }
        //convert byte array back into a string
        String plainPassword = new String(output);
        return plainPassword;
    }
}
