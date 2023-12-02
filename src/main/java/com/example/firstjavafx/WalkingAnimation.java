package com.example.firstjavafx;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
public class WalkingAnimation extends Application {
    private static final int NUM_FRAMES = 6; // Number of frames in the animation
    private static final int FRAME_DURATION = 100; // Duration for each frame in milliseconds
    private static final double STEP_SIZE = 3.0; // Pixel movement per frame

    private ImageView imageView;
    private int currentFrame = 0; // Initialize currentFrame to 0
    private double positionX = -100; // Initialize the starting X position

    @Override
    public void start(Stage primaryStage) {
        // Load images
        Image[] frames = new Image[NUM_FRAMES];
        for (int i = 0; i < NUM_FRAMES; i++) {
            String imagePath = "walk" + (i + 1) + ".png";
            frames[i] = new Image(getClass().getResource(imagePath).toExternalForm());
        }

        // Create and configure ImageView
        imageView = new ImageView(frames[0]);
//        imageView.setX(0);
//        imageView.setY(0);
        imageView.setFitWidth(50); // Set your desired width
        imageView.setFitHeight(50); // Set your desired height
        imageView.setTranslateX(positionX); // Set initial translation
//        imageView.setX(-20);

        // Create a timeline to change frames and move the ImageView
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(FRAME_DURATION),
                e -> {
                    imageView.setImage(frames[currentFrame]);
                    currentFrame = (currentFrame + 1) % NUM_FRAMES;
                    System.out.println(imageView.getTranslateX());

                    // Update position for walking effect using translateX
                    imageView.setTranslateX(positionX);
//                    imageView.setTranslateX(-100);
                    positionX += STEP_SIZE;
                }
        ));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        // Create the scene
        StackPane root = new StackPane();
        root.getChildren().add(imageView);
        Scene scene = new Scene(root, 500, 300); // Adjust the scene width as needed

        // Set the stage
        primaryStage.setTitle("Walking Animation");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}

