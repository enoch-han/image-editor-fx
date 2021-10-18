package com.company;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.scene.image.*;
import networking.*;

import java.io.File;
import javafx.scene.effect.*;
import javafx.scene.paint.*;
import javafx.scene.input.*;

import static javafx.scene.input.KeyCode.*;


public class Gui extends Application {
    Stage helpstage = new Stage();
    Stage aboutstage = new Stage();
    private Scene scene;
    private Stage primaryStage;
    private BorderPane mainPane = new BorderPane();
    private MenuBar menuBar;
    private VBox rightpane;
    private StackPane middlepane;
    private ScrollPane imageScrollPane;
    private GridPane infopane;
    private Label dimension;
    private TextField nameField, resizeWidth, resizeHeight;
    private TextArea imageText;
    private String saveDir = "C:/Users/hp/Documents/books/projects/java codes/Image Editor/src/resource/saved/";
    private String url = "image2.jpg";
    private MyImage image = new MyImage(url);
    private Button enterText,addcrop,crop,top,bottom,left,right,position,applyeffect,store,fitscreen,resize;
    private ColorPicker picker;
    private ChoiceBox<String> sizeBox,blendBox;
    private CheckBox ratioCheck;
    private ObservableList<String> sizelist,blendtypes;
    private int[] size = {4, 8, 16, 32, 64};
    private String[] blendString = {"Multiply","Difference","Src over","Exclusion","Overlay","Red","Green","Blue"};
    private String[] sizeString = {"4", "8", "16", "32", "64"};
    private double widthRatio,heightRatio,cropWidth,cropHeight;
    private int percentYChange = 0;
    private int percentXChange = 0;
    private int percentWidthChange = 0;
    private int percentHeightChange = 0;
    private int percentTextXchange = 0;
    private int percentTextYchange = 0;
    private Boolean isCropSet = false;
    private ImageView view = image.getView();
    private Text text;
    private AnchorPane textpane;
    //sliders
    private Slider contrastSlider, brightnessSlider, hueSlider,saturationSlider, sepiaSlider, glowSlider,redSlider,greenSlider,blueSlider,opacitySlider;
    Handler handler;
    Blend blend = new Blend();
    ColorInput colorInput = new ColorInput();
    Color color = new Color(0.6,0,0,0.5);
    private boolean istextadded = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //setting the stage properties
        scene = new Scene(mainPane, 1000, 700);
        handler = new Handler();
        mainPane.setTop(northPane());
        mainPane.setRight(rightPane());
        mainPane.setCenter(middlePane());
        primaryStage.getIcons().add(new Image("icon/logo.png"));
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setScene(scene);
        primaryStage.setTitle("ስነ PICTORIAL Image Editor  version 1.0");
        primaryStage.show();

    }

    public void load(String url) {
        image = new MyImage(url);
    }

    public MenuBar northPane() {
        //menu bar for the gui
        menuBar = new MenuBar();

        //menus for the menu bar
        Menu file = new Menu("_File");
        Menu options = new Menu("_Options");
        Menu help = new Menu("_Help");
        Menu about = new Menu("_About Us");

        //menu items for the file menu
        MenuItem openFile = new MenuItem("Open   Ctrl + O");
        MenuItem save = new MenuItem("Save   Ctrl + S");
        MenuItem saveAs = new MenuItem("Save as  Ctrl + Shift + S");
        MenuItem exit = new MenuItem("Exit   Ctrl + E");
        file.getItems().addAll(openFile, new SeparatorMenuItem(), save, saveAs, new SeparatorMenuItem(), exit);

        //menu items for the options menu
        MenuItem share = new MenuItem("Share");
        options.getItems().add(share);

        //menu items for the help menu
        MenuItem helpme = new MenuItem("Help");
        help.getItems().add(helpme);

        //menu items for the about menu
        MenuItem aboutUs = new MenuItem("_About Us");
        about.getItems().add(aboutUs);

        //event handler registration
        openFile.setOnAction(e -> handler.menuHandler(openFile));
        middlePane();
        save.setOnAction(e -> handler.menuHandler(save));
        saveAs.setOnAction(e -> handler.menuHandler(saveAs));
        exit.setOnAction(e -> handler.menuHandler(exit));
        helpme.setOnAction(e -> handler.menuHandler(helpme));
        aboutUs.setOnAction(e -> handler.menuHandler(aboutUs));
        share.setOnAction(e -> handler.menuHandler(share));

        //setting the menubar
        menuBar.getMenus().addAll(file, options, help, about);


        return menuBar;
    }

    public StackPane middlePane() {
        //pane for the image or the middle pane
        textpane = new AnchorPane();
        middlepane = new StackPane();
        imageScrollPane = new ScrollPane();
        imageScrollPane.setMinHeight(scene.getHeight() - 25);
        final DoubleProperty zoomproperty = new SimpleDoubleProperty(scene.getHeight());
        widthRatio = imageScrollPane.getWidth() / imageScrollPane.getHeight();
        heightRatio = imageScrollPane.getHeight() / imageScrollPane.getWidth();
        fitScreen();
        imageScrollPane.setContent(view);
        zoomproperty.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable obs) {
                image.getView().setFitWidth(zoomproperty.get());
                image.getView().setFitHeight(zoomproperty.get());
            }
        });
        imageScrollPane.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaY() > 0) {
                    zoomproperty.set(zoomproperty.get() * 1.1);
                } else if (event.getDeltaY() < 0) {
                    zoomproperty.set(zoomproperty.get() / 1.1);
                }
            }
        });

        cropWidth = middlepane.getWidth();
        cropHeight = middlepane.getHeight();
        middlepane.getChildren().add(imageScrollPane);
        return middlepane;
    }

    public ScrollPane rightPane() {
        //the left side of the border pane
        ScrollPane parent = new ScrollPane();
        Accordion accordion = new Accordion();
        rightpane = new VBox();
        rightpane.setMaxWidth(scene.getWidth() / 3);
        accordion.getPanes().addAll(cropPane(),resizePane(),textPane(),adjust(),effect());
        accordion.setExpandedPane(adjust());
        rightpane.getChildren().addAll(infoPane(),accordion);
        parent.setContent(rightpane);
        return parent;
    }

    public VBox infoPane() {
        Button rotate = new Button("Rotate");
        VBox parent = new VBox(5);
        parent.setPadding(new Insets(5));
        HBox bar = new HBox(10);
        bar.setAlignment(Pos.CENTER);
        infopane = new GridPane();
        infopane.setPadding(new Insets(5, 10, 5, 10));
        nameField = new TextField();
        nameField.setText(image.getName());
        dimension = new Label(image.getImage().getWidth() + "x" + image.getImage().getHeight());

        fitscreen = new Button("Fit screen");
        fitscreen.setOnAction(event -> fitScreen());
        rotate.setOnAction(event -> {
            view.setRotate(view.getRotate() + 90);
        });
        bar.getChildren().addAll(fitscreen,rotate);
        infopane.add(new Label("Name :"), 0, 0);
        infopane.add(nameField, 1, 0);
        infopane.add(new Label("Dimemsion :  "), 0, 1);
        infopane.add(dimension, 1, 1);
        infopane.add(new Label("URL :"), 0, 2);
        infopane.add(new Label(image.getSource_url()), 1, 2);
        parent.getChildren().addAll(infopane,bar);
        return parent;
    }

    public TitledPane cropPane (){
        double initx = 0;
        double inity = 0;
        double initwidth = middlepane.getWidth();
        double initheight = middlepane.getHeight();
        double x,y,width,height;
        isCropSet = true;
        TitledPane rootPane = new TitledPane();
        VBox vbar = new VBox(10);
        vbar.setPadding(new Insets(10));
        vbar.setAlignment(Pos.CENTER);
        HBox hbar = new HBox(20);
        HBox lrbar = new HBox(20);
        lrbar.setAlignment(Pos.CENTER);
        addcrop = new Button("Add Tools");
        addcrop.setOnAction(e->addCrop());
        crop = new Button("Crop");
        crop.setOnAction(event -> crop());
        top = new Button("Top");
        top.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == UP){
                    if(colorInput.getY() != 0){
                        colorInput.setY(colorInput.getY()-1);
                        colorInput.setHeight(colorInput.getHeight()+1);
                        changePercentCalculator(1);
                    }
                }
                else if (event.getCode() == DOWN){
                    if(colorInput.getY() != middlepane.getHeight()){
                        colorInput.setY(colorInput.getY()+1);
                        colorInput.setHeight(colorInput.getHeight()-1);
                        changePercentCalculator(1);
                    }
                }
                else;
            }
        });
        bottom = new Button("Bottom");
        bottom.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == UP){
                    if(colorInput.getHeight() != 0){
                        colorInput.setHeight(colorInput.getHeight()-1);
                        changePercentCalculator(2);
                    }
                }
                else if (event.getCode() == DOWN){
                    if(colorInput.getHeight()!= middlepane.getHeight()){
                        colorInput.setHeight(colorInput.getHeight()+1);
                        changePercentCalculator(2);
                    }
                }
                else;
            }
        });
        left = new Button("Left");
        left.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == LEFT){
                    if(colorInput.getX() != 0){
                        colorInput.setX(colorInput.getX()-1);
                        colorInput.setWidth(colorInput.getWidth()+1);
                        changePercentCalculator(1);
                    }
                }
                else if(event.getCode() == RIGHT){
                    if(colorInput.getX() != middlepane.getWidth()){
                        colorInput.setX(colorInput.getX()+1);
                        colorInput.setWidth(colorInput.getWidth()-1);
                        changePercentCalculator(1);
                    }
                }
                else;
            }
        });
        right = new Button("Right");
        right.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == LEFT){
                    if(colorInput.getWidth() != 0){
                        colorInput.setWidth(colorInput.getWidth()-1);
                        changePercentCalculator(2);
                    }
                }
                else if(event.getCode() == RIGHT){
                    if(colorInput.getWidth() != middlepane.getWidth()){
                        colorInput.setWidth(colorInput.getWidth()+1);
                        changePercentCalculator(2);
                    }
                }
                else;
            }
        });
        lrbar.getChildren().addAll(left,right);
        hbar.setPadding(new Insets(5));
        hbar.getChildren().addAll(addcrop,crop);
        vbar.getChildren().addAll(hbar,new Label("choose position"),top,lrbar,bottom);

        rootPane.setContent(vbar);
        rootPane.setText("Crop");


        return rootPane;
    }
    public TitledPane resizePane(){
       TitledPane rootPane = new TitledPane();
       rootPane.setText("Resize");
        HBox resizehbox = new HBox(10);
        VBox resizevbox = new VBox(20);
        widthRatio = image.getImage().getWidth()/image.getImage().getHeight();
        heightRatio = image.getImage().getHeight()/image.getImage().getWidth();
        resizeWidth = new TextField("" + image.getImage().getWidth());
        resizeWidth.setPrefColumnCount(5);
        resizeHeight = new TextField("" + image.getImage().getHeight());
        resizeHeight.setPrefColumnCount(5);
        ratioCheck = new CheckBox("Preserve ratio");
        resize = new Button("resize");
        resizeWidth.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(ratioCheck.isSelected()){
                    try{
                        resizeHeight.setText("" + Integer.parseInt(resizeWidth.getText())*heightRatio);
                    }catch (NumberFormatException excep){
                        resizeHeight.setText("" + image.getImage().getHeight());
                    }
                }
            }
        });
        resizeHeight.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(ratioCheck.isSelected()){
                    try{
                        resizeWidth.setText("" + Integer.parseInt(resizeHeight.getText())*widthRatio);
                    }catch (NumberFormatException excep){
                        resizeWidth.setText("" + image.getImage().getWidth());
                    }
                }
            }
        });
        resize.setOnAction(e -> {
            image.setWidth(Integer.parseInt(resizeWidth.getText()));
            image.setHeight(Integer.parseInt(resizeHeight.getText()));
        });
        resizehbox.getChildren().addAll(resizeWidth, new Label("x"), resizeHeight);
        resizevbox.getChildren().addAll(resizehbox, ratioCheck,resize);
        resizevbox.setPadding(new Insets(20, 20, 10, 20));
        rootPane.setContent(resizevbox);
       return rootPane;
    }
    public TitledPane textPane(){
        TitledPane rootPane = new TitledPane();
        rootPane.setText("Insert Text");
        VBox textbox = new VBox(5);
        HBox stylebox = new HBox(10);
        HBox positionbox = new HBox();
        positionbox.setAlignment(Pos.CENTER);
        positionbox.setPadding(new Insets(5,30,5,20));
        textbox.setPadding(new Insets(5, 10, 5, 10));
        imageText = new TextArea();
        imageText.setPrefRowCount(6);
        imageText.setPrefColumnCount(10);
        imageText.setPromptText("Enter your text here. After picking your size and color click Insert you can use arrow keys to position text");
        enterText = new Button("Insert");
        picker = new ColorPicker();
        sizelist = FXCollections.observableArrayList();
        for (int i = 0; i < size.length; i++) {
            sizelist.add(sizeString[i]);
        }
        position = new Button("Position");
        sizeBox = new ChoiceBox<>(sizelist);
        sizeBox.getSelectionModel().select(2);
        position.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                percentXChange = (int)((text.getX()/middlepane.getWidth())*100);
                percentYChange = (int)((text.getY()/middlepane.getHeight())*100);
                if(event.getCode() == UP){
                    text.setY(text.getY()-1);
                }
                else if(event.getCode() == DOWN){
                    text.setY(text.getY()+1);
                }
                else if(event.getCode()== LEFT){
                    text.setX(text.getX()-1);
                }
                else if(event.getCode() == RIGHT){
                    text.setX(text.getX()+1);
                }
            }
        });

        enterText.setOnAction(event -> {
            if(!istextadded){
                middlepane.getChildren().add(textpane);
                istextadded = true;
                enterText((int)middlepane.getWidth()/2,(int)middlepane.getHeight()/2,imageText.getText());
            }
            else {
                middlepane.getChildren().remove(textpane);
                istextadded = false;
            }

        });
        positionbox.getChildren().add(position);
        stylebox.getChildren().addAll(enterText, picker, sizeBox);
        textbox.getChildren().addAll(new Label("Text"), imageText, stylebox,position);

        rootPane.setContent(textbox);
        return rootPane;
    }

    public TitledPane adjust() {
        //pane for adjusting the color and adding some tone
        Button refresh = new Button("refresh");
        Button refreshtone = new Button("refresh");

        TitledPane rootpane = new TitledPane();
        rootpane.setText("color adjust");
        TabPane adjustpane = new TabPane();
        Tab colorAd = new Tab("Color Adjust");
        VBox vBox = new VBox(5);
        contrastSlider = new Slider(0, 1, 0.5);
        contrastSlider.setShowTickMarks(true);
        contrastSlider.setShowTickLabels(true);
        contrastSlider.setMajorTickUnit(0.25f);
        contrastSlider.setBlockIncrement(0.1f);
        contrastSlider.valueChangingProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                image.contrastPoint = contrastSlider.getValue();
                colorAdjust(1);
            }
        });
        brightnessSlider = new Slider(0, 1, 0.5);
        brightnessSlider = new Slider(0, 1, 0.5);
        brightnessSlider.setShowTickMarks(true);
        brightnessSlider.setShowTickLabels(true);
        brightnessSlider.setMajorTickUnit(0.25f);
        brightnessSlider.setBlockIncrement(0.1f);
        hueSlider = new Slider(0, 1, 0.5);
        hueSlider.setShowTickMarks(true);
        hueSlider.setShowTickLabels(true);
        hueSlider.setMajorTickUnit(0.25f);
        hueSlider.setBlockIncrement(0.1f);
        saturationSlider = new Slider(0,1,0.5);
        saturationSlider.setShowTickMarks(true);
        saturationSlider.setShowTickLabels(true);
        saturationSlider.setMajorTickUnit(0.25f);
        saturationSlider.setBlockIncrement(0.1f);


        vBox.getChildren().addAll(new Label("Contrast"), contrastSlider, new Label("Brightness"), brightnessSlider, new Label("Hue"), hueSlider, new Label("Saturation"), saturationSlider,refresh);
        vBox.setPadding(new Insets(5, 10, 5, 10));

        refresh.setOnAction(event -> {
            view.setEffect(new ColorAdjust(hueSlider.getValue(),saturationSlider.getValue(),brightnessSlider.getValue(),contrastSlider.getValue()));
        });

        colorAd.setContent(vBox);

        Tab tone = new Tab("Tones");
        VBox vBox1 = new VBox(10);
        sepiaSlider = new Slider(0, 1, 0.5);
        sepiaSlider.setShowTickMarks(true);
        sepiaSlider.setShowTickLabels(true);
        sepiaSlider.setMajorTickUnit(0.25f);
        sepiaSlider.setBlockIncrement(0.1f);
        glowSlider = new Slider(0, 1, 0.5);
        glowSlider.setShowTickMarks(true);
        glowSlider.setShowTickLabels(true);
        glowSlider.setMajorTickUnit(0.25f);
        glowSlider.setBlockIncrement(0.1f);

        refreshtone.setOnAction(event -> {
            SepiaTone sepiaTone = new SepiaTone();
            Glow glow = new Glow();
            glow.setLevel(glowSlider.getValue());
            sepiaTone.setInput(glow);
            sepiaTone.setLevel(sepiaSlider.getValue());
            view.setEffect(sepiaTone);
        });
        vBox1.getChildren().addAll(new Label("Sepia tone"), sepiaSlider, new Label("Glow tone"), glowSlider,refreshtone);
        vBox1.setPadding(new Insets(5, 10, 5, 10));
        tone.setContent(vBox1);

        adjustpane.getTabs().addAll(colorAd, tone);
        rootpane.setContent(adjustpane);
        //adjustpane.setMinHeight(scene.getHeight()-((4/5)*scene.getHeight()-10));
        return rootpane;
    }

    public TitledPane effect() {
        TitledPane rootpane = new TitledPane();
        rootpane.setText("effects");
        TabPane effect = new TabPane();
        /*********************************************************************************/
        Tab builtIn = new Tab("Built In");
        VBox outerbox = new VBox(10);
        outerbox.setAlignment(Pos.CENTER);
        Functionality functionality = new Functionality();
        MyEffect[] effects = functionality.readEffect();
        ImageView demo = new ImageView("resource/effects/demo.jpg");
        demo.setFitHeight(200);
        demo.setFitWidth(200);
        for(int i=0;i<effects.length;i++){
            effects[i].width = demo.getFitWidth();
            effects[i].height = demo.getFitHeight();
            effects[i].setColorInput();
        }
        Pagination pagination = new Pagination(effects.length);
        pagination.setPageFactory((Integer pageindex)->createpagination(effects,pageindex,demo));
        applyeffect = new Button("Apply");
        applyeffect.setOnAction(event -> {
            System.out.println("herre");
            effects[pagination.getCurrentPageIndex()].width = view.getImage().getWidth();
            effects[pagination.getCurrentPageIndex()].height = view.getImage().getHeight();
            effects[pagination.getCurrentPageIndex()].setColorInput();
            view.setEffect(effects[pagination.getCurrentPageIndex()]);
        });
        outerbox.getChildren().addAll(pagination,applyeffect);
        builtIn.setContent(outerbox);
        /**--------------------------------------------------------------------------------------*/


        /*****************************************************************************************/
        Tab addEffect = new Tab("MY Effects");

        ImageView demo2 = new ImageView("resource/effects/demo.jpg");
        TextField field = new TextField();
        field.setPromptText("enter the name here");
        HBox box = new HBox(5);
        Button refresh = new Button("refresh");
        blendtypes = FXCollections.observableArrayList();
        for(int i=0;i<blendString.length;i++){
            blendtypes.add(blendString[i]);
        }
        blendBox = new ChoiceBox<>(blendtypes);
        blendBox.getSelectionModel().select(2);
        VBox parent = new VBox(10);
        parent.setPadding(new Insets(10));
        parent.setAlignment(Pos.CENTER);
        GridPane grid = new GridPane();
        redSlider = new Slider(0, 1, 0.5);
        redSlider.setShowTickMarks(true);
        redSlider.setShowTickLabels(true);
        redSlider.setMajorTickUnit(0.25f);
        redSlider.setBlockIncrement(0.1f);
        greenSlider = new Slider(0, 1, 0.5);
        greenSlider.setShowTickMarks(true);
        greenSlider.setShowTickLabels(true);
        greenSlider.setMajorTickUnit(0.25f);
        greenSlider.setBlockIncrement(0.1f);
        blueSlider = new Slider(0, 1, 0.5);
        blueSlider.setShowTickMarks(true);
        blueSlider.setShowTickLabels(true);
        blueSlider.setMajorTickUnit(0.25f);
        blueSlider.setBlockIncrement(0.1f);
        opacitySlider = new Slider(0, 1, 0.5);
        opacitySlider.setShowTickMarks(true);
        opacitySlider.setShowTickLabels(true);
        opacitySlider.setMajorTickUnit(0.25f);
        opacitySlider.setBlockIncrement(0.1f);

        grid.add(new Label("R"),0,0);
        grid.add(redSlider,1,0);
        grid.add(new Label("G"),0,1);
        grid.add(greenSlider,1,1);
        grid.add(new Label("B"),0,2);
        grid.add(blueSlider,1,2);
        grid.add(new Label("O"),0,3);
        grid.add(opacitySlider,1,3);

        demo2.setFitWidth(200);
        demo2.setFitHeight(150);

        store = new Button("store to file");
        refresh.setOnAction(event -> {
            MyEffect efct = new MyEffect(field.getText(),redSlider.getValue(),greenSlider.getValue(),blueSlider.getValue(),opacitySlider.getValue(),blendBox.getSelectionModel().getSelectedIndex());
            efct.setWidth(200);
            efct.setHeight(150);
            efct.setColorInput();
            demo2.setEffect(efct);
        });

        store.setOnAction(event -> new Functionality((MyEffect) demo2.getEffect()).writeEffect());

        box.getChildren().addAll(blendBox,refresh);
        parent.getChildren().addAll(box,grid,demo2,field,store);

        addEffect.setContent(parent);

        /**--------------------------------------------------------------------------------------*/


        effect.getTabs().addAll(builtIn, addEffect);
        rootpane.setContent(effect);
        //effect.setMinHeight(scene.getHeight()-((3/5)*scene.getHeight())-10);
        return rootpane;
    }
    public VBox createpagination(MyEffect[] effects,int index,ImageView demo){
        VBox imagepane = new VBox(20);
        imagepane.setAlignment(Pos.CENTER);
        imagepane.setPadding(new Insets(10));
        //demo.setEffect(null);

        effects[index].width = image.getImage().getWidth();
        effects[index].height = image.getImage().getHeight();
        demo.setEffect(effects[index]);
        Label name = new Label();
        name.setText(effects[index].getName());
        imagepane.getChildren().addAll(demo,name);
        return imagepane;
    }
    public void addCrop(){
        if(!isCropSet){
            isCropSet = true;
            colorInput.setPaint(color);
            colorInput.setWidth(middlepane.getWidth());
            colorInput.setHeight(middlepane.getHeight());
            blend.setTopInput(colorInput);
            view.setEffect(blend);
        }
        else{
            isCropSet = false;
            view.setEffect(null);
        }
    }
    public void crop(){
        image.cropPercent(percentXChange,percentYChange,percentWidthChange,percentYChange);
        view = image.getView();
        imageScrollPane.setContent(view);
        fitScreen();
    }
    public void changePercentCalculator(int choice){
        //one implies for the top and left crop
        //two implies for the bottom and right crop
        switch(choice){
            case 1:
                percentYChange = (int)((colorInput.getY()/middlepane.getHeight())*100);
                percentXChange = (int)((colorInput.getX()/middlepane.getWidth())*100);
                break;
            case 2:
                percentYChange = (int)((colorInput.getY()/middlepane.getHeight())*100);
                percentXChange = (int)((colorInput.getX()/middlepane.getWidth())*100);
                percentHeightChange = (int)((((middlepane.getHeight()-colorInput.getHeight())/middlepane.getHeight())*100)-percentYChange);
                percentWidthChange = (int)((((middlepane.getWidth()-colorInput.getWidth())/middlepane.getWidth())*100))-percentXChange;
                break;
            default:
                System.out.println("invalid choice Gui:changePercentCalculator");
        }

        System.out.println(percentXChange+"/"+percentYChange+"/"+percentWidthChange+"/"+percentHeightChange);

    }
    public void fitScreen(){
        image.getView().setFitWidth(scene.getWidth() - (scene.getHeight() / 3));
        image.getView().setFitHeight(scene.getHeight() - 30);
        if(image.getImage().getWidth()>image.getImage().getHeight()){
            widthRatio = image.getImage().getWidth()/image.getImage().getHeight();
            heightRatio = 0;
            //image.getView().setFitWidth(scene.getWidth()-(scene.getHeight()/3));
            //image.getView().setFitHeight((middlepane.getWidth()*widthRatio)-30);
        }
        else {
            heightRatio = image.getImage().getHeight()/image.getImage().getHeight();
            widthRatio = 0;
        }
        image.getView().setPreserveRatio(false);
    }
    public void enterText(int x,int y,String draft){
        if(draft == null)draft = "";
        textpane.getChildren().remove(text);
        text = new Text();
        text.setX(x);
        text.setY(y);
        text.setText(draft);
        text.setFont(new Font(size[(int)sizeBox.getSelectionModel().getSelectedIndex()]));
        text.setFill(picker.getValue());
        textpane.getChildren().add(text);
        percentXChange = (int)((text.getX()/middlepane.getWidth())*100);
        percentYChange = (int)((text.getY()/middlepane.getHeight())*100);
        image.setTextPosition(percentXChange,percentYChange);
        //image.saveFilePane((WritableImage) image.getImage(),"C:/Users/Enoch/IdeaProjects/Image Editor/src/resource/saved/",image.getName(),text,Color.RED,middlepane;
    }
    public void colorAdjust(int choice){
        Effect temp = view.getEffect();
        ColorAdjust colorAdjust = image.colorAdjust;
        switch (choice){
            case 1:
                if (temp instanceof SepiaTone){
                    view.setEffect(null);
                    ((SepiaTone) temp).setInput(colorAdjust);
                    view.setEffect(temp);
                    colorAdjust.setContrast(image.contrastPoint);
                }
                else if(temp instanceof Glow){
                    view.setEffect(null);
                    ((Bloom) temp).setInput(colorAdjust);
                    view.setEffect(temp);
                    colorAdjust.setContrast(image.contrastPoint);
                }
                else{
                    view.setEffect(colorAdjust);
                    colorAdjust.setContrast(image.contrastPoint);
                }
                break;
            case 2:
                if(temp instanceof SepiaTone){
                    view.setEffect(null);
                    ((SepiaTone) temp).setInput(colorAdjust);
                    view.setEffect(temp);
                    colorAdjust.setBrightness(image.brightnessPoint);
                }
                else if(temp instanceof Glow){
                    view.setEffect(null);
                    ((Bloom) temp).setInput(colorAdjust);
                    view.setEffect(temp);
                    colorAdjust.setBrightness(image.brightnessPoint);
                }
                else{
                    view.setEffect(colorAdjust);
                    colorAdjust.setBrightness(image.brightnessPoint);
                }
                break;
            case 3:
                if(temp instanceof SepiaTone){
                    view.setEffect(null);
                    ((SepiaTone) temp).setInput(colorAdjust);
                    view.setEffect(temp);
                    colorAdjust.setHue(image.huePoint);
                }
                else if(temp instanceof Glow){
                    view.setEffect(null);
                    ((Bloom) temp).setInput(colorAdjust);
                    view.setEffect(temp);
                    colorAdjust.setHue(image.huePoint);
                }
                else{
                    view.setEffect(colorAdjust);
                    colorAdjust.setHue(image.huePoint);
                }
                break;
            case 4:
                if(temp instanceof SepiaTone){
                    view.setEffect(null);
                    ((SepiaTone) temp).setInput(colorAdjust);
                    view.setEffect(temp);
                    colorAdjust.setSaturation(image.saturationPoint);
                }
                else if(temp instanceof Glow){
                    view.setEffect(null);
                    ((Bloom) temp).setInput(colorAdjust);
                    view.setEffect(temp);
                    colorAdjust.setSaturation(image.saturationPoint);
                }
                else{
                    view.setEffect(colorAdjust);
                    colorAdjust.setSaturation(image.saturationPoint);
                }
                break;
        }

    }
    public void sepiaTone(){
        Effect temp = view.getEffect();
        SepiaTone sepiaTone = new SepiaTone(image.sepiaPoint);
        if(temp instanceof ColorAdjust){
            view.setEffect(null);
            ((ColorAdjust) temp).setInput(sepiaTone);
            view.setEffect(temp);
        }
        if(temp instanceof Glow){
            view.setEffect(null);
            ((Bloom) temp).setInput(sepiaTone);
            view.setEffect(temp);
        }
    }
    public void glow(){
        Effect temp = view.getEffect();
        Glow glow = new Glow(image.glowPoint);
        if(temp instanceof ColorAdjust){
            view.setEffect(null);
            ((ColorAdjust) temp).setInput(glow);
            view.setEffect(temp);
        }
        if(temp instanceof SepiaTone){
            view.setEffect(null);
            ((Glow) temp).setInput(glow);
            view.setEffect(temp);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    private class Handler {
        /**************************************** Event Handlers *********************************************/
        public void menuHandler(MenuItem item) {
            FileChooser fileChooser = new FileChooser();
            switch (item.getText()) {
                case "Open   Ctrl + O":
                    fileChooser.setTitle("Open Resource File");
                    fileChooser.getExtensionFilters().addAll(
                            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpeg", "*.gif", "*.bmp", "*.jpg"),
                            new FileChooser.ExtensionFilter("All Files", "*.*"));
                    File selectedFile = fileChooser.showOpenDialog(primaryStage);
                    if (selectedFile != null) {
                        System.out.println(selectedFile);
                        url = selectedFile.toURI().toString();
                        System.out.println(url);
                        image = new MyImage(url);
                        view = image.getView();
                        image.getView().setFitWidth(scene.getWidth() - (scene.getHeight() / 3));
                        image.getView().setFitHeight(scene.getHeight() - 30);
                        image.getView().setPreserveRatio(false);
                        imageScrollPane.setContent(view);
                    }
                    break;
                case "Save   Ctrl + S":
                        image.saveFilePane((WritableImage) image.getImage(),saveDir,nameField.getText(),text,picker.getValue(),middlepane);

                    break;
                case "Save as  Ctrl + Shift + S":
                    File savefile = new File(saveDir);
                    fileChooser.setInitialDirectory(savefile);
                    fileChooser.setInitialFileName(nameField.getText());
                    fileChooser.showSaveDialog(primaryStage);
                    String saveurl = "C:/Users/Enoch/IdeaProjects/Image Editor/src/resource/saved";
                            saveurl = fileChooser.getInitialFileName();
                            System.out.println(saveurl);
                    if (istextadded){
                        image.saveFilePane((WritableImage) image.getImage(),saveurl,fileChooser.getInitialFileName(),text,picker.getValue(),middlepane);
                    }
                    else {
                        image.saveFile((WritableImage) image.getImage(),savefile.getPath(),fileChooser.getInitialFileName());
                    }
                    break;
                case "Exit   Ctrl + E":
                    System.out.println("strike exit");
                    System.exit(0);
                    break;
                case "Help":
                    TextArea helparea = new TextArea();
                    String text1="=====================    This is the help pane   ==================\n" +
                            "========================    User Guide   ==========================\n" +
                            "The graphical user interface contains the image in the middle pane and \n"+
                            "the left pane contains the controllers.\n"+
                            "Info pane : contains the information about the image and can rename the \n"+
                            "image in the textfield\n"+
                            "Crop pane : contains tools for cropping, use add tools to insert tool\n"+
                            "and use the arrow tools to position and use crop button to crop\n"+
                            "Resize pane : contains tools for resizing, you can freely change the value \n"+
                            "or check preserve ratio and change values depending on ratio\n"+
                            "Insert Text Pane : contains tools for inserting text into the picture.\n"+
                            " after creating your text use enter to insert and use position to position\n"+
                            "Color Adjust Pane : adjust your colors and use refresh to see the change\n"+
                            "Effect Pane built in : use desired effect from the options given and use apply\n"+
                            "to insert effect on the image \n"+
                            "Effect pane MyEffect : use the sliders to set the colors and choose the blend style\n"+
                            "then your can try the effects by using refresh button and use add to file to insert\n"+
                            "the effects in local file\n"+
                            "\n";
                    helparea.setText(text1);
                    VBox parent = new VBox(10);
                    parent.setPadding(new Insets(10));
                    parent.getChildren().add(helparea);
                    Scene helpscene = new Scene(parent);
                    helpstage.setScene(helpscene);
                    helpstage.showAndWait();
                    break;
                case "_About Us":
                    TextArea aboutarea = new TextArea();
                    aboutarea.setEditable(false);
                    String text="ABOUT \n"+"=================================================================\n"+
                            "Photos have been influential in the world we live in.\n"+
                            "The most significant events in this age have been documented digitally, be it videos or pictures.\n"+
                            "Photos are not only a way of self-expression but also a way of telling a story.\n"+
                            "Sure Pictures in general are appeasing to humanity and have been so since the dawn of age.\n"+
                            "But only with photos have we truly started depicting the world as it is.\n"+
                            "Photos are the purest expressions of the world around us and how we conceive it.\n"+
                            "And this is exactly why we designed a photo editor.\n"+
                            "Our photo editor is capable of implementing the functions that most photo editors are capable of.\n"+
                            "But our  photo editor is unique for it can add a user defined effect.\n"+
                            "We were also able to add the chat room required.\n"+
                            "=======================================================================\n"+
                            "program Version 1.0\n"+
                            "GROUP MEMBERS\n"+"***********************************************\n"+
                            "Enqueselassie Kassahun       ETS-0450/10\n" +
                            "Ibrahim Abdulkadir            ETS-0411/10\n" +
                            "Ermiyas Tesfaye                 ETS-0462/10\n" +
                            "Henos Girma                      ETS-0627/10\n" +
                            "Henock Tesaye                   ETS-0614/10\n";
                    aboutarea.setText(text);
                    VBox aboutparent = new VBox(10);
                    aboutparent.setPadding(new Insets(10));
                    aboutparent.getChildren().add(aboutarea);
                    Scene aboutscene = new Scene(aboutparent);
                    aboutstage.setScene(aboutscene);
                    aboutstage.showAndWait();
                    break;
                case "Share":
                    networking.Gui gui = new networking.Gui();

                    Stage sharestage = gui.stage;
                    Scene scene = gui.scene;
                    //sharestage.setScene(scene);
                    sharestage.showAndWait();
                    break;
                }
            }
        }
}
