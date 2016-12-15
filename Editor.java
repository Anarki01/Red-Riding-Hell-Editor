import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Editor extends JPanel {
    private BufferedImage img;
    private int sceneWidth;
    private int sceneHeight;
    private int blockSize;
    private int[][] grid;

    private int cols, rows;

    public Editor(int sceneWidth, int sceneHeight, int blockSize, JFrame frame) {
        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
        this.blockSize = blockSize;
        cols = sceneWidth / blockSize;
        rows = sceneHeight / blockSize;
        grid = new int[rows][cols];
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent arg0) {
                System.out.println("X: " + (MouseInfo.getPointerInfo().getLocation().x -frame.getLocation().x - 8)/blockSize + " Y: " + (MouseInfo.getPointerInfo().getLocation().y -frame.getLocation().y - 32)/blockSize);
            }
    });
    }

    public void paint(Graphics g){
        super.paint(g);
        g.setColor(Color.BLACK);
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                g.drawLine(i * blockSize, 0, i * blockSize, sceneHeight);
            }
            g.drawLine(0, j * blockSize, sceneWidth, j * blockSize);
        }
        //Paint spritesheet

    }

    public void move(){

    }
}
