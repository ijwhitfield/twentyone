import java.io.*;
import java.net.*;

public class Client{
    private Socket socket;
    private String ip,name;
    private int port;
    private String[] playerList;
    private String status = "";
    private boolean waitForInput;
    private TextWriter tw;
    public static void main(String[] args) throws InterruptedException{
        Client client = new Client();
        client.start();
        //this used to be for outputting prints to another file, but i removed prints and it never worked anyway
        /*while(true){
            try{
                String logString = "log-"+new Date().toString();
                String errString = "err-"+new Date().toString();
                new File(logString).createNewFile();
                new File(errString).createNewFile();
                System.setOut(new PrintStream(new File(logString)));
                System.setErr(new PrintStream(new File(errString)));
                break;
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
        }*/
        
    }

    public Client(){
        tw = new TextWriter();
    }

    public void start(){
        tw.start();
        tw.write("enter ip");
        tw.askInput();
        ip = tw.getInput();
        tw.clear();
        tw.write("enter port");
        tw.askInput();
        port = Integer.parseInt(tw.getInput());
        tw.clear();
        tw.write("enter your name");
        tw.askInput();
        name = tw.getInput();
        tw.clear();
        for(int i = 0; i<10; i++){
            tw.write("attempting connection .... try number "+Integer.toString(i+1));
            try{
                socket = new Socket(ip, port);
                Network.send(socket,(byte) 0, StringPacket.write(name));
                tw.write("Connection success, waiting for other players to finish connecting...");  
                break;
            }catch(ConnectException e){
                tw.write("Connection failed");
            }catch(IOException e){
                e.printStackTrace();
            }
            try{
                Thread.sleep(5000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        while(true){
            try{
                Packet packet = Network.read(socket);
                switch(packet.getId()){
                    case (byte)1: //update players
                        playerList = ((String)(((StringPacket) packet).read()[0])).split("/");
                        print();
                        break;
                    case (byte)2: //update this client
                        Object[] items = ((StatusPacket) packet).read();
                         //print cards or score
                        status = (String)items[1];
                        waitForInput = ((Byte)items[0]==1);
                        print();
                        break;
                    default: //send error
                        break;
                }
            }catch(EOFException e){
                tw.write("something is broken, server is probably down.");
                System.exit(0);
            }catch(SocketException e){
                tw.write("something is broken, server is probably down.");
                System.exit(0);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    private void print(){
        String outputString = "";
        for(int i = 0; i<playerList.length; i++){
            outputString+=playerList[i]+"\n";
        }
        outputString+=status+"\n";
        if(waitForInput){
            tw.clear();
            tw.write(outputString); //print
            tw.askChoice();
            int button = tw.getChoice(); //block until chosen
            waitForInput=false;
            try{
                Network.send(socket,new InputPacket((byte)3,InputPacket.write(button)));
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        else{
            outputString+="waiting for other players...\n";
            tw.clear();
            tw.write(outputString);
        }
        
    }
}