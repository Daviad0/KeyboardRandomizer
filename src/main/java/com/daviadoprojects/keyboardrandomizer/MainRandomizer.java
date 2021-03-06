/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.daviadoprojects.keyboardrandomizer;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JButton;
import java.time.*;
import java.util.ArrayList;
import javax.swing.Timer;

/**
 *
 * @author daviado
 */
public class MainRandomizer extends javax.swing.JFrame {

    /**
     * Creates new form MainRandomizer
     */
    
    // define all of the important variables for the task, including created classes so everything can work smoothly
    private ArrayList<Double> timeSplits = new ArrayList<Double>();
    private HashMap<Integer, JButton> objMapping = new HashMap<Integer,JButton>();
    public KeyboardPhase currentPhase;
    public KeyHandler thisKeyHandler;
    private LocalDateTime wordStartTime;
    private LocalDateTime keyboardStartTime;
    private double totalSeconds;
    private MainRandomizer instance;
    
    // keep track of the confidence NOW and the first confidence
    private double baselineConfidence;
    private double currentConfidence;
    
    // add all of the buttons to link a number (index basically but at 1) to the button so we can highlight w/o if statements
    private void initializeHashmap(){
        
        // since it is only a pointer, we can put it in this dictionary to get an item by index instead of using if statements
        objMapping.put(1, pos1);
        objMapping.put(2, pos2);
        objMapping.put(3, pos3);
        objMapping.put(4, pos4);
        objMapping.put(5, pos5);
        objMapping.put(6, pos6);
        objMapping.put(7, pos7);
        objMapping.put(8, pos8);
        objMapping.put(9, pos9);
        objMapping.put(10, pos10);
        objMapping.put(11, pos11);
        objMapping.put(12, pos12);
        objMapping.put(13, pos13);
        objMapping.put(14, pos14);
        objMapping.put(15, pos15);
        objMapping.put(16, pos16);
        objMapping.put(17, pos17);
        objMapping.put(18, pos18);
        objMapping.put(19, pos19);
        objMapping.put(20, pos20);
        objMapping.put(21, pos21);
        objMapping.put(22, pos22);
        objMapping.put(23, pos23);
        objMapping.put(24, pos24);
        objMapping.put(25, pos25);
        objMapping.put(26, pos26);
        
        
        // and then set the colors
        
        for(int i = 1; i <= 26; i++){
            JButton obj = objMapping.get(i);
            obj.setBackground(Color.GRAY);
            
            obj.addKeyListener(thisKeyHandler);
            obj.setFocusable(true);
        }
        
        
        // set original background color
        textBox.setBackground(Color.BLUE);
        
    }
    
