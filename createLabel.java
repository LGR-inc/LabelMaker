/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javacolourlabelling;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static javafx.scene.paint.Color.color;
import javax.imageio.ImageIO;

/**
 *
 * @author Martin
 * This Routine handles the creation of a label
 * which is printed with Graphics 2D onto a JFrame Container
 */
public class createLabel extends JFrame {
    
    
        
    //Set up the label size and shape
    public createLabel(String[] queueData, String[] labelData, String defaultsString[]) {
        super(queueData[0] + " - " + labelData[2]);
        
        int scaler = 7;
        int labelWidth = Integer.parseInt(labelData[0]);
        int labelHeight = Integer.parseInt(labelData[1]);
        
        setSize((labelWidth * scaler) + 18, (labelHeight * scaler) +40);
        setBackground(Color.DARK_GRAY);
        //repaint();
        //setLayout (null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container cp = this.getContentPane();
        cp.setSize(labelWidth * scaler,labelHeight * scaler);
        
        //cp.setSize(730,400);
        cp.setBackground(Color.white);
        cp.setLayout(null);
        int contentWidth = cp.getWidth();
        int contentHeight = cp.getHeight();
        
        System.out.println("Bounds - " + labelWidth * scaler + " by " + labelHeight * scaler);
        System.out.println("content - " + contentWidth + " by " + contentHeight);
        setVisible (true);
        
        //DISCOVER LABEL PARAMETERS
        //find name and split into fields
        String labelName = (labelData[2]);
        System.out.println (labelName);
        // fields = how many fields on the label
        int fields = 1;
        //datainputs = how many data items identify a field
        int dataInputs = 1;
        
        for (int i=3; i < labelData.length; i++) {
             if (labelData[i].equals (labelName)){
                 fields++;
             } else {
                 if (fields == 1){
                     dataInputs++;
                 }
             }
        }
        
        //Test to see it works
        System.out.println ("fields = " + fields +"   dataInputs " + dataInputs);
        
        
        //MAIN ROUTINE FOR ADDING LABEL FIELDS ONE BY ONE

        //iterate through fields
        for (int i=0; i<fields; i++){
            
            int jump = dataInputs;
            int start = 2+ (jump * i);
                    
            //Collect queue data into variables
            String type = (labelData[start + 2]);
            System.out.println(i+" - " +type);
            int xSize = Integer.parseInt(labelData[start + 3]);
            int ySize = Integer.parseInt(labelData[start + 4]);
            int xPos = Integer.parseInt(labelData[start + 5]);
            int yPos = Integer.parseInt(labelData[start + 6]);
            Boolean isProfile = Boolean.parseBoolean(labelData[start + 7]);//whether data from profile or special
            Boolean isFontVariable = Boolean.parseBoolean(labelData[start +8]);
            Boolean areLinesReduceable = Boolean.parseBoolean(labelData[start +9]);
            int listboxNo = Integer.parseInt(labelData[start + 10]);
            String fixedValueString = (labelData[start + 11]);//such as border width etc
            int noLines = Integer.parseInt(labelData[start + 12]);
            String justify = (labelData[start + 13]);
            Boolean isFontColourProfile = Boolean.parseBoolean(labelData[start +14]);
            String fontName = (labelData[start + 15]);  
            int fontSize = Integer.parseInt(labelData[start + 16]);   
            Boolean isFontBold = Boolean.parseBoolean(labelData[start +17]);
            Boolean isFontItalic = Boolean.parseBoolean(labelData[start +18]);
            String sentColour = (labelData[start +19]);
            
            String profileTextColour = (queueData[12]);
            String profileBorderColour = (queueData[15]);
            String profileBackgroundColour = (queueData[16]);
            
            //set justification as an integer
            int justifyInt = 0;
            switch (justify.trim()){
                case "left": justifyInt = 0;
                    break;
                case "right": justifyInt = 2;
                    break;
                case "center" : justifyInt = 1;
            }
            
            //set content size and position
            int lines =1;//set lines as 1 and then increase if necessary later
            int yPosd = 0;
            int xPosd = 0;
            int xSized = contentWidth*xSize/100;
            int ySized = contentHeight*ySize/100/ (lines);
            if (xPos == 0 ) {
            //set as centred
            xPosd = (contentWidth/2) * (100-xSize)/100;
            } else {
            xPosd = contentWidth*xPos/100;
            }
        
            if (yPos == 0 ) {
            yPosd = (contentHeight/2) * (100-ySize)/100;
            } else {
            yPosd = contentHeight*yPos/100;
            }
            
            
            
            switch (type) {
                case "text" :
                    
                    //load with custome text from label
                    String textToSend = labelData [start + 11];
                    if (isProfile){
                        //if text comes from queue entry, load this instead
                        textToSend = queueData[listboxNo];
                    } 
                    
                    //Work out colour
                    Color colourFont = Color.decode(getHexColour (sentColour));
                    if (!isFontColourProfile){
                        colourFont = Color.decode(getHexColour (profileTextColour));
                    } 
                    
                    String[] textToSendArray = new String[1]; 
                    textToSendArray[0]=  textToSend;
                    if (noLines > 1){
                        //handle multi-line text
                        String[] labelReturned = new String[noLines];   
                        labelReturned = textOperations.splitText(textToSend,noLines); 
                        paintText (cp,labelReturned,labelReturned.length,xPosd,yPosd,xSized,ySized,
                                justifyInt, fontName, fontSize, colourFont,
                                isFontVariable,areLinesReduceable,isFontBold,isFontItalic,textToSend);
                        } else {
                        //send single line text
                        paintText (cp,textToSendArray,1,xPosd,yPosd,xSized,ySized,
                                justifyInt, fontName, fontSize, colourFont,
                                isFontVariable,areLinesReduceable,isFontBold,isFontItalic,textToSend);
                    }
                                            
                    break;
                
                case "border" :
                    
                    Color borderColour = Color.decode(getHexColour (profileBorderColour));
                    int borderWidth = Integer.parseInt(fixedValueString);
                    borderWidth = (contentWidth*borderWidth/100);
                    paintBorder(cp,xPosd,yPosd,xSized,ySized,borderWidth,borderColour);
                    
                    break;
                
                case "colourbox" :
                    
                    Color colourBoxColour = Color.decode(getHexColour (profileBackgroundColour));
                    paintColourbox (cp,xPosd,yPosd,xSized,ySized,colourBoxColour);
                    
                    break;
                    
                case "image" :
                    
                    String pictureString = new String();
                    if (isProfile) {
                        pictureString = (defaultsString[0] + queueData[10]);
                    } else {
                        pictureString = (defaultsString[1] + fixedValueString);
                    }
                    
                    System.out.println(pictureString);
                    paintImage (cp,xPosd,yPosd,xSized,ySized,pictureString);
                    
                    break;
                    
                default :
                    break;
            } 
        }
        
    }



