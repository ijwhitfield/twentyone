import java.net.*;
import java.io.*;
import java.util.*;
public class Server{

    private ServerSocket socket;
    private ArrayList<Socket> clients = new ArrayList<Socket>();
    private ArrayList<Socket> waiting = new ArrayList<Socket>();
    private TwentyOneGame game;
    private int gameState;
    private int currentSocket = 0;
    private int playerLimit = 2;
    /*
    gamestates:
    -1 - wait for server to change gamestate before proceeding
    0 - waiting for round to start
    1 - playing round
        while gamestate is one, players can send packets with input
            if input packet comes from nextExpected, it is read
            depending on that input, either the playercounter moves to the next, or waits for more input
    2 - scoring
        while gamestate is two, no packets are accepted at all.
    */

    public static void main(String[] args){
        
        final Server server = new Server(Integer.parseInt(args[0]));
        if(args.length>1){
            server.setPlayerLimit(Integer.parseInt(args[1]));
        }
        server.start();
        
    }


    private void start(){
        new Thread(new Runnable(){
            public void run(){
                acceptConnections();
            }
        }).start();
        System.out.println("Server started");
        game = new TwentyOneGame();
    }

    public Server(int port){
        try{
            socket = new ServerSocket(port);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void acceptConnections(){
        while(true){
            try{
                if(gameState==0){
                    clients.add(socket.accept());
                    Socket newSocket = clients.get(clients.size()-1);
                    System.out.println("new connection from "+newSocket.getInetAddress().toString());
                    Packet packet;
                    do{
                        packet=Network.read(newSocket);
                        game.addPlayer(((String)((StringPacket)packet).read()[0]), newSocket);
                    }while(packet.getId()!=0);
                    new Thread(new Runnable(){
                        public void run(){
                            socketThread();
                        }
                    }).start();
                    if(game.getPlayers().size()>=playerLimit&&gameState==0){
                        startRound();
                    }
                }
                else{
                    waiting.add(socket.accept());
                    System.out.println("User has connected and is waiting");
                }
                
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    private void startRound(){
        gameState=-1;
        game.resetCards();
        for(int i = 0; i<game.getPlayers().size(); i++){
            Player player = game.getPlayers().get(clients.get(i));
            player.setState(0);
            player.bust();
            player.emptyHand();
            player.getHand().add(game.draw());
            player.getHand().add(game.draw());
        }
        update();
        gameState = 1;
    }


    private void score(){
        gameState = 2;
        int highest = 0;
        ArrayList<Integer> winners = new ArrayList<Integer>();
        for(int i = 0; i<game.getPlayers().size(); i++){
            Player player =game.getPlayers().get(clients.get(i));
            if(player.getRS()>highest){
                highest = player.getRS();
                winners = new ArrayList<Integer>();
            }
            if(player.getRS()==highest){
                winners.add(i);
            }
        }
        for(int i = 0; i<game.getPlayers().size(); i++){
            if(winners.contains(i)){
                game.getPlayers().get(clients.get(i)).win();
            }
            else{
                game.getPlayers().get(clients.get(i)).lose();
            }
        }
        update();
        try{
            Thread.sleep(5000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        startRound();
    }

    private boolean stillPlaying(){
        for(int i = 0; i<game.getPlayers().size(); i++){
            if(game.getPlayers().get(clients.get(i)).getState()!=3){
                return true;
            }
        }
        return false;
    }

    private void scorePlayer(){
        Player player = game.getPlayers().get(clients.get(currentSocket));
        boolean canUseAce = 21-player.calcLowScore()>=10;
        for(int i = 0; i<player.getHand().size(); i++){
            int score = Math.min(10,player.getHand().get(i).getRank()+1);
            if(canUseAce&&score==1){ //if theres an ace
                player.addRS(11);
                canUseAce=false;
            }
            else{
                player.addRS(score);
            }
        }
        player.setState(3);
        nextSocket();
    }
    
    private void nextSocket(){
        while(true){
            if(currentSocket>=clients.size()){
                currentSocket=0;
                //all the players have completed this loop.
                if(!stillPlaying()){
                    score();
                    return;
                }
                for(int i = 0; i<game.getPlayers().size(); i++){
                    if(game.getPlayers().get(clients.get(i)).getState()==2){
                        game.getPlayers().get(clients.get(i)).setState(0);
                    }
                }
                continue;
            }
            else{
                if(game.getPlayers().get(clients.get(currentSocket)).getState()==0){
                    break;
                }
                currentSocket++;
            }            
        }
        update();
    }

    private void socketThread(){
        Socket socket = clients.get(clients.size()-1);
        Player player = game.getPlayers().get(socket);
        while(true){
            try{
                Packet packet = Network.read(socket);
                System.out.println("received packet from "+socket.toString());
                switch(packet.getId()){
                    case 3: //input
                        if(socket==clients.get(currentSocket)){
                            int button = (Integer)((InputPacket) packet).read()[0];
                            if(player.getState()==1){ //is playing
                                if(button==0){ //hit
                                    player.getHand().add(game.draw());
                                    if(player.calcLowScore()>21){
                                        player.bust();
                                        player.setState(3);
                                    }
                                    else{
                                        player.setState(2);
                                    }
                                    nextSocket();
                                }
                                else{ //stand
                                    scorePlayer();
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
            }catch(IOException e){
                clients.remove(socket);
                return;
            }
        }
    }

    private void update(){
        if(game.getPlayers().get(clients.get(currentSocket)).getState()==0){
            game.getPlayers().get(clients.get(currentSocket)).setState(1); //if ready to play and current socket, start playing
        }
        String playerListString = "";
        for(int i = 0; i<game.getPlayers().size(); i++){
            Player player = game.getPlayers().get(clients.get(i));
            
            String cardNumber = " Cards: ";
            if(gameState==2){
                cardNumber+=Player.handAsString(player.getHand());
            }
            else if(player.getState()!=3){
                for(int card = 0; card<player.getHand().size(); card++){
                    cardNumber+="# ";
                }
            }
            
            String endString = "";
            if(gameState==2){
                endString="Score: "+Integer.toString(player.getRS());
                if(player.getWin()){
                    endString+=" --Winner!--";
                }
            }
            String turn = (i==currentSocket)?">":"";
            playerListString+=
            turn+
            Integer.toString(i+1)+". "+
            player.getName()+
            "["+Integer.toString(player.getGS())+"]"+
            cardNumber+
            endString+
            "/";
        }
        sendAll(new StringPacket(1,StringPacket.write(playerListString)));
        

        for(int i = 0; i<game.getPlayers().size(); i++){
            /*
            update status for every player
            */
            Socket pSocket=clients.get(i);
            Player player = game.getPlayers().get(pSocket);
            
            switch(player.getState()){
                case 0: //not playing, send cards only
                case 2:
                    try{
                        Network.send(pSocket,new StatusPacket(
                            (byte)2,
                            StatusPacket.write((byte)0,Player.handAsString(player.getHand()))));
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                    break;
                case 1: //playing, send cards and wait for input
                    try{
                        Network.send(pSocket,new StatusPacket(
                            (byte)2,
                            StatusPacket.write((byte)1,Player.handAsString(player.getHand()))));
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                    break;
                case 3: //stood, send roundScore
                    try{
                        String roundScore = Integer.toString(player.getRS());
                        Network.send(pSocket,new StatusPacket(
                            (byte)2,
                            StatusPacket.write((byte)0,roundScore)));
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                    break;
            }
        }

        
    }

    private void sendAll(Packet packet){
        //sends a packet to all connected users
        for(int i = 0; i < clients.size(); i++){
            try{
                Network.send(clients.get(i), packet);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    public void setPlayerLimit(int limit){this.playerLimit=limit;}
}