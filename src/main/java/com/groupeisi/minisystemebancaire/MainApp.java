package com.groupeisi.minisystemebancaire;

import com.groupeisi.minisystemebancaire.models.Admin;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 📌 Ajoute un admin par défaut
        ajouterAdminParDefaut();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/groupeisi/minisystemebancaire/UI_Main.fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setTitle("Mini Système Bancaire - Connexion");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ajouterAdminParDefaut() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("banquePU");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // Vérifier si un admin existe déjà
            Long count = em.createQuery("SELECT COUNT(a) FROM Admin a", Long.class).getSingleResult();

            if (count == 0) {
                // Créer un admin par défaut
                Admin admin = new Admin();
                admin.setUsername("admin");
                admin.setPassword("admin123"); // ⚠️ Pense à hasher le mot de passe en production
                admin.setRole("ADMIN");

                em.persist(admin);
                System.out.println("✅ Admin par défaut ajouté !");
            } else {
                System.out.println("✅ Un admin existe déjà, pas besoin d'ajout.");
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }

    public static void main(String[] args) {
        launch(args); // Démarre l'application JavaFX
    }
}
