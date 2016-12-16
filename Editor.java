import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Editor extends JPanel {
    private BufferedImage img = ResourceLoader.loadImage("spritesheet1.png");
    private BufferedImage imgnext = ResourceLoader.loadImage("btn_next.png");
    private BufferedImage imgprevious = ResourceLoader.loadImage("btn_previous.png");
    private BufferedImage imgsave = ResourceLoader.loadImage("btn_save.png");
    private BufferedImage cursor;
    private int sceneWidth;
    private int sceneHeight;
    private int blockSize;
    private Tile[][] grid;
    private int pallettePicked = -1;

    private int xShift = 0;
    private int yShift = 0;

    private int cols, rows;
    private int mouseLocX, mouseLocY;

    public Editor(int sceneWidth, int sceneHeight, int blockSize) {
        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
        this.blockSize = blockSize;
        cols = sceneWidth / blockSize;
        rows = sceneHeight / blockSize;
        grid = new Tile[10][30];
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent arg0) {

                if(mouseLocY > rows * blockSize && mouseLocX < 15 * blockSize){ //Clicked inside Pallette
                        pallettePicked = ((mouseLocY - rows * blockSize) / blockSize) * 10 + mouseLocX / blockSize;
                        cursor = img.getSubimage((mouseLocX / blockSize) * blockSize, ((mouseLocY - rows * blockSize) / blockSize) * blockSize, blockSize, blockSize);
                    }
                else if(mouseLocY < rows * blockSize && mouseLocX < 16 * blockSize){ //Clicked inside Scene
                    if(pallettePicked!= -1){
                        grid[mouseLocY/blockSize][mouseLocX/blockSize+xShift] = new Tile(cursor, (mouseLocX/blockSize)*blockSize, (mouseLocY/blockSize)*blockSize,pallettePicked);
                    }
                }else checkButtonPressed();

            }
    });
    }
    public void paint(Graphics g){
        super.paint(g);
        g.setColor(Color.BLACK);
        for (int j = 0; j < rows + 1; j++) {
            for (int i = 0; i < cols+ 1; i++) {
                g.drawLine(i * blockSize, 0, i * blockSize, blockSize * rows);
            }
            g.drawLine(0, j * blockSize, blockSize * cols, j * blockSize);
        }
        //Draw Buttons
        g.drawImage(imgnext, 15*blockSize,blockSize*rows ,null);
        g.drawImage(imgprevious, 15*blockSize,blockSize*rows + 42 ,null);
        g.drawImage(imgsave, 15*blockSize,blockSize*rows + 84 ,null);
        g.drawImage(imgnext, 15*blockSize,blockSize*rows + 126 ,null);
        //Paint spritesheet
        g.drawImage(img, 0, rows * blockSize, null);
        //Paint cursor
        g.drawImage(cursor, mouseLocX, mouseLocY,null);
        //Draw Tiles
        for(int j = 0; j < rows; j++){
            for(int i = 0; i < cols; i++) {
                if(grid[j][i+xShift] != null)grid[j][i+xShift].paint(g);
            }
        }
        //Draw tile numbers
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", 0, 10));
        for(int i = 0; i < cols; i++) {
            g.drawString("" + (i+xShift), i*blockSize, 10);
        }
    }

    public void move(JFrame frame){
        setMouseLocX(MouseInfo.getPointerInfo().getLocation().x -frame.getLocation().x - 8);
        setMouseLocY(MouseInfo.getPointerInfo().getLocation().y -frame.getLocation().y - 32);
    }
    public void checkButtonPressed(){
        if(mouseLocY > rows * blockSize && mouseLocX > 15 * blockSize){ //Check if its in the button area
            if(mouseLocY < rows * blockSize + 42)pressedNext();
            else if(mouseLocY < rows * blockSize + 84){
                pressedPrevious();
            }else if(mouseLocY < rows * blockSize + 126){
                readToFile();
            }else if(mouseLocY < rows * blockSize + 168){
                if(xShift < 14)shiftLeft();
            }else{
                if(xShift > 0)shiftRight();
            }
        }
    }
    public void pressedNext(){
        img = ResourceLoader.loadImage("spritesheet2.png");
    }
    public void pressedPrevious(){
        img = ResourceLoader.loadImage("spritesheet1.png");
    }
    public void readToFile(){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(("res/images/level.txt")));
            for(int j = 0; j < rows; j++){
                for(int i = 0; i < cols; i++){
                writer.write(Integer.toString(grid[j][i].getID()));
                }
                writer.newLine();
            }
            writer.close();
        }catch(Exception e){
            System.out.println("Failed to Open.");
        }
    }
    public void shiftLeft(){
        xShift++;
        for(int j = 0; j < 10; j++){
            for(int i = 0; i < 30; i++){
                if(grid[j][i] != null)grid[j][i].setX(grid[j][i].getX() - blockSize);
                if(grid[j][i] != null)System.out.println(i + " : " + j + " : " + grid[j][i].getID());
            }
        }
    }
    public void shiftRight(){
        xShift--;
        for(int j = 0; j < 10; j++){
            for(int i = 0; i < 30; i++){
                if(grid[j][i] != null)grid[j][i].setX(grid[j][i].getX() + blockSize);
                if(grid[j][i] != null)System.out.println(i + " : " + j + " : " + grid[j][i].getID());
            }
        }
    }
    public void setMouseLocX(int mouseX){
        mouseLocX = mouseX;
    }
    public void setMouseLocY(int mouseY){
        mouseLocY = mouseY;
    }
}
