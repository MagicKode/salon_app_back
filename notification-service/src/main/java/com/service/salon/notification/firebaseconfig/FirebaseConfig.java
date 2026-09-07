package com.service.salon.notification.firebaseconfig;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.service-account.file:firebase-service-account.json}")
    private String firebaseCredentialsPath;

    @PostConstruct
    public void initialize() {
        try {
            System.out.println("🔍 Loading Firebase credentials from: " + firebaseCredentialsPath);

            InputStream serviceAccount;

            // Определяем тип пути
            if (firebaseCredentialsPath.startsWith("classpath:")) {
                // Путь из classpath (resources)
                String path = firebaseCredentialsPath.substring("classpath:".length());
                serviceAccount = new ClassPathResource(path).getInputStream();
            } else if (firebaseCredentialsPath.startsWith("/") || firebaseCredentialsPath.contains(":")) {
                // Абсолютный путь (в Docker или на диске)
                serviceAccount = new FileInputStream(firebaseCredentialsPath);
            } else {
                // Относительный путь — пробуем как classpath
                serviceAccount = new ClassPathResource(firebaseCredentialsPath).getInputStream();
            }

            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("✅ Firebase application has been initialized successfully!");
            } else {
                System.out.println("ℹ️ Firebase already initialized");
            }
        } catch (IOException e) {
            System.err.println("❌ Failed to initialize Firebase: " + e.getMessage());
            System.err.println("⚠️  Continuing without Firebase (push notifications will not work)");
            // Не выбрасываем исключение, чтобы приложение запустилось даже без Firebase
        }
    }
}
