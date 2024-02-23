import java.nio.ByteBuffer;
import java.io.UnsupportedEncodingException;
public abstract class ByteBuilder{
    public static byte[] unwrap(Byte[] bytes){
        byte[] output = new byte[bytes.length];
        for(int i = 0; i<bytes.length; i++){
            output[i]=bytes[i].byteValue();
        }
        return output;
    }
    public static byte[] get(ByteBuffer buf){
        //resets buffer to position zero and returns it as an array
        byte[] data = new byte[buf.position()];
        buf.position(0);
        buf.get(data);
        return data;
    }

    public static byte[] writeString(String input, String charset){
        try{
            ByteBuffer buf = ByteBuffer.allocate(Integer.SIZE/8+input.getBytes("UTF-8").length);
            buf.putInt(input.getBytes("UTF-8").length);
            buf.put(input.getBytes("UTF-8"));
            return get(buf);
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
            ByteBuffer buf = ByteBuffer.allocate(Integer.SIZE/8+input.getBytes().length);
            buf.putInt(input.getBytes().length);
            buf.put(input.getBytes());
            return get(buf);
        }
    }
    
    public static byte[] writeInt(int integer){
        ByteBuffer buf = ByteBuffer.allocate(Integer.SIZE/8);
        buf.putInt(integer);
        return get(buf);
    }
}