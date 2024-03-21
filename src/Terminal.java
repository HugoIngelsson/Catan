import java.io.*;

public class Terminal {
    public static final String ANSI_RESET_COLOR = "\u001b[0m";

    public static PrintWriter pw;
    public static int WIDTH, HEIGHT;
    public static int x, y;

    public static String[][] pixelHistory;
    public static char[][] annotationHistory;

    // (1,1) is the top left corner
    public static void setXY(int x, int y) {
        Terminal.x = x;
        Terminal.y = y;

        pw.print("\u001b[" + y + ";" + x + "H");
    }
    
    public static void setPixel(String bgColor, String textColor, char text) {
        pixelHistory[x][y] = bgColor;
        annotationHistory[x][y] = text;
        
        pw.print(bgColor + textColor + text + ANSI_RESET_COLOR);
        x++;
    }

    public static void setPixel(String bgColor, char text) {
        pixelHistory[x][y] = bgColor;
        annotationHistory[x][y] = text;
        
        pw.print(bgColor + text + ANSI_RESET_COLOR);
        x++;
    }

    public static void setPixelShallow(String bgColor, char text) {
        pw.print(bgColor + text + ANSI_RESET_COLOR);
        x++;
    }

    public static void setPixel(String bgColor) {
        pixelHistory[x][y] = bgColor;
        
        pw.print(bgColor + ' ' + ANSI_RESET_COLOR);
        x++;
    }

    public static void setPixel(char text) {
        annotationHistory[x][y] = text;

        pw.print(text);
        x++;
    }

    public static void flush() {
        pw.flush();
    }

    public static void clear() {
        pw.print("\u001b[2J");
        pw.print("\u001b[1;1H");
        Terminal.flush();
    }

    public static void print(String bgColor, String s) {
        pw.print(bgColor + s + ANSI_RESET_COLOR);

        for (int i=0; i<s.length(); i++)
            annotationHistory[x+i][y] = s.charAt(i);
        
        x += s.length();
    }

    public static void print(String s) {
        pw.print(s);

        for (int i=0; i<s.length(); i++)
            annotationHistory[x+i][y] = s.charAt(i);

        x += s.length();
    }

    public static void printShallow(String bgColor, String s) {
        pw.print(bgColor + s + ANSI_RESET_COLOR);
        x += s.length();
    }

    public static void initialize() throws IOException, InterruptedException {
        String[] cmd = {"/bin/sh", "-c", "stty raw </dev/tty"};
        Runtime.getRuntime().exec(cmd).waitFor();
        pw = new PrintWriter(System.out);

        pw.print("\u001b[2J");
        pw.print("\u001b[s");
        pw.print("\u001b[5000;5000H");
        pw.print("\u001b[6n");

        Terminal.flush();

        HEIGHT = 0;
        WIDTH = 0;
        boolean heightDone = false;
        int byteBuffer;
        Reader inputReader = System.console().reader();
        while ((byteBuffer = inputReader.read()) > -1) {
            if (byteBuffer == 27 || byteBuffer == '[') continue;
            else if (byteBuffer == ';') {
                heightDone = true;
            }
            else if (byteBuffer == 'R') break;
            else if (heightDone) {
                WIDTH *= 10;
                WIDTH += byteBuffer - '0';
            }
            else {
                HEIGHT *= 10;
                HEIGHT += byteBuffer - '0';
            }
        }

        // hide cursor
        pw.print("\u001b[?25l");

        Terminal.flush();
        pixelHistory = new String[WIDTH+1][HEIGHT+1];
        annotationHistory = new char[WIDTH+1][HEIGHT+1];
        x = 1; y = 1;
    }

    public static void close() throws IOException, InterruptedException {
        Terminal.setXY(WIDTH, HEIGHT);
        
        pw.print("\u001b[?25h");
        pw.flush();

        pw.close();

        String[] cmd = {"/bin/sh", "-c", "stty cooked </dev/tty"};
        Runtime.getRuntime().exec(cmd).waitFor();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Terminal.initialize();

        Thread.sleep(1000);
        Terminal.clear();
        Thread.sleep(1000);

        setXY(30, 15);
        setPixel("\u001b[48;2;118;250;130m", "\u001b[38;2;118;250;130m", 'X');
        flush();

        Thread.sleep(10000);
    }
}