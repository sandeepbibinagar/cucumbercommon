package com.experian.common.helpers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class ExecuteCommand {
    public static final String DEFAULT_DIR = System.getProperty("java.io.tmpdir");
    private File outFile, outErrorFile;
    private ProcessBuilder pb;

    public ExecuteCommand() {
        outFile = new File(DEFAULT_DIR + File.separator + UUID.randomUUID().toString());
        outErrorFile = new File(DEFAULT_DIR + File.separator + UUID.randomUUID().toString());
        System.out.println(DEFAULT_DIR);
        pb = new ProcessBuilder()
                .inheritIO()
                .redirectOutput(outFile)
                .redirectError(outErrorFile);
    }

    private boolean errorPatternMatch(final Path outputFile, final String regex) throws java.io.IOException{
        Pattern p = Pattern.compile(regex);
        boolean foundError = false;

        try (Stream<String> lines = Files.lines(outputFile)) {
            Optional<String> message = lines.filter(p.asPredicate()).findFirst();
            if (message.isPresent()) {
                Matcher matcher = p.matcher(message.get());
                matcher.find();
                String group = matcher.group(0);
                System.out.println(group);
            }
            foundError = message.isPresent();
        }

        return foundError;
    }

    public void setDirectory(String dir) {
        pb.directory(new File(dir));
    }

    public boolean start(String regex, List<String> commands) throws IOException, InterruptedException {
        pb.command(commands);
        Process process = pb.start();
        process.waitFor();

        return !errorPatternMatch(outErrorFile.toPath(), regex) &&
                !errorPatternMatch(outFile.toPath(), regex);
    }

    public static boolean run(String dir, String regex, List<String> commands) throws IOException, InterruptedException {
        ExecuteCommand c = new ExecuteCommand();
        c.setDirectory(dir);
        return c.start(regex, commands);
    }

    public static boolean run(String regex, List<String> commands) throws IOException, InterruptedException {
        return ExecuteCommand.run(ExecuteCommand.DEFAULT_DIR, regex, commands);
    }

    public static boolean run(String dir, String regex, String command) throws IOException, InterruptedException {
        return ExecuteCommand.run(dir, regex,
                new ArrayList<String>(Arrays.asList(command.split(" "))));
    }

    public static boolean run(String regex, String command) throws IOException, InterruptedException {
        return ExecuteCommand.run(ExecuteCommand.DEFAULT_DIR, regex,
                new ArrayList<String>(Arrays.asList(command.split(" "))));
    }

    public static boolean run(String command) throws IOException, InterruptedException {
        return ExecuteCommand.run(ExecuteCommand.DEFAULT_DIR, "(?i)error",
                new ArrayList<String>(Arrays.asList(command.split(" "))));
    }
}
