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
public class KeyHandler implements KeyListener {
    private MainRandomizer instance;
    public KeyHandler(MainRandomizer instance){
        this.instance = instance;
    }
    
    
    public void keyPressed(KeyEvent e){
        this.instance.handleKeyPress(Character.toString(e.getKeyChar()));
        //System.out.println("UWUWUWUUWUWUWUWU");
        if(e.getKeyCode() == 10){
            this.instance.clearTextbox();
            System.out.println("Cleared");
        }else if(e.getKeyCode() == 8){
            this.instance.backspace();
            System.out.println("Backspace");
        }
    }
    
    public void keyReleased(KeyEvent e){
        this.instance.handleKeyStop(Character.toString(e.getKeyChar()));
    }
    
    public void keyTyped(KeyEvent e){
        // uwu
    }
    
}