    public KeyboardSetup k;
    public MainRandomizer() {
        // initialize every component and variable necessary along with the baseline default keyboard
        instance = this;
        initComponents();
        initializeHashmap();
        currentPhase = new KeyboardPhase("initial", new KeyboardSetup(true), 300);
        // set all of the buttons on the screen to the corresponding letters that they represent in the phase
        pos1.setText(currentPhase.currentSetup.getAssociatedLetter("q", false).toUpperCase());
        pos2.setText(currentPhase.currentSetup.getAssociatedLetter("w", false).toUpperCase());
        pos3.setText(currentPhase.currentSetup.getAssociatedLetter("e", false).toUpperCase());
        pos4.setText(currentPhase.currentSetup.getAssociatedLetter("r", false).toUpperCase());
        pos5.setText(currentPhase.currentSetup.getAssociatedLetter("t", false).toUpperCase());
        pos6.setText(currentPhase.currentSetup.getAssociatedLetter("y", false).toUpperCase());
        pos7.setText(currentPhase.currentSetup.getAssociatedLetter("u", false).toUpperCase());
        pos8.setText(currentPhase.currentSetup.getAssociatedLetter("i", false).toUpperCase());
        pos9.setText(currentPhase.currentSetup.getAssociatedLetter("o", false).toUpperCase());
        pos10.setText(currentPhase.currentSetup.getAssociatedLetter("p", false).toUpperCase());
        pos11.setText(currentPhase.currentSetup.getAssociatedLetter("a", false).toUpperCase());
        pos12.setText(currentPhase.currentSetup.getAssociatedLetter("s", false).toUpperCase());
        pos13.setText(currentPhase.currentSetup.getAssociatedLetter("d", false).toUpperCase());
        pos14.setText(currentPhase.currentSetup.getAssociatedLetter("f", false).toUpperCase());
        pos15.setText(currentPhase.currentSetup.getAssociatedLetter("g", false).toUpperCase());
        pos16.setText(currentPhase.currentSetup.getAssociatedLetter("h", false).toUpperCase());
        pos17.setText(currentPhase.currentSetup.getAssociatedLetter("j", false).toUpperCase());
        pos18.setText(currentPhase.currentSetup.getAssociatedLetter("k", false).toUpperCase());
        pos19.setText(currentPhase.currentSetup.getAssociatedLetter("l", false).toUpperCase());
        pos20.setText(currentPhase.currentSetup.getAssociatedLetter("z", false).toUpperCase());
        pos21.setText(currentPhase.currentSetup.getAssociatedLetter("x", false).toUpperCase());
        pos22.setText(currentPhase.currentSetup.getAssociatedLetter("c", false).toUpperCase());
        pos23.setText(currentPhase.currentSetup.getAssociatedLetter("v", false).toUpperCase());
        pos24.setText(currentPhase.currentSetup.getAssociatedLetter("b", false).toUpperCase());
        pos25.setText(currentPhase.currentSetup.getAssociatedLetter("n", false).toUpperCase());
        pos26.setText(currentPhase.currentSetup.getAssociatedLetter("m", false).toUpperCase());
        // adding an event listener for keys
        textBox.addKeyListener(thisKeyHandler);
        confidence.setVisible(false);
        textBox.setFocusable(true);
        keyboardStartTime = LocalDateTime.now();
        // updating the instructional elements to give the next prompt
        updateInstructions();
        
        // every 1/20 of a second, set the timer and confidence elements 
        new Timer(50, new ActionListener(){
            public void actionPerformed(ActionEvent e){
                Duration d = Duration.between(wordStartTime, LocalDateTime.now());
                timer.setText(String.valueOf(d.toMinutesPart()) + ":" + addPadding(String.valueOf(d.toSecondsPart()), 2));
                d = Duration.between(keyboardStartTime, LocalDateTime.now());
                totalTimer.setText(String.valueOf(d.toMinutesPart()) + ":" + addPadding(String.valueOf(d.toSecondsPart()), 2));
                recalcConfidence();
            }
        }).start();
        
        // try to refocus window and keep the keylistener on every second in case of clickoff
        new Timer(1000, new ActionListener(){
            public void actionPerformed(ActionEvent e){
                instance.requestFocus();
            }
        }).start();
        
    }
    
    private void recalcConfidence(){
        // recalculate the running confidence based on the most recent set of data (60% weight) and the total average data (40% weight)
        int numberToUse = Math.min(timeSplits.size(), 60);
        currentConfidence = ((sumOfList(timeSplits,60) / numberToUse)*.6) + ((totalSeconds / currentPhase.numCompleted)*.4);
    }
    
    private double sumOfList(ArrayList l, int maxSize){
        // dynamic function to get the sum of any ArrayList passed in (and getting the max size of it as well)
        double sum = 0;
        for(int i = 0; i < Math.min(l.size(), maxSize); i++){
            sum += (double)l.get(i);
        }
        return sum;
    }
    
    // adding 0s to the beginning of the string to make sure it is formatted like a time
    private String addPadding(String orig, int totalLength){
        String finalStr = orig;
        for(int i = orig.length(); i < totalLength; i++){
            finalStr = "0"+finalStr;
        }
        return finalStr;
    }
    
    // handle clear event
    public void clearTextbox(){
        textBox.setText("");
        checkIfCorrect(textBox.getText());
    }
    
    // handle backspace event
    public void backspace(){
        if(textBox.getText().length() > 0){
            textBox.setText(textBox.getText().substring(0,textBox.getText().length()-1));
            checkIfCorrect(textBox.getText());
        }
    }
   
