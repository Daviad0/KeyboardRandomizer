/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.daviadoprojects.keyboardrandomizer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author daviado
 */

// this inherits from the SwingUI KeyListener to listen for a key
public class KeyHandler implements KeyListener {
    // be able to do things back in the window class
    private MainRandomizer instance;
    public KeyHandler(MainRandomizer instance){
        this.instance = instance;
    }
    
    // handle when a key is PRESSED
    public void keyPressed(KeyEvent e){
        // send event back
        this.instance.handleKeyPress(Character.toString(e.getKeyChar()));
        // special events like backspace and cleared
        if(e.getKeyCode() == 10){
            this.instance.clearTextbox();
            System.out.println("Cleared");
        }else if(e.getKeyCode() == 8){
            this.instance.backspace();
            System.out.println("Backspace");
        }
    }
    
    // handle when a key is released
    public void keyReleased(KeyEvent e){
        this.instance.handleKeyStop(Character.toString(e.getKeyChar()));
    }
    
    public void keyTyped(KeyEvent e){
        // required function for implementation
    }
    
}
