package com.groupeisi.minisystemebancaire.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class MainController {

    @FXML
    private Button btnAdmin;

    @FXML
    private Button btnClient;

    @FXML
    private void handleAdminClick(ActionEvent event) {
        chargerVue("/com/groupeisi/minisystemebancaire/admin/UI_Login.fxml", event);
    }

    @FXML
    private void handleClientClick(ActionEvent event) {
        chargerVue("/com/groupeisi/minisystemebancaire/client/UI_Login.fxml", event);
    }

    private void chargerVue(String fichierFXML, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fichierFXML));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
