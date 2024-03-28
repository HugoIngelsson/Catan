![img/home_art.png](img/home_art.png)

# Project Description

Welcome to Catan! This is a project I made during February/March in 2024 intending to make an easy-to-use terminal-based version of the popular board game Catan.

As of right now, it contains 5333 lines (163,132 characters) of Java code. The main directory contains these things:  
 - `app.sh`: The bash script to compile and run the Java code
 - `debug-messages.txt`: Where error messages get sent. Hopefully should be irrelevant if you download this repo to play on your own (fingers crossed)
 - `README.md`: This file
 - `class`: Contains the compiled Java code
 - `data`: Contains the text files that describe the images used in the game
 - `src`: Contains the Java files that run the game
 - `img`: Contains images for this file. Not necessary for the functionality of the game

# Getting Started

To run the app, first download the repository by either downloading it as a zip file or running `git clone https://github.com/HugoIngelsson/Catan` in the file location you want it downloaded. Then, enter the `Catan` directory using the terminal and run the command `bash app.sh`. The game should then run as inteded, at which point you should go to the Controls subheading in this file to learn how to navigate the game. If it doesn't work at this point, look at the Potential Issues subheading.

# Potential Issues

The most common issue I've encountered is the terminal window being unable to fit all the pixels on screen, leading it to run into an error when printing. The way to fix this is to do `<CMD>`+`-`, which will shrink the text and allow the entire window to fit on your screen.

There aren't many other issues I've faced, though I expect there may be some if you're not using macOS. I tested running the program on an Ubuntu Virtual Machine as well and it seemed to work after some slight changes, though I have no clue what a Windows machine will do with the program, if it's even able to run.
