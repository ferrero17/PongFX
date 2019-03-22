package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application {

    final int SCENE_TAM_X = 600;
    final int SCENE_TAM_Y = 400;
    final int STICK_WIDTH = 7;
    final int STICK_HEIGHT = 50;

    int stickPosY = (SCENE_TAM_Y-STICK_HEIGHT) /2;

    int ballCenterX = 10;
    int ballCurrentSpeedX = 3;

    int ballCenterY = 30;
    int ballCurrentSpeedY = 3;

    @Override
    public void start(Stage primaryStage) throws Exception {


        Pane root = new Pane();

        Scene scene = new Scene(root, SCENE_TAM_X, SCENE_TAM_Y, Color.BLACK);

        primaryStage.setTitle("PONG-FX");
        primaryStage.setScene(scene);
        primaryStage.show();

        Circle circleBall = new Circle(ballCenterX,ballCenterY,7,Color.WHITE);
        Rectangle rectStick = new Rectangle(SCENE_TAM_X*0.9,stickPosY,STICK_WIDTH,STICK_HEIGHT);
        rectStick.setFill(Color.WHITE);
        // Añadimos los elementos stick y ball al elemento root.
        root.getChildren().add(rectStick);
        root.getChildren().add(circleBall);


        AnimationTimer animationBall = new AnimationTimer() {
            @Override
            public void handle(long now) {

                circleBall.setCenterX(ballCenterX);
                ballCenterX+= ballCurrentSpeedX;

                circleBall.setCenterY(ballCenterY);
                ballCenterY+= ballCurrentSpeedY;


                if (ballCenterX >= SCENE_TAM_X){
                    ballCurrentSpeedX = -3;
                }

                if (ballCenterX <= 0){
                    ballCurrentSpeedX = 3;
                }

                if (ballCenterY >= SCENE_TAM_Y){
                    ballCurrentSpeedY = -3;
                }
                if (ballCenterY <= 0){
                    ballCurrentSpeedY = 3;
                }

            }
        };

        animationBall.start();
    }


    public static void main(String[] args) {
        launch(args);
    }
}