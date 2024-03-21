public class PlayerRender {
    public static final String WRITING_BACKGROUND = "\u001b[48;5;230m";
    public static final String GOLD =  "\u001b[38;5;172m";
    public static final String BRICK = "\u001b[48;5;124m";
    public static final String ORE =   "\u001b[48;5;250m";
    public static final String WHEAT = "\u001b[48;5;221m";
    public static final String WOOD =  "\u001b[48;5;130m";
    public static final String SHEEP = "\u001b[48;5;82m" ;
    public static final String[] FRAME = {
        "..............................................",
        ".. B                      ..                ..",
        ".. O                      ..                ..",
        ".. W                      ..                ..",
        ".. L                      ..                ..",
        ".. S                      ..                ..",
        ".............................................."
    };


    private int x, y;
    private Player player;
    private String bgColor;

    public PlayerRender(Player player, int x, int y) {
        this.player = player;
        this.x = x;
        this.y = y;

        this.bgColor = Generator.getBackgroundColor(player.getColor());
    }

    public void render() {
        drawBackground();

        Terminal.setXY(x+5, y+1); drawResourceBar(BRICK, player.getBrick());
        Terminal.setXY(x+5, y+2); drawResourceBar(ORE  , player.getOre()  );
        Terminal.setXY(x+5, y+3); drawResourceBar(WHEAT, player.getWheat());
        Terminal.setXY(x+5, y+4); drawResourceBar(WOOD , player.getWood() );
        Terminal.setXY(x+5, y+5); drawResourceBar(SHEEP, player.getSheep());

        Terminal.setXY(x+29, y+1);
        Terminal.print(WRITING_BACKGROUND, "VICTORY PTS: " + player.getVP());

        Terminal.setXY(x+29, y+2);
        Terminal.print(WRITING_BACKGROUND, "CARDS: ");
        if (player.getCards().size() > 5) {
            for (int i=0; i<5; i++) {
                Card c = player.getCards().get(i);
                Terminal.setPixel(WRITING_BACKGROUND, c.getRepresentation());
            }

            for (int i=0; i<player.getCards().size()-5 && i<3; i++)
                Terminal.print(WRITING_BACKGROUND, ".");
        }
        else for (Card c : player.getCards())
            Terminal.setPixel(WRITING_BACKGROUND, c.getRepresentation());

        Terminal.setXY(x+29, y+4);
        if (player.hasLongestRoad())
            Terminal.print(WRITING_BACKGROUND + GOLD, "ROAD LENGTH: " + player.getRoadLength());
        else
            Terminal.print(WRITING_BACKGROUND, "ROAD LENGTH: " + player.getRoadLength());

        Terminal.setXY(x+29, y+5);
        if (player.hasLargestArmy())
            Terminal.print(WRITING_BACKGROUND + GOLD, "ARMY SIZE: " + player.getArmySize());
        else
            Terminal.print(WRITING_BACKGROUND, "ARMY SIZE: " + player.getArmySize());
    }

    public void drawBackground() {
        for (int i=0; i<FRAME.length; i++) {
            Terminal.setXY(x, y+i);

            for (int j=0; j<FRAME[i].length(); j++) {
                if (FRAME[i].charAt(j) == '.')
                    Terminal.setPixel(bgColor);
                else
                    Terminal.setPixel(WRITING_BACKGROUND, FRAME[i].charAt(j));
            }
        }
    }

    public void drawResourceBar(String color, int count) {
        if (count <= 0) return;

        Terminal.print(color, ""+count);
        if (count > 9) count--; count--;

        for (int i=0; i<count; i++)
            Terminal.setPixelShallow(color, ' ');
    }
}
