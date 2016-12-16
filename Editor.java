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
        grid = new Tile[30][30];
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent arg0) {

                if(mouseLocY > rows * blockSize && mouseLocX < 15 * blockSize){ //Clicked inside Pallette
                        pallettePicked = ((mouseLocY - rows * blockSize) / blockSize) * 10 + mouseLocX / blockSize;
                        cursor = img.getSubimage((mouseLocX / blockSize) * blockSize, ((mouseLocY - rows * blockSize) / blockSize) * blockSize, blockSize, blockSize);
                    }
                else if(mouseLocY < rows * blockSize && mouseLocX < 16 * blockSize){ //Clicked inside Scene
                    if(pallettePicked!= -1){
                        grid[mouseLocY/blockSize + yShift][mouseLocX/blockSize+xShift] = new Tile(cursor, (mouseLocX/blockSize)*blockSize, (mouseLocY/blockSize)*blockSize,pallettePicked);
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
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", 0, 10));
        g.drawString("Sprite Sheet:", 16 * blockSize + 24, 12);
        g.drawImage(imgprevious, 16*blockSize + 4,14 ,null);
        g.drawImage(imgnext, 16*blockSize + 54, 14 ,null);
        g.drawString("Shift Scene Horizontally:", 16 * blockSize + 2, 72);
        g.drawImage(imgprevious, 16*blockSize + 4,74 ,null);
        g.drawImage(imgnext, 16*blockSize + 54, 74 ,null);
        g.drawString("Shift Scene Vertically:", 16 * blockSize + 2, 132);
        g.drawImage(imgprevious, 16*blockSize + 4,134 ,null);
        g.drawImage(imgnext, 16*blockSize + 54, 134 ,null);
        g.drawString("Save to File: ", 16 * blockSize + 2, 192);
        g.drawImage(imgsave, 16*blockSize + 12, 194 ,null);
        //Paint spritesheet
        g.drawImage(img, 0, rows * blockSize, null);
        //Paint cursor
        g.drawImage(cursor, mouseLocX, mouseLocY,null);
        //Draw Tiles
        for(int j = 0; j < rows; j++){
            for(int i = 0; i < cols; i++) {
                if(grid[j+yShift][i+xShift] != null)grid[j+yShift][i+xShift].paint(g);
            }
        }
        //Draw tile numbers
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", 0, 10));
        for(int i = 0; i < cols; i++) { //Columns
            g.drawString("" + (i+xShift), i*blockSize, 10);
        }
        for(int i = 0; i < rows; i++) {
            g.drawString("" + (i+yShift), 1, (i+1)*blockSize);
        }
    }

    public void move(JFrame frame){
        setMouseLocX(MouseInfo.getPointerInfo().getLocation().x -frame.getLocation().x - 8);
        setMouseLocY(MouseInfo.getPointerInfo().getLocation().y -frame.getLocation().y - 32);
    }
    public void checkButtonPressed() {
        if (mouseLocX > 16 * blockSize) { //Check if its in the button area
            if (mouseLocY > 14 && mouseLocY < 62) {//First Row
                if (mouseLocX > 16 * blockSize + 4 && mouseLocX < 16 * blockSize + 52) pressedPrevious();
                else if (mouseLocX > 16 * blockSize + 54 && mouseLocX < 16 * blockSize + 102) pressedNext();
            } else if (mouseLocY > 74 && mouseLocY < 122) {//Second Row
                if (mouseLocX > 16 * blockSize + 4 && mouseLocX < 16 * blockSize + 52)shiftLeft();
                else if (mouseLocX > 16 * blockSize + 54 && mouseLocX < 16 * blockSize + 102)shiftRight();
            } else if (mouseLocY > 132 && mouseLocY < 180) {//Third Row
                if (mouseLocX > 16 * blockSize + 4 && mouseLocX < 16 * blockSize + 52) shiftUp();
                else if(mouseLocX > 16 * blockSize + 54 && mouseLocX < 16 * blockSize + 102)shiftDown();
            }else if (mouseLocY > 192 && mouseLocY < 240) {//Fourth Row
                if (mouseLocX > 16*blockSize + 12 && mouseLocX < 16*blockSize + 60) readToFile();
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
        if(xShift < 14){
            xShift++;
            for (int j = 0; j < 10; j++) {
                for (int i = 0; i < 30; i++) {
                    if (grid[j][i] != null) grid[j][i].setX(grid[j][i].getX() - blockSize);
                }
            }
        }
    }
    public void shiftRight(){
        if(xShift > 0){
            xShift--;
            for (int j = 0; j < 10; j++) {
                for (int i = 0; i < 30; i++) {
                    if (grid[j][i] != null) grid[j][i].setX(grid[j][i].getX() + blockSize);
                }
            }
        }
    }
    public void shiftUp(){
        if(yShift < 3){
            yShift++;
            for (int j = 0; j < 10; j++) {
                for (int i = 0; i < 30; i++) {
                    if (grid[j][i] != null) grid[j][i].setY(grid[j][i].getY() - blockSize);
                }
            }
        }
    }
    public void shiftDown(){
        if(yShift > 0){
            yShift--;
            for (int j = 0; j < 10; j++) {
                for (int i = 0; i < 30; i++) {
                    if (grid[j][i] != null) grid[j][i].setY(grid[j][i].getY() + blockSize);
                }
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
