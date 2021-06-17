package com.siberteam.edu.zernest.asolver.exceptions;

public class AnagramSolverAppException extends Exception {
    private final AnagramSolverExitCode exitCode;

    public AnagramSolverAppException(AnagramSolverExitCode exitCode,
                                     String message) {
        super(message);
        this.exitCode = exitCode;
    }

    public AnagramSolverAppException(AnagramSolverExitCode exitCode) {
        super("Anagram Solver app exception");
        this.exitCode = exitCode;
    }

    public AnagramSolverExitCode getExitCode() {
        return exitCode;
    }

}
