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
    public HashMap<String, String> mapping = new HashMap<String, String>();
    public static String keyString = "qwertyuiopasdfghjklzxcvbnm";
    
    public KeyboardSetup(){
        String keysLeft = keyString;
        for(int k = 0; k < keyString.length(); k++){
            int rI = (int)(Math.random()*keysLeft.length());
            mapping.put(keyString.substring(k, k+1), keysLeft.substring(rI, rI+1));
            if(keysLeft.length() > 1){
                keysLeft = keysLeft.substring(0, rI) + keysLeft.substring(rI + 1);
            }
        }
    }
    
    public String getAssociatedLetter(String origKey, boolean upper){
        String key = mapping.get(origKey.toLowerCase());
        if(upper)
            return key.toUpperCase();
        else
            return key.toLowerCase();
    }
    
    public int getLetterIndex(String origKey){
        return keyString.indexOf(origKey.toLowerCase());
    }
}
