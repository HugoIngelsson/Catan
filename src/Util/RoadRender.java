public class RoadRender {
    public static final String[][] PATTERNS = {
        {
            "--..-=\"\"-.-"
        },
        {
            "\\  ",
            " ) ",
            " ( ",
            "  \\"
        },
        {
            "  /",
            " ( ",
            " ) ",
            "/  "
        }
    };

    public static final String[] ROAD_COLOR = {
        "\u001b[38;5;9m",
        "\u001b[38;5;12m",
        "\u001b[38;5;10m",
        "\u001b[38;5;255m"
    };

    public static final int[][] OFFSETS = {
        {1, 0}, {1, 1}, {-3, 1}, {-11, 0}, {-3, -4}, {1, -4}
    };

    public static void renderRoad(String color, int dir, int x, int y) {
        String roadColor = ROAD_COLOR[Generator.colorEnum(color)];
        final String[] pattern = PATTERNS[dir%3];
        x += OFFSETS[dir][0];
        y += OFFSETS[dir][1];

        for (int i=0; i<pattern.length; i++) {
            Terminal.setXY(x, y+i);
            for (int j=0; j<pattern[i].length(); j++) {
                if (pattern[i].charAt(j) != ' ') {
                    Terminal.setPixel(Terminal.pixelHistory[x+j][y+i],
                                             roadColor, 
                                             pattern[i].charAt(j));
                }
                else Terminal.setXY(x+j+1, y+i);
            }
        }
    }
}