    // handle when a key is pressed
    public void handleKeyPress(String letter){
        int keyToUse = currentPhase.currentSetup.getLetterIndex(letter)+1;
        // is key a valid one to use? If so, then highlight
        if(keyToUse > 0){
            JButton button = objMapping.get(keyToUse);
            button.setBackground(Color.CYAN);
            
        }
        
    }
    
    // handle when a key is released
    public void handleKeyStop(String letter){
        int keyToUse = currentPhase.currentSetup.getLetterIndex(letter)+1;
        // is key a valid key? then unhighlight and add letter to textbox
        if(keyToUse > 0){
            JButton button = objMapping.get(keyToUse);
            button.setBackground(Color.GRAY);
            
            String newLetter = currentPhase.currentSetup.getAssociatedLetter(letter, letter.toUpperCase().equals(letter));
            textBox.setText(textBox.getText() + newLetter);
            // check if the word is done
            checkIfCorrect(textBox.getText());
            
        }
        
    }
    
    // checks if the current word is correct
    public void checkIfCorrect(String toCheck){
        // checking if the word equals the correct one, meaning the next one can be shown
        if(toCheck.toLowerCase().equals(currentPhase.currentTask.correctAnswer.toLowerCase())){
            
            // adding the time to the total time so far (weighted based on length)
            LocalDateTime nowTime = LocalDateTime.now();
            Duration d = Duration.between(wordStartTime, nowTime);
            timeSplits.add(((double)d.toMillis() / 1000)/ ((currentPhase.currentTask.correctAnswer.length() / 3) + 1));
            totalSeconds += ((double)d.toMillis() / 1000)/ ((currentPhase.currentTask.correctAnswer.length() / 3) + 1);
            // make sure that most recent ones are kept track of
            if(timeSplits.size() > 60){
                timeSplits.remove(0);
                // remove first item every time
            }
    
            //System.out.println("Current confidence is: " + currentConfidence + ", Baseline is: " + baselineConfidence + " (" + (baselineConfidence - currentConfidence) + ")");
            // set confidence, correct, textbox, and complete task
            confidence.setText(String.valueOf((int)((baselineConfidence - currentConfidence)*100)));
            textBox.setBackground(Color.GREEN);
            textBox.setText("");
            currentPhase.completeTask();
            
            numCorrect.setText(String.valueOf((int)currentPhase.currentScore));
            wordStartTime = LocalDateTime.now();
            
            // reupdate the instructions and check if the phase is over
            updateInstructions();
            checkPhaseStatus();
        }else{
            textBox.setBackground(Color.BLUE);
        }
    }
    
