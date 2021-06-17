package com.siberteam.edu.zernest.asolver.interfaces;

public interface ILogger {
    default void log(String message) {
        System.out.println(message);
    }
}
