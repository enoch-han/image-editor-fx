package networking;
import java.net.*;
import java.io.*;


public class Client /*extends Thread*/ {
    Socket socket;
    ObjectInputStream objin;
    ObjectOutputStream objout;
    WritableGui gui;

    public void setGui(WritableGui gui) {
        this.gui = gui;
    }

    /******************************************************************************************************************/
    public void createConnection(){
        try{
            socket = new Socket("127.0.0.1",3306);
        }catch (IOException excep){
            excep.printStackTrace();
        }
    }
    /******************************************************************************************************************/
    public void closeConnection(){
        try{
            if(objin != null){
                objin.close();
            }
            if(objout != null){
                objout.close();
            }
            socket.close();
        }catch (IOException excep){
            excep.printStackTrace();
        }
    }
    /******************************************************************************************************************/
    public void send(Message message){
        try {
            createConnection();
            objout = new ObjectOutputStream(socket.getOutputStream());
            objout.writeObject(message);
            System.out.println("message sent client");
        }catch (IOException excep){
            excep.printStackTrace();
        }
    }
    public Message receive(){
        Message message = null;
        try {
            //if(objin != null)objin.close();
            objin = new ObjectInputStream(socket.getInputStream());
            message = (Message) objin.readObject();
            if(objin != null){
                System.out.println("message received client");
            }
        }catch (IOException excep){
            excep.printStackTrace();
        }
        catch (ClassNotFoundException excep){
            excep.printStackTrace();
        }
        catch (Exception e){}
        return message;
    }

    /*@Override
    public void run() {
        try {
            createConnection();
            Message message = null;
            while (true) {
                System.out.println("1111111111111111111111");
                objout = new ObjectOutputStream(socket.getOutputStream());
                objin = new ObjectInputStream(socket.getInputStream());
                if(objin != null){
                    message = (Message) objin.readObject();
                    gui.writeGui(message);
                }
            }
        }catch (Exception e){}
    }*/
}
