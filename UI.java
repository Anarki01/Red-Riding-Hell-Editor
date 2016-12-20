import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class UI extends JPanel {
    private BufferedImage img;
    private BufferedImage img2;
    private String label;
    private int x;
    private int y;

    public UI(BufferedImage img, int x, int y, String label) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.label = label;
    }

    public UI(BufferedImage img, BufferedImage img2, int x, int y, String label) {
        this.img = img;
        this.img2 = img2;
        this.x = x;
        this.y = y;
        this.label = label;
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.drawString(label, x, y + 10);
        g.drawImage(img, x, y + 14, null);
        if (img2 != null) g.drawImage(img2, x + 52, y + 14, null);
    }

    public boolean checkWithin(int mouseX, int mouseY) {
        if (mouseY > y + 14 && mouseY < y + 62) {
            if (mouseX > x && mouseX < x + 48) return true;
        }
        return false;
    }

    public boolean checkWithin2(int mouseX, int mouseY) {
        if (mouseY > y + 14 && mouseY < y + 62) {
            if (mouseX > x + 52 && mouseX < x + 100) return true;
        }
        return false;
    }
}
