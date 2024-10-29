package com.lazicode.workflow;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.ConsoleHandler;

public class Main {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.FINE);
        for (java.util.logging.Handler handler : logger.getHandlers()) {
            if (handler instanceof ConsoleHandler) {
                handler.setLevel(Level.FINE);
            }
        }

        // Your application logic here
    }
}
