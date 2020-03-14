package dev.houshce29.cc.common.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Service for file IO operations.
 * todo finish implementing for code generator and input reader
 */
public final class FileService {

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
