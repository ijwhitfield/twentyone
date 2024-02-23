import java.io.*;
import java.net.*;
public class Network{
    public static Packet read(Socket socket) throws IOException, EOFException{
        DataInputStream in = new DataInputStream(socket.getInputStream());
        int length = in.readInt();
        byte[] data = new byte[length-(Integer.SIZE/8+1)];
        byte id = in.readByte();
        in.read(data);
        switch(id){
            case 0: //hello
            case 1: //updateplayers
                return new StringPacket(id,data);
            case 2: //update status
                return new StatusPacket(id, data);
            case 3: //button was pressed
                return new InputPacket(id, data);
            default:
                return new Packet(id, data);
        }
        
    }
    public static void send(Socket socket, byte id, byte[] data) throws IOException{
        //this is the really bad one
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeInt(data.length+Integer.SIZE/8+1);
        out.writeByte(id);
        out.write(data);
    }
    public static void send(Socket socket, Packet packet) throws IOException{
        //this is the really good one
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeInt(packet.getLength());
        out.writeByte(packet.getId());
        out.write(packet.getData());
    }
}