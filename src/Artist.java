import java.io.*;

public class Artist {
    static final String[] COLORS = {
        "\u001b[48;5;88m" , "\u001b[48;5;130m", "\u001b[48;5;172m", "\u001b[48;5;216m",
        "\u001b[48;5;1m"  , "\u001b[48;5;94m" , "\u001b[48;5;180m", "\u001b[48;5;240m",
        "\u001b[48;5;245m", "\u001b[48;5;250m", "\u001b[48;5;230m", "\u001b[48;5;222m",
        "\u001b[48;5;252m", "\u001b[48;5;254m", "\u001b[48;5;208m", "\u001b[48;5;214m",
        "\u001b[48;5;221m", "\u001b[48;5;0m"  , "\u001b[48;5;107m", "\u001b[48;5;150m",
        "\u001b[48;5;28m" , "\u001b[48;5;22m" , "\u001b[48;5;76m" , "\u001b[48;5;82m" ,
        "\u001b[48;5;235m", "\u001b[48;5;52m" , "\u001b[48;5;151m", "\u001b[48;5;153m",
        "\u001b[48;5;33m" , "\u001b[48;5;255m", "\u001b[48;5;214m", "\u001b[48;5;12m" ,
        "\u001b[48;5;9m"  , "\u001b[48;5;10m" , "\u001b[48;5;193m", "\u001b[48;5;124m",
        "\u001b[48;5;52m" , "\u001b[48;5;229m", "\u001b[48;5;200m", "\u001b[48;5;213m",
        "\u001b[48;5;140m", "\u001b[48;5;96m" , "\u001b[48;5;117m", "\u001b[48;5;173m",
        "\u001b[48;5;160m", "\u001b[48;5;202m", "\u001b[48;5;179m", "\u001b[48;5;203m",
        "\u001b[0m"       , "\u001b[0m"
    };

    public static void drawFromFile(String fileName, int x, int y) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));

        String in;
        while ((in = br.readLine()) != null) {
            if (y > Terminal.HEIGHT || x + in.length() - 1 > Terminal.WIDTH) {
                br.close();
                throw new IllegalArgumentException("Image too big!");
            }
            
            Terminal.setXY(x, y);

            for (int i=0; i<in.length(); i++) {
                if (in.charAt(i) < '!') {
                    Terminal.setXY(x+i+1, y);
                    continue;
                }

                Terminal.setPixel(COLORS[in.charAt(i)-'!'], ' ');
            }

            y++;
        }

        br.close();
    }

    public static void drawFromFileShallow(String fileName, int x, int y) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));

        String in;
        while ((in = br.readLine()) != null) {
            if (y > Terminal.HEIGHT || x + in.length() - 1 > Terminal.WIDTH) {
                br.close();
                throw new IllegalArgumentException("Image too big!");
            }
            
            Terminal.setXY(x, y);

            for (int i=0; i<in.length(); i++) {
                if (in.charAt(i) < '!') {
                    Terminal.setXY(x+i+1, y);
                    continue;
                }

                Terminal.setPixelShallow(COLORS[in.charAt(i)-'!'], ' ');
            }

            y++;
        }

        br.close();
    }

    public static void drawFromFile(String fileName, String overlay, int x, int y) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        BufferedReader br2 = new BufferedReader(new FileReader(overlay));

        String in, in2;
        while ((in = br.readLine()) != null) {
            in2 = br2.readLine();
            if (y > Terminal.HEIGHT || x + in.length() - 1 > Terminal.WIDTH) {
                br.close();
                br2.close();
                throw new IllegalArgumentException("Image too big!");
            }
            
            Terminal.setXY(x, y);

            for (int i=0; i<in.length(); i++) {
                if (in.charAt(i) < '!') {
                    Terminal.setXY(x+i+1, y);
                    continue;
                }

                Terminal.setPixel(COLORS[in.charAt(i)-'!'], in2.charAt(i));
            }

            y++;
        }

        br.close();
        br2.close();
    }

    public static void drawFromFileShallow(String fileName, String overlay, String textColor, int x, int y) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        BufferedReader br2 = new BufferedReader(new FileReader(overlay));

        String in, in2;
        while ((in = br.readLine()) != null) {
            in2 = br2.readLine();
            if (y > Terminal.HEIGHT || x + in.length() - 1 > Terminal.WIDTH) {
                br.close();
                br2.close();
                throw new IllegalArgumentException("Image too big!");
            }
            
            Terminal.setXY(x, y);

            for (int i=0; i<in.length(); i++) {
                if (in.charAt(i) < '!') {
                    Terminal.setXY(x+i+1, y);
                    continue;
                }

                Terminal.setPixelShallow(COLORS[in.charAt(i)-'!']+textColor, in2.charAt(i));
            }

            y++;
        }

        br.close();
        br2.close();
    }

    public static void drawNumber(int n, int x, int y) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("data/numbers/num" + n + ".txt"));
        
        String textColor = "\u001b[38;5;0m";
        if (n == 6 || n == 8) textColor = "\u001b[38;5;9m";

        String in;
        while ((in = br.readLine()) != null) {
            Terminal.setXY(x, y);

            for (int i=0; i<5; i++) {
                Terminal.setPixel(COLORS[10], textColor, in.charAt(i));
            }

            y++;
        }

        br.close();
    }

    public static void drawWave(int x, int y) {
        if (x > 111 || y > 61) return;

        String colorAtPoint = Terminal.pixelHistory[x][y];
        if (colorAtPoint.equals("\u001b[48;5;33m") || colorAtPoint.equals("\u001b[48;5;153m")) {
            Terminal.setXY(x, y);
            Terminal.setPixel(colorAtPoint, "\u001b[38;5;75m", '~');
        }
    }

    public static void drawPlayerEmblem(String color) throws IOException {
        final String PATH = "data/emblems/";
        String file = PATH + color + ".txt";

        drawFromFile(file, 144, 3);
    }

    public static void drawColorBox(int x1, int y1, int x2, int y2, String color) {
        switch (color) {
            case "red":
                color = "\u001b[48;5;9m";
                break;
            case "blue":
                color = "\u001b[48;5;12m";
                break;
            case "white":
                color = "\u001b[48;5;255m";
                break;
            case "green":
                color = "\u001b[48;5;10m";
                break;
            case "light_red":
                color = "\u001b[48;5;216m";
                break;
            case "light_green":
                color = "\u001b[48;5;150m";
                break;
            case "beige":
                color = "\u001b[48;5;230m";
                break;
            default:
                color = "\u001b[48;5;0m";
                break;
        }

        for (int i=y1; i<=y2; i++) {
            Terminal.setXY(x1, i);
            for (int j=x1; j<=x2; j++) {
                Terminal.setPixelShallow(color, ' ');
            }
        }
    }

    public static void restoreSubscreen(int x1, int y1, int x2, int y2) {
        for (int i=y1; i<=y2; i++) {
            Terminal.setXY(x1, i);
            for (int j=x1; j<=x2; j++) {
                Terminal.setPixel(Terminal.pixelHistory[j][i], Terminal.annotationHistory[j][i]);
            }
        }
    }
}
