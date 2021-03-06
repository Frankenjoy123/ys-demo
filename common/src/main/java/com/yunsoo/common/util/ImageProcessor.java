package com.yunsoo.common.util;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by  : Lijian
 * Created on  : 2015/7/24
 * Descriptions:
 */
public final class ImageProcessor {

    private BufferedImage bufferedImage;

    public ImageProcessor() {
    }

    public ImageProcessor(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    /**
     * @param inputStream InputStream
     * @return this
     * @throws IOException
     */
    public ImageProcessor read(InputStream inputStream) throws IOException {
        this.bufferedImage = ImageIO.read(inputStream);
        return this;
    }

    /**
     * @param inputStream InputStream
     * @param contentType String [image/png|image/jpeg]
     * @return this
     * @throws IOException
     */
    public ImageProcessor read(InputStream inputStream, String contentType) throws IOException {
        ImageInputStream imageInputStream = ImageIO.createImageInputStream(inputStream);
        ImageReader imageReader = ImageIO.getImageReadersByMIMEType(contentType).next();
        imageReader.setInput(imageInputStream, true);
        this.bufferedImage = imageReader.read(0);
        return this;
    }

    /**
     * @param outputStream OutputStream
     * @param contentType  String [image/png|image/jpeg]
     * @return this
     * @throws IOException
     */
    public ImageProcessor write(OutputStream outputStream, String contentType) throws IOException {
        if (this.bufferedImage != null) {
            ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);
            ImageWriter imageWriter = ImageIO.getImageWritersByMIMEType(contentType).next();
            imageWriter.setOutput(imageOutputStream);
            imageWriter.write(bufferedImage);
            imageWriter.dispose();
            imageOutputStream.close();
        }
        return this;
    }

    public ImageProcessor crop(int x, int y, int width, int height) {
        return new ImageProcessor(this.bufferedImage.getSubimage(x, y, width, height));
    }

    public ImageProcessor resize(int width, int height) {
        Image image = this.bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage newBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        newBufferedImage.getGraphics().drawImage(image, 0, 0, null);
        return new ImageProcessor(newBufferedImage);
    }

    public int getWidth() {
        return this.bufferedImage == null ? 0 : this.bufferedImage.getWidth();
    }

    public int getHeight() {
        return this.bufferedImage == null ? 0 : this.bufferedImage.getHeight();
    }

}
