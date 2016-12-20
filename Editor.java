import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class Editor extends JPanel {

    /**
     * File saving and loading
     */
    private JFileChooser jfc;

    private JTextField nameField;
    private UI btnSpriteSheet;
    private UI btnShiftHori;
    private UI btnSkipHori;
    private UI btnShiftVert;
    private UI btnSkipVert;
    private UI btnSaveLoad;
    private UI btnZoom;
    private UI btnDeleteTile;
    private BufferedImage img = ResourceLoader.loadImage("spriteSheets/spritesheet0.png");
    private BufferedImage imgUI = ResourceLoader.loadImage("ui.png");
    private BufferedImage cursor;
    private int sceneWidth;
    private int sceneHeight;
    private int blockSize;
    private Tile[][] grid;
    private boolean pallettePicked = false;
    private int palletteX;
    private int palletteY;
    private int spriteSheet = 0;

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
        grid = new Tile[30][60];
        btnSpriteSheet = new UI(imgUI.getSubimage(48, 0, 48, 48), imgUI.getSubimage(0, 0, 48, 48), 25 * blockSize + 10, 52, "Sprite Sheet");
        btnShiftHori = new UI(imgUI.getSubimage(48, 0, 48, 48), imgUI.getSubimage(0, 0, 48, 48), 25 * blockSize + 120, 52, "Shift Horizontally:");
        btnShiftVert = new UI(imgUI.getSubimage(48, 0, 48, 48), imgUI.getSubimage(0, 0, 48, 48), 25 * blockSize + 120, 112, "Shift Vertically:");
        btnSaveLoad = new UI(imgUI.getSubimage(48, 0, 48, 48), imgUI.getSubimage(0, 0, 48, 48), 25 * blockSize + 10, 112, "Save/Load:");
        btnZoom = new UI(imgUI.getSubimage(48, 0, 48, 48), imgUI.getSubimage(0, 0, 48, 48), 25 * blockSize + 10, 172, "Zoom In/Out:");
        btnSkipHori = new UI(imgUI.getSubimage(48, 0, 48, 48), imgUI.getSubimage(0, 0, 48, 48), 25 * blockSize + 120, 172, "Skip Horizontally:");
        btnSkipVert = new UI(imgUI.getSubimage(48, 0, 48, 48), imgUI.getSubimage(0, 0, 48, 48), 25 * blockSize + 120, 232, "Skip Vertically:");
        btnDeleteTile = new UI(imgUI.getSubimage(96, 48, 48, 48), 25 * blockSize + 10, 317, "Delete Tile");
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent arg0) {
                if (mouseLocY > rows * blockSize && mouseLocX < 24 * blockSize) { //Clicked inside Pallette
                    if (!pallettePicked) pallettePicked = true;
                    palletteX = mouseLocX / blockSize;
                    palletteY = (mouseLocY - rows * blockSize) / blockSize;
                    cursor = img.getSubimage((mouseLocX / blockSize) * blockSize, ((mouseLocY - rows * blockSize) / blockSize) * blockSize, blockSize, blockSize);
                } else if (mouseLocY < rows * blockSize && mouseLocX < 25 * blockSize) { //Clicked inside Scene
                    if (pallettePicked)
                        grid[mouseLocY / blockSize + yShift][mouseLocX / blockSize + xShift] = new Tile(cursor, (mouseLocX / blockSize) * blockSize, (mouseLocY / blockSize) * blockSize, palletteX, palletteY, spriteSheet, xShift, yShift);
                    else grid[mouseLocY / blockSize + yShift][mouseLocX / blockSize + xShift] = null;
                } else checkButtonPressed();

            }
        });
    }


    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.BLACK);
        for (int j = 0; j < rows + 1; j++) {
            for (int i = 0; i < cols + 1; i++) {
                g.drawLine(i * blockSize, 0, i * blockSize, blockSize * rows);
            }
            g.drawLine(0, j * blockSize, blockSize * cols, j * blockSize);
        }
        g.drawRect(25 * blockSize + 4, 52, 220, 250);
        g.drawRect(25 * blockSize + 4, 317, 220, 67);


        //Draw Headings
        g.setFont(new Font("Arial", 0, 10));
        g.drawString("SCENE TOOLS", 25 * blockSize + 75, 48);
        g.drawString("TOOLS", 25 * blockSize + 90, 313);
        //Draw Buttons
        btnSpriteSheet.paint(g);
        btnShiftHori.paint(g);
        btnShiftVert.paint(g);
        btnSaveLoad.paint(g);
        btnZoom.paint(g);
        btnSkipHori.paint(g);
        btnSkipVert.paint(g);
        btnDeleteTile.paint(g);
        //Paint spritesheet
        g.drawImage(img, 0, rows * blockSize, null);
        //Paint cursor
        if (pallettePicked) g.drawImage(cursor, mouseLocX, mouseLocY, null);
        //Draw Tiles
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                if (grid[j + yShift][i + xShift] != null) grid[j + yShift][i + xShift].paint(g);
            }
        }
