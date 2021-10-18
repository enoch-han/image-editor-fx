package com.company;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import java.awt.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;


public class MyImage {
    private Image o_image;
    private PixelReader reader;
    private PixelWriter writer;
    private double height,width;
    private WritableImage image;
    private ObjectProperty imageProperty = new SimpleObjectProperty(image);
    private String source_url;
    private String save_url = "C:/Users/hp/Documents/books/projects/java codes/Image Editor/src/resource/saved/";
    private String name = "mypic.png";
    private ImageInput imageInput;
    private ImageView view;
    private double widthRatio;
    private double heightRatio;
    private double textXPercentage = 50;
    private double textYPercentage = 50;

    public ColorAdjust colorAdjust = new ColorAdjust();
    public SepiaTone sepiaTone = new SepiaTone();
    public Glow glow = new Glow();
    public double contrastPoint = 0;
    public double brightnessPoint = 0;
    public double huePoint = 0;
    public double saturationPoint = 0;
    public double sepiaPoint = 0;
    public double glowPoint = 0;
    public boolean colstat = false;
    public boolean sepstat = false;
    public boolean glostat = false;

    public void setName(String name){
        this.name = name;
    }

    public void setContrastPoint(double contrastPoint) {
        this.contrastPoint = contrastPoint;
        this.colstat = true;
        colorAdjust.setContrast(contrastPoint);
        sepiaTone.setInput(glow);
        colorAdjust.setInput(sepiaTone);
        view.setEffect(colorAdjust);
    }

    public void setBrightnessPoint(double brightnessPoint) {
        this.brightnessPoint = brightnessPoint;
        this.colstat = true;
        colorAdjust.setBrightness(brightnessPoint);
        sepiaTone.setInput(glow);
        colorAdjust.setInput(sepiaTone);
        view.setEffect(colorAdjust);
    }

    public void setHuePoint(double huePoint) {
        this.huePoint = huePoint;
        this.colstat = true;
        colorAdjust.setHue(huePoint);
        sepiaTone.setInput(glow);
        colorAdjust.setInput(sepiaTone);
        view.setEffect(colorAdjust);
    }

    public void setSaturationPoint(double saturationPoint) {
        this.saturationPoint = saturationPoint;
        this.colstat = true;
        colorAdjust.setSaturation(saturationPoint);
        sepiaTone.setInput(glow);
        colorAdjust.setInput(sepiaTone);
        view.setEffect(colorAdjust);
    }

    public void setSepiaPoint(double sepiaPoint) {
        this.sepiaPoint = sepiaPoint;
        this.sepstat = true;
        sepiaTone.setLevel(sepiaPoint);
        sepiaTone.setInput(glow);
        colorAdjust.setInput(sepiaTone);
        view.setEffect(colorAdjust);
    }

    public void setGlowPoint(double glowPoint) {
        this.glowPoint = glowPoint;
        this.glostat = true;
        glow.setLevel(glowPoint);
        sepiaTone.setInput(glow);
        colorAdjust.setInput(sepiaTone);
        view.setEffect(colorAdjust);
    }

    public void setWidth(double width){
        this.width = width;
    }

    public void setHeight(double height){
        this.height = height;
    }

    public void setTextPosition(double x,double y){
        textXPercentage = x;
        textYPercentage = y;
    }

    public String getName(){
        return this.name;
    };

    public Image getImage(){
        return this.image;
    }
    public ImageView getView(){
        return this.view;
    }
    public String getSource_url(){
        return this.source_url;
    }


    public MyImage(String url){
        this.source_url = url;
        load_image();
    }
    public MyImage(String url,double contrast,double brightness, double hue, double saturation, double sepia,double glow){
        this.source_url = url;
        load_image();
        setContrastPoint(contrast);
        setBrightnessPoint(brightness);
        setHuePoint(hue);
        setSaturationPoint(saturation);
        setSepiaPoint(sepia);
        setGlowPoint(glow);
    }


    public boolean load_image(){
        boolean status = true;
        System.out.println("loading");
        this.o_image = new Image(source_url);
        if (o_image.isError()){
            status = false;
            System.out.println("error loading image :MyImage.load_image");
        }
        this.height = o_image.getHeight();
        this.width = o_image.getWidth();
        this.reader = o_image.getPixelReader();
        if (this.reader == null){
            status = false;
            System.out.println("error getting pixel reader :MyImage.load_image");
        }
        this.image = new WritableImage((int)width,(int)height);
        widthRatio = image.getWidth()/image.getHeight();
        heightRatio = image.getHeight()/image.getWidth();
        writer = image.getPixelWriter();
        for(int i=0; i<height;i++){
            for (int y=0; y<width; y++){
                writer.setColor(y,i,reader.getColor(y,i));
            }
        }
        imageInput = new ImageInput(image);
        view = new ImageView(image);
        Rectangle2D rectangle = new Rectangle2D(0,0,width,height);
        view.setViewport(rectangle);
        view.setPreserveRatio(true);
        System.out.println("loading finished");
        return  status;
    }

