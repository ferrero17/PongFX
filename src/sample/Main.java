package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    final int SCENE_TAM_X = 600;
    final int SCENE_TAM_Y = 400;
    final int STICK_WIDTH = 7;
    final int STICK_HEIGHT = 50;
    final int TEXT_SIZE = 24; //Medida de la fuente del texto

    int stickPosY = (SCENE_TAM_Y-STICK_HEIGHT) /2;

    int ballCenterX = 10;
    int ballCurrentSpeedX = 3;

    int ballCenterY = 30;
    int ballCurrentSpeedY = 3;

    int stickCurrentSpeed =0;

    //Puntuación actual
    int score;
    //Puntuacion maxima
    int highScore;


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


        //LAYOUTS PARA MOSTRAR PUNTUACIONES

        //Layout Principal
        HBox paneScores = new HBox();
        paneScores.setTranslateY(20);
        paneScores.setMinWidth(SCENE_TAM_X);
        paneScores.setAlignment(Pos.CENTER);
        paneScores.setSpacing(100);
        root.getChildren().add(paneScores);

        //Layout para puntuación actual
        HBox paneCurrentScore = new HBox();
        paneCurrentScore.setSpacing(10);
        paneScores.getChildren().add(paneCurrentScore);
        //Layout para puntuación máxima, histórico
        HBox paneHighScore = new HBox();
        paneHighScore.setSpacing(10);
        paneScores.getChildren().add(paneHighScore);
        //Texto de etiqueta para la puntuación
        Text texTitleScore = new Text("SCORE: ");
        texTitleScore.setFont(Font.font(TEXT_SIZE));
        texTitleScore.setFill(Color.WHITE);
        //Texto para la puntuacion
        Text textScore = new Text("0");
        textScore.setFont(Font.font(TEXT_SIZE));
        textScore.setFill(Color.WHITE);
        //Texto de etiqueta para la puntuación máxima
        Text textTitleHighScore = new Text("Max. Score: ");
        textTitleHighScore.setFont(Font.font(TEXT_SIZE));
        textTitleHighScore.setFill(Color.WHITE);
        //Texto para la puntuación máxima
        Text textHighScore = new Text("0");
        textHighScore.setFont(Font.font(TEXT_SIZE));
        textHighScore.setFill(Color.WHITE);
        //Añadiendo los textos a los layouts reservados para ellos
        paneCurrentScore.getChildren().add(texTitleScore);
        paneCurrentScore.getChildren().add(textScore);
        paneHighScore.getChildren().add(textTitleHighScore);
        paneHighScore.getChildren().add(textHighScore);


        //Creamos las línias divisorias del campo
        for (int i = 0; i <SCENE_TAM_Y ; i+=30) {

            Line line = new Line(SCENE_TAM_X/2,i,SCENE_TAM_X/2,i+10);
            line.setStroke(Color.WHITE);
            line.setStrokeWidth(4);
            root.getChildren().add(line);
        }


        //Animaciñon-movimiento de la bola y el stick
        AnimationTimer animationBall = new AnimationTimer() {
            @Override
            public void handle(long now) {

                Shape shapeColision = Shape.intersect(circleBall,rectStick);

                boolean colisionVacia = shapeColision.getBoundsInLocal().isEmpty();

                if (colisionVacia == false && ballCurrentSpeedX > 0){
                    ballCurrentSpeedX = -3;
                    //Incrementamos la puntuacion
                    score+=1;
                    textScore.setText(String.valueOf(score));

                }

                circleBall.setCenterX(ballCenterX);
                ballCenterX+= ballCurrentSpeedX;

                circleBall.setCenterY(ballCenterY);
                ballCenterY+= ballCurrentSpeedY;

                stickPosY += stickCurrentSpeed;

                if (stickPosY < 0){
                    stickPosY = 0;

                }else {
                    if (stickPosY > SCENE_TAM_Y){

                        stickPosY = SCENE_TAM_Y - STICK_HEIGHT;
                    }
                }

                rectStick.setY(stickPosY);


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

        scene.setOnKeyPressed(event -> {


            switch (event.getCode()){

                case UP:
                    //pulsada tecla 'arriba'
                    stickCurrentSpeed = -6;
                    break;

                case DOWN:
                //pulsada tecla abajo
                    stickCurrentSpeed = 6;
                    break;

            }

        });

        scene.setOnKeyReleased(event -> {
            //al dejar de pulsar cualquiera de las dos teclas
                stickCurrentSpeed = 0;
        });




    }


    public static void main(String[] args) {
        launch(args);
    }
}
