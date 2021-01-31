package com.apachebruteforce;

import com.apachebruteforce.core.AuthThread;
import com.apachebruteforce.core.Brute;
import com.apachebruteforce.core.Data;
import com.apachebruteforce.gui.MainWindow;

public class Main {
    public static void main(String[] args) {
        try {
            MainWindow window = new MainWindow();
            Brute brute = Brute.getBruteInstance(window);
            Data data = Data.getInstance(window.getStartPassword(), window.getEndPassword(), brute);

            //init threads
            AuthThread.staticInitClass(brute, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