    public void checkPhaseStatus(){
        // checks to see if the user has surpassed the required score and required confidence to pass
        if(currentPhase.requiredScore <= currentPhase.currentScore && (int)((baselineConfidence - currentConfidence)*100) >= -100){
            // checking if should store in baseline
            if(currentPhase.name.equals("initial")){
                baselineConfidence = currentConfidence;
                confidence.setVisible(true);
            }
            
            // creating a new phase
            currentConfidence = 0;
            currentPhase = new KeyboardPhase("new", new KeyboardSetup(false), 500);
            // resetting button contents
            pos1.setText(currentPhase.currentSetup.getAssociatedLetter("q", false).toUpperCase());
            pos2.setText(currentPhase.currentSetup.getAssociatedLetter("w", false).toUpperCase());
            pos3.setText(currentPhase.currentSetup.getAssociatedLetter("e", false).toUpperCase());
            pos4.setText(currentPhase.currentSetup.getAssociatedLetter("r", false).toUpperCase());
            pos5.setText(currentPhase.currentSetup.getAssociatedLetter("t", false).toUpperCase());
            pos6.setText(currentPhase.currentSetup.getAssociatedLetter("y", false).toUpperCase());
            pos7.setText(currentPhase.currentSetup.getAssociatedLetter("u", false).toUpperCase());
            pos8.setText(currentPhase.currentSetup.getAssociatedLetter("i", false).toUpperCase());
            pos9.setText(currentPhase.currentSetup.getAssociatedLetter("o", false).toUpperCase());
            pos10.setText(currentPhase.currentSetup.getAssociatedLetter("p", false).toUpperCase());
            pos11.setText(currentPhase.currentSetup.getAssociatedLetter("a", false).toUpperCase());
            pos12.setText(currentPhase.currentSetup.getAssociatedLetter("s", false).toUpperCase());
            pos13.setText(currentPhase.currentSetup.getAssociatedLetter("d", false).toUpperCase());
            pos14.setText(currentPhase.currentSetup.getAssociatedLetter("f", false).toUpperCase());
            pos15.setText(currentPhase.currentSetup.getAssociatedLetter("g", false).toUpperCase());
            pos16.setText(currentPhase.currentSetup.getAssociatedLetter("h", false).toUpperCase());
            pos17.setText(currentPhase.currentSetup.getAssociatedLetter("j", false).toUpperCase());
            pos18.setText(currentPhase.currentSetup.getAssociatedLetter("k", false).toUpperCase());
            pos19.setText(currentPhase.currentSetup.getAssociatedLetter("l", false).toUpperCase());
            pos20.setText(currentPhase.currentSetup.getAssociatedLetter("z", false).toUpperCase());
            pos21.setText(currentPhase.currentSetup.getAssociatedLetter("x", false).toUpperCase());
            pos22.setText(currentPhase.currentSetup.getAssociatedLetter("c", false).toUpperCase());
            pos23.setText(currentPhase.currentSetup.getAssociatedLetter("v", false).toUpperCase());
            pos24.setText(currentPhase.currentSetup.getAssociatedLetter("b", false).toUpperCase());
            pos25.setText(currentPhase.currentSetup.getAssociatedLetter("n", false).toUpperCase());
            pos26.setText(currentPhase.currentSetup.getAssociatedLetter("m", false).toUpperCase());
            // resetting everything else for a new phase
            textBox.addKeyListener(thisKeyHandler);
            textBox.setFocusable(true);
            keyboardStartTime = LocalDateTime.now();
            updateInstructions();
            timeSplits = new ArrayList<Double>();
            totalSeconds = 0;
            
        }
    }
    
