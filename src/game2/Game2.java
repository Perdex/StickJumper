package game2;

import java.util.Random;
import javax.swing.JFrame;

public class Game2 implements Runnable{
    
    public static final int rNum = 10, ballR = 23, size = 500;
    public static boolean over = false;
    public static Rect rect[] = new Rect[rNum];
    public static Enemy[] en = new Enemy[2];
    public static int lastR = 0;
    public static final Random r = new Random();
    public static final Draw asdf = new Draw();
    
    private static int nextR1 = 40, nextR2 = 120;
    
    public Game2(int q){
        
        for(int i = 0; i < 2; i++){
            en[i] = new Enemy(i);                       //set up enemies
            en[i].x = r.nextInt(400) + 50;
            en[i].y = -2500 -r.nextInt((i+1)*2000);
        }
        en[1].xs = r.nextInt(7) - 3;
        en[1].txs = 5+en[1].xs;
        
        for(int i = 0; i < rNum; i++) rect[i] = new Rect();     //set up platforms
        
    }
    
    public static void initRect(int i){
        rect[i] = new Rect();
        rect[i].y = -20;
        do{
        rect[i].x = r.nextInt(380) + 50;
        }while(en[0].y > -150 && Math.abs(en[0].x - rect[i].x) < rect[i].w + 20);
        if(r.nextInt(6) == 1){
            rect[i].spring();
            
        }else if(r.nextInt(30) == 1){
            rect[i].rocket();
        }
    }
    
    @Override
    public void run(){
        while(true){
            if(!over){
                if(rect[lastR].y > nextR1 && r.nextInt(20) == 0 || rect[lastR].y > nextR2){
                    nextR1 = 30;
                    nextR2 = 120;
                    
                    lastR++;
                    if(lastR == rNum-1)
                        lastR = 0;

                    initRect(lastR);
                    if(asdf.points > 50000){
                        nextR1 = 125;
                        nextR2 = 125;
                        rect[lastR].xs = r.nextInt(11)-5;
                    }else if(asdf.points > 15000){
                        nextR1 = 100;
                        rect[lastR].xs = r.nextInt(9)-4;
                    }else if(asdf.points > 8000){
                        nextR1 = 80;
                        rect[lastR].xs = r.nextInt(7)-3;
                    }else if(asdf.points > 2000){
                        nextR1 = 50;
                        rect[lastR].xs = r.nextInt(2)-1;
                    }
                    
                    if(rect[lastR].spring && r.nextInt(5) == 0){
                        nextR1 = 600;
                        nextR2 = 600;
                    }
                }

                for(int i = 0; i < rNum; i++){
                    platform(i);
                }

                enemy(0);
                enemy(1);

                character();

                moveScreen();
                
            }
            asdf.repaint();
            try{
                Thread.sleep(30);
            }catch(InterruptedException e){}
            
        }
    }//paint
    
    public void character(){
        
        if(Dude.rocket){
            Dude.rocketTime--;
            Dude.ys = -20;
            if(Dude.rocketTime == 0)
                Dude.rocket = false;
        }
        Dude.y += Dude.ys;
        Dude.x += Dude.xs;
        
        Dude.ys++;
        
        Dude.xs *= 0.8;
        if(asdf.lPressed)
            Dude.xs -= 3;
        if(asdf.rPressed)
            Dude.xs += 3;
        
        if(Dude.x < 0)
            Dude.x += size;
        else if(Dude.x > size)
            Dude.x -= size;
        
        if(Dude.y > size + 50){
            over = true;
            return;
        }
        
        for(int i = 0; i < rNum; i++){
            if(Math.abs(rect[i].x - Dude.x) < rect[i].w/2+ballR && 
                    Math.abs(rect[i].y + 11 - Dude.y) < rect[i].h/2 && Dude.ys > 0){
                Dude.ys = -18;
                
                if(rect[i].spring && 
                        Math.abs(rect[i].sPos+rect[i].x-rect[i].w/2 - Dude.x) < 35){
                    Dude.ys = -35;
                    rect[i].sState = 1;
                }else if(rect[i].rocket && 
                        Math.abs(rect[i].rPos+rect[i].x-rect[i].w/2 - Dude.x) < 35){
                    Dude.rocket = true;
                    rect[i].rocket = false;
                    Dude.rocketTime = 100;
                }
                Dude.y = rect[i].y - rect[i].h/2 - ballR;
            }
        }
        if(en[0].distance(Dude.x, Dude.y-40) < 20)
            over = true;
        else if(en[0].distance(Dude.x, Dude.y-30) < 80 + en[0].r){
            double dx = Dude.x - en[0].x, dy = Dude.y-30 - en[0].y;
            double t = Math.sqrt(dx*dx+dy*dy);
            Dude.xs -= dx/t;
            Dude.ys -= dy/t;
            Dude.x -= dx/t;
            Dude.y -= dy/t;
        }
        if(en[1].distance(Dude.x, Dude.y-30) < 30 + en[1].r){
            if(Dude.ys > 0 || Dude.rocket){
                Dude.ys = -18;
                en[1].ys = 10;
            }else
                over = true;
        }
    }//character
    
    public void enemy(int i){
        en[i].x += en[i].xs + en[i].txs;
        en[i].txs *= -1;
        en[i].y += en[i].ys;
        if(en[i].x < en[i].r || en[i].x > size-en[i].r){
            en[i].xs *= -1;
        }
        if(en[i].y > 520){
            en[i].y = -Math.pow(r.nextInt(50), 2) - r.nextInt((int)(7500/(i+0.5)));
            en[i].ys = 0;
            en[i].x = r.nextInt(400) + 50;
            en[i].xs = i * (r.nextInt(7) - 3);
            en[i].txs = i * (5+(int)en[i].xs);
        }
    }
    
    public void platform(int i){
        rect[i].x += rect[i].xs;
        if(rect[i].x < rect[i].w/2 || rect[i].x > size - rect[i].w/2)
                rect[i].xs *= -1;
    }
    
    public void moveScreen(){
        double h = 200 - Dude.y;
        if(Dude.y < 200){
            for(int i = 0; i < rNum; i++){
                rect[i].y += h;
            }
            en[0].y += h;
            en[1].y += h;
            asdf.points += h;
            asdf.lPos += h;
            Dude.y += h;
        }
    }
    
    
    public static void main(String[] args) {
        JFrame fr = new JFrame("2nd game");
        Runnable T = new Game2(1);
        
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        fr.setSize(506, 525);
        fr.add(asdf);
        fr.setResizable(false);
        fr.setVisible(true);
        fr.setLocation(350, 20);
        asdf.setFocusable(true);
        
        while(asdf.initializing){
            try{
                Thread.sleep(20);
            }catch(InterruptedException e){}
        }
        T.run();
    }
    
}

