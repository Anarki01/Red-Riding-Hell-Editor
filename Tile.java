import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile extends JPanel {
    private BufferedImage img;
    private int x;
    private int y;
    private int ID;
    public Tile(BufferedImage img, int x, int y, int ID){
        this.img = img;
        this.x = x;
        this.y = y;
        this.ID = ID;
    }
    public void paint(Graphics g){
        super.paint(g);
        g.drawImage(img, x, y, null);
    }
    public int getID(){
        return ID;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }

}
