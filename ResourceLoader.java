import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;

public abstract class ResourceLoader {
    protected static void writeFile(int in_rows, int in_cols, Tile in_grid[][], String level) {

        File dir = new File(System.getProperty("user.home") + File.separator + "Documents/Red-Riding-Hell-Levels");
        dir.mkdir();

        String filePath = System.getProperty("user.home") + File.separator + "Documents/Red-Riding-Hell-Levels" + File.separator + level;

        try {
            OutputStream file = new FileOutputStream(filePath);
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(file))) {

                for(int i = 0; i < in_cols; i++){
                    for(int j = 0; j < in_rows; j++) {
                        Tile tile = in_grid[j][i];
                        if (tile != null) {
                            writer.write(String.valueOf(tile.getX()/48) +  ";" + String.valueOf(tile.getY()/48) +  ";" + String.valueOf(tile.getPX()) +  ";" + String.valueOf(tile.getPY()) +  ";" + String.valueOf(tile.getSpriteSheet()) +  ";");
                            writer.newLine();
                        }
                    }
                }
                writer.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Tile[][] readFile(BufferedImage in_img, int in_blockSize, String level) {

        Tile[][] pass_grid = new Tile[30][30];
        String filePath = System.getProperty("user.home") + File.separator + "Documents/Red-Riding-Hell-Levels" + File.separator + level;
        int lineNum = 0;
        String line;

        try {
            InputStream file = new FileInputStream(filePath);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(file))) {

                while ((line = reader.readLine()) != null) {

                    String[] parts = line.split(";");
                        pass_grid[Integer.parseInt(parts[1])][Integer.parseInt(parts[0])] = new Tile(in_img.getSubimage(Integer.parseInt(parts[2])*48,Integer.parseInt(parts[3])*48, 48, 48),Integer.parseInt(parts[0])*48,Integer.parseInt(parts[1])* 48, Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), 0, 0);
                    lineNum++;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return pass_grid;
    }
    protected static BufferedImage loadImage(String file) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream filePath = loader.getResourceAsStream("res/images/" + file);
        BufferedImage img = null;
        try {
            img = ImageIO.read(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    /**
     * Gets a random image from the specified directory
     *
     * @return null if the folderPath specified is not a directory
     * else returns a random BufferedImage from the directory
     */
    protected static BufferedImage getRandomImage(String folderPath) {
        File folderToSearch = new File(folderPath);

        // Return null if this method was not supplied with a directory
        if (!folderToSearch.isDirectory()) {
            System.out.println("ResourceLoader - THIS IS NOT A DIR");
            return null;
        }

        File[] imageFiles = folderToSearch.listFiles();
        Random rand = new Random();
        int randomFileNumber;

        // Make sure we don't pick a folder instead of a file
        do {
            randomFileNumber = rand.nextInt(imageFiles.length);
        } while (imageFiles[randomFileNumber].isDirectory());

        // Prepare string to send to the loadImage() method
        String[] pathSegments = folderPath.split("images/");
        String imageFileName = imageFiles[randomFileNumber].getName();

        return loadImage(pathSegments[pathSegments.length - 1] + "/" + imageFileName);
    }

    protected static BufferedImage getRandomPlanet() {
        return getRandomImage("src/res/images/Planets");
    }

    protected static void loadAudio(String file) {

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(loader.getResourceAsStream("res/sounds/" + file));
            clip.open(inputStream);
            if (file == "bgm.wav") {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            clip.start();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException exx) {
            exx.printStackTrace();
        }
    }
}
