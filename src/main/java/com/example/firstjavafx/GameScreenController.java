package com.example.firstjavafx;

import javafx.animation.*;
import javafx.geometry.Bounds;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.security.Key;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

public class GameScreenController extends Application{
    //+++++++++++++++++++++++++++++++++++++++
    private static final int NUM_FRAMES = 6; // Number of frames in the animation
    private static final int FRAME_DURATION = 80; // Duration for each frame in milliseconds
    private static final double STEP_SIZE = 10; // Pixel movement per frame

    private ImageView imageView;
    private int currentFrame = 0; // Initialize currentFrame to 0
    private double positionX = -100; // Initialize the starting X position
    //+++++++++++++++++++++++++++++++++++++++

    private boolean isSpaceBarPressed = false;
    ArrayList<GameObjects> gameObjects = new ArrayList<>();
    @FXML
    Rectangle pillar1, pillar2;
    Stick stick;
    @FXML
    private Text Score;
    private boolean stick_grown_once = false;
    private boolean isMoving = false;
    Scene scene;
    Diamonds diamond;
    History history = new History();
    Image[] frames;

    @FXML
    private Rectangle stickR;
    @FXML
    private ImageView player;
    boolean lost = false;
    private boolean isJumping = false;


    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private ImageView backgroundImage;

    private Rectangle tempP, tempS;
    int cnt = 0;

    private int score;
    private int flip_i = -1;
    private int gems;
    private boolean gameRunning;
    Point2D rootPaneCoords;

    // Methods for initializing and cleaning up the game screen
    private void initializeGameScreen() {
        score = 0;
        gems = 0;
        gameRunning = false;
    }

    private void cleanupGameScreen() {
        // Add cleanup logic for the game screen here
    }

    // Method to start the game
    @FXML
    private void startGame() {
        if (!gameRunning) {
            gameRunning = true;
            initializeGameScreen();
            updateGame();
        }
    }

    // Method to update the game state
    private void updateGame() {
        // Add code to update the game here
        Score.setText(String.format("%d", cnt*3));
    }


    private void growStick() {
        final double growthRate = 5.0;
        final double maxHeight = 500.0;

        // Increase stick height while space bar is pressed
        if ( isSpaceBarPressed && stickR.getHeight() < maxHeight) {
            double newHeight = stickR.getHeight() + growthRate;
            stickR.setHeight(newHeight);
            stickR.setY(stickR.getY()- growthRate);
        }



    }

