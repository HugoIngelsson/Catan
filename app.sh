printf '\033[8;61;161t'

javac -d class/ `find src -name *.java`
java -cp class/ Catan 2> debug-messages.txt