    public boolean load_image_writable(WritableImage wimage){
        boolean status = true;
        this.width = wimage.getWidth();
        this.height = wimage.getHeight();
        this.reader = wimage.getPixelReader();
        if (this.reader == null){
            status = false;
            System.out.println("error getting pixel reader :MyImage.load_image");
        }
        this.image = new WritableImage((int)width,(int)height);
        widthRatio = image.getWidth()/image.getHeight();
        heightRatio = image.getHeight()/image.getWidth();
        writer = image.getPixelWriter();
        for(int i=0; i<height;i++){
            for (int y=0; y<width; y++){
                writer.setColor(y,i,reader.getColor(y,i));
            }
        }
        imageInput = new ImageInput(image);
        view = new ImageView(image);
        System.out.println("((((((((((((((((((((((((((((((((((((()))))))))))))))))))))))))))))))))))))");
        Rectangle2D rectangle = new Rectangle2D(0,0,width,height);
        view.setViewport(rectangle);
        view.setPreserveRatio(true);
        return status;
    }


    public boolean crop(double x0, double y0, double x1, double y1){
        boolean status = false;
        int startx = (int)x0;
        int starty = (int)y0;
        int endx = (int)x1;
        int endy = (int)y1;
        System.out.println("++++++++++++++++++++++++++++++++++++++");
        System.out.println(startx);
        System.out.println(starty);
        System.out.println(endx);
        System.out.println(endy);
        WritableImage cropedImage = new WritableImage(reader,startx,starty,endx,endy);
        load_image_writable(cropedImage);
        saveFile(image,save_url,name);
        return status;
    }
    public boolean cropPercent(double startXPercent, double startYPercent, double endXPercent, double endYPercent){
        double xAxis = image.getWidth()/2;
        double yAxis = image.getHeight()/2;
        double startX = 0;
        double startY = 0;
        if(startXPercent != 0) startX = (startXPercent/100)*image.getWidth();
        if(startYPercent != 0) startY = (startYPercent/100)*image.getHeight();
        double endX = image.getWidth()-startX;
        double endY = image.getHeight()-startY;
        if(endXPercent != 0) endX = (image.getWidth()-(((endXPercent/100)*image.getWidth())+startX));
        if(endYPercent != 0) endY = (image.getHeight()-(((endYPercent/100)*image.getHeight())+startY));
        System.out.println("am in===============================");
        System.out.println(startX);
        System.out.println(startY);
        System.out.println(endX);
        System.out.println(endY);
        //rotate(-90);
        if(startX+endX<=image.getWidth() && startY+endY<=image.getHeight() && crop(startX,startY,endX,endY))return true;
        else return false;
    }
    public void saveFile(WritableImage image,String path,String name){
        File file = new File(path+name);
        view.setFitWidth(image.getWidth());
        view.setFitHeight(image.getHeight());
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(view.snapshot(null,null),null);
        try{
            ImageIO.write(bufferedImage,"png",file);
            System.out.println("File saved");
        }
        catch(Exception e){
            System.out.println("File saving failed :Myimage.saveFile");
            e.printStackTrace();
        }
    }
    public void saveFilePane(WritableImage image, String path, String name, Text text, Color color, Pane pane){
        File file = new File(path+name);
        int tempx = (int)text.getX();
        int tempy = (int)text.getY();
        view.setFitWidth(image.getWidth());
        view.setFitHeight(image.getHeight());
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(view.snapshot(null,null),null);
        Graphics2D graphics = bufferedImage.createGraphics();
        double wid = (textXPercentage/100)*image.getWidth();
        double hei = (textYPercentage/100)*image.getHeight();
        java.awt.Color awtcol = new java.awt.Color(1,2,3,3);
        graphics.setColor(new java.awt.Color((int)(color.getRed()*255),(int)(color.getGreen()*255),(int)(color.getBlue()*255)));
        graphics.setFont(new Font("SansSerif",Font.PLAIN,(int)text.getFont().getSize()));
        graphics.drawString(text.getText(),(int)wid,(int)hei);

        try{
            ImageIO.write(bufferedImage,"png",file);
            System.out.println("File saved");
        }
        catch(Exception e){
            System.out.println("File saving failed :Myimage.saveFile");
            e.printStackTrace();
        }
        text.setX(tempx);
        text.setY(tempy);
        view.setFitWidth(pane.getWidth());
        view.setFitHeight(pane.getHeight());
    }
    public void rotate(int direction){
            view.setRotate(view.getRotate()+direction);
    }
}
