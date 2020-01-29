# TicTacToe_Online
Online Tic-Tac-Toe game written in Java using UTP and TCP protocols

1) Detailed description of the implementation

Project is using TCP protocol for game communication and UDP protocol for broadcasting game 
state to viewers. 

Server:

When you start GameServer, program will open new Server socket on the IP of the computer and 
port provided as paramiter then it will start to listen for new connections

When new connection is detected program will connect with new client using TCP connection
and give the client randomly generated ID
Server will exchange messages with the client untill it receives "EXIT" message;
When "LIST" is received, server will send String with all currently connected clients
When "PLAY" is received server will look if any other client also is ready to play.
If server finds opponent, it will start new duel and send both clienst the name of their opponent, 
whether or not he is starting and which port will be used to broadcast the state of this game 
(multicasted actually).

From now on the communication will be handled by Duel class

Server will wait for the input from client (String containing state of the game as "x", "o" and " "
 separated by ";") and send it to the other client. In addition, server will also multicast the player IDs,
 which player is which (o or x) and game state to the Multicast address (230.0.0.0) on randomly chosen port (Port is
 set for each duel and will also be sent do the client)

  Process will continue until the server receives message "FINISH" signaling that the game has been 
  finished and then it will return to listen for "PLAY" and "LIST" message.

Server can support multiple clients and games simultaneously
The opponent will be chosen randomly from the list of Players with readyToPlay set to true

Client:

When client proccess is started it will try to connect to the server on IP address and port number 
provided as paramiter.
When connection is established, client will open JFrame for easier experience. 
At the top of the frame, you will see id that server chose for this client
Each button represens appropriate message, when you press List client will send "LIST" message to the server
and wait for the response (String of all currently connected users)
When you press PLAY, client will send "PLAY" message and wait for the response from the server (id of the opponent
and port on which viewer can watch the game) 

When client receives the response it will start new Game. The window will show array of 9 buttons each representing
one field of Tic-Tac-Toe game, and at the button you will see whather or not thats the client's turn and 
which port is used for broadcasting this particular game. 
Each time you press the button, client will send current state of the game to the server 
Then both clients will receive updated state, update game view accordingly and check whether or not there is a draw
or one of the players has won the Game.

When client detects that game has been finished, it will send "FINISH" message to the server and wait untill button 
"back" is pressed. When you press back, client will return to it's begining stage. Process then can be repeated how 
many times you want

Viewer:
 
 When viewer process is started, it will open MulticastSocket on port provided as starting parameter.
 It will listen on this port for UDP packets containing String message (ID of both players, which is x which is o
 and the game state)
Viewer has similar gui to the client, but it only updates according to the state in received packet.
Process will be repeated untill Viewer detects that one player has won or there has been a draw.
Then it will stop listening and wait to be closed.


Multiple viewers can watch each game at the same time

Game state has format for example "x; ;o;x; ; ; ; ; "
I used ";" to seperate string into array which then can be processed easily in the code


2) How to compile

To compile, go to the "TicTacToe" directory in terminal/cmd and compile all the classes using

UNIX:
javac Server/*.java
javac Client/*.java
javac Viewer/*.java

WINDOWS:
javac Server\*.java
javac Client\*.java
javac Viewer\*.java


3) How to run
Processes should be run in appropriate order: 
GameServer -> Client -> Viewer (when you started the game and know the port)

Go to the TicTacToe directory in terminal/cmd and run:

UNIX:
javac Server/GameServer.java {portNumber}
javac Client/Client.java {serverIpAddress} {serverPort}
javac Viewer/Viewer.java {broadcastPortAvailableInGameWindow}

WINDOWS:
javac Server\GameServer.java {portNumber}
javac Client\Client.java {serverIpAddress} {serverPort}
javac Viewer\Viewer.java {broadcastPortAvailableInGameWindow}

4) How to use

I hope the usage is easy. The only interaction after starting processes should be on client window buttons.