//        grid.length;
//        grid[0].length;
        //Draw tile numbers
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", 0, 10));
        for (int i = 0; i < cols; i++) { //Columns
            g.drawString("" + (i + xShift), i * blockSize, 10);
        }
        for (int i = 0; i < rows; i++) {
            g.drawString("" + (i + yShift), 1, (i + 1) * blockSize);
        }
    }

    public void move(JFrame frame) {
        setMouseLocX(MouseInfo.getPointerInfo().getLocation().x - frame.getLocation().x - 8);
        setMouseLocY(MouseInfo.getPointerInfo().getLocation().y - frame.getLocation().y - 32);
    }

    public void checkButtonPressed() {
        if (btnSpriteSheet.checkWithin(mouseLocX, mouseLocY)) pressedPrevious();
        if (btnSpriteSheet.checkWithin2(mouseLocX, mouseLocY)) pressedNext();
        if (btnShiftHori.checkWithin(mouseLocX, mouseLocY)) shiftLeft();
        if (btnShiftHori.checkWithin2(mouseLocX, mouseLocY)) shiftRight();
        if (btnShiftVert.checkWithin(mouseLocX, mouseLocY)) shiftUp();
        if (btnShiftVert.checkWithin2(mouseLocX, mouseLocY)) shiftDown();
        if (btnSaveLoad.checkWithin(mouseLocX, mouseLocY)) writeToFile();
        if (btnSaveLoad.checkWithin2(mouseLocX, mouseLocY)) readFromFile();
//        if(btnZoom.checkWithin(mouseLocX, mouseLocY)) //zoom in
//        if(btnZoom.checkWithin2(mouseLocX, mouseLocY)) //zoom out
//        if(btnSkipHori.checkWithin(mouseLocX, mouseLocY)) //skip left
//        if(btnSkipHori.checkWithin2(mouseLocX, mouseLocY))//skip right
//        if(btnSkipVert.checkWithin(mouseLocX, mouseLocY))//skip up
//        if(btnSkipVert.checkWithin2(mouseLocX, mouseLocY))//skip down
        if (btnDeleteTile.checkWithin(mouseLocX, mouseLocY)) pallettePicked = false;
    }

    public void pressedNext() {
        img = ResourceLoader.loadImage("spriteSheets/spritesheet1.png");
        spriteSheet = 1;
    }

    public void pressedPrevious() {
        img = ResourceLoader.loadImage("spriteSheets/spritesheet0.png");
        spriteSheet = 0;
    }

    public void writeToFile() {
        File saveLocation = null;
        int returnValue = jfc.showSaveDialog(null);
        if (returnValue == jfc.APPROVE_OPTION) {
            saveLocation = jfc.getSelectedFile();
            ResourceLoader.writeFile(30, 30, grid, saveLocation);
        }
    }

    public void readFromFile() {
        File openLocation = null;
        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == jfc.APPROVE_OPTION) {
            resetGrid();
            openLocation = jfc.getSelectedFile();
            System.out.println(openLocation.getName());

            grid = ResourceLoader.readFile(img, blockSize, openLocation.getName());
        }
    }

    public void shiftLeft() {
        if (xShift < grid[0].length - 25) {
            xShift++;
            for (int j = 0; j < 10; j++) {
                for (int i = 0; i < 30; i++) {
                    if (grid[j][i] != null) grid[j][i].setX(grid[j][i].getTempX() - blockSize);
                }
            }
        }
    }

    public void shiftRight() {
        if (xShift > 0) {
            xShift--;
            for (int j = 0; j < 10; j++) {
                for (int i = 0; i < 30; i++) {
                    if (grid[j][i] != null) grid[j][i].setX(grid[j][i].getTempX() + blockSize);
                }
            }
        }
    }

    public void shiftUp() {
        if (yShift < grid.length - 12) {
            yShift++;
            for (int j = 0; j < 10; j++) {
                for (int i = 0; i < 30; i++) {
                    if (grid[j][i] != null) grid[j][i].setY(grid[j][i].getTempY() - blockSize);
                }
            }
        }
    }

    public void shiftDown() {
        if (yShift > 0) {
            yShift--;
            for (int j = 0; j < 10; j++) {
                for (int i = 0; i < 30; i++) {
                    if (grid[j][i] != null) grid[j][i].setY(grid[j][i].getTempY() + blockSize);
                }
            }
        }
    }

    public void setMouseLocX(int mouseX) {
        mouseLocX = mouseX;
    }

    public void setMouseLocY(int mouseY) {
        mouseLocY = mouseY;
    }

    public void resetGrid() {
        for (int j = 0; j < grid[0].length; j++) {
            for (int i = 0; i < grid.length; i++)
                grid[j][i] = null;
        }
        xShift = 0;
        yShift = 0;
    }

    public void resizeGrid(int in_height, int in_width) {
        xShift = 0;
        yShift = 0;
        Tile[][] oldGrid = grid;
        grid = new Tile[in_height][in_width];

        int width;
        int height;

        if (oldGrid[0].length > grid[0].length) {
            width = grid[0].length;
        } else {
            width = oldGrid[0].length;
        }

        if (oldGrid.length > grid.length) {
            height = grid.length;
        } else {
            height = oldGrid.length;
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                grid[j][i] = oldGrid[j][i];
            }
        }
    }

    public int getGridWidth() {
        return grid[0].length;
    }

    public int getGridHeight() {
        return grid.length;
    }
}
