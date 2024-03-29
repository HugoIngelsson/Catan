![img/home_art.png](img/home_art.png)

# Project Description

Welcome to Catan! This is a project I made during February/March in 2024 intending to make an easy-to-use terminal-based version of the popular board game Catan.

As of right now, it contains 5333 lines (163,132 characters) of Java code. The main directory contains these things:  
 - `app.sh`: The bash script to compile and run the Java code.
 - `debug-messages.txt`: Where error messages get sent. Hopefully should be irrelevant if you download this repo to play on your own (fingers crossed).
 - `README.md`: This file.
 - `class`: Contains the compiled Java code.
 - `data`: Contains the text files that describe the images used in the game.
 - `src`: Contains the Java files that run the game.
 - `img`: Contains images for this file. Not necessary for the functionality of the game.

# Getting Started

To run the app, first download the repository by either downloading it as a zip file or running `git clone https://github.com/HugoIngelsson/Catan` in the file location you want it downloaded. Then, enter the `Catan` directory using the terminal and run the command `bash app.sh`. The game should then run as inteded, at which point you should go to the Controls subheading in this file to learn how to navigate the game. If it doesn't work at this point, look at the Potential Issues subheading.

# Potential Issues

The most common issue I've encountered is the terminal window being unable to fit all the pixels on screen, leading it to run into an error when printing. The way to fix this is to do `<CMD>`+`-`, which will shrink the text and allow the entire window to fit on your screen.

There aren't many other issues I've faced, though I expect there may be some if you're not using macOS. I tested running the program on an Ubuntu Virtual Machine as well and it seemed to work after some slight changes, though I have no clue what a Windows machine will do with the program, if it's even able to run.

# Controls

Here is a summary of the controls you'll need to play the game:
 - `ARROW KEYS`: How to navigate every menu and move the cursor on the screen. Should be pretty intuitive.
 - `SPACE`: How to interact with different screen elements. The button you'll press with space will almost always be highlighted in a golden color.
 - `R`: Roll the dice (when possible).
 - `ENTER`: Pass turn to the next player (only works when not in another menu and after rolling the dice).
 - `TAB`: Shift into or out of "road building mode" after rolling the dice and not in a menu.
 - `D`: Open the development card menu.
 - `A`: Buy a development card.
 - `T`: Open the trade menu.

I also have a section in this README file that goes deeper into how the game is played ("In-Depth Explanation Through a Game"). If you're confused on any of these controls, go there to gain further insight.

# Future Additions

Though the main game of Catan is completely done, there are a couple of things I'd like to add if I ever have time (if you're reading this years after I made this and none of these things got done... well, I guess I never had time).

On the top of my list are:
 - Make the other buttons in the menu selection actually do something. Right now there's `New Game`, `Options`, and `Rules`, but only the first of the three actually do something. I suppose this file serves as a sort of substitute for the other two, but it'd be nice if learning how to play the game could be self-contained within the game itself. As a temporary fix, I could add simple GUIs to each that only have text on them for each, but eventually being able to change the controls and having some kind of nice graphics for the rule book could be a nice thing to include.
 - Comment all of my code. I made the mistake of simply blazing through the coding without caring to add explanations to code blocks. It worked for me since the project was contained within about a month of production, but if I ever return that make things really difficult. It really sucks to do, though...
 - Add multiplayer on local networks. Pretty self-explanatory: make the game playable online by having some kind of lobby/hosting feature. Included in this would be having someone act as the central server that keeps track of the entire game state with other players simply sending messages to that server when trying to do things. This would solve the issue of players being able to see exactly what resources and development cards each other have, though I'd imagine it'd take a large amount of work to implement.
 - Create an API for bots and allow players to play against NPCs. This allows players to play alone or with fewer players and could also allow someone like you (the person reading this) to make your own bots.

# In-Depth Explanation Through a Game

WIP
