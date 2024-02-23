import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
public class StringPacket extends Packet{
    //send a string
    public StringPacket(byte[] bytes){
        super(bytes);
    }
    public StringPacket(int id, byte[] data){
        super(id, data);
    }

    public Object[] read(){
        ByteBuffer buf = ByteBuffer.wrap(super.getData());
        try{
            return new Object[]{Packet.readString(buf,"UTF-8")};
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return new Object[]{Packet.readString(buf)};
    }

    public static byte[] write(String string){
        //list of items in byte array formula
        ArrayList<byte[]> items = new ArrayList<byte[]>();
        items.add(ByteBuilder.writeString(string, "UTF-8"));
        //values are done being read
        return Packet.dataCompile(items);
    }  
}