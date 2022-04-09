package imageprocessing;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Latency {
    public static final String SOURCE_FILE = "./resources/many-flowers.jpg";
    public static final String DESTINATION_FILE = "./out/many-flowers.jpg";
    public static void main(String[] args) throws IOException {
        BufferedImage originalImage = ImageIO.read(new File(SOURCE_FILE));
        BufferedImage resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        long startTime = System.currentTimeMillis();
//        recolorSingleThreaded(originalImage, resultImage);
//        recolorMultiThreaded(originalImage, resultImage, 1);
        recolorMultiThreaded(originalImage, resultImage, 16);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        File outputFile = new File(DESTINATION_FILE);
        ImageIO.write(resultImage, "jpg", outputFile);
        System.out.println(duration);


    }

    public static void recolorMultiThreaded(BufferedImage originalImage, BufferedImage result, int numberOfThreads) {
        List<Thread> threads = new ArrayList<>();
        int width = originalImage.getWidth();
        int height = originalImage.getHeight()/numberOfThreads;

        for(int i=0; i<numberOfThreads; i++) {
            final int threadMultiplier = i;
            Thread thread = new Thread(() -> {
                int leftCorner = 0;
                int topCorner = height * threadMultiplier;

                recolorImage(originalImage, result, leftCorner, topCorner, width, height);
            });
            threads.add(thread);
        }
        for(Thread thread: threads) {
            thread.start();
        }
        for(Thread thread: threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void recolorSingleThreaded(BufferedImage originalImage, BufferedImage result) {
        recolorImage(originalImage, result, 0, 0, originalImage.getWidth(), originalImage.getHeight());
    }
    public static void recolorImage(BufferedImage originalImage, BufferedImage result, int leftCorner, int topCorner,
                                    int width, int height) {
        for(int x = leftCorner; x < leftCorner + width && x < originalImage.getWidth(); x++) {
            for(int y = topCorner; y < height + topCorner && y < originalImage.getHeight(); y++) {
                recolorPixel(originalImage, result, x, y);
            }
        }
    }
    public static void recolorPixel(BufferedImage originalImage, BufferedImage resultImage, int x, int y) {
        int rgb = originalImage.getRGB(x, y);
        int red = getRed(rgb);
        int green = getGreen(rgb);
        int blue = getBlue(rgb);

        int newRed, newGreen, newBlue;
        if(isShadeOfGray(red, green, blue)) { //10, 80 - try and error, purple - red&blue, hence reduce green very much
            newRed = Math.min(255, red+10);
            newGreen = Math.max(0, green - 80);
            newBlue = Math.max(0, blue - 20);
        } else { //not part of white flower
            newBlue = blue;
            newGreen = green;
            newRed = red;
        }
        int newRGB = createRGBFromColors(newRed, newGreen, newBlue);
        setRGB(resultImage, x, y, newRGB);
    }

    public static void setRGB(BufferedImage image, int x, int y, int rgb) {
//        image.setRGB(x, y, rgb);
        image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
    }

    public static boolean isShadeOfGray(int red, int green, int blue) {
        return Math.abs(red - green) < 30 && Math.abs(red -blue) < 30 && Math.abs(green-blue) < 30; //30 arbitrary/empirical
    }
    public static int createRGBFromColors(int red, int green, int blue) {
        int rgb = 0;
        rgb |= blue;
        rgb |= green<<8;
        rgb |= red<<16;
        rgb |= 0xFF000000; //set alpha to highest value to make pixel opaque
        return rgb;
    }
    public static int getRed(int rgb) {
        return (rgb & 0x00FF0000)>>16;
    }
    public static int getGreen(int rgb) {
        return (rgb & 0x0000FF00)>>8; //0xff = 256
    }
    public static int getBlue(int rgb) {
        return rgb & 0x000000FF;
    }
}
