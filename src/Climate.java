import java.io.*;

public class Climate {
    private static final String PATH = "data/climate/";
    private static int ticker, thresh1, thresh2, thresh3;
    private static boolean scarcity, flooding;

    public static void initClimate() throws IOException {
        ticker = Catan.PLAYERS;
        thresh1 = Math.max((Catan.PLAYERS * Catan.VP_REQUIREMENT) / 6, ticker+1);
        thresh2 = thresh1 * 2;
        thresh3 = thresh1 * 3;

        scarcity = false;
        flooding = false;
        
        renderThermo();
    }

    public static boolean increment() throws IOException {
        ticker++;
        renderThermo();

        if (!scarcity && ticker >= thresh1) {
            ChatBox.scarceResources();
            scarcity = true;
        }

        if (!flooding && ticker >= thresh2) {
            ChatBox.flooding();
            flooding = true;

            for (int i=0; i<Catan.PLAYERS; i++)
                Board.nextPlayer().resetTrades();

            try {
                Artist.animateTransition(PATH + "flooded.txt", 1, 1, 60, 120);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (ticker == thresh3) {
            ChatBox.globalClimateDeath();
            return true;
        }

        return false;
    }

    public static void decrement() throws IOException {
        if (ticker > 0)
            ticker--;
        
        renderThermo();
    }

    public static void renderThermo() throws IOException {
        Artist.drawFromFile(PATH + "thermo.txt", 3, 43);
        Artist.drawFromFile(PATH + "num_bg.txt", PATH + "num/" + ticker + ".txt", 5, 57);

        int fillup = 4*ticker/thresh1;
        if (fillup >= 1)  Artist.drawColorBox(7, 55, 9, 55, "paper_yellow");
        if (fillup >= 2)  Artist.drawColorBox(7, 54, 9, 54, "paper_yellow");
        if (fillup >= 3)  Artist.drawColorBox(7, 53, 9, 53, "gold");
        if (fillup >= 4)  Artist.drawColorBox(7, 52, 9, 52, "gold");
        if (fillup >= 5)  Artist.drawColorBox(7, 51, 9, 51, "orange");
        if (fillup >= 6)  Artist.drawColorBox(7, 50, 9, 50, "orange");
        if (fillup >= 7)  Artist.drawColorBox(7, 49, 9, 49, "rose");
        if (fillup >= 8)  Artist.drawColorBox(7, 48, 9, 48, "rose");
        if (fillup >= 9)  Artist.drawColorBox(7, 47, 9, 47, "red");
        if (fillup >= 10) Artist.drawColorBox(7, 46, 9, 46, "red");
        if (fillup >= 11) Artist.drawColorBox(7, 45, 9, 45, "dark_red");
        if (fillup >= 12) Artist.drawColorBox(7, 44, 9, 44, "dark_red");
    }

    public static boolean hasFlooded() {
        return flooding;
    }

    public static boolean isScarce() {
        return scarcity;
    }
}
