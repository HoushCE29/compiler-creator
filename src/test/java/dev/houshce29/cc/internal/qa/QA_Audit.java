package dev.houshce29.cc.internal.qa;

import dev.houshce29.cc.common.utils.FileService;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QA_Audit {
    private static final Set<Class<? extends Annotation>> AUDITS = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(BadPractice.class, Complex.class)));
    private static final Path ROOT = Paths.get("", "src", "main", "java", "dev", "houshce29", "cc");
    private static final Pattern PACKAGE_REGEX = Pattern.compile("package\\s+([a-zA-Z0-9_$.]+)\\s*;");
    private static final double MAX_PERCENTAGE = 0.25;

    @Test
    public void audit() {
        Statistics stats = loadStatistics();
        double actualPercentage = stats.getAudits().size() / ((double) stats.getScanned());
        Assert.assertTrue(actualPercentage < MAX_PERCENTAGE);
        System.out.println(stats.getAudits());
    }

    private Statistics loadStatistics() {
        Statistics stats = new Statistics();
        Set<File> javaFiles = FileService.scan(new File(ROOT.toUri()), ".java");
        for (File file : javaFiles) {
            Matcher matcher = PACKAGE_REGEX.matcher(FileService.readFirstLine(file).trim());
            if (!matcher.matches()) {
                continue;
            }
            String simpleClassName = file.getName().substring(0, file.getName().length() - 5);
            stats.push(find(matcher.group(1) + "." + simpleClassName));
            stats.countScan();
        }

        return stats;
    }

    private Collection<AuditEntry> find(String name) {
        List<AuditEntry> audits = new ArrayList<>();
        Class<?> clazz = findClass(name);
        if (clazz == null) {
            return audits;
        }
        for (Method method : clazz.getDeclaredMethods()) {
            // method.setAccessible(true);
            for (Annotation annotation : method.getDeclaredAnnotations()) {
                if (AUDITS.stream().anyMatch(an -> an.isInstance(annotation))) {
                    audits.add(new AuditEntry(method, annotation.toString()));
                }
            }
        }
        return audits;
    }

    private Class<?> findClass(String name) {
        try {
            return Class.forName(name);
        }
        catch (ClassNotFoundException ex) {
            System.out.println("Could not find " + name);
            return null;
        }
    }

    private static final class Statistics {
        private final List<AuditEntry> audits = new ArrayList<>();
        private int scanned = 0;

        public int getScanned() {
            return scanned;
        }

        public void countScan() {
            scanned++;
        }

        public List<AuditEntry> getAudits() {
            return audits;
        }

        public void push(Collection<AuditEntry> entries) {
            audits.addAll(entries);
        }
    }

    private static final class AuditEntry {
        private final Method method;
        private final String data;

        public AuditEntry(Method method, String data) {
            this.method = method;
            this.data = data;
        }

        public Method getMethod() {
            return method;
        }

        public String getData() {
            return data;
        }

        @Override
        public String toString() {
            return getMethod().toString() + " ~ " + getData() + "\n";
        }
    }
}
