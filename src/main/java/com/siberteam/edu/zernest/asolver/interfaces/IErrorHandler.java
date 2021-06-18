package com.siberteam.edu.zernest.asolver.interfaces;

import com.siberteam.edu.zernest.asolver.CommandLineValidator;
import com.siberteam.edu.zernest.asolver.exceptions.AnagramSolverExitCode;

public interface IErrorHandler extends ILogger {
    default void handleException(AnagramSolverExitCode exitCode, Exception e) {
        log(exitCode.getDescription() + "\n" + e + "\n" + e.getCause());
        System.exit(exitCode.getCode());
    }

    default void handleException(CommandLineValidator validator, Exception e) {
        validator.printHelp();
        AnagramSolverExitCode exitCode = AnagramSolverExitCode
                .COMMAND_LINE_USAGE;
        handleException(exitCode, e);
    }
}
