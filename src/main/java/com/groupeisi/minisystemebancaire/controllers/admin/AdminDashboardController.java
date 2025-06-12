package com.groupeisi.minisystemebancaire.controllers.admin;

import com.groupeisi.minisystemebancaire.dto.ClientDTO;
import com.groupeisi.minisystemebancaire.dto.CompteDTO;
import com.groupeisi.minisystemebancaire.dto.TransactionDTO;
import com.groupeisi.minisystemebancaire.dto.CarteBancaireDTO;
import com.groupeisi.minisystemebancaire.dto.TicketSupportDTO;
import com.groupeisi.minisystemebancaire.services.ClientService;
import com.groupeisi.minisystemebancaire.services.CompteService;
import com.groupeisi.minisystemebancaire.services.TransactionService;
import com.groupeisi.minisystemebancaire.services.CarteBancaireService;
import com.groupeisi.minisystemebancaire.services.TicketSupportService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class AdminDashboardController {

    private final ClientService clientService = new ClientService();
    private final CompteService compteService = new CompteService();
    private final TransactionService transactionService = new TransactionService();
    private final CarteBancaireService carteBancaireService = new CarteBancaireService();
    private final TicketSupportService ticketSupportService = new TicketSupportService();

    @FXML private Label lblNbClients, lblNbComptes, lblNbTransactions, lblNbCartes;
    @FXML private TableView<TransactionDTO> tableOperationsSuspectes;
    @FXML private TableColumn<TransactionDTO, Long> colIdOpSuspecte;
    @FXML private TableColumn<TransactionDTO, Double> colMontantOpSuspecte;
    @FXML private TableColumn<TransactionDTO, String> colTypeOpSuspecte, colCompteSourceOp, colDateOpSuspecte;
    @FXML private TableView<TicketSupportDTO> tableReclamations;
    @FXML private TableColumn<TicketSupportDTO, Long> colIdReclamation;
    @FXML private TableColumn<TicketSupportDTO, String> colClientReclamation, colSujetReclamation, colStatutReclamation;
    @FXML private ChoiceBox<String> choiceCompteFrais, choiceTypeFrais;
    @FXML private TextField txtMontantFrais;
    @FXML private Button btnAppliquerFrais, btnRepondreReclamation, btnResoudreReclamation, btnRafraichirDashboard, btnDeconnexion;

    /**
     * ✅ Initialisation du tableau et chargement des statistiques
     */
    @FXML
    public void initialize() {
        colIdOpSuspecte.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMontantOpSuspecte.setCellValueFactory(new PropertyValueFactory<>("montant"));
        colTypeOpSuspecte.setCellValueFactory(new PropertyValueFactory<>("type"));
        colCompteSourceOp.setCellValueFactory(new PropertyValueFactory<>("compteSourceId"));
        colDateOpSuspecte.setCellValueFactory(new PropertyValueFactory<>("date"));

        colIdReclamation.setCellValueFactory(new PropertyValueFactory<>("id"));
        colClientReclamation.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        colSujetReclamation.setCellValueFactory(new PropertyValueFactory<>("sujet"));
        colStatutReclamation.setCellValueFactory(new PropertyValueFactory<>("statut"));

        loadStatistics();
        loadOperationsSuspectes();
        loadReclamations();
        loadComptesDansChoiceBox();
    }

    /**
     * ✅ Chargement des statistiques générales
     */
    private void loadStatistics() {
        lblNbClients.setText(String.valueOf(clientService.getAllClients().size()));
        lblNbComptes.setText(String.valueOf(compteService.getAllComptes().size()));
        lblNbTransactions.setText(String.valueOf(transactionService.getAllTransactions().size()));
        lblNbCartes.setText(String.valueOf(carteBancaireService.getAllCartes().size()));
    }

    /**
     * ✅ Chargement des opérations suspectes
     */
    private void loadOperationsSuspectes() {
        List<TransactionDTO> operationsSuspectes = transactionService.getTransactionsSuspectes();
        tableOperationsSuspectes.getItems().setAll(operationsSuspectes);
    }

    /**
     * ✅ Chargement des réclamations clients
     */
    private void loadReclamations() {
        List<TicketSupportDTO> reclamations = ticketSupportService.getAllTickets();
        tableReclamations.getItems().setAll(reclamations);
    }

    /**
     * ✅ Changer de vue en fermant la fenêtre actuelle
     */
    private void changerDeVue(ActionEvent event, String fichierFXML) {
        try {
            // Fermer la fenêtre actuelle
            Stage stageActuel = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stageActuel.close();

            // Charger la nouvelle vue
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fichierFXML));
            Scene scene = new Scene(loader.load());
            Stage nouveauStage = new Stage();
            nouveauStage.setTitle("Mini Système Bancaire");
            nouveauStage.setScene(scene);
            nouveauStage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la vue : " + fichierFXML);
            e.printStackTrace();
        }
    }

    /**
     * ✅ Navigation vers Gestion des Clients
     */
    @FXML
    public void handleGestionClients(ActionEvent event) {
        changerDeVue(event, "/com/groupeisi/minisystemebancaire/admin/UI_Gestion_Clients.fxml");
    }

    /**
     * ✅ Navigation vers Gestion des Comptes
     */
    @FXML
    public void handleGestionComptes(ActionEvent event) {
        changerDeVue(event, "/com/groupeisi/minisystemebancaire/admin/UI_Gestion_Comptes_Bancaires.fxml");
    }

    /**
     * ✅ Navigation vers Gestion des Transactions
     */
    @FXML
    public void handleGestionTransactions(ActionEvent event) {
        changerDeVue(event, "/com/groupeisi/minisystemebancaire/admin/UI_Gestion_Transactions.fxml");
    }

    /**
     * ✅ Navigation vers Gestion des Crédits
     */
    @FXML
    public void handleGestionCredits(ActionEvent event) {
        changerDeVue(event, "/com/groupeisi/minisystemebancaire/admin/UI_Gestion_Credits.fxml");
    }

    /**
     * ✅ Navigation vers Gestion des Cartes Bancaires
     */
    @FXML
    public void handleGestionCartes(ActionEvent event) {
        changerDeVue(event, "/com/groupeisi/minisystemebancaire/admin/UI_Gestion_Cartes_Bancaires.fxml");
    }

    /**
     * ✅ Navigation vers Service Client & Rapports
     */
    @FXML
    public void handleGestionSupport(ActionEvent event) {
        changerDeVue(event, "/com/groupeisi/minisystemebancaire/admin/UI_Service_Client_Rapports.fxml");
    }

    /**
     * ✅ Navigation vers le Dashboard
     */
    @FXML
    public void handleDashboard(ActionEvent event) {
        changerDeVue(event, "/com/groupeisi/minisystemebancaire/admin/UI_Dashboard.fxml");
    }

    @FXML
    public void handleDeconnexion(ActionEvent event) {
        Stage stage = (Stage) btnDeconnexion.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleRepondreReclamation() {
        TicketSupportDTO selectedTicket = tableReclamations.getSelectionModel().getSelectedItem();
        if (selectedTicket == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner une réclamation.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Répondre à une réclamation");
        dialog.setHeaderText("Répondre à la réclamation ID: " + selectedTicket.getId());
        dialog.setContentText("Votre réponse:");

        dialog.showAndWait().ifPresent(response -> {
            selectedTicket.setReponse(response);
            selectedTicket.setStatut("Répondu");
            ticketSupportService.updateTicket(selectedTicket);
            loadReclamations();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Réponse envoyée avec succès !");
        });
    }

    @FXML
    public void handleResoudreReclamation() {
        TicketSupportDTO selectedTicket = tableReclamations.getSelectionModel().getSelectedItem();
        if (selectedTicket == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner une réclamation.");
            return;
        }

        selectedTicket.setStatut("Résolu");
        ticketSupportService.updateTicket(selectedTicket);
        loadReclamations();
        showAlert(Alert.AlertType.INFORMATION, "Succès", "La réclamation ID: " + selectedTicket.getId() + " a été résolue.");
    }


    /**
     * ✅ Chargement des comptes disponibles pour appliquer des frais
     */
    private void loadComptesDansChoiceBox() {
        List<CompteDTO> comptes = compteService.getAllComptes();
        for (CompteDTO compte : comptes) {
            choiceCompteFrais.getItems().add(compte.getNumero()); // Ajoute le numéro du compte
        }
        choiceTypeFrais.getItems().addAll("Frais de maintenance", "Frais de virement", "Frais de gestion");
    }

    /**
     * ✅ Appliquer des frais bancaires
     */
    @FXML
    public void handleAppliquerFrais(ActionEvent event) {
        String compteNumero = choiceCompteFrais.getValue();
        String typeFrais = choiceTypeFrais.getValue();
        String montantStr = txtMontantFrais.getText().trim();

        if (compteNumero == null || typeFrais == null || montantStr.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            double montant = Double.parseDouble(montantStr);

            // Appliquer les frais au compte concerné
            CompteDTO compte = compteService.getCompteByNumero(compteNumero);
            if (compte == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Compte non trouvé.");
                return;
            }

            compteService.appliquerFrais(compte.getId(), montant); // Correspond exactement à la méthode


            showAlert(Alert.AlertType.INFORMATION, "Succès", "Frais de " + montant + " appliqué au compte " + compteNumero);

            // Rafraîchir le tableau des comptes après application des frais
            handleRafraichirDashboard();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le montant doit être un nombre valide.");
        }
    }

    /**
     * ✅ Rafraîchir le dashboard
     */
    @FXML
    public void handleRafraichirDashboard() {
        loadStatistics();
        loadOperationsSuspectes();
        loadReclamations();
        showAlert(Alert.AlertType.INFORMATION, "Actualisation", "Le tableau de bord a été mis à jour.");
    }

    /**
     * ✅ Afficher une alerte
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
