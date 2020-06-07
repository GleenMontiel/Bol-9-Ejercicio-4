package app;

import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {

        Principal frame = new Principal();
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}