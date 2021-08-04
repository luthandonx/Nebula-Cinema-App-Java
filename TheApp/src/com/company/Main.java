package com.company;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
                // write your code here
               // Pictures pic = new Pictures("Ghost in a shell(1995).jpeg");

                JFrame Application = new JFrame("NebulaApp");
                Application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                Application.setSize(900,700);

                App app = new App();
                Application.add(app);
                Application.setVisible(true);
    }

}