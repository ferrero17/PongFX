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

import java.util.Random;

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

    Text textScore;


    private void resetGame(){

        score = 0;
        textScore.setText(String.valueOf(score));
        ballCenterX = 10;
        ballCurrentSpeedY = 3;
        //Posición random de la bola al inicio de cada partida
        Random random = new Random();
        ballCenterY = random.nextInt(SCENE_TAM_Y);

    }


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
        textScore = new Text("0");
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

        resetGame();
        //Animaciñon-movimiento de la bola y el stick
        AnimationTimer animationBall = new AnimationTimer() {
            @Override
            public void handle(long now) {

                Shape shapeColision = Shape.intersect(circleBall,rectStick);

                boolean colisionVacia = shapeColision.getBoundsInLocal().isEmpty();

                //Cuando la bola impacte en el stick
                if (colisionVacia == false && ballCurrentSpeedX > 0){
                    int collisionZone = getStickCollisionZone(circleBall,rectStick);
                    calculateBallSpeed(collisionZone);
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

                //Compruebo si la bola toca el lado derecho
                if (ballCenterX >= SCENE_TAM_X){
                    //Compruebo si hay una nueva puntuacion mas alta
                    if(score > highScore){
                        //Actualizar nueva puntuación más alta
                        highScore = score;
                        textHighScore.setText(String.valueOf(highScore));

                    }
                    //Reinicia la partida si la bola toca el lado derecho
                    resetGame();
                }

                //Se comprueba si la bola toca la izquierda de la pared o el techo
                //Aquí habrá que modificar también que velocidad tomará segun el LVL de la partida --> TO-DO:

                //Si la bola toca la pared IZQUIERDA
                if (ballCenterX <= 0){
                    ballCurrentSpeedX = 3;
                }

                //Si la bola toca el borde SUPERIOR
                if (ballCenterY >= SCENE_TAM_Y){
                    ballCurrentSpeedY = -3;
                }

                //Si la bola toca el borde INFERIOR
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
                //pulsada tecla 'abajo'
                    stickCurrentSpeed = 6;
                    break;

            }

        });

        scene.setOnKeyReleased(event -> {
            //al dejar de pulsar cualquiera de las dos teclas
                stickCurrentSpeed = 0;
        });


    }

    private int getStickCollisionZone(Circle ball, Rectangle stick){

        if (Shape.intersect(ball,stick).getBoundsInLocal().isEmpty()){
            return 0;
        }else {
            double offsetBallStick = ball.getCenterY()- stick.getY();
            if (offsetBallStick < stick.getHeight() *0.1){
                return 1;
            }else if (offsetBallStick < stick.getHeight() / 2){
                return 2;
            } else if (offsetBallStick >= stick.getHeight() / 2 && offsetBallStick < stick.getHeight() * 0.9) {
                return 3;
            }else {
                return 4;
            }

            }
        }


        private void calculateBallSpeed(int collisionZone){

        switch (collisionZone){
            case 0:
                //No hay colisión
                break;

            case 1:
                //Colision con esquina superior
                    ballCurrentSpeedX = -3;
                    ballCurrentSpeedY = -6;
                break;

            case 2:
                //Colision con lado superior
                    ballCurrentSpeedX = -3;
                    ballCurrentSpeedY = -3;
                break;

            case 3:
                //colision con lado inferior
                    ballCurrentSpeedX = -3;
                    ballCurrentSpeedY = 3;
                break;

            case 4:
                //Colision con esquina inferior
                    ballCurrentSpeedX = -3;
                    ballCurrentSpeedY = 6;
                break;

        }


        if (score > 5){
            ballCurrentSpeedX *= 2;
            ballCurrentSpeedY *= 2;
        }

            if (score > 10 && score <= 15){
                ballCurrentSpeedX *= 3;
                ballCurrentSpeedY *= 3;
            }

            if (score > 15 && score <= 20){
                ballCurrentSpeedX *= 4;
                ballCurrentSpeedY *= 4;
            }

            if (score > 20 && score <= 25){
                ballCurrentSpeedX *= 5;
                ballCurrentSpeedY *= 5;
            }


    }

    public static void main(String[] args) {
        launch(args);
    }
}
