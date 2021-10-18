package com.company;

import java.io.*;
import java.util.ArrayList;

public class Functionality {

    /**fields of the class*/
    private String url = "src/resource/effects/";
    MyEffect effect = new MyEffect();
    public Functionality(MyEffect effect){
        this.effect = effect;
    }
    public Functionality(){}

    /**method which writes effects like RGB, opacity, name and blend mode in to a file*/
    public void writeEffect(){
        try {
            try(DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(url+"effects.txt",true))){
                outputStream.writeUTF(effect.getName());
                outputStream.writeDouble(effect.getRed());
                outputStream.writeDouble(effect.getGreen());
                outputStream.writeDouble(effect.getBlue());
                outputStream.writeDouble(effect.getO());
                outputStream.writeInt(effect.getMyMode());
                System.out.println("effects stored successfully");
            }
        }
        catch(FileNotFoundException excep){
            System.out.println("effects.txt file not found");
        }
        catch (IOException excep){
            System.out.println("io exception found in writeColor method");
        }
    }
    /**method to read effect properties form previously stored data*/
    public MyEffect[] readEffect(){
        ArrayList<MyEffect> effects = new ArrayList<>();
        try {
            try(DataInputStream inputStream = new DataInputStream(new FileInputStream(url+"effects.txt"))){
                boolean status = true;
                while(status){
                    try{
                        String tempname = inputStream.readUTF();
                        double tempred = inputStream.readDouble();
                        double tempgreen = inputStream.readDouble();
                        double tempblue = inputStream.readDouble();
                        double tempopacity= inputStream.readDouble();
                        int tempmode = inputStream.readInt();

                        MyEffect tempeffect = new MyEffect(tempname,tempred,tempgreen,tempblue,tempopacity,tempmode);
                        effects.add(tempeffect);
                    }
                    catch (EOFException excep){
                        System.out.println("all file read");
                        status = false;
                    }
                }
            }
        }
        catch(IOException ioexep){
            System.out.println("IOException found in readEffect method");
            ioexep.printStackTrace();
        }
        MyEffect[] tempeffects = new MyEffect[effects.size()];
        return effects.toArray(tempeffects);
    }
}
