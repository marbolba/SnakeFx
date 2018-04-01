package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.awt.*;


public class Main extends Application
{
    Stage window;
    BorderPane framework;
    GridPane grid;
    Board board;
    int posX,posY;
    int speed=1000;
    Scene scena;
    AIPlayer player;

    Thread delayer = new Thread () {
        public void run () {
            while(true){
            try {
                Thread.sleep(speed); //speed of game
                if(!board.moveToDir(grid,player))//autonimic
                    delayer.join();
            } catch (InterruptedException ex) {
            }
        }}
    };
    Thread listener = new Thread () {
        public void run () {
            scena.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    switch (event.getCode()) {
                        case UP: {
                            //delayer.interrupt();
                            board.tryMovement(grid,'w',player);
                            //try {
                            //    delayer.start();
                            //} catch (Exception ex) {}
                            break;
                        }
                        case DOWN: {
                            //delayer.interrupt();
                            board.tryMovement(grid,'s',player);
                            //try {
                            //    delayer.start();
                            //} catch (Exception ex) {}
                            break;
                        }
                        case LEFT: {
                            //delayer.interrupt();
                            board.tryMovement(grid,'a',player);
                            //try {
                            //    delayer.start();
                            //} catch (Exception ex) {}
                            break;
                        }
                        case RIGHT: {
                            //delayer.interrupt();
                            board.tryMovement(grid,'d',player);
                            //try {
                            //    delayer.start();
                            //} catch (Exception ex) {}
                            break;
                        }
                    }
                }
            });
        }
    };

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        window=primaryStage;
        initView();
        initBoard();
        player=new AIPlayer();
        startGame();

    }
    void startGame()
    {
        listener.start();
        //delayer.start();

    }
    void initBoard()
    {
        grid = new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setVgap(1);
        grid.setHgap(1);

        board=new Board(30,grid);
        //System.out.println("zaczynam");
        board.showBoard(grid);

        framework.setCenter(grid);
    }
    void initView()
    {
        window.setTitle("Snake");
        framework = new BorderPane();
        scena= new Scene(framework, 645, 645, Color.BLACK);
        window.setScene(scena);
        window.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
