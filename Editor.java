import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Editor extends JPanel {
    private BufferedImage img = ResourceLoader.loadImage("animate2.png");
    private BufferedImage cursor;
    private int sceneWidth;
    private int sceneHeight;
    private int blockSize;
    private Tile[][] grid;
    private int pallettePicked = -1;

    private int cols, rows;
    private int mouseLocX, mouseLocY;

    public Editor(int sceneWidth, int sceneHeight, int blockSize) {
        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
        this.blockSize = blockSize;
        cols = sceneWidth / blockSize;
        rows = sceneHeight / blockSize;
        grid = new Tile[rows][cols];
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent arg0) {
                if(mouseLocY > rows * blockSize){ //Clicked inside Pallette
                    pallettePicked = ((mouseLocY - rows * blockSize)/blockSize)*10 + mouseLocX/blockSize;
                    cursor = img.getSubimage((mouseLocX/blockSize)*blockSize, ((mouseLocY-rows * blockSize)/blockSize)*blockSize, blockSize, blockSize);
                }
                else{ //Clicked inside Scene
                    if(pallettePicked!= -1){
                        grid[mouseLocY/blockSize][mouseLocX/blockSize] = new Tile(cursor, (mouseLocX/blockSize)*blockSize, (mouseLocY/blockSize)*blockSize,pallettePicked);
                    }
                }

            }
    });
    }
    public void paint(Graphics g){
        super.paint(g);
        g.setColor(Color.BLACK);
        for (int j = 0; j < rows + 1; j++) {
            for (int i = 0; i < cols+ 1; i++) {
                g.drawLine(i * blockSize, 0, i * blockSize, sceneHeight);
            }
            g.drawLine(0, j * blockSize, sceneWidth, j * blockSize);
        }
        //Paint spritesheet
        g.drawImage(img, 0, rows * blockSize, null);
        //Paint cursor
        g.drawImage(cursor, mouseLocX, mouseLocY,null);
        //Draw Tiles
        for(int j = 0; j < rows; j++){
            for(int i = 0; i < cols; i++) {
                if(grid[j][i] != null)grid[j][i].paint(g);
            }
        }
    }

    public void move(JFrame frame){
        setMouseLocX(MouseInfo.getPointerInfo().getLocation().x -frame.getLocation().x - 8);
        setMouseLocY(MouseInfo.getPointerInfo().getLocation().y -frame.getLocation().y - 32);
    }
    public void setMouseLocX(int mouseX){
        mouseLocX = mouseX;
    }
    public void setMouseLocY(int mouseY){
        mouseLocY = mouseY;
    }
}
