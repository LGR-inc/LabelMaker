/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javacolourlabelling;

import static javax.swing.SwingUtilities.isEventDispatchThread;

/**
 *
 * @author Martin
 */
public class JavaColourLabelling {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
                       
        Boolean runIt = new Boolean(true);
        
        if (runIt) {
        //**USE TEMPORARY DATAREADER TO COLLECT A QUEUE ENRTY AND A LABEL **
        String whereFiles = new String ("D:\\JavaLabelling\\");
        //file with sample queue entry;
        String name = new String(whereFiles + "ColourQueue.txt");
        String[] queueString = dataReader.readFile(name);
        //file with a sample label definition;
        name = whereFiles + "Labels.txt";
        String[] labelString = dataReader.readFile(name);
        //file with default settings
        name = whereFiles + "defaults.txt";
        String[] defaultsString = dataReader.readFile(name);
        
        // try a sample label 
        if(isEventDispatchThread()){
            System.out.println("JAVACL - EventDispatchThread");
        } else {
            System.out.println("JAVACL - NOT EventDispatchThread");
        }
        createLabel newLabel = new createLabel(queueString, labelString, defaultsString);
        //tryLabel tryLabel = new tryLabel(queueString, labelString);
        
        }
        
        tempStart newTempStart = new tempStart();
        newTempStart.setVisible(true);
        
        
        
        
// TODO code application logic here
    }
    
}
