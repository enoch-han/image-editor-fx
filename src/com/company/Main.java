package com.company;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.geometry.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.ScrollEvent;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Main /*extends Application */{/*
    private ImageView imageview;
    private StackPane pane = new StackPane();
    private ScrollPane scrollPane= new ScrollPane();
    Scene scene = new Scene(pane,960,600);
    final  DoubleProperty zoomproperty = new SimpleDoubleProperty(scene.getHeight());
    MyImage image = new MyImage("image.jpg");
    Image img = image.getImage();
    Button button = new Button("crop");
    String[] temp;
    @Override
    public void start(Stage primaryStage) throws Exception {
            double widthratio = scene.getWidth()/scene.getHeight();
            imageview = image.getView();
            pane.getChildren().addAll(scrollPane,button);
            zoomproperty.addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable obs) {
                    imageview.setFitWidth(zoomproperty.get()*widthratio);
                    imageview.setFitHeight(zoomproperty.get());
                }
            });
            scrollPane.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
                @Override
                public void handle(ScrollEvent event) {
                    if(event.getDeltaY()>0){
                        zoomproperty.set(zoomproperty.get()*1.1);
                    }else if (event.getDeltaY()<0){
                        zoomproperty.set(zoomproperty.get()/1.1);
                    }
                }
            });
            button.setOnAction(event ->crop() );
            scrollPane.setContent(imageview);
            imageview.setPreserveRatio(true);
            imageview.setFitHeight(scene.getHeight()-2);
            imageview.setFitWidth(scene.getWidth()-1);
            primaryStage.setScene(scene);
            primaryStage.show();
         }
         public void crop(){
            //image.crop(400,400,500,500);
            image.cropPercent(30,30,30,30);
            imageview.setImage(image.getImage());
             imageview.setFitHeight(scene.getHeight()-2);
             imageview.setFitWidth(scene.getWidth()-1);
             System.out.println(">>>>>>>>>" + imageview.getFitHeight());
             System.out.println(">>>>>>>>>" + imageview.getFitHeight()*(image.getImage().getWidth()/image.getImage().getHeight()));
         }
         */
    }