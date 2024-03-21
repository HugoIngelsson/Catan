public class DiceRender {
    public static final String WRITING_BACKGROUND = "\u001b[48;5;230m";
    public static final String BLACK = "\u001b[48;5;0m";
    public static final String[][] FACES = {
        {
            "        ",
            "        ",
            "        "
        },
        {
            "        ",
            "   ##   ",
            "        "
        },
        {
            "        ",
            " ##  ## ",
            "        "
        },
        {
            "   ##   ",
            "        ",
            " ##  ## "
        },
        {
            " ##  ## ",
            "        ",
            " ##  ## "
        },
        {
            "##    ##",
            "   ##   ",
            "##    ##"
        },
        {
            "## ## ##",
            "        ",
            "## ## ##"
        }
    };

    public static void clearDice() {
        setFace(117, 4, 0);
        setFace(129, 4, 0);
    }

    public static int rollDice(int x, int y) {
        int rand = (int)(Math.random()*6)+1;

        setFace(x, y, rand);

        return rand;
    }

    public static void setFace(int x, int y, int n) {
        for (int i=0; i<FACES[n].length; i++) {
            Terminal.setXY(x, y+i);

            for (int j=0; j<FACES[n][i].length(); j++) {
                if (FACES[n][i].charAt(j) == '#')
                    Terminal.setPixel(BLACK);
                else
                    Terminal.setPixel(WRITING_BACKGROUND);
            }
        }
    }

    public static int animateRolling() throws InterruptedException {
        for (int i=0; i<30; i++) {
            rollDice(117, 4);
            rollDice(129, 4);
            Terminal.flush();

            Thread.sleep(5+i*4);
        }

        int r1 = rollDice(117, 4);
        int r2 = rollDice(129, 4);
        int ret = r1 + r2;

        ChatBox.receiveDiceRoll(Board.peekPlayer(), r1, r2);
        Terminal.flush();

        return ret;
    }
}
