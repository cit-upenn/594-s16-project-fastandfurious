#FastAndFurious

### Introduction

A really awesome and exciting interactive game – Conquer the Universe! User can choose to watch two computer players compete against each other, or play against the computer. The game lasts a certain amount of time, and the player with the 

### How to install and run this game

To run this game from command line, export the entire project into a Runnable JAR. In a terminal window, cd to the directory that contains this JAR, type in: java -jar <JarName>.jar. The login screen should show up automatically. 

To run this game in eclipse, find the class GameLauncher.java and click “Run As”  “Java Application”. The login screen should show up shortly. 

In the login screen, please enter information for both player1 and player2. Select the player type, avatar and enter the player’s name. Note that only one human player is allowed, so if you select “human” for either one, the other will only have one option left – “computer”. In addition, please select the game time from the drop down list (default to 60 sec). Then click start to start the game, and the login screen will change to the game screen. 

The aim of the game is to conquer as many planets as possible in certain amount of time. Each player has some initial wealth. Players can conquer a planet by spending certain amount of money and building an edge from your current location to the target location. The conquered planets by your opponents can also be attacked and reclaimed by you. If a planet is attacked, all the edges connected to the planet are broken. If the territory of a player is cut by this breakdown of edges, the path is no longer connected and the player needs to build a new path to the subset of territory to regain control of that subset. When all time has elapsed, the player with the most planets and edges win (a more detailed description of the winning rule is down below).

In the game window, each triangle represents a player. Each circle in the universe represent a different planet, and their colors indicate their level of value (also the amount of money you need to spend to conquer them). The small squares are supply stations. If you don’t have enough money, you may find yourself unable to go to the planet you selected, but you might have enough money to build a path along the supply stations and regain some power.

If both players are computer, they explore their path by using strategies including BFS, DFS and Dijkstra.   

If you are human player, you will compete with the computer player. You can select any route you want by dragging your mouse along the route. To travel to the target node along your selected route, click your current node (the node where the triangle currently locates), and the triangle will move to the target node along the selected path. Note that sometimes there is not enough money to travel to the target node, then the triangle will travel to as far as possible and stop. To attack an opponent’s planet, right-click your mouse. If the opponent’s planet is attacked and the edges are broken, the edges in the unreachable region will change to gray. You can attack these nodes and steal these planets as well. Once the time is up, the galaxy will stop and you cannot move your player anymore. You can see the change of your wealth in the entire process.

When the time is up or any player’s planets have been lost completely, the game is over and the player with the most planets (weighted by the planets’ value) wins. The winner is displayed in the message board on each player’s control panel.

### Features and design 
The project uses MVC design pattern to govern the entire organization of various components. Specifically, the following features are covered in our project.

-	__Graphical User Interface__
-	__Multithreading__
-	__Data Structures:__
Graph, Adjacency List, Map, Set, Heap, Priority Queue, Linked List, etc.
-	__Interfaces:__ 
Player (to make different type of players be somewhat consistent)
Node (to represent different nodes including planets and supply stations)
-	__Design Pattern:__
Singleton, Observer, Iterator, Factory method, etc.
-	__Game strategy design__

