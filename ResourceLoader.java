import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public abstract class ResourceLoader {

    protected static BufferedImage loadImage(String file) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream filePath = loader.getResourceAsStream("" + file);
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
