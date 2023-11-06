package com.psdat.slangdictionary;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SlangController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}