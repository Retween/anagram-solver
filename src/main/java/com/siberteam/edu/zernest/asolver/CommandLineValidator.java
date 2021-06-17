package com.siberteam.edu.zernest.asolver;

import com.siberteam.edu.zernest.asolver.exceptions.AnagramSolverAppException;
import com.siberteam.edu.zernest.asolver.exceptions.AnagramSolverExitCode;
import org.apache.commons.cli.*;

import java.io.File;

public class CommandLineValidator {
    private Options options;
    private File inputFile;
    private File outputFile;
    private int producersCount;
    private int consumersCount;

    public void parseCommandLine(String[] args) throws ParseException {
        options = new Options();
        CommandLineParser parser = new DefaultParser();

        options.addRequiredOption("i", "inputFile", true,
                "File with URL addresses list");
        options.addRequiredOption("o", "outputFile", true,
                "Output file for recording the result");
        options.addRequiredOption("p", "producersNumber", true,
                "Number of producers threads (>=1)");
        options.addRequiredOption("c", "consumersNumber", true,
                "Number of consumers threads (>=1)");

        CommandLine cmd = parser.parse(options, args);
        inputFile = new File(cmd.getOptionValue("i"));
        outputFile = new File(cmd.getOptionValue("o"));
        producersCount = Integer.parseInt(cmd.getOptionValue("p"));
        consumersCount = Integer.parseInt(cmd.getOptionValue("c"));

        if (producersCount < 1 || consumersCount < 1) {
            throw new ParseException("producersCount: " + producersCount
                    + " consumersCount: " + consumersCount);
        }
    }

    public void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        String syntax = "Main";
        String usageHeader = "Example of Using anagram-solver app";
        String usageFooter = "Usage example: -i inputFile.txt -o " +
                "outputFile.txt -p 4 -c 4";
        formatter.printHelp(syntax, usageHeader, options, usageFooter);
    }

    public File getInputFile() {
        return inputFile;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public int getConsumersCount() {
        return consumersCount;
    }

    public int getProducersCount() {
        return producersCount;
    }
}
