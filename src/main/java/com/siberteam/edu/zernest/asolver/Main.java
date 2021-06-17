package com.siberteam.edu.zernest.asolver;

import com.siberteam.edu.zernest.asolver.exceptions.AnagramSolverAppException;
import com.siberteam.edu.zernest.asolver.exceptions.AnagramSolverExitCode;
import com.siberteam.edu.zernest.asolver.input.InputStreamToQueueReader;
import com.siberteam.edu.zernest.asolver.interfaces.IErrorHandler;
import com.siberteam.edu.zernest.asolver.output.AnagramsToOutputStreamWriter;
import com.siberteam.edu.zernest.asolver.process.ProducerConsumerStarter;
import org.apache.commons.cli.ParseException;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        CommandLineValidator validator = new CommandLineValidator();
        try {
            long s = System.currentTimeMillis();
            validator.parseCommandLine(args);
            File inputFile = validator.getInputFile();
            File outputFile = validator.getOutputFile();
            int producersCount = validator.getProducersCount();
            int consumersCount = validator.getConsumersCount();

            if (!inputFile.exists()) {
                throw new AnagramSolverAppException(
                        AnagramSolverExitCode.CANNOT_OPEN_INPUT,
                        inputFile.getName());
            }

//            if (outputFile.exists() && outputFile.isFile()) {
//                throw new AnagramSolverAppException(
//                        AnagramSolverExitCode.FILE_ALREADY_EXISTS,
//                        outputFile.getName());
//            }


            try (InputStream inputStream = new FileInputStream(inputFile);
                 OutputStream outputStream = new FileOutputStream(outputFile)) {

                InputStreamToQueueReader reader =
                        new InputStreamToQueueReader(inputStream);

                ProducerConsumerStarter starter = new ProducerConsumerStarter(
                        reader.getUrlQueue(), producersCount, consumersCount);
                starter.start();

                AnagramsToOutputStreamWriter writer =
                        new AnagramsToOutputStreamWriter(outputStream);

                writer.writeListToFile(starter.getAnagramsMap());
            }

            long f = System.currentTimeMillis();
            System.out.println(f - s);
        } catch (IOException | ParseException | AnagramSolverAppException | InterruptedException e) {
            handler.handleException(AnagramSolverExitCode.UNEXPECTED_ERROR, e);
        }
    }

    public static IErrorHandler handler = new IErrorHandler() {
        @Override
        public void handleException(AnagramSolverExitCode exitCode, Exception e) {
            IErrorHandler.super.handleException(exitCode, e);
        }
    };
}