    private void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.DOWN && !isJumping && isMoving) {

            ScaleTransition flip = new ScaleTransition(Duration.millis(100), imageView);
            flip.setToY(flip_i); // Flips vertically
            flip_i=-flip_i;

            TranslateTransition push = new TranslateTransition(Duration.millis(50), imageView);
            push.setByY((flip_i)*imageView.getFitHeight());

            push.play();
            flip.play();
        }

        if (event.getCode() == KeyCode.UP && !isJumping) {
            isJumping = true;


            TranslateTransition jumpUp = new TranslateTransition(Duration.millis(300), imageView);
            jumpUp.setByY(-80);
            jumpUp.setCycleCount(2);
            jumpUp.setAutoReverse(true);
            RotateTransition rotateTransition = new RotateTransition(Duration.millis(600), imageView);
            rotateTransition.setByAngle(360); // Set the rotation angle
            rotateTransition.setCycleCount(1); // Number of rotations


            jumpUp.setOnFinished(event1 -> {
                isJumping = false;
            });

            rotateTransition.play();
            jumpUp.play();
        }

        if (event.getCode() == KeyCode.SPACE) {
            isSpaceBarPressed = true;
            System.out.println("before " + stick_grown_once);
            if(!stick_grown_once){
                System.out.println("hifdf");
                growStick();
            }
        }
    }

    private void onKeyReleased(KeyEvent cevent) {

        if (!stick_grown_once && cevent.getCode() == KeyCode.SPACE) {
            isSpaceBarPressed = false;
            stick_grown_once = true;
            double initial_height = stickR.getHeight();
//            System.out.println(stickR.getX());
//            System.out.println(stickR.getY());
            double pivotX = stickR.getX() + (stickR.getWidth() / 2); // Calculate the center X
            double pivotY = stickR.getY() + stickR.getHeight(); // Set pivot at the bottom of the stick
//            System.out.println(stickR.getY());
            Rotate rotate = new Rotate(90, pivotX, pivotY); // Create a Rotate transform
            stickR.getTransforms().add(rotate); // Apply the rotation to the rectangle
//            System.out.println(stickR.getY());

            double targetAngle = 90; // Final angle to rotate
            Duration duration = Duration.seconds(0.8); // Duration of the rotation

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(rotate.angleProperty(), 0)),
                    new KeyFrame(duration, new KeyValue(rotate.angleProperty(), targetAngle))
            );

            timeline.play(); // Start the rotation animation
            System.out.println(stickR.getY());

            System.out.println("stick height: "+initial_height);


            System.out.println();
            timeline.setOnFinished(event -> {
                double distance = 0;
                if(stickR.getX()+stickR.getHeight()>pillar2.getX() && stickR.getX()+stickR.getHeight()<pillar2.getX()+pillar2.getWidth()){
                    System.out.println("in between");
                    System.out.println("player x: "+ imageView.getX());
                    System.out.println("pillar2 x: "+ imageView.getX());
                    distance = pillar2.getX() + pillar2.getWidth() - imageView.getX()-25 -imageView.getTranslateX();
                }
                else {

                    distance = stickR.getX()+stickR.getHeight() -imageView.getX()-5-imageView.getTranslateX() ;

                    lost = true;
                }

                moveCharacter2(distance);
            });

        }
    }

    @FXML
    private void growPillar() {
        // Add logic to grow the pillar here
    }

    private void jump() {
        isJumping = true;


        TranslateTransition jumpUp = new TranslateTransition(Duration.millis(200), imageView);
        jumpUp.setByY(-50);
        jumpUp.setCycleCount(2);
        jumpUp.setAutoReverse(true);


        jumpUp.setOnFinished(e -> {
            isJumping = false;
        });

        jumpUp.play();
    }
    private void moveCharacter2(double distance) {
//        System.out.println("flsjdflsdjflsjf");
        positionX = imageView.getTranslateX();
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(FRAME_DURATION),
                e -> {
                    imageView.setImage(frames[currentFrame]);
                    currentFrame = (currentFrame + 1) % NUM_FRAMES;
                    System.out.println(imageView.getTranslateX());
                    System.out.println(stick_grown_once);

                    // Update position for walking effect using translateX
                    imageView.setTranslateX(positionX);
//                    imageView.setTranslateX(-100);
                    positionX += STEP_SIZE;
                }
        ));
        timeline.setCycleCount((int)(distance/STEP_SIZE));
        isMoving = true;
        timeline.play();
        timeline.setOnFinished(event -> {
            isMoving = false;
            imageView.setImage(frames[0]);
//            System.out.println("after transition player x : "+player.getX() + " " + player.getLayoutX() + " " + player.getTranslateX());


            if(lost){
                System.out.println("GAME OVER!");
                System.out.println("your score : " + cnt*3);

                double pivotX = stickR.getX() + (stickR.getWidth()); // Calculate the center X HEHE
                double pivotY = stickR.getY() + stickR.getHeight(); // Set pivot at the bottom of the stick
//            System.out.println(stickR.getY());
                Rotate rotate = new Rotate(90, pivotX, pivotY); // Create a Rotate transform
                stickR.getTransforms().add(rotate); // Apply the rotation to the rectangle
//            System.out.println(stickR.getY());

                double targetAngle = 90; // Final angle to rotate
                Duration dur = Duration.seconds(1); // Duration of the rotation

                Timeline fall_stick_timeline = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(rotate.angleProperty(), 0)),
                        new KeyFrame(dur, new KeyValue(rotate.angleProperty(), targetAngle))
                );

                TranslateTransition char_fall = new TranslateTransition(dur, imageView);
                char_fall.setByY(imageView.getY());
                RotateTransition rotateTransition = new RotateTransition(Duration.millis(900), imageView);
                rotateTransition.setByAngle(360); // Set the rotation angle
                rotateTransition.setCycleCount(1); // Number of rotations
                fall_stick_timeline.play(); // Start the rotation animation
                char_fall.play();
                rotateTransition.play();
                fall_stick_timeline.setOnFinished(event1 -> {

                System.exit(0);
                });
            }
            rootAnchorPane.getChildren().remove(tempS);
            rootAnchorPane.getChildren().remove(tempP);

            if(!lost){
                shiftAllElementsSmoothly(pillar2.getX() - pillar1.getX(), 0.4);

            }

        });

    }
    private void shiftAllElementsSmoothly(double distance, double t) {
        Duration duration = Duration.seconds(t); // Duration for the translation animation

        // Create TranslateTransition for pillar1
        TranslateTransition pillar1Transition = new TranslateTransition(duration, pillar1);
        pillar1Transition.setByX(-distance);

        // Create TranslateTransition for pillar2
        TranslateTransition pillar2Transition = new TranslateTransition(duration, pillar2);
        pillar2Transition.setByX(-distance);
//        pillar2.setLayoutX(pillar2.getY()-distance);

        // Create TranslateTransition for the stick
        TranslateTransition stickTransition = new TranslateTransition(duration, stickR);
        stickTransition.setByX(-distance);

        TranslateTransition characterTransition = new TranslateTransition(duration, imageView);
        characterTransition.setByX(-distance);

        ParallelTransition parallelTransition;

        parallelTransition = new ParallelTransition(pillar1Transition, pillar2Transition, stickTransition, characterTransition);

        parallelTransition.play();

        parallelTransition.setOnFinished(event -> {

            createNewPillar();
            updateGame();
        });
    }


    private void createNewPillar(){
        double ht = pillar2.getHeight();
        double wt = pillar2.getWidth();
        Paint[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.PURPLE, Color.ORANGE, Color.PINK};
        Random random = new Random();
        //Get a random No. between 0 and 6
        int index = random.nextInt(7);
        Paint old = pillar2.getFill();
        Paint color = colors[index];
        double x =94;
//        System.out.println("x :"+x);
        double y = 415;
//        rootAnchorPane.getChildren().remove(pillar1);
        cnt++;
        tempP = pillar1;
        rootAnchorPane.getChildren().remove(pillar2);
        pillar1 = new Rectangle();
        pillar2 = new Rectangle();
        rootAnchorPane.getChildren().add(pillar2);
        rootAnchorPane.getChildren().add(pillar1);
        pillar1.setFill(old);
        pillar1.setX(x);
        pillar1.setY(y);
        pillar1.setHeight(ht);
        pillar1.setWidth(wt);
        pillar2.setFill(color);


        double offset = x + 15 + 100*random.nextDouble();//ensure ths is withing bounds later
//        System.out.println(offset);
        pillar2.setX(x+offset);
        pillar2.setY(y);
        pillar2.setHeight(0);
        double width = 25 + 65*random.nextDouble();


        pillar2.setWidth(width);
        // Use Timeline to animate the height of the pillar
        Duration duration = Duration.seconds(0.75);
        KeyValue keyValue = new KeyValue(pillar2.heightProperty(), ht, Interpolator.EASE_BOTH);
        KeyFrame keyFrame = new KeyFrame(duration, keyValue);
        Timeline timeline = new Timeline(keyFrame);
        timeline.play();



        Rectangle newstick = new Rectangle();
//        pillar2.getLayoutX()+pillar2.getTranslateX()+pillar2.getWidth()-2, pillar2.getLayoutY(), stickR.getWidth(), 0
        newstick.setX(x+pillar1.getWidth()-2);
        newstick.setY(y);
        newstick.setWidth(2);
        newstick.setHeight(0);
        newstick.setFill(stickR.getFill());
//        rootAnchorPane.getChildren().remove(stickR);
        rootAnchorPane.getChildren().add(newstick);
        newstick.setFill(stickR.getFill());
//        System.out.println("new stick " + newstick.getLayoutX());
//        System.out.println("new pillar - " + pillar2.getLayoutX());
//        rootAnchorPane.getChildren().remove(stickR);
        tempS = stickR;
        stickR = newstick;
//        sou
        System.out.println(stick_grown_once);
        stick_grown_once = false;
        System.out.println(stick_grown_once);

    }
    @FXML
    private void jumpCharacter() {
        // Add logic to jump the character here
    }

    @FXML
    private void shiftPillars() {
        // Add logic to shift the pillars here
    }

    @FXML
    private void reviveCharacter() {
        // Add logic to revive the character here
    }

    // Other necessary methods (if any)

    @Override
    public void start(Stage stage) throws IOException {

        System.out.println("hello");
        frames = new Image[NUM_FRAMES];
        for (int i = 0; i < NUM_FRAMES; i++) {
            String imagePath = "walk" + (i + 1) + ".png";
            frames[i] = new Image(getClass().getResource(imagePath).toExternalForm());
        }


//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameScreen.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("GameScreen.fxml"));

        scene = new Scene(root, 1000, 1000.0);
        stickR = (Rectangle) scene.lookup("#stickR");
//        player = (ImageView) scene.lookup(("#player"));
        pillar2 = (Rectangle)scene.lookup("#pillar2");

//        System.out.println("at start: "+pillar2.getLayoutX());
        pillar1 = (Rectangle)scene.lookup("#pillar1");
        rootAnchorPane = (AnchorPane)scene.lookup("#rootAnchorPane");

        imageView = new ImageView(frames[0]);
        imageView.setFitWidth(40); // Set your desired width
        imageView.setFitHeight(40); // Set your desired height
        rootAnchorPane.getChildren().add(imageView);

        imageView.setX(105);
        imageView.setY(375);

        scene.setOnKeyPressed(this::onKeyPressed);
        scene.setOnKeyReleased(this::onKeyReleased);

//        System.out.println("hdflk");
            Score = (Text) scene.lookup("#Score");

        stage.setTitle("Game Screen!");
        stage.setMinWidth(467); // Set minimum width
        stage.setMaxWidth(467); // Set maximum width
        stage.setMinHeight(582); // Set minimum height
        stage.setMaxHeight(582); // Set maximum height
        stage.setScene(scene);
//        stick = new Stick(1, 10, 10, 100, 1);
//        stickR.setX(stick.getX());
//        stickR.setY(stick.getY());
//        stickR.setHeight(stick.getHeight());
//        stickR.setWidth((stick.getWidth()));


        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
