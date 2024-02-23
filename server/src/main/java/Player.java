import java.util.ArrayList;
public class Player{
    private ArrayList<Card> hand = new ArrayList<Card>();
    private int gameScore,roundScore, state;
    private String name;
    private boolean isWinner=false;
    
    public Player(String name){
        this.name = name;
    }

    public String getName(){return this.name;}
    public void addGS(){this.gameScore++;} //when you win a round
    public ArrayList<Card> getHand(){return this.hand;}
    public static String handAsString(ArrayList<Card> hand){
        String output = "";
        for(int i = 0; i<hand.size(); i++){
            output+=Card.suitCharacters[hand.get(i).getSuit()].toString()+
            Card.rankCharacters[hand.get(i).getRank()].toString()+" ";
        }
        return output;
    }
    public int getGS(){return this.gameScore;}
    public int getRS(){return this.roundScore;}
    public boolean getWin(){return this.isWinner;}
    public void emptyHand(){this.hand = new ArrayList<Card>();}
    public void setState(int state){this.state=state;}
    public int getState(){return this.state;}
    public int calcLowScore(){
        int output=0;
        for(int i = 0; i<hand.size(); i++){
            output+=Math.min(10,hand.get(i).getRank()+1);
        }
        return output;
    }
    public void addRS(int score){this.roundScore+=score;}
    public void bust(){
        this.roundScore=0;
    }
    public void win(){
        this.isWinner=true;
        addGS();
    }
    public void lose(){this.isWinner=false;}

    /*state values are:
        0 - waiting
        1 - playing, hit/stand
        2 - done for this round
        3 - stood
    */

}