package com.siberteam.edu.zernest.asolver.exceptions;

public class AnagramSolverAppException extends Exception {
    private final AnagramSolverExitCode exitCode;

    public AnagramSolverAppException(AnagramSolverExitCode exitCode,
                                     String message) {
        super(message);
        this.exitCode = exitCode;
    }

    public AnagramSolverExitCode getExitCode() {
        return exitCode;
    }

    @Override
    public String toString() {
        return "AnagramSolverAppException" + "[" +
                "exitCode=" + exitCode +
                ']';
    }
}
