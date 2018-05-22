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
    int speed=90;//speed of game  //1s
    public Scene scena;
    public AIPlayer player;

    public Thread delayer = new Thread () {
        public void run () {
            while(true){
            try {
                Thread.sleep(speed); //speed of game
                if(!board.moveToDir(grid,player))//autonimic
                    delayer.join();             //recursive
            } catch (InterruptedException ex) {
            }
        }}
    };
    /**
     * Listener nasluchujacy na gracza
     *
     * */
    Thread listenerPlayer = new Thread () {
        //threads memory
        char dir='w';
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
    Thread listenerAI = new Thread () {
        public void run () {
            scena.setOnKeyPressed(new EventHandler<KeyEvent>() {        //ON KEY PRESSED LISTEN
                @Override
                public void handle(KeyEvent event) {
                    switch (event.getCode()) {
                        case UP: {
                            delayer.interrupt();
                            board.tryMovement(grid,'w',player);
                            try {
                                delayer.start();
                            } catch (Exception ex) {}
                            break;
                        }
                        case DOWN: {
                            delayer.interrupt();
                            board.tryMovement(grid,'s',player);
                            try {
                                delayer.start();
                            } catch (Exception ex) {}
                            break;
                        }
                        case LEFT: {
                            delayer.interrupt();
                            board.tryMovement(grid,'a',player);
                            try {
                                delayer.start();
                            } catch (Exception ex) {}
                            break;
                        }
                        case RIGHT: {
                            delayer.interrupt();
                            board.tryMovement(grid,'d',player);
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
        //String s=null;//tmp string
        //boolean gg=false;

        {           //temporary
            listenerAI.start();
            System.out.println("listener loaded...");
        }
        /*while(!gg) {
            int a;
            Scanner S=new Scanner(System.in);
            System.out.println("player || ai");
            try {
                s = S.nextLine();
            } catch (Exception e) {
            }
            if (s.toLowerCase().equals("player"))
            {
                gg=true;
                listenerPlayer.start();
                System.out.println("done");
            }

            if (s.toLowerCase().equals("ai"))
            {
                gg=true;
                listenerAI.start();
                System.out.println("done");
            }
        }*/


        delayer.start();
    }
    void initBoard()
    {
        grid = new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setVgap(1);
        grid.setHgap(1);

        board=new Board(30,grid);
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
