package networking;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;



public class Gui extends Application implements WritableGui{
    Client client;
    TextArea messageArea;
    TextField messageField,to;
    Button send;
    Profile profile;
    public Stage stage;
    public Scene scene;

    public Client getClient() {
        return client;
    }

    public Gui(){
        client = new Client();
        profile = new Profile();
        profile.setFullName("henock");
        profile.setUserName("hen");
        client.setGui(this);
        //client.start();
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane grandpa = new BorderPane();
        grandpa.setCenter(centerPane());
        grandpa.setLeft(leftPane());

        Scene scene = new Scene(grandpa);
        primaryStage.setScene(scene);
        primaryStage.show();
        this.scene = scene;
        this.stage = primaryStage;
    }

    public VBox centerPane(){
        VBox parent = new VBox(10);
        parent.setPadding(new Insets(10));
        messageArea = new TextArea();
        messageArea.setPrefColumnCount(30);
        messageArea.setPrefRowCount(25);
        HBox bar = new HBox(10);
        messageField = new TextField();
        to = new TextField();
        send = new Button("Send");
        send.setOnAction(event -> send());

        bar.getChildren().addAll(to,messageField,send);
        parent.getChildren().addAll(messageArea,bar);


        return parent;
    }

    public VBox leftPane(){
        VBox parent = new VBox(10);
        parent.setPadding(new Insets(10));
        TextField temp = new TextField();
        parent.getChildren().add(temp);
        return parent;
    }

    public void send(){
        Message message = new Message(messageField.getText(),profile.getUserName(),to.getText(),1);
        message.setMethod_invoke(1);
        client.send(message);
        //client.start();
        receive();
    }

    public void receive(){
        int terminate = 0;
        while(terminate != 2){
            Message message = client.receive();
            System.out.println(message.getReceiver());
            if(message.getReceiver().equals(profile.getUserName())){
                if(message.getText() != null){
                    messageArea.setText(messageArea.getText().concat("\n" +"________________________________________"+"\n"+ message.getText()));
                }
            }
            terminate = message.getMessage_type();
        }
    }

    @Override
    public void writeGui(Message message) {
        System.out.println("innn guiiiii");
        if(message.getReceiver().equals(profile.getUserName())){
            if(message.getText() != null){
                messageArea.setText(messageArea.getText().concat("\n" +"________________________________________"+"\n"+ message.getText()));
            }
        }
    }

    public static void main(String[] args){
        launch(args);
    }

}
