import java.nio.ByteBuffer;
import java.io.*;
import java.util.ArrayList;

public class Packet implements ObjectReadable{
    private int length, id;
    private byte[] data;
    public Packet(byte[] bytes){
        //write packet from stream
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        this.length=buf.getInt();
        this.id=buf.get();
        buf.get(this.data);
    }
    public Packet(int id, byte[] data){
        //write packet manually
        this.length=data.length+5;
        this.id=id;
        this.data=data;
    }
    public static String readString(ByteBuffer buf){
        //static class method to read string from a buffer
        int stringSize = buf.getInt();
        byte[] stringBytes = new byte[stringSize];
        buf.get(stringBytes);
        return new String(stringBytes);
    }
    public static String readString(ByteBuffer buf, String charset) throws UnsupportedEncodingException{
        //static class method to read string from a buffer
        int stringSize = buf.getInt();
        byte[] stringBytes = new byte[stringSize];
        buf.get(stringBytes);
        return new String(stringBytes, charset);
    }

    public Object[] read(){
        //should be overridden for every subclass, returns values encoded into packet data
        return new Object[]{this.length, this.id, this.data};
    }
    
    public static byte[] dataCompile(ArrayList<byte[]> items){
        //add all byte arrays together
        ArrayList<Byte> data = new ArrayList<Byte>();
        for(int item = 0; item<items.size(); item++){
            for(int i = 0; i<items.get(item).length; i++){
                data.add(items.get(item)[i]);
            }
        }
        return ByteBuilder.unwrap(data.toArray(new Byte[0]));
    }
    public int getLength(){return this.length;}
    public int getId(){return this.id;}
    public byte[] getData(){return this.data;}
}