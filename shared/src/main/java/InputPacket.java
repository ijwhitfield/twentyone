//this packet is sent to the server whenever the gui wants to send the server some input
//right now, input to the server is simplified to an integer to indicate what button was presssed
import java.nio.ByteBuffer;
import java.util.ArrayList;
public class InputPacket extends Packet{
    public InputPacket(byte[] bytes){
        super(bytes);
    }
    public InputPacket(int id, byte[] data){
        super(id, data);
    }

    public Object[] read(){
        ByteBuffer buf = ByteBuffer.wrap(super.getData());
        return new Object[]{buf.getInt()};
    }

    public static byte[] write(int input){
        //list of items in byte array formula
        ArrayList<byte[]> items = new ArrayList<byte[]>();
        items.add(ByteBuilder.writeInt(input));
        //values are done being read
        return Packet.dataCompile(items);
    }  
}