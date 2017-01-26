package game2;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.swing.JPanel;
import javax.imageio.ImageIO;

public class Draw extends JPanel implements KeyListener {
    
    final int size = 500;
    final Random r = new Random();
    boolean lPressed = false, rPressed = false, initializing = true;
    BufferedImage dude[] = new BufferedImage[4], Platform[] = new BufferedImage[2],
            spring[] = new BufferedImage[2], rocket;
    int points = 0, req = 0, lPos = 0;
    
    public Draw(){
        addKeyListener(this);
        
        try{
            dude[0] = ImageIO.read(getClass().getResource("J.png"));
            dude[1] = ImageIO.read(getClass().getResource("J1.png"));
            dude[2] = ImageIO.read(getClass().getResource("J2.png"));
            dude[3] = ImageIO.read(getClass().getResource("JRocket.png"));
            Platform[0] = ImageIO.read(getClass().getResource("PlatformG.png"));
            Platform[1] = ImageIO.read(getClass().getResource("PlatformB.png"));
            spring[0] = ImageIO.read(getClass().getResource("Spring.png"));
            spring[1] = ImageIO.read(getClass().getResource("SpringStraight.png"));
            rocket = ImageIO.read(getClass().getResource("Rocket.png"));
        }catch(IOException e){
            System.out.println("images not found");
        }
    }
    
    @Override
    public void paint(Graphics g){
        if(Game2.over){
            over(g);
            try{
                Thread.sleep(30);
            }catch(InterruptedException e){}
        
            repaint();
            return;
        }
        
        requestFocusInWindow();
        
        drawBG(g);
        
        Rect[] rect = Game2.rect;
        for(int i = 0; i < Game2.rNum; i++){
            int t = 0;
            if(rect[i].xs != 0)
                t = 1;
            g.drawImage(Platform[t], rect[i].x - rect[i].w/2, 
                    rect[i].y - rect[i].h/2, null);
            
            if(rect[i].spring){
                t = 26;
                if(rect[i].sState == 1)
                    t = 41;
                g.drawImage(spring[rect[i].sState], 
                        rect[i].sPos+rect[i].x-rect[i].w/2-8, rect[i].y - t, null);
            }else if(rect[i].rocket){
                t = 30;
                g.drawImage(rocket, rect[i].rPos+rect[i].x-rect[i].w/2-8, 
                        rect[i].y - t, null);
            }
        }

        Enemy[] en = Game2.en;
        
        if(en[0].y > -30)
            g.drawImage(en[0].im, (int)(en[0].x-en[0].r), (int)(en[0].y-en[0].r), null);
        else if(en[0].y > -500){
            g.setColor(Color.black);
            g.fillOval((int)en[0].x-7, 5, 15, 20);
        }
        if(en[1].y > -30)
            g.drawImage(en[1].im, (int)(en[1].x-en[1].r), (int)(en[1].y-en[1].r), null);
        else if(en[1].y > -500){
            g.setColor(Color.red);
            g.fillOval((int)en[1].x-7, 5, 15, 20);
        }
        
        g.setFont(new Font("asdf", 10, 25));
        g.setColor(Color.black);
        g.drawString(Integer.toString(points), 10, 25);
        
        if(points > req)
            req = points;

        g.setColor(new Color(0, 200, 0));
        g.drawString(Integer.toString(req), 10, 50);

        int t;
        if(Dude.rocket)
            t = 3;
        else if(Dude.xs > 1)
            t = 1;
        else if(Dude.xs < -1)
            t = 2;
        else
            t = 0;
        g.drawImage(dude[t], (int)Dude.x-23, (int)Dude.y-80, null);
        initializing = false;
    }//draw
    
    @Override
    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();
        if(Game2.over && !lPressed && !rPressed){
            Game2.over = false;
            points = 0;
            for(int i = 0; i < Game2.rNum; i++) Game2.rect[i] = new Rect();
            for(int i = 5; i < Game2.rNum; i++){
                Game2.initRect(i);
            }
            Enemy[] en = Game2.en;
            for(int i = 0; i < 2; i++){ 
                do{
                    Game2.rect[i].x = r.nextInt(380) + 50;
                }while(en[0].y > -150 && 
                        Math.abs(en[0].x - Game2.rect[i].x) < Game2.rect[i].w + 20);
                en[i].y = -2500 -r.nextInt((i+1)*1000);
            }
            en[1].xs = r.nextInt(7) - 3;
            en[1].txs = 5+(int)en[1].xs;
            
            Dude.reset();
            Game2.lastR = 0;
            
            return;
        }
        
        if(key == KeyEvent.VK_A || key == KeyEvent.VK_J || key == KeyEvent.VK_LEFT)
            lPressed = true;
        else if(key == KeyEvent.VK_D || key == KeyEvent.VK_L || key == KeyEvent.VK_RIGHT)
            rPressed = true;
         
    }
    @Override
    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_A || key == KeyEvent.VK_J || key == KeyEvent.VK_LEFT)
            lPressed = false;
        else if(key == KeyEvent.VK_D || key == KeyEvent.VK_L || key == KeyEvent.VK_RIGHT)
            rPressed = false;
    }
    
    public void over(Graphics g){
        g.setColor(Color.white);
        g.fillRect(0, 0, size, size);

        g.setColor(Color.lightGray);
        for(int i = -10; i <= 510; i += 30){
            g.drawLine(0, i, 500, i);
            g.drawLine(i, 0, i, 500);
        }
        g.setColor(Color.red);
        g.setFont(new Font("asdf", 10, 25));
        g.drawString("You lost; press any key to continue", 60, 190);
    }
    
    public void drawBG(Graphics g){
        g.setColor(Color.white);
        g.fillRect(0, 0, size, size);

        g.setColor(Color.lightGray);
        while(lPos > 30)
            lPos -= 30;
        for(int i = -10; i <= 510; i += 30){
            g.drawLine(0, i+lPos, 500, i+lPos);
            g.drawLine(i, 0, i, 500);
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e){}
    
}