    public void paintText (Container whereTo, String[] textArray,int lines,int xPosd, int yPosd, int xSized, int ySized, int justify, 
            String fontName, int fontSize, Color colourFont,
            boolean isFontVariable, boolean areLinesReduceable, boolean isFontBold,boolean isFontItalic, String textToSend){
        
        int fontNewSize = 10;
        
        String fontForm = new String("");
        if (isFontBold) fontForm = fontForm + "BOLD";
        if (isFontItalic) fontForm = fontForm + "ITALIC";        
        
        
        if (!isFontVariable) fontNewSize = fontSize;
        
            //create graphics object
            Graphics g = whereTo.getGraphics();
            Graphics2D g1 = (Graphics2D) g;
            
            g1.setColor(colourFont);
            RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g1.setRenderingHints(rh);            
            
            Font plainTextFont = new Font(fontName, Font.PLAIN, fontSize);
            Font boldTextFont =  new Font(fontName, Font.BOLD, fontSize);
            Font italicTextFont = new Font(fontName, Font.ITALIC, fontSize);
            Font boldItalicTextFont = new Font(fontName, Font.BOLD + Font.ITALIC, fontSize);
            switch (fontForm) {
                case "BOLD" :
                    g1.setFont(boldTextFont);
                break;
                case "ITALIC" :
                    g1.setFont(italicTextFont);
                break;
                case "BOLDITALIC" :
                    g1.setFont(boldItalicTextFont);
                break;
                default :
                    g1.setFont(plainTextFont);
                break;
                
            }
            System.out.println ("Font is " + fontForm);
            
            Font fontSet = g1.getFont();
                
        //GO FOR PRINTING
        int yOriginalSized = ySized;
        ySized = ySized/lines;
        g1.drawString("",0,0);
        
        double[] factorsToUse = new double[textArray.length];
        
        //Find out the correct font size to use
        
        Boolean haveLinesReduced = new Boolean (false);
        int realNoLines = textArray.length;
        
            if ( areLinesReduceable && lines > 1 ) {
                System.out.println("***REDUCING LINES ROUTINE ***");      
                haveLinesReduced = true;
                double bestFactor=0;
                int bestLines = 1;
                for (int f = lines ;f > 0; f--) {
                    String[] labelReturned = new String[f];   
                    labelReturned = textOperations.splitText(textToSend,f);
                    System.out.println ("REDUCING F = " +f);
                    
                    for (int i=0; i < f; i++) {
                    System.out.println( labelReturned[i]);

                    Font fontToSend = g1.getFont();

                    Double factorToUse;
                    factorToUse = sizeGraphicText(whereTo, labelReturned[i], fontToSend , xSized, (yOriginalSized/f));

                    factorsToUse[i] = factorToUse;
                    }
                    double returnedFactor = 0;
                    if (isFontVariable) {
                        double averageFactor = 0;
                        System.out.println("Font IS Variable. Lines - " + labelReturned.length);
                        for (int x = 0; x < (labelReturned.length); x ++) {
                            averageFactor = averageFactor + factorsToUse[x];
                            System.out.println("Average Factor = "+ averageFactor);
                        }
                        returnedFactor = averageFactor/(labelReturned.length);
                        
                    } else {
                        System.out.println("Font NOT Variable");
                        double averageFactor = 99999999;
                        for (int x = 0; x < (labelReturned.length); x ++) {
                            if (averageFactor > factorsToUse[x]) {
                                averageFactor = factorsToUse[x];
                            }
                        }
                        returnedFactor = averageFactor;
                    }
                    System.out.println ("Best - " + bestFactor );
                    System.out.println("Returned - "+returnedFactor);
                    if (returnedFactor > bestFactor) {
                        bestFactor = returnedFactor;
                        bestLines = f;
                    }
                }
                
                String[] labelReturned = new String[bestLines];   
                labelReturned = textOperations.splitText(textToSend,bestLines);
                
                for (int i=0; i < (labelReturned.length); i++) {
                System.out.println( "Chosen - "+ labelReturned[i]);

                Font fontToSend = g1.getFont();

                Double factorToUse;
                factorToUse = sizeGraphicText(whereTo, labelReturned[i], fontToSend , xSized, (yOriginalSized/(labelReturned.length)));

                factorsToUse[i] = factorToUse;
                }
                realNoLines = labelReturned.length;
                
                ySized = yOriginalSized/realNoLines;
                
                for (int i=0; i < (labelReturned.length); i++) {
                    textArray[i] = labelReturned[i];
                    
                    //Draw edges of field
                g1.setStroke(new BasicStroke(1));
                g1.drawRect(xPosd, (yPosd + (i*ySized)) , xSized, ySized );
                }
 
                
            } else {
                
                System.out.println("### NOT REDUCING ROUTINE ###");
                for (int i=0; i < (textArray.length); i++) {
                System.out.println( textArray[i]);

                Font fontToSend = fontSet;
                //Font fontToSend = g1.getFont();

                //Draw edges of field
                //g1.setStroke(new BasicStroke(1));
                //g1.drawRect(xPosd, (yPosd + (i*ySized)) , xSized, ySized );

                Double factorToUse;
                factorToUse = sizeGraphicText(whereTo, textArray[i], fontSet , xSized, ySized);

                factorsToUse[i] = factorToUse;
                }
            }

            Double factorToUse;
            g1.setFont(fontSet);
            //Find and store current font
            //FontMetrics metrics = g1.getFontMetrics();
            //Font currentFont = metrics.getFont();
            
            //Set all fonts to the same if required
            if (!isFontVariable) {
                Double smallestFactor = (double)99999;
                for ( int i = 0;  i < (textArray.length); i++) {
                if (factorsToUse[i] < smallestFactor ) smallestFactor = factorsToUse[i];               
                }
                for ( int i = 0;  i < (textArray.length); i++) {
                factorsToUse[i] = smallestFactor ;               
                }
            }
    
            
            for (int i = 0; i < (realNoLines); i++) {
                
                factorToUse = factorsToUse[i];
                    // create new font from current 
                    AffineTransform at = new AffineTransform();
                    at.setToScale(factorToUse, factorToUse);
                    Font finalFont = fontSet.deriveFont(at);
                    System.out.println("Factor at setting - "+factorToUse);
                g1.setFont(finalFont);
                FontMetrics metrics = g1.getFontMetrics();
                
                //find size and find positions
                int newWidth = metrics.stringWidth(textArray[i]);
                int newHeight = metrics.getAscent();
        
                    System.out.println("Justification - " + justify);
                    System.out.println ("XSized - " + xSized + " , width - " + newWidth);
          
                
                //Set horizontal position
                int xxPosd = 0;
                switch (justify) {
                    case 0 :
                        xxPosd = xPosd;               
                    break;
                    case 1 :
                        xxPosd = xPosd + ((xSized - newWidth)/2) ;                
                    break;
                    case 2 :
                        xxPosd = xPosd + xSized - newWidth;
                    break;   
                }
                
                //Centre Height
                System.out.println ("YSized - " + ySized + " , height - " + newHeight);
                int yySized=ySized;
                int yyPosd = (yPosd + yySized + ( i * yySized) );
                yyPosd = yyPosd - ((yySized - newHeight)/2) ;
                
                g1.drawString(textArray[i], xxPosd, yyPosd);
            
            }
                
        
        
    }
    
