package dev.houshce29.cc.common.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Service for file IO operations.
 */
public final class FileService {
    public static final String DEFAULT_LINE_BREAK_DELIMITER = "\n";

    /**
     * Converts the path string into a file.
     * @param path String representing a file.
     * @return File object from the path string.
     */
    public static File getFile(String path) {
        return Paths.get(path).toFile();
    }

    /**
     * Scans deeply to gather all files of the given extension.
     * @param start Root directory.
     * @param fileExtension File extension (including dot is optional).
     * @return Set of all files of the given extension.
     */
    public static Set<File> scan(File start, String fileExtension) {
        String extension = fileExtension;
        if (!fileExtension.startsWith(".")) {
            extension = "." + fileExtension;
        }
        return scanDeep(start, extension);
    }

    /**
     * Reads the given file and applies the defined line break delimiter
     * between each line.
     * @param file File to read from.
     * @param lineBreakDelimiter Character(s) to delimit line breaks with.
     * @return Full string contents of file.
     */
    public static String read(File file, String lineBreakDelimiter) {
        StringBuilder builder = new StringBuilder();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine());
                if (scanner.hasNextLine()) {
                    builder.append(lineBreakDelimiter);
                }
            }
        }
        catch (FileNotFoundException ex) {
            throw new IllegalArgumentException("Could not find the specified file.", ex);
        }
        return builder.toString();
    }

    /**
     * Reads the given file and applies the default line break delimiter
     * between each line (\n).
     * @param file File to read from.
     * @return Full string contents of file.
     */
    public static String read(File file) {
        return read(file, DEFAULT_LINE_BREAK_DELIMITER);
    }

    /**
     * Reads the first line from the given file.
     * @param file File to read from.
     * @return Content.
     */
    public static String readFirstLine(File file) {
        try (Scanner scanner = new Scanner(file)) {
            if (scanner.hasNextLine()) {
                return scanner.nextLine();
            }
        }
        catch (FileNotFoundException ex) {
            // Swallow
        }
        return "";
    }

    /**
     * Writes the content to the given file. If the value of append is true,
     * then the content will be appended to the file if it already exists.
     * @param file File to write to. This may or may not exist. If this file
     *             does not already exist, the file should contain the full
     *             path.
     * @param content Content to write.
     * @param append Whether or not to append to the file if it already exists.
     */
    public static void write(File file, String content, boolean append) {
        try {
            Path path = Paths.get(file.getParent() != null ? file.getParent() : "");
            if (Files.notExists(path)) {
                Files.createDirectories(path);
            }
            try (FileWriter writer = new FileWriter(file, append)) {
                writer.write(content);
            }
        }
        catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    /**
     * Writes the content to the given file. If the file already exists,
     * it will be over written.
     * @param file File to write to.
     * @param content Content to write.
     */
    public static void write(File file, String content) {
        write(file, content, false);
    }

    /**
     * Removes the file if it exists.
     * @param file File to remove.
     */
    public static void remove(File file) {
        try {
            Files.deleteIfExists(file.toPath());
        }
        catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private static Set<File> scanDeep(File start, String fileExtension) {
        Set<File> files = new HashSet<>();
        File[] children = start.listFiles();
        if (!start.isDirectory() || children == null) {
            return files;
        }
        for (File file : children) {
            if (file.isDirectory()) {
                files.addAll(scanDeep(file, fileExtension));
            }
            else if (file.getName().endsWith(fileExtension)) {
                files.add(file);
            }
        }

        return files;
    }
}
