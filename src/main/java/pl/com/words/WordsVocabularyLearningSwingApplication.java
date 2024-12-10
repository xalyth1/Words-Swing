package pl.com.words;

import pl.com.words.configuration.ConfigLoader;
import pl.com.words.gui.GUI;
import pl.com.words.persistence.Database;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class WordsVocabularyLearningSwingApplication {
    public static void main(String[] args) {
        if (ConfigLoader.getInstance().getProperty("words.service.url").equals("http://localhost:8080/")) {
            startLocalWordsServiceLocallyForTesting();
        }

        SwingUtilities.invokeLater(new GUI());
    }


    private static void startLocalWordsServiceLocallyForTesting() {
        String jarPath = ConfigLoader.getInstance().getProperty("words.service.localAppName.path");
        System.out.println(jarPath);
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", jarPath);
        processBuilder.environment().put("SPRING_PROFILES_ACTIVE", "development");
        Process process = null;
        try {
            process = processBuilder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            int exitCode = process.waitFor();
            System.out.println("Words-Service-Local-Test instance application exited with code: " + exitCode);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null && process.isAlive()) {
                process.destroy();
                System.out.println("Words-Service-Local-Test instance application process terminated.");
            }
        }

    }
}
