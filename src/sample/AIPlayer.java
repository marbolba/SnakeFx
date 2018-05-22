package sample;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class AIPlayer
{
    int INPUT=4;
    int NEURON=1;
    int OUTPUT=1;

    class InputNode
    {
        String id;
        int val=0;//wartosc neuronu wejscia
        double w[]; //tab polaczen

        InputNode(String n)
        {
            id=n;
            Random rand = new Random();
            w=new double[6];        //jak chce ja uczyc to nie moge zawsze losowac
            for(int i=0;i<6;i++)
                w[i]=rand.nextDouble(); //losuje poczatkowe wagi polaczen
        }
        @Override
        public String toString()
        {
            String out="I."+id+":w["+w[0]+" , "+w[1]+" , "+w[2]+" , "+w[3]+" , "+w[4]+" , "+w[5]+"]";
            return out;
        }
    }
    class NeuronNode
    {
        int id;
        double val=0;
        double bias=1;  //zmienna zainteresowania(sth like that)
        double w[];
        double sum=0;
        double beta=1;

        NeuronNode(int n)
        {
            id=n;
            Random rand = new Random();
            w=new double[4];
            for(int i=0;i<4;i++)
                w[i]=rand.nextDouble();
        }
        void calcSum()
        {
            sum=0;
            for(int j=0;j<4;j++)
                sum+=i[j].val*i[j].w[id];
            sum+=bias;
            System.out.print(" N."+id+".sum="+sum);

            //przeniesc do innej funkcji
            //funkcja aktywacji
            val=(1/(1+Math.exp(-beta*sum)));
            System.out.println(", val="+val);

        }
        @Override
        public String toString()
        {
            String out=" N."+id+"(b:"+bias+"):w["+w[0]+" , "+w[1]+" , "+w[2]+" , "+w[3]+"]";
            return out;
        }
    }
    class OutputNode
    {
        int id;
        double val=0;
        //double w[];   //nie posiada juz dalszych polaczen
        double sum=0;

        OutputNode(int n)
        {
            id=n;
            /*Random rand = new Random();
            w=new double[4];
            for(int i=0;i<4;i++)
                w[i]=rand.nextDouble();*/
        }
        void calcSum()
        {
            sum=0;
            for(int j=0;j<6;j++)
                sum+=n[j].val*n[j].w[id];
            System.out.println("  O."+id+".sum="+sum);

        }
        @Override
        public String toString()
        {
            String out="  O."+id+".val="+val;
            return out;
        }
    }
    //variables
    Robot r;
    private InputNode[] i=new InputNode[INPUT];   //inputs
    private NeuronNode[] n=new NeuronNode[NEURON];    //neurons
    private OutputNode[] o=new OutputNode[OUTPUT];//outputs
    AIPlayer()
    {
        try{
        r=new Robot();
        } catch (AWTException e) {}
        initNeuralNetwork();

    }
    void initNeuralNetwork()
    {
        //input
        i[0]=new InputNode("isL");//"obstacleLeft"
        i[1]=new InputNode("isF");//"obstacleFront"
        i[2]=new InputNode("isR");//"obstacleRight"
        i[3]=new InputNode("Dir:");//"suggestedDir"
        //neuron
        for(int i=0;i<NEURON;i++)
            n[i]=new NeuronNode(i);
        //output
        for(int i=0;i<OUTPUT;i++)
            o[i]=new OutputNode(i);
    }
    void calcNetwork()
    {
        //neuron val calc
        for(int i=0;i<NEURON;i++)
            n[i].calcSum();
        //output val calc
        for(int i=0;i<OUTPUT;i++)
            o[i].calcSum();

        //guess output
        int index=findStrongest();
        System.out.println("___STRONGEST: "+index+"___");
        switch (index) //sterowanie
        {
            case 0:   moveLeft();
                break;
            case 1:   moveRight();
                break;
            case 2:   moveUp();
                break;
            case 3:   moveDown();
                break;
        }
    }
    /*int calcError()
    {
        //zzz dalej here!
    }*/
    int findStrongest() //zwraca najsilniejszy output
    {
        double max=0;
        int index=0;
        if(o[0].val>max)
        {
            max=o[0].val;
            index=0;
        }
        if(o[1].val>max)
        {
            max=o[1].val;
            index=1;
        }
        if(o[2].val>max)
        {
            max=o[2].val;
            index=2;
        }
        if(o[3].val>max)
        {
            max=o[3].val;
            index=3;
        }
        return index;
    }
    /**
     * Wejscia do mojej sieci neuronowej :
     *
     * Is there an obstacle to the left of the snake (1 — yes, 0 — no)
     * Is there an obstacle in front of the snake (1 — yes, 0 — no)
     * Is there an obstacle to the right of the snake (1 — yes, 0 — no)
     * Suggested direction (-1 — left, 0 — forward, 1 — right)
     * */
    /**
     * s->obstacleLeft
     * aD->obstacleFront
     * dX->obstacleRight
     * dY->suggestedDir
     *
     * */
    void readInputs(int obstacleLeft,int obstacleFront,int obstacleRight,int suggestedDir)     //wejscia do sieci neuronowej, dobrze wybrac
    {
        i[0].val=obstacleLeft;    //experimental
        i[1].val=obstacleFront;
        i[2].val=obstacleRight;
        i[3].val=suggestedDir;
        System.out.println("INPUT:[L:"+obstacleLeft+",F"+obstacleFront+",R"+obstacleRight+",dir"+suggestedDir+"]");

        //next step
        calcNetwork();
    }
    void moveLeft()
    {
        r.keyPress(KeyEvent.VK_LEFT);
        r.keyRelease(KeyEvent.VK_LEFT);
    }
    void moveRight()
    {
        r.keyPress(KeyEvent.VK_RIGHT);
        r.keyRelease(KeyEvent.VK_RIGHT);
    }
    void moveUp()
    {
        r.keyPress(KeyEvent.VK_UP);
        r.keyRelease(KeyEvent.VK_UP);
    }
    void moveDown()
    {
        r.keyPress(KeyEvent.VK_DOWN);
        r.keyRelease(KeyEvent.VK_DOWN);
    }
}