    public void updateInstructions(){
        // updates important elements
        wordStartTime = LocalDateTime.now();
        instructions.setText(currentPhase.currentTask.instructions);
        requiredWord.setText(currentPhase.currentTask.correctAnswer);
        // set each of the buttons to not highlighted after a word
        for(int i = 1; i <= 26; i++){
            JButton btnToRemove = objMapping.get(i);
            btnToRemove.setBackground(Color.GRAY);
        }
        // reset each of the required words to be highlighted
        for(String lU : currentPhase.currentTask.lightUp){
            int index = currentPhase.currentSetup.getLetterIndexOpp(lU)+1;
            if(index > 0){
                System.out.println("Letter: " + lU + ", Index: " + index);
                JButton button = objMapping.get(index);
                if(button != null)
                    button.setBackground(Color.YELLOW);
            }
            
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pos1 = new javax.swing.JButton();
        pos2 = new javax.swing.JButton();
        pos3 = new javax.swing.JButton();
        pos4 = new javax.swing.JButton();
        pos5 = new javax.swing.JButton();
        pos6 = new javax.swing.JButton();
        pos7 = new javax.swing.JButton();
        pos8 = new javax.swing.JButton();
        pos9 = new javax.swing.JButton();
        pos10 = new javax.swing.JButton();
        pos11 = new javax.swing.JButton();
        pos12 = new javax.swing.JButton();
        pos13 = new javax.swing.JButton();
        pos14 = new javax.swing.JButton();
        pos15 = new javax.swing.JButton();
        pos16 = new javax.swing.JButton();
        pos17 = new javax.swing.JButton();
        pos18 = new javax.swing.JButton();
        pos19 = new javax.swing.JButton();
        pos20 = new javax.swing.JButton();
        pos21 = new javax.swing.JButton();
        pos22 = new javax.swing.JButton();
        pos23 = new javax.swing.JButton();
        pos24 = new javax.swing.JButton();
        pos25 = new javax.swing.JButton();
        pos26 = new javax.swing.JButton();
        textBox = new javax.swing.JTextField();
        requiredWord = new javax.swing.JLabel();
        instructions = new javax.swing.JLabel();
        numCorrect = new javax.swing.JLabel();
        timer = new javax.swing.JLabel();
        totalTimer = new javax.swing.JLabel();
        confidence = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pos1.setBackground(new java.awt.Color(102, 102, 102));
        pos1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        pos1.setText("q");
        pos1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pos1.setPreferredSize(new java.awt.Dimension(35, 40));
        pos1.setSize(new java.awt.Dimension(80, 30));
        pos1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pos1ActionPerformed(evt);
            }
        });

        pos2.setBackground(new java.awt.Color(102, 102, 102));
        pos2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        pos2.setText("w");
        pos2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pos2.setPreferredSize(new java.awt.Dimension(35, 40));
        pos2.setSize(new java.awt.Dimension(80, 30));
        pos2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pos2ActionPerformed(evt);
            }
        });

        pos3.setBackground(new java.awt.Color(102, 102, 102));
        pos3.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        pos3.setText("e");
        pos3.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pos3.setPreferredSize(new java.awt.Dimension(35, 40));
        pos3.setSize(new java.awt.Dimension(80, 30));

        pos4.setBackground(new java.awt.Color(102, 102, 102));
        pos4.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        pos4.setText("r");
        pos4.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pos4.setPreferredSize(new java.awt.Dimension(35, 40));
        pos4.setSize(new java.awt.Dimension(80, 30));

        pos5.setBackground(new java.awt.Color(102, 102, 102));
        pos5.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        pos5.setText("t");
        pos5.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pos5.setPreferredSize(new java.awt.Dimension(35, 40));
        pos5.setSize(new java.awt.Dimension(80, 30));

        pos6.setBackground(new java.awt.Color(102, 102, 102));
        pos6.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        pos6.setText("y");
        pos6.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pos6.setPreferredSize(new java.awt.Dimension(35, 40));
        pos6.setSize(new java.awt.Dimension(80, 30));

        pos7.setBackground(new java.awt.Color(102, 102, 102));
        pos7.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        pos7.setText("u");
        pos7.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pos7.setPreferredSize(new java.awt.Dimension(35, 40));
        pos7.setSize(new java.awt.Dimension(80, 30));

        pos8.setBackground(new java.awt.Color(102, 102, 102));
        pos8.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        pos8.setText("i");
        pos8.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pos8.setPreferredSize(new java.awt.Dimension(35, 40));
        pos8.setSize(new java.awt.Dimension(80, 30));

        pos9.setBackground(new java.awt.Color(102, 102, 102));
        pos9.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        pos9.setText("o");
        pos9.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pos9.setPreferredSize(new java.awt.Dimension(35, 40));
        pos9.setSize(new java.awt.Dimension(80, 30));

        pos10.setBackground(new java.awt.Color(102, 102, 102));
        pos10.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        pos10.setText("p");
        pos10.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pos10.setPreferredSize(new java.awt.Dimension(35, 40));
        pos10.setSize(new java.awt.Dimension(80, 30));

        pos11.setBackground(new java.awt.Color(102, 102, 102));
        pos11.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        pos11.setText("a");
        pos11.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pos11.setPreferredSize(new java.awt.Dimension(35, 40));
        pos11.setSize(new java.awt.Dimension(80, 30));
        pos11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pos11ActionPerformed(evt);
            }
        });

        pos12.setBackground(new java.awt.Color(102, 102, 102));
        pos12.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        pos12.setText("s");
        pos12.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pos12.setPreferredSize(new java.awt.Dimension(35, 40));
        pos12.setSize(new java.awt.Dimension(80, 30));

        pos13.setBackground(new java.awt.Color(102, 102, 102));
        pos13.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        pos13.setText("d");
        pos13.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pos13.setPreferredSize(new java.awt.Dimension(35, 40));
        pos13.setSize(new java.awt.Dimension(80, 30));

        pos14.setBackground(new java.awt.Color(102, 102, 102));
        pos14.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        pos14.setText("f");
        pos14.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pos14.setPreferredSize(new java.awt.Dimension(35, 40));
        pos14.setSize(new java.awt.Dimension(80, 30));

        pos15.setBackground(new java.awt.Color(102, 102, 102));
        pos15.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        pos15.setText("g");
        pos15.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pos15.setPreferredSize(new java.awt.Dimension(35, 40));
        pos15.setSize(new java.awt.Dimension(80, 30));
        pos15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pos15ActionPerformed(evt);
            }
        });

        pos16.setBackground(new java.awt.Color(102, 102, 102));
        pos16.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        pos16.setText("h");
        pos16.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pos16.setPreferredSize(new java.awt.Dimension(35, 40));
        pos16.setSize(new java.awt.Dimension(80, 30));

        pos17.setBackground(new java.awt.Color(102, 102, 102));
        pos17.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        pos17.setText("j");
        pos17.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pos17.setPreferredSize(new java.awt.Dimension(35, 40));
        pos17.setSize(new java.awt.Dimension(80, 30));

        pos18.setBackground(new java.awt.Color(102, 102, 102));
        pos18.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        pos18.setText("k");
        pos18.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pos18.setPreferredSize(new java.awt.Dimension(35, 40));
        pos18.setSize(new java.awt.Dimension(80, 30));

        pos19.setBackground(new java.awt.Color(102, 102, 102));
        pos19.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        pos19.setText("l");
        pos19.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pos19.setPreferredSize(new java.awt.Dimension(35, 40));
        pos19.setSize(new java.awt.Dimension(80, 30));

        pos20.setBackground(new java.awt.Color(102, 102, 102));
        pos20.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        pos20.setText("z");
        pos20.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pos20.setPreferredSize(new java.awt.Dimension(35, 40));
        pos20.setSize(new java.awt.Dimension(80, 30));

        pos21.setBackground(new java.awt.Color(102, 102, 102));
        pos21.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        pos21.setText("x");
        pos21.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pos21.setPreferredSize(new java.awt.Dimension(35, 40));
        pos21.setSize(new java.awt.Dimension(80, 30));

        pos22.setBackground(new java.awt.Color(102, 102, 102));
        pos22.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        pos22.setText("c");
        pos22.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pos22.setPreferredSize(new java.awt.Dimension(35, 40));
        pos22.setSize(new java.awt.Dimension(80, 30));

        pos23.setBackground(new java.awt.Color(102, 102, 102));
        pos23.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        pos23.setText("v");
        pos23.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pos23.setPreferredSize(new java.awt.Dimension(35, 40));
        pos23.setSize(new java.awt.Dimension(80, 30));

        pos24.setBackground(new java.awt.Color(102, 102, 102));
        pos24.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        pos24.setText("b");
        pos24.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pos24.setPreferredSize(new java.awt.Dimension(35, 40));
        pos24.setSize(new java.awt.Dimension(80, 30));
        pos24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pos24ActionPerformed(evt);
            }
        });

        pos25.setBackground(new java.awt.Color(102, 102, 102));
        pos25.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        pos25.setText("n");
        pos25.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pos25.setPreferredSize(new java.awt.Dimension(35, 40));
        pos25.setSize(new java.awt.Dimension(80, 30));

        pos26.setBackground(new java.awt.Color(102, 102, 102));
        pos26.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        pos26.setText("m");
        pos26.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pos26.setPreferredSize(new java.awt.Dimension(35, 40));
        pos26.setSize(new java.awt.Dimension(80, 30));

        textBox.setEditable(false);
        textBox.setBackground(new java.awt.Color(0, 0, 102));
        textBox.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        textBox.setForeground(new java.awt.Color(255, 255, 255));
        textBox.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textBox.setToolTipText("Your Word Shows Here");

        requiredWord.setFont(new java.awt.Font("Arial Narrow", 0, 24)); // NOI18N
        requiredWord.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        requiredWord.setText("A");

        instructions.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        instructions.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        instructions.setText("Please experiment around with the new keyboard!");

        numCorrect.setBackground(new java.awt.Color(51, 255, 51));
        numCorrect.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        numCorrect.setForeground(new java.awt.Color(0, 153, 0));
        numCorrect.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        numCorrect.setText("0");

        timer.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        timer.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        timer.setText("0:00");
        timer.setToolTipText("");

        totalTimer.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        totalTimer.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalTimer.setText("0:00");
        totalTimer.setToolTipText("");

        confidence.setBackground(new java.awt.Color(51, 255, 51));
        confidence.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        confidence.setForeground(new java.awt.Color(0, 102, 255));
        confidence.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        confidence.setText("0.00");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(95, 95, 95)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(130, 130, 130)
                                .addComponent(pos3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pos4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(pos20, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(pos11, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(pos12, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(pos13, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(pos14, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(pos21, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(pos22, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(pos5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pos6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pos7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pos8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pos9, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pos10, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(pos15, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pos16, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pos17, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pos18, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pos19, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(pos23, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pos24, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pos25, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pos26, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pos1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pos2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(requiredWord, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(130, 130, 130)))
                .addContainerGap(104, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(238, 238, 238)
                .addComponent(textBox, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(numCorrect, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(timer, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(confidence, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(248, 248, 248)
                        .addComponent(totalTimer, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(23, 23, 23))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(105, 105, 105)
                    .addComponent(instructions, javax.swing.GroupLayout.PREFERRED_SIZE, 633, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(106, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(timer, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pos1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pos2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pos3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pos4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pos5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pos6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pos7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pos8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pos9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pos10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pos11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pos12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pos13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pos14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pos15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pos16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pos17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pos18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pos19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pos20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pos21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pos22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pos23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pos24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pos25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pos26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(41, 41, 41)
                        .addComponent(requiredWord, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textBox, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(208, 208, 208)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numCorrect, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalTimer)
                    .addComponent(confidence, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(417, Short.MAX_VALUE)
                    .addComponent(instructions, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(144, 144, 144)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pos1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pos1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pos1ActionPerformed

    private void pos15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pos15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pos15ActionPerformed

    private void pos24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pos24ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pos24ActionPerformed

    private void pos2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pos2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pos2ActionPerformed

    private void pos11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pos11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pos11ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainRandomizer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainRandomizer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainRandomizer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainRandomizer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainRandomizer mR = new MainRandomizer();
                mR.setFocusable(true);
                mR.thisKeyHandler = new KeyHandler(mR);
                mR.addKeyListener(mR.thisKeyHandler);
                mR.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel confidence;
    private javax.swing.JLabel instructions;
    private javax.swing.JLabel numCorrect;
    private javax.swing.JButton pos1;
    private javax.swing.JButton pos10;
    private javax.swing.JButton pos11;
    private javax.swing.JButton pos12;
    private javax.swing.JButton pos13;
    private javax.swing.JButton pos14;
    private javax.swing.JButton pos15;
    private javax.swing.JButton pos16;
    private javax.swing.JButton pos17;
    private javax.swing.JButton pos18;
    private javax.swing.JButton pos19;
    private javax.swing.JButton pos2;
    private javax.swing.JButton pos20;
    private javax.swing.JButton pos21;
    private javax.swing.JButton pos22;
    private javax.swing.JButton pos23;
    private javax.swing.JButton pos24;
    private javax.swing.JButton pos25;
    private javax.swing.JButton pos26;
    private javax.swing.JButton pos3;
    private javax.swing.JButton pos4;
    private javax.swing.JButton pos5;
    private javax.swing.JButton pos6;
    private javax.swing.JButton pos7;
    private javax.swing.JButton pos8;
    private javax.swing.JButton pos9;
    private javax.swing.JLabel requiredWord;
    private javax.swing.JTextField textBox;
    private javax.swing.JLabel timer;
    private javax.swing.JLabel totalTimer;
    // End of variables declaration//GEN-END:variables
}
