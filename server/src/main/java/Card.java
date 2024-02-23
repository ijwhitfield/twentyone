public class Card{
    private int suit, rank;
    public static Character[] suitCharacters = new Character[]{'♠','♥','♣','♦'};
    public static Character[] rankCharacters = new Character[]{'1','2','3','4','5','6','7','8','9','T','J','Q','K'};
    public Card(int suit, int rank){
        this.suit = suit;
        this.rank = rank;
    }
    public int getSuit(){return this.suit;}
    public int getRank(){return this.rank;}
}