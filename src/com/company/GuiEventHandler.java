package com.company;

import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.*;
import javafx.stage.FileChooser.ExtensionFilter;
import java.io.File;

public class GuiEventHandler {

    Stage stage;
    Scene scene;
    public GuiEventHandler(Stage stage,Scene scene){
        this.stage = stage;
        this.scene = scene;
    }
    public void menuHanler(MenuItem item, String url, MyImage image ,ImageView view,ScrollPane pane){
        switch(item.getText()){
            case "Open   Ctrl + O":
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                fileChooser.getExtensionFilters().addAll(
                        new ExtensionFilter("Image Files", "*.png", "*.jpeg", "*.gif","*.bmp","*.jpg"),
                        new ExtensionFilter("All Files", "*.*"));
                File selectedFile = fileChooser.showOpenDialog(stage);
                if (selectedFile != null) {
                    System.out.println(selectedFile);
                    url = selectedFile.toURI().toString();

                }
                break;

        }
    }
}
