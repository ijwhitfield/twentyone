QUICK START
step one in terminal:
cd to the bin folder of my project
then
"java -jar server.jar 4444"

step two in finder:
double click client.jar
    enter 127.0.0.1
    enter 4444
    enter a name
double click client.jar again and input the same but with a new name
---if this step doesn't work because it's refocusing on the other application, run in
---terminal: "java -jar client.jar | cat" inside the bin folder
everything should be connected, you can use the two client.jar windows to play

If I set the game up already that should all already be done

use arrow keys and enter to select buttons

the basic idea is to not go over 21 in card value, but whoever has a higher score wins. face cards have a value of ten (and T cards are ten) and aces are 1 or 11 depending on your score.


--FURTHER INFORMATION--
HOW TO RUN
all executables are located in bin, source files for each module are in their respective folders in the root of the project directory.

start server with argument specifying port number (you can use 4444), and optionally supply argument specifying number of players (default is 2)

start as many clients as specified in server argument (default, again, is 2) with no arguments supplied
clients can either be started in terminal or by double clicking the jar in finder

on each client, enter the ip to connect to (127.0.0.1 is local) and the port (4444 was suggested earlier) and then the name the server should refer to this client as.

once every client is connected, the game will start

HOW TO PLAY
the goal of the game is to have a score closer to 21 at the end of the round than other players in the game. if you go over 21 your score becomes 0. every player takes turns choosing to hit or stay, hit meaning to draw another card, and stay meaning to stop playing until scores are calculated. scores are based on the value of the cards in your hand. 1-9 are face value, T=10, JQK=10. if your score is less than 11 after score is calculated and you have a 1 value card, the 1 value card counts as 11. ties in score at the end of each round give both players a point. the game will go indefinitely unless you stop the program.