    public  Double sizeGraphicText(Container whereTo,String textSent, Font fontSent, int xSized, int ySized) {
        
        //PRODUCES A SCALE FACTOR TO MULTIPLY THE DEFAULT FONT.
        
        int scaler = 100;
        double sensitivity = 1;
        Graphics g = whereTo.getGraphics();
        Graphics2D g1 = (Graphics2D) g;
        Font firstFont=fontSent.deriveFont(scaler);
        g1.setFont(firstFont); // set huge font to render with here
        
        
        //Get sizes
        FontMetrics metrics = g1.getFontMetrics();
        String text = textSent;
        //Rectangle2D rect = metrics.getStringBounds(text, g1);
        //int width = (int)rect.getWidth();
        //int height = (int)rect.getHeight();
        int width = metrics.stringWidth(text);
        int height = metrics.getAscent();
        
        //compare to sizes wanted
        double factorToUse = 0;
        double xFactor = ((double)xSized/(double)width);
        double yFactor = ((double)ySized/(double)height);
        
        System.out.println("Text Sizing.  ** text = "+ text);
        System.out.println("Text Sizing.  ** xSized = "+ xSized + ".  width = "+width);
        System.out.println("Text Sizing.  ** ySized = "+ ySized + ".  height = "+height);
        System.out.println("Text Sizing.  ** xFactor = "+ xFactor);
        System.out.println("Text Sizing.  ** yFactor = "+ yFactor);
        
        int newFontSize = 0;
        if (xFactor < yFactor) {
            factorToUse = (xFactor * sensitivity);
        } else {
            factorToUse = (yFactor * sensitivity);
        }
        System.out.println("Text Sizing.  ** factorToUse = "+ factorToUse);
        return factorToUse;
    }
    
    
    public void paintBorder( Container whereTo, int xPosd, int yPosd, int xSized, int ySized, int borderWidth, Color definedColour){
        
        System.out.println("got to border "+xPosd+","+yPosd+","+xSized+","+ySized);
                
            Graphics g = whereTo.getGraphics();
            Graphics2D g1 = (Graphics2D) g;

                System.out.println("got to paint. Colour " + definedColour);
                g1.setStroke(new BasicStroke(borderWidth));
                g1.setColor(definedColour);
                g1.drawString("",150,150);     
                
                g1.draw(new Rectangle2D.Double(xPosd,yPosd,xSized,ySized));
                
                
    }
    
