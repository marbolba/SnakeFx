package sample;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class AIPlayer
{
    class InputNode
    {
        int id;
        double val=0;//wartosc neuronu wejscia
        double w[]; //tab poloczen

        InputNode(int n)
        {
            id=n;
            Random rand = new Random();
            w=new double[6];
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
    //inputs
    InputNode[] i=new InputNode[4];
    //neurons
    NeuronNode[] n=new NeuronNode[6];
    //outputs
    OutputNode[] o=new OutputNode[4];




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
        i[0]=new InputNode(0);//"score"
        i[1]=new InputNode(1);//"appleDist"
        i[2]=new InputNode(2);//"distX"
        i[3]=new InputNode(3);//"distY"
        //neuron
        n[0]=new NeuronNode(0);
        n[1]=new NeuronNode(1);
        n[2]=new NeuronNode(2);
        n[3]=new NeuronNode(3);
        n[4]=new NeuronNode(4);
        n[5]=new NeuronNode(5);
        //output
        o[0]=new OutputNode(0);
        o[1]=new OutputNode(1);
        o[2]=new OutputNode(2);
        o[3]=new OutputNode(3);
        /*i0.print();
        i1.print();
        i2.print();
        i3.print();*/
    }
    void calcNetwork()
    {
        //neuron val calc
        n[0].calcSum();
        n[1].calcSum();
        n[2].calcSum();
        n[3].calcSum();
        n[4].calcSum();
        n[5].calcSum();
        //output val calc
        o[0].calcSum();
        o[1].calcSum();
        o[2].calcSum();
        o[3].calcSum();

        //guess output
        //int error=calcError();
        int index=findStrongest();
        System.out.println("___STRONGEST: "+index+"___");
        /*switch (index) //sterowanie
        {
            case 0:   moveLeft();
                break;
            case 1:   moveRight();
                break;
            case 2:   moveUp();
                break;
            case 3:   moveDown();
                break;
        }*/
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
    void readInputs(double s,double aD,double dX,double dY)
    {
        i[0].val=s/10;    //experimental
        i[1].val=aD/10;
        i[2].val=dX/10;
        i[3].val=dY/10;
        System.out.println("INPUT:[s:"+s+",aD:"+aD+",dX:"+dX+",dY:"+dY+"]");

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
