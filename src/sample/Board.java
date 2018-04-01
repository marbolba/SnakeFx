package sample;

import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class Board
{
    class Pos
    {
        int x,y;
        Pos(int xi,int yi)
        {
            x=xi;
            y=yi;
        }

    }
    int size;           //size of board: (size X size)
    char direction;
    Rectangle[][] Rtab;
    int rectSize;       //size of rect x==y==rectSize
    int[][] form;
    int alive;
    int score;
    /** form:
     * 0-nothing
     * 1-snake
     * 2-apple*/
    int posX,posY;
    int lastX,lastY;
    int applePosX,applePosY;
    LinkedList<Pos> fifo;

    //musze pamietac pozycje ostatniego klocka snakea
    Board(int s,GridPane grid)
    {
        score=1;
        rectSize=20;
        size=s;
        alive=1;
        posX=getStartPosition();
        posY=posX;
        lastX=lastY=0;
        direction='n';//new - waiting for new

        fifo = new LinkedList<Pos>();
        fifo.add(new Pos(posX,posY+1));

        Rtab=new Rectangle[s][s];
        form=new int[s][s];


        initForm();
        initSetting();
        setNextApple(grid);
    }
    void restartGame(GridPane grid)
    {
        score=1;
        alive=1;
        posX=getStartPosition();
        posY=posX;
        lastX=lastY=0;
        direction='n';//new - waiting for new

        fifo = new LinkedList<Pos>();
        fifo.add(new Pos(posX,posY+1));

        Rtab=new Rectangle[size][size];
        form=new int[size][size];


        initForm();
        initSetting();
        setNextApple(grid);

        showBoard(grid);
    }
    void setNextApple(GridPane grid)
    {
        Random rand = new Random();
        while(true)
        {
            int x = rand.nextInt(size);
            int y = rand.nextInt(size);
            System.out.println("["+x+","+y+"]");
            if (form[x][y] == 0)
            {
                form[x][y] = 2;//set first apple
                refreshCell(grid,x,y);
                applePosX=x;
                applePosY=y;
                break;
            }
        }


    }
    void tryMovement(GridPane grid,char dir,AIPlayer player)//do boolean
    {
        //if(direction=='n')
        {
            //System.out.println("new "+dir);
            direction=dir;          //tu sie zastanowic!!!
            switch (dir)
            {
                case 'w':   moveSnake(grid,posX,--posY,player);
                            break;
                case 's':   moveSnake(grid,posX,++posY,player);
                            break;
                case 'a':   moveSnake(grid,--posX,posY,player);
                            break;
                case 'd':   moveSnake(grid,++posX,posY,player);
                            break;
            }
        }
        /*else if((direction=='a'||direction=='d')&&(dir=='w'||dir=='s'))
        {
            direction=dir;
            //System.out.print("skrecam: "+dir);
            switch (dir)
            {
                case 'w':
                    moveSnake(grid, posX, --posY,player);
                    break;
                case 's':
                    moveSnake(grid, posX, ++posY,player);
                    break;
            }

        }
        else if((direction=='s'||direction=='w')&&(dir=='a'||dir=='d'))
        {
            direction=dir;
            //System.out.print("skrecam: "+dir);
            switch (dir)
            {
                case 'a':   moveSnake(grid,--posX,posY,player);
                    break;
                case 'd':   moveSnake(grid,++posX,posY,player);
                    break;
            }

        }*/
    }
    boolean checkMovement(int i,int j,GridPane grid)
    {
        if(i<0||j<0||i>size-1||j>size-1)
        {
            alive=0;
            System.out.println("---KONIEC GRY---");
            System.out.println("wynik:"+score);
            refreshScreen(grid);
            restartGame(grid);
            return false;
        }
        else if(form[i][j]==1)
        {
            alive=0;
            System.out.println("---KONIEC GRY---");
            System.out.println("wynik:"+score);
            refreshScreen(grid);
            restartGame(grid);
            return false;
        }
        return true;
    }
    boolean checkApple(int i,int j)
    {
        if(form[i][j]==2)
        {
            score++;
            return true;
        }
        return false;
    }
    void moveSnake(GridPane grid,int i,int j,AIPlayer player)
    {
        //sprawdzenie ruchu


        if(checkMovement(i,j,grid))//sets alive var
        {
            if(alive==1)
            {
                //System.out.println("ide("+i+","+j+")");
                if (checkApple(i, j)) {
                    form[i][j] = 1;
                    refreshCell(grid, i, j);
                    setNextApple(grid);

                    lastX = i;
                    lastY = j;
                } else {
                    if (lastX != 0 || lastY != 0) {
                        fifo.add(new Pos(lastX, lastY));
                        lastX = lastY = 0;
                    }
                    form[i][j] = 1;
                    fifo.add(new Pos(i, j));
                    refreshCell(grid, i, j);
                    {
                        Pos p = fifo.getFirst();
                        //System.out.println("fifoSize:"+fifo.size()+" <"+p.x+","+p.y+">");
                        form[p.x][p.y] = 0;
                        refreshCell(grid, p.x, p.y);
                        fifo.removeFirst();
                    }

                }
                player.readInputs(score, calcDist(), posX - applePosX, posY - applePosY);      //sends inputs to neural network
            }
        }
    }
    double calcDist()
    {
        return Math.sqrt( Math.pow( Math.abs(posX-applePosX),2 ) + Math.pow( Math.abs(posY-applePosY),2 ));
    }
    boolean moveToDir(GridPane grid,AIPlayer player)
    {
        if(alive==0)
            return false;
        //System.out.print("autonomic:");
        switch (direction)
        {
            case 'w':   moveSnake(grid,posX,--posY,player);
                        break;
            case 's':   moveSnake(grid,posX,++posY,player);
                        break;
            case 'a':   moveSnake(grid,--posX,posY,player);
                        break;
            case 'd':   moveSnake(grid,++posX,posY,player);
                        break;
            case 'n':   break;
        }
        return true;
    }
    int getStartPosition()
    {
        return size/2;
    }
    void initForm()
    {
        for(int i=0;i<size;i++)
        {
            for(int j=0;j<size;j++)
            {
                form[i][j]=0;
            }
        }
    }
    void initSetting()
    {
        for(int i=0;i<size;i++)
        {
            for(int j=0;j<size;j++)
            {
                Rtab[i][j]=new Rectangle();
                Rtab[i][j].setX(0);//i*rectSize
                Rtab[i][j].setY(0);
                Rtab[i][j].setWidth(rectSize);
                Rtab[i][j].setHeight(rectSize);
                Rtab[i][j].setFill(Color.WHITE);
            }
        }
        //System.out.println("end of initSetting");
    }
    void refreshScreen(GridPane grid)
    {
        initForm();
        initSetting();
    }
    void refreshCell(GridPane grid,int i,int j)
    {
        if(form[i][j]==0)
        {
            Rtab[i][j].setFill(Color.WHITE);
        }
        else if(form[i][j]==1)
        {
            Rtab[i][j].setFill(Color.BLACK);
        }
        else if(form[i][j]==2)
        {
            Rtab[i][j].setFill(Color.GREEN);
        }
    }
    void showBoard(GridPane grid)
    {
        for(int i=0;i<size;i++)
        {
            for (int j = 0; j < size; j++)
            {
                if(form[i][j]==0)
                {
                    Rtab[i][j].setFill(Color.WHITE);
                }
                else if(form[i][j]==1)
                {
                    Rtab[i][j].setFill(Color.BLACK);
                }
                else if(form[i][j]==2)
                {
                    Rtab[i][j].setFill(Color.GREEN);
                }
                grid.add(Rtab[i][j],i,j);
            }
        }
    }



}