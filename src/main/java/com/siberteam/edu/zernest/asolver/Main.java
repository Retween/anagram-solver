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

    public static void main(String[] args) {
        CommandLineParser parser = new CommandLineParser();
        try {
            parser.parseCommandLine(args);

            try (InputStream inputStream = new FileInputStream(parser
                    .getInputFile());
                 OutputStream outputStream = new FileOutputStream(parser
                         .getOutputFile())) {
                InputStreamToQueueReader reader =
                        new InputStreamToQueueReader(inputStream);

                ProducerConsumerStarter starter = new ProducerConsumerStarter(
                        reader.getUrlQueue(), parser.getProducersCount(),
                        parser.getConsumersCount());
                starter.startThreads();

                AnagramsToOutputStreamWriter writer =
                        new AnagramsToOutputStreamWriter(outputStream);

                writer.writeListToFile(starter.getAnagramsMap());
            }
        } catch (Exception e) {
            if (e instanceof ParseException) {
                parser.printHelp();
                defaultHandler.handleException(AnagramSolverExitCode
                        .COMMAND_LINE_USAGE, e);
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
}
