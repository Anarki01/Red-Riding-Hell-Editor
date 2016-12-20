import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class Main {
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 400;
    private static final int PALLETTE_WIDTH = 300;
    private static final int PALLETTE_HEIGHT = 200;
    private static final int TOOLS_WIDTH = 200;
    private static final int TOOLS_HEIGHT = 600;
    static JFrame frame = new JFrame();
    private static Editor editor;

    public static void main(String[] args) throws InterruptedException {
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(SCREEN_WIDTH + TOOLS_WIDTH + 16, SCREEN_HEIGHT + PALLETTE_HEIGHT + 32);
        createMenu();
        runnit();
    }

    public static void createMenu() {
        JMenuBar toolBar = new JMenuBar();

        JMenu properties = new JMenu("Properties");
        properties.setMnemonic(KeyEvent.VK_F);

        JPanel inputPane = new JPanel();

        JTextField widthField = new JTextField(5);
        JTextField heightField = new JTextField(5);

        inputPane.add(new JLabel("width:"));
        inputPane.add(widthField);
        inputPane.add(Box.createHorizontalStrut(15));

        inputPane.add(new JLabel("height:"));
        inputPane.add(heightField);

        JMenuItem gridSize = new JMenuItem("Grid Size");
        gridSize.setMnemonic(KeyEvent.VK_G);
        gridSize.setToolTipText("Set Grid Dimensions");
        gridSize.addActionListener((ActionEvent event) -> {
            widthField.setText(String.valueOf(editor.getGridWidth()));
            heightField.setText(String.valueOf(editor.getGridHeight()));
            int choice = JOptionPane.showConfirmDialog(null, inputPane, "Enter width and height of grid", JOptionPane.OK_CANCEL_OPTION);
            if (choice == JOptionPane.OK_OPTION) {
                editor.resizeGrid(Integer.valueOf(heightField.getText()), Integer.valueOf(widthField.getText()));
            }
        });

        properties.add(gridSize);

        toolBar.add(properties);
        frame.setJMenuBar(toolBar);
    }

    public static void runnit() throws InterruptedException {
        editor = new Editor(SCREEN_WIDTH, SCREEN_HEIGHT, 32);
        frame.add(editor);
        frame.revalidate();
        frame.repaint();
        while (true) {
            editor.repaint();
            editor.move(frame);
            Thread.sleep(20);
        }
    }
}
