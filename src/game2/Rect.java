package game2;

public class Rect {

    public int x = 0, y = 1000, xs = 0, w = 102, h = 22, sState = 0, sPos = x, rPos = x;
    public boolean spring = false, rocket = false;
    
    public void spring(){
        spring = true;
        sPos = Game2.r.nextInt(w-20)+10;
    }
    public void rocket(){
        rocket = true;
        rPos = Game2.r.nextInt(w-20)+10;
    }
}
