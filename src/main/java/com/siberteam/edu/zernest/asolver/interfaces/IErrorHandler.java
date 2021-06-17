package com.siberteam.edu.zernest.asolver.interfaces;

import com.siberteam.edu.zernest.asolver.exceptions.AnagramSolverExitCode;

public interface IErrorHandler extends ILogger {
    default void handleException(AnagramSolverExitCode exitCode, Exception e) {
        log(exitCode.getDescription() + "\n" + e + "\n" + e.getCause());
        System.exit(exitCode.getCode());
    }
}
