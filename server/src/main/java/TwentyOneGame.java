import java.util.HashMap;
import java.net.Socket;
import java.util.ArrayList;
public class TwentyOneGame{
    private ArrayList<Card> deck = new ArrayList<Card>();
    private HashMap<Socket, Player> players = new HashMap<Socket, Player>();
    private ArrayList<Card> generate(){
        ArrayList<Card> deck = new ArrayList<Card>();
        for(int suit = 0; suit<4; suit++){
            for(int rank = 0; rank<13; rank++){
                deck.add(new Card(suit, rank));
            }
        }
        return deck;
    }
    public ArrayList<Card> getDeck(){return this.deck;}
    public HashMap<Socket, Player> getPlayers(){return this.players;}
    public Player addPlayer(String name, Socket socket){
        players.put(socket, new Player(name));
        return players.get(socket);
    }
    public void resetCards(){
        this.deck=generate();
    }
    public Card draw(){
        int pick = (int)Math.round(Math.random()*(deck.size()-1));
        Card drawCard = this.deck.get(pick);
        this.deck.remove(pick);
        return drawCard;
    }
}