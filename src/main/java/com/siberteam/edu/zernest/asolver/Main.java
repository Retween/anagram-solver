package com.siberteam.edu.zernest.asolver;

import com.siberteam.edu.zernest.asolver.exceptions.AnagramSolverAppException;
import com.siberteam.edu.zernest.asolver.exceptions.AnagramSolverExitCode;
import com.siberteam.edu.zernest.asolver.input.InputStreamToQueueReader;
import com.siberteam.edu.zernest.asolver.interfaces.IErrorHandler;
import com.siberteam.edu.zernest.asolver.output.AnagramsToOutputStreamWriter;
import com.siberteam.edu.zernest.asolver.process.ProducerConsumerStarter;
import org.apache.commons.cli.ParseException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        CommandLineValidator validator = new CommandLineValidator();
        try {
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

            if (outputFile.exists() && outputFile.isFile()) {
                throw new AnagramSolverAppException(
                        AnagramSolverExitCode.FILE_ALREADY_EXISTS,
                        outputFile.getName());
            }

            try (InputStream inputStream = new FileInputStream(inputFile);
                 OutputStream outputStream = new FileOutputStream(outputFile)) {

                InputStreamToQueueReader reader =
                        new InputStreamToQueueReader(inputStream);

                ProducerConsumerStarter starter = new ProducerConsumerStarter(
                        reader.getUrlQueue(), producersCount, consumersCount);
                starter.startThreads();

                AnagramsToOutputStreamWriter writer =
                        new AnagramsToOutputStreamWriter(outputStream);

                writer.writeListToFile(starter.getAnagramsList());
            }
        } catch (Exception e) {
            if (e instanceof ParseException) {
                defaultHandler.handleException(validator, e);
            }

            IErrorHandler handler = errorHandlerMap.get(e.getClass());
            if (handler != null) {
                if (e instanceof AnagramSolverAppException) {
                    handler.handleException(((AnagramSolverAppException) e)
                            .getExitCode(), e);
                }
                handler.handleException(errorExitCode.get(e.getClass()), e);
            }

            defaultHandler.handleException(AnagramSolverExitCode
                    .UNEXPECTED_ERROR, e);
        }
    }

    private static final Map<Class<?>, IErrorHandler> errorHandlerMap =
            new HashMap<>();
    private static final Map<Class<?>, AnagramSolverExitCode> errorExitCode =
            new HashMap<>();

    private static final IErrorHandler defaultHandler = new IErrorHandler() {
        @Override
        public void handleException(AnagramSolverExitCode exitCode,
                                    Exception e) {
            IErrorHandler.super.handleException(exitCode, e);
        }
    };

    static {
        errorHandlerMap.put(InterruptedException.class, defaultHandler);
        errorExitCode.put(InterruptedException.class, AnagramSolverExitCode
                .INTERRUPTED);

        errorHandlerMap.put(IOException.class, defaultHandler);
        errorExitCode.put(IOException.class, AnagramSolverExitCode
                .INPUT_OUTPUT);

        errorHandlerMap.put(AnagramSolverAppException.class, defaultHandler);
    }
}
