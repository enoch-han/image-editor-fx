package com.company;

import javafx.scene.paint.*;
import javafx.scene.effect.*;



public class MyEffect extends Blend {
    /**class field declaration*/
    private String name;
    private double red;
    private double green;
    private double blue;
    private double opacity;
    private int myMode;
    public double width ;
    public double height;
    private ColorInput colorInput;
    private Color paint;
    private String url = "source/";
    /*****************************/

    /**setters of the class*/

    public void setName(String name) {
        this.name = name;
    }

    public void setMyMode(int choice){
        this.myMode = choice;
        BlendMode[] modes = {BlendMode.MULTIPLY,BlendMode.DIFFERENCE,BlendMode.SRC_OVER,BlendMode.EXCLUSION,BlendMode.OVERLAY,BlendMode.RED,BlendMode.GREEN,BlendMode.BLUE};
        setMode(modes[choice]);
    }

    public void setRed(double red){
        this.red = red;
    }

    public void setBlue(double blue) {
        this.blue = blue;
    }

    public void setGreen(double green) {
        this.green = green;
    }

    public void setO(double opacity){
        this.opacity = opacity;
    }

    public void setRGB(double red,double green,double blue){
        setRed(red);
        setGreen(green);
        setBlue(blue);
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setPaint() {
        this.paint = new Color(red,green,blue,opacity);
    }

    public void setColorInput(){
        this.colorInput = new ColorInput();
        colorInput.setWidth(width);
        colorInput.setHeight(height);
        colorInput.setPaint(paint);
        setTopInput(this.colorInput);
    }
    /********************************************************/

    /**getters of the class*/

    public String getName() {
        return name;
    }

    public Color getPaint() {
        return paint;
    }

    public double getBlue() {
        return blue;
    }

    public double getRed() {
        return red;
    }

    public double getGreen() {
        return green;
    }
    public double getO(){
        return opacity;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public int getMyMode() {
        return myMode;
    }
    /***********************************************************/

    /**constructors of the class*/
    public MyEffect(String name,double red,double green,double blue,double opacity,double width,double height,int mode){
        setName(name);
        setRGB(red,green,blue);
        setO(opacity);
        setWidth(width);
        setHeight(height);
        setMyMode(mode);
        setColorInput();
        setTopInput(this.colorInput);
    }
    public MyEffect(String name,double red,double green,double blue,double width,double height,int mode){
        setName(name);
        setRGB(red,green,blue);
        setWidth(width);
        setHeight(height);
        setMyMode(mode);
        setColorInput();
        setTopInput(this.colorInput);
    }
    public MyEffect(String name,double red,double green,double blue,double opacity,int mode){
        setName(name);
        setRGB(red,green,blue);
        setMyMode(mode);
        setColorInput();
        setTopInput(this.colorInput);
    }
    public MyEffect(){
    }
    /*****************************************************************/


}
