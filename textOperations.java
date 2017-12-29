/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javacolourlabelling;

import java.io.*;
 //*
 ////* @author Martin
 //*/

// ** THIS CLASS WORKS WELL **
//  Take the string sent and splits it into a given number of equal pieces at the spaces.
// if there aren't enough spaces it splits it into less pieces.

public class textOperations {
    
    public static String[] splitText(String toSplit, int pieces) {
        String decisionText;
        String partialText ="";
        int finalCount = 0;
        int finalPieces = pieces;
        int splits;
        String[] splitString = new String[2]; 
        
        //remove leading and trailiing spaces and asign
        decisionText=toSplit.trim();
        
        //test that there are enough gaps to split into
        int testSpaces = textOperations.countGaps(decisionText);
        if ( (testSpaces + 1) < pieces){
                finalPieces = testSpaces + 1;
           }
        splits = finalPieces;
        String[] returnText = new String[(finalPieces)]; 

      
        for (int count=0; count < (finalPieces-1); count ++){
           
            int textLength = decisionText.length();
            int textPiece = (textLength/splits);
            int textPositionStart = textPiece;
            
            
            char testCharPlus;
            char testCharMinus;
            boolean state = false;
            boolean testPlus=false;
            boolean testMinus=false;
            
            testSpaces = textOperations.countGaps(decisionText);
            if ((testSpaces + 1 ) == splits){
                textPositionStart = 1;
                //break;
            }
            
            //set after test for gaps
            int textPositionPlus = textPositionStart;
            int textPositionMinus = textPositionStart;
            int splitPosition = textPositionStart;
            int splitPositionPlus = splitPosition;
            int splitPositionMinus = splitPosition;
            
                    
            while (state == false) {        
                 testCharPlus = decisionText.charAt(textPositionPlus);
                 testCharMinus = decisionText.charAt(textPositionMinus);
                 
                 //test upstring of position
                 if (testCharPlus == ' '){
                     splitPositionPlus = textPositionPlus;
                     state=true;
                     testPlus=true;
                 }
                 //test downstring of position
                 if (testCharMinus == ' '){
                     splitPositionMinus = textPositionMinus;
                     state=true; 
                     testMinus=true;
                 }
                 textPositionPlus ++;
                 if (textPositionPlus > (decisionText.length()-1)){
                     textPositionPlus = decisionText.length();
                 }
                 textPositionMinus --;
                 if (textPositionMinus < 0 ) {
                     textPositionMinus = 0;
                 }
                 
            } 
            
        if (testPlus == true) {
            splitPosition = splitPositionPlus;
        }
        if (testMinus == true) {
            splitPosition = splitPositionMinus;
        }
        
        //split Text in two
        splitString=textOperations.splitTextAtPoint(decisionText, splitPosition);
        //set return text segment
        returnText[count] = splitString[0].trim();

        //trim of segment ready to go again
        decisionText = splitString[1].trim();
        
        //set index fro final segment assignment
        finalCount = count+1;  
        
        //reduce segements as we go
        splits--;
        }
        
        //set last segment
        returnText[finalCount]=decisionText;
        
 
        return returnText;        
    }

    public static int countGaps(String gapText) {
        
        int gaps = 0;
        boolean space = false;
        
        for (int count=0; count < gapText.length(); count++){
            
            if (gapText.charAt(count)== ' '){
                if (space==false){
                    gaps++;
                    space=true;
                }
            } else {
                space=false;
            }
        }
            
        return gaps;

}
    
    public static String[] splitTextAtPoint(String toSplit, int splitPosition){
        
        String[] returnText = new String [2];
        
        returnText[0] = toSplit.substring(0,splitPosition);
        returnText[1] = toSplit.substring(splitPosition);
        
        return returnText;
}

}

    

