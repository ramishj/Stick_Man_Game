package com.example.firstjavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Stack;

public class HomePageController {
    @FXML
    private AnchorPane stackPane;
    public static int HighScore=0;
    @FXML
    private ImageView backgroundImage;

    @FXML
    private void initialize() {
        // Bind the size of the ImageView to the size of the StackPane
        backgroundImage.fitWidthProperty().bind(stackPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(stackPane.heightProperty());
    }
    @FXML
    private Button StartButton;


    @FXML
    private void startButtonClicked() {
        // Code to switch to the game screen
        System.out.println("Start button clicked!");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HomePage.class.getResource("GameScreen.fxml"));
            Parent gameScreenRoot = fxmlLoader.load();
            GameScreenController g = new GameScreenController();
            //retrieve current running stage

            Stage stage = (Stage) stackPane.getScene().getWindow();
            stage.setTitle("Game Screen!");
            stage.setScene(new Scene(gameScreenRoot, 800, 600));
            g.start(stage);
            stage.show();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    }