    public void paintColourbox ( Container whereTo, int xPosd, int yPosd, int xSized, int ySized, Color definedColour){
        
            Graphics g = whereTo.getGraphics();
            Graphics2D g1 = (Graphics2D) g;

                System.out.println("got to colourbox Colour " + definedColour);
                
                g1.drawString("",150,150);     
                g1.setPaint(definedColour);
                g1.fill (new Rectangle2D.Double(xPosd,yPosd,xSized,ySized));
                
                
    }  
    
    public void paintImage ( Container whereTo, int xPosd, int yPosd, int xSized, int ySized, String imageFile){
        
            Graphics g = whereTo.getGraphics();
            Graphics2D g1 = (Graphics2D) g;

                System.out.println("got to image " + imageFile);
                
                BufferedImage img = null;
                try {
                    img = ImageIO.read(new File(imageFile));
                    System.out.println("read image file ");
                } catch (IOException e) {
                }
                g1.drawString("",150,150);     
                g1.drawImage(img,xPosd,yPosd,xSized,ySized,null);
                
            }
    
    public String getHexColour (String sentColour){
        
        //CONVERT AN INTEGER COLOUR INTO A HEX COLOUR
        //sent colour as RGB single number
        String gotHexColour = "";
        
        //split single colour number into 3 components
        int numberColour=(Integer.parseInt(sentColour));
        int red = (numberColour/256/256);
        int greenblue = numberColour - (red*256*256);
        int green = (greenblue/256);
        int blue = (greenblue-(green * 256));
        
        System.out.println ("colours - " + red + " , " + green + " , " + blue);
        
        //convert integer to Hex value
        // value of 00 can convert to "0", so check and change
        String redString = Integer.toHexString(red);
        if ( redString.equals ("0")) redString = "00";
        
        String greenString = Integer.toHexString(green);
        if ( greenString.equals ("0")) greenString = "00";
        
        String blueString = Integer.toHexString(blue);
        if ( blueString.equals ("0")) blueString = "00";
        
        //add into single Hex value
        gotHexColour = "#" + redString + greenString + blueString;
        System.out.println (gotHexColour);
        
    return gotHexColour;
}
    
    
    
    
    
}



