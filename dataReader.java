/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javacolourlabelling;

import java.io.*;

/**
 *
 * @author Martin
 */

//  **THIS IS A TEMPORARY CLASS **
// Its purpose is to collect data for one label from a couple of temporary text files
// It seems to work well

public class dataReader {
 
    public static String[] readFile(String fileName) {
        
        File inputFile = new File(fileName);
        
        try (FileInputStream streamIn = new FileInputStream(inputFile)) {
            
            //read file into byte array
            int size = (int) inputFile.length();
            byte[] readIn = new byte[size];
            streamIn.read(readIn);
            String readInString = new String(readIn);
           
            
            //find out divisions and set string array to right size
            int pipeCount=0;
            for(int i=0; i<readInString.length(); i++){
                if (readInString.charAt(i) == '|') {
                    pipeCount++;
                }
            }
            String[] outputString = new String[pipeCount];
            
            int counter = 0;
            String addString = new String ("");
            
            for(int i=0; i<readInString.length(); i++){
                if (readInString.charAt(i) != '|') {
                    addString = addString + readInString.charAt(i);
            } else {
                    outputString[counter] = addString;
                    addString = "";
                    counter++;
                  //System.out.println(counter + " - " + outputString[counter]);
                }               
            }
            
           return outputString ;
                                    
        } catch (IOException ioe) {
           
            String[] outputString = new String[1];
            return outputString ;          
        }            
        
        
    }
    
    
}
