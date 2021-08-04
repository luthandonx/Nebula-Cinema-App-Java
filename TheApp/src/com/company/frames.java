package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class frames extends JPanel {

    public Pictures pic ;
    public frames(){

    }

    public frames (Pictures pic)
    {
        this.pic = pic;
      // addMouseListener(new mouse());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        pic.draw(g);
    }


}
