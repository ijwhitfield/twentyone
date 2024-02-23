//this packet is send to the client when the server wishes to update the client with serverside playerdata
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.io.UnsupportedEncodingException;
public class StatusPacket extends Packet{
    public StatusPacket(byte[] bytes){
        super(bytes);
    }
    public StatusPacket(int id, byte[] data){
        super(id, data);
    }

    public Object[] read() {
        ByteBuffer buf = ByteBuffer.wrap(super.getData());
        try{
            return new Object[]{buf.get(),Packet.readString(buf,"UTF-8")};
        }catch(UnsupportedEncodingException e){
            //System.out.println("utf failed");
            e.printStackTrace();
            return new Object[]{buf.get(),Packet.readString(buf)};
        }
    }

    public static byte[] write(byte showButtons, String header){
        //list of items in byte array formula
        ArrayList<byte[]> items = new ArrayList<byte[]>();
        items.add(new byte[]{showButtons});
        items.add(ByteBuilder.writeString(header, "UTF-8"));
        //values are done being read
        return Packet.dataCompile(items);
    }  
}