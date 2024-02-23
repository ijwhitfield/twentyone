public class Event implements ObjectReadable{
    //this class was going to be used for the textwriter write function,
    //but then I placed the responsibility of compiling text to client class
    int id;
    String contents;
    public Event(int id, String contents){
        this.id=id;
        this.contents=contents;
    }
    public Object[] read(){
        return new Object[]{contents};
    }
    public int getId(){return this.id;}
}