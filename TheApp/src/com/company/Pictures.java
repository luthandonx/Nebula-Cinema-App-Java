package com.company;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Pictures {

    String name ;
    Image pic;

    public Pictures(String sn)
    {
        this.name = sn;
        getImage();
    }

    void getImage ()
    {
        try {//This only works only if the absolute path of the image is used!!! I don't understand why
            pic = ImageIO.read(new File("/home/nxumalo/Documents/COMP306/TheApp/movie images/"+name+".jpeg"));
           // System.out.println("Got image");
        }
        catch (IOException e)
        {
            JPanel p = new JPanel();
            JOptionPane.showMessageDialog(p,"Could not find image","ERROR",JOptionPane.WARNING_MESSAGE);
        }
    }

   void draw (Graphics g)
   {
       g.drawImage(pic, 0,0,100,100,null);
   }
}
