package com.groupeisi.minisystemebancaire.controllers.client;

import com.groupeisi.minisystemebancaire.dto.TransactionDTO;
import com.groupeisi.minisystemebancaire.services.TransactionService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class ClientTransactionsController {
    private final TransactionService transactionService = new TransactionService();

    private Long clientId; // ID du client connecté

    @FXML
    private ChoiceBox<String> transactionType;

    @FXML
    private TextField montantField, compteSourceField, compteDestField;

    @FXML
    private Button btnEffectuerTransaction, btnAnnulerTransaction, btnDepot, btnRetrait, btnVirement, btnTransactions, btnCredits, btnCartes, btnSupport, btnDeconnexion;

    @FXML
    private TableView<TransactionDTO> tableTransactions;

    @FXML
    private TableColumn<TransactionDTO, Long> colId;

    @FXML
    private TableColumn<TransactionDTO, String> colType;

    @FXML
    private TableColumn<TransactionDTO, Double> colMontant;

    @FXML
    private TableColumn<TransactionDTO, LocalDateTime> colDate;

    @FXML
    private TableColumn<TransactionDTO, String> colStatut;

    /**
     * ✅ Initialise la table des transactions et les choix de type de transaction
     */
    @FXML
    public void initialize() {
        if (transactionType != null) {
            transactionType.getItems().addAll("Dépôt", "Retrait", "Virement");
            transactionType.setValue("Dépôt"); // Valeur par défaut
        } else {
            System.out.println("⚠ Erreur : transactionType est NULL. Vérifie le fx:id dans le FXML.");
        }
        // Initialisation des colonnes de la table
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montant"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));

        // Initialisation des types de transactions
        transactionType.getItems().addAll("Dépôt", "Retrait", "Virement");
        transactionType.setValue("Dépôt"); // Valeur par défaut
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
        loadTransactions(clientId);
    }


    /**
     * ✅ Charge l'historique des transactions pour le client
     */
    public void loadTransactions(Long clientId) {
        this.clientId = clientId;
        afficherTransactions();
    }

    /**
     * ✅ Affiche les transactions du client
     */
    private void afficherTransactions() {
        List<TransactionDTO> transactions = transactionService.getTransactionsByClientId(clientId);

        // Nettoyer les anciennes données
        tableTransactions.getItems().clear();

        // Ajouter les nouvelles transactions
        tableTransactions.getItems().addAll(transactions);
    }

    /**
     * ✅ Gère une transaction (Dépôt, Retrait, Virement)
     */
    @FXML
    public void handleTransaction() {
        try {
            String type = transactionType.getValue();
            double montant = Double.parseDouble(montantField.getText().trim());
            Long compteSourceId = clientId;
            Long compteDestId = Long.parseLong(compteSourceField.getText().trim());;

            if (type.equals("Virement")) {
                if (compteDestField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer le compte du destinataire.");
                    return;
                }
                compteDestId = Long.parseLong(compteDestField.getText().trim());
            }

            TransactionDTO transaction = new TransactionDTO(
                    null,
                    type,
                    montant,
                    LocalDateTime.now(),
                    compteSourceId,
                    compteDestId,
                    "Validé"
            );

            transactionService.enregistrerTransaction(transaction);
            afficherTransactions(); // Rafraîchir la liste des transactions

            showAlert(Alert.AlertType.INFORMATION, "Transaction réussie", "Votre " + type + " de " + montant + " FCFA a été effectué !");

            // Effacer les champs après la transaction
            clearFields();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez entrer des valeurs valides.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite : " + e.getMessage());
        }
    }

    /**
     * ✅ Efface les champs après une transaction
     */
    @FXML
    private void clearFields() {
        montantField.clear();
        compteSourceField.clear();
        compteDestField.clear();
    }

    /**
     * ✅ Gère la navigation vers une autre interface
     */

    @FXML
    public void handleNavigation(String page) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/groupeisi/minisystemebancaire/client/" + page + ".fxml"));
            BorderPane pane = loader.load();

            // Récupérer le contrôleur de la nouvelle page
            Object controller = loader.getController();

            // Vérifier si le contrôleur a une méthode pour récupérer l'ID du client
            if (controller instanceof ClientTransactionsController) {
                ((ClientTransactionsController) controller).setClientId(clientId);
            } else if (controller instanceof ClientCreditsController) {
                ((ClientCreditsController) controller).setClientId(clientId);
            } else if (controller instanceof ClientDashboardController) {
                ((ClientDashboardController) controller).setClientId(clientId);
            } else if (controller instanceof ClientCartesController) {
                ((ClientCartesController) controller).setClientId(clientId);
            } else if (controller instanceof ClientSupportController) {
                ((ClientSupportController) controller).setClientId(clientId);
            }

            // Afficher la nouvelle scène
            Stage stage = (Stage) btnTransactions.getScene().getWindow();
            stage.setScene(new Scene(pane));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page.");
        }
    }



    /**
     * ✅ Gestion des boutons de navigation
     */
    @FXML
    public void goToDashboard() { handleNavigation("UI_Dashboard"); }
    @FXML
    public void goToTransactions() { handleNavigation("UI_Transactions"); }

    @FXML
    public void goToCredits() { handleNavigation("UI_Credits"); }

    @FXML
    public void goToCartes() { handleNavigation("UI_Cartes_Bancaires"); }

    @FXML
    public void goToSupport() { handleNavigation("UI_Service_Client"); }

    @FXML
    public void handleLogout() { handleNavigation("UI_Login"); }

    /**
     * ✅ Affiche une alerte
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
