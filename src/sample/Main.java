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
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;


public class Main extends Application
{
    public Stage window;
    public BorderPane framework;
    public GridPane grid;
    public Board board;
    int posX,posY;
    int boartSize=30;
    int speed=90;//speed of game  //1s
    public Scene scena;
    public AIPlayer player;

    public Thread delayer = new Thread () {
        public void run () {
            while(true){
            try {
                Thread.sleep(speed); //speed of game
                if(!board.moveToDir(grid,player))//automatic move every speed'
                    delayer.join();             //recursive
            } catch (InterruptedException ex) {
            }
        }}
    };
    /**
     * Listener nasluchujacy na gracza
     *
     * */
    Thread listener = new Thread () {
        //threads memory
        char dir;
        public void run () {                    //GLOWNA PETLA PROGRAMU
            scena.setOnKeyPressed(new EventHandler<KeyEvent>() {        //ON KEY PRESSED LISTEN
                @Override
                public void handle(KeyEvent event) {
                    switch (event.getCode()) {
                        case UP: {
                            if(!(dir=='w'||dir=='s')){
                                delayer.interrupt();
                                dir = 'w';
                                board.tryMovement(grid, dir, player);
                            }
                            try {
                                delayer.start();
                            } catch (Exception ex) {}
                            break;
                        }
                        case DOWN: {
                            if(!(dir=='w'||dir=='s')){
                                delayer.interrupt();
                                dir = 's';
                                board.tryMovement(grid, dir, player);
                            }
                            try {
                                delayer.start();
                            } catch (Exception ex) {}
                            break;
                        }
                        case LEFT: {
                            if(!(dir=='a'||dir=='d')){
                                delayer.interrupt();
                                dir = 'a';
                                board.tryMovement(grid, dir, player);
                            }
                            try {
                                delayer.start();
                            } catch (Exception ex) {}
                            break;
                        }
                        case RIGHT: {
                            if(!(dir=='a'||dir=='d')){
                                delayer.interrupt();
                                dir = 'd';
                                board.tryMovement(grid, dir, player);
                            }
                            try {
                                delayer.start();
                            } catch (Exception ex) {}
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
        String s=null;//tmp string
        boolean gg=false;

        board.control=1;        //  1-player    2-AI
        listener.start();           //listener
        delayer.start();
    }
    void initBoard()
    {
        grid = new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setVgap(1);
        grid.setHgap(1);

        board=new Board(boartSize,grid);
        System.out.println("init board...");
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
