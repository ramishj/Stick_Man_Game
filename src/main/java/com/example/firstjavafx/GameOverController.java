package com.example.firstjavafx;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class GameOverController extends Application {
    @FXML
    private ImageView backgroundImage;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Text currentScore;
    @FXML
    private Text HighScore;
    public void start(Stage stage,int Score) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameOver.fxml"));
        Parent gameOver = fxmlLoader.load();

        // Access the controller to perform any initialization if needed
        GameOverController controller = fxmlLoader.getController();
        currentScore = (Text) gameOver.lookup("#currentScore");
        HighScore = (Text) gameOver.lookup("#HighScore");

       currentScore.setText(Integer.toString(Score));

       if(Score>HomePageController.HighScore){
           HighScore.setText(Integer.toString(Score));
           HomePageController.HighScore=Score;
       }
       else {
           HighScore.setText(Integer.toString(HomePageController.HighScore));
       }


        controller.initialize(); // Call your initialize method if necessary

        stage.setTitle("Game Over Screen");
        stage.setScene(new Scene(gameOver, 467  , 582));
        stage.show();
    }

    private void initialize() {
        // Bind the size of the ImageView to the size of the AnchorPane
        backgroundImage.fitWidthProperty().bind(anchorPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(anchorPane.heightProperty());
    }
    public void Restart(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HomePage.class.getResource("GameScreen.fxml"));
            Parent gameScreenRoot = fxmlLoader.load();
            GameScreenController g = new GameScreenController();
            //retrieve current running stage

            Stage stage = (Stage) anchorPane.getScene().getWindow();
            stage.setTitle("Game Screen!");
            stage.setScene(new Scene(gameScreenRoot, 467, 582));
            g.start(stage);
            stage.show();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
    public void Exit(){
        System.exit(0);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}
