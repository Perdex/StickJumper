package game2;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Enemy {

    public double x = 0, y = 0, xs = 0, ys = 0, r = 0, txs = 0;
    public BufferedImage im;
    
    public Enemy(int i){
        try{
            if(i == 0){
                r = 39;
                im = ImageIO.read(getClass().getResource("en0.png"));
            }else{
                r = 19;
                im = ImageIO.read(getClass().getResource("en.png"));
            }
        }catch(IOException e){}
    }
    
    public double distance(double x, double y){
        return Math.sqrt(Math.pow(x-this.x, 2) + Math.pow(y-this.y, 2));
    }
    
}
