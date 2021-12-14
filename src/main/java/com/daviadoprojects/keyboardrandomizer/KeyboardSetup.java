/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.daviadoprojects.keyboardrandomizer;
import java.util.*;
/**
 *
 * @author daviado
 */
public class KeyboardSetup {
    // allow for a orig:new key mapping
    public HashMap<String, String> mapping = new HashMap<String, String>();
    // all possible characters (typed out in qwerty L=>R T=>B format)
    public static String keyString = "qwertyuiopasdfghjklzxcvbnm";
    
    // constructor
    public KeyboardSetup(boolean regularKeyboard){
        String keysLeft = keyString;
        // check if its not a regular keyboard and should be randomized
        if(!regularKeyboard){
            // loop through each key that should be replaced
            for(int k = 0; k < keyString.length(); k++){
                int rI = (int)(Math.random()*keysLeft.length());
                // replace with new randomly generated key and add to mapping
                mapping.put(keyString.substring(k, k+1), keysLeft.substring(rI, rI+1));
                // remove new key from available keys list if allowed
                if(keysLeft.length() > 1){
                    keysLeft = keysLeft.substring(0, rI) + keysLeft.substring(rI + 1);
                }
            }
        }else{
            for(int k = 0; k < keyString.length(); k++){
                // put all of the regular qwerty keyboard letters in their place.
                mapping.put(keyString.substring(k, k+1), keysLeft.substring(k, k+1));
            }
        }
        
    }
    
    // checks what key is in what position (if given the original key on the keyboard)
    public String getAssociatedLetter(String origKey, boolean upper){
        // get the new key
        String key = mapping.get(origKey.toLowerCase());
        // return uppercase if it was requested
        if(upper)
            return key.toUpperCase();
        else
            return key.toLowerCase();
    }
    // get what index the original key was from, useful for highlighting keys
    public int getLetterIndex(String origKey){
        return keyString.indexOf(origKey.toLowerCase());
    }
    
    // get the original key from the new key, but cannot be done as easily as .get()
    public int getLetterIndexOpp(String newKey){
        // loop through each key and check if it is a match
        for(String key : mapping.keySet()){
            if(mapping.get(key).equalsIgnoreCase(newKey))
                return keyString.indexOf(key);
        }
        // item does not exist
        return -1;
    }
}
