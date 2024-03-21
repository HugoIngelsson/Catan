public class Cursor {
    public static final String[][] CURSOR = {
        {
            " /\"\\ ",
            "(   )"  ,
            " \\_/ "
        },
        {
            "     ",
            " ( ) "  ,
            "     "
        },
        {
            "  .......  ",
            " ..     .. ",
            "..       ..",
            "..       ..",
            "..       ..",
            " ..     .. ",
            "  .......  ",
        }
    };

    public static final String[][] ROAD = {
        {
            "==========="
        },
        {
            "\\\\   ",
            " \\\\  ",
            "  \\\\ ",
            "   \\\\"
        },
        {
            "   //",
            "  // ",
            " //  ",
            "//   "
        }
    };

    public static int mode = 0;

    // places a cursor centered at (x, y)
    public static void drawCursor(int x, int y) {
        for (int i=-1; i<=1; i++) {
            for (int j=-2; j<=2; j++) {
                Terminal.setXY(x+j, y+i);
                if (CURSOR[mode][i+1].charAt(j+2) != ' ')
                    Terminal.setPixel(
                        Terminal.pixelHistory[x+j][y+i],
                        "\u001b[30m", 
                        CURSOR[mode][i+1].charAt(j+2)
                    );
            }
        }

        if (Board.getBuildingAt(new Point(x, y)).getYield() != 0 ||
                Board.getBuildingAt(new Point(x, y)).isBuildable()) {
            Terminal.setXY(x, y);
            Terminal.setPixelShallow(
                "\u001b[0m",
                '*'
            );
        }
    }

    public static void drawCursor(Point p) {
        drawCursor(p.x, p.y);
    }

    // removes a cursor centered at (x, y)
    public static void removeCursor(int x, int y) {
        for (int i=-1; i<=1; i++) {
            for (int j=-2; j<=2; j++) {
                Terminal.setXY(x+j, y+i);
                Terminal.setPixel(
                    Terminal.pixelHistory[x+j][y+i],
                    ' '
                );
            }
        }
    }

    // removes the cursor then re-renders the building at that location
    public static void removeCursor(Point p) {
        removeCursor(p.x, p.y);

        try {
            Board.getBuildingAt(p).render();
        }
        catch (Exception e) {
            System.err.println("Something went wrong...");
        }
    }

    public static void swapSprite() {
        mode = (mode+1)%2;
    }

    public static void drawRobberCursor(int x, int y) {
        x += 4;
        y += 1;

        for (int i=0; i<CURSOR[2].length; i++) {
            for (int j=0; j<CURSOR[2][i].length(); j++) {
                Terminal.setXY(x+j, y+i);
                if (CURSOR[2][i].charAt(j) == '.') {
                    Terminal.setPixelShallow(Terminal.pixelHistory[x+j][y+i], '#');
                }
            }
        }
    }

    public static void drawRobberCursor(Point p) {
        drawRobberCursor(p.x, p.y);
    }

    public static void removeRobberCursor(int x, int y) {
        x += 4;
        y += 1;

        for (int i=0; i<CURSOR[2].length; i++) {
            for (int j=0; j<CURSOR[2][i].length(); j++) {
                Terminal.setXY(x+j, y+i);
                if (CURSOR[2][i].charAt(j) == '.') {
                    Terminal.setPixelShallow(Terminal.pixelHistory[x+j][y+i], ' ');
                }
            }
        }
    }

    public static void removeRobberCursor(Point p) {
        removeRobberCursor(p.x, p.y);
    }

    /*
     * Draws a road in one of 6 potential directions:
     * 
     * 0        1        2        3        4        5
     *   *--      *          *      --*      \          /
     *             \        /                 \        /
     *              \      /                   *      *
     */
    public static void drawRoad(int x, int y, int dir) {
        Point p = new Point(x, y);
        String[] paste = ROAD[dir%3];
        switch (dir) {
            case 0:
                x++;
                break;
            case 1:
                y++;
                break;
            case 2:
                y++;
                x-=4;
                break;
            case 3:
                x-=11;
                break;
            case 4:
                y-=4;
                x-=4;
                break;
            default:
                y-=4;
                break;
        }

        for (int i=0; i<paste.length; i++) {
            for (int j=0; j<paste[i].length(); j++) {
                Terminal.setXY(x+j, y+i);
                Terminal.setPixel(
                    Terminal.pixelHistory[x+j][y+i],
                    paste[i].charAt(j)
                );
            }
        }

        try {
            Board.getBuildingAt(p).render();
        }
        catch (Exception e) {
            System.err.println("Something went wrong...");
        }
    }

    public static void removeRoad(int x, int y, int dir) {
        String[] paste = ROAD[dir%3];
        switch (dir) {
            case 0:
                x++;
                break;
            case 1:
                y++;
                break;
            case 2:
                y++;
                x-=4;
                break;
            case 3:
                x-=11;
                break;
            case 4:
                y-=4;
                x-=4;
                break;
            default:
                y-=4;
                break;
        }

        for (int i=0; i<paste.length; i++) {
            for (int j=0; j<paste[i].length(); j++) {
                Terminal.setXY(x+j, y+i);
                Terminal.setPixel(
                    Terminal.pixelHistory[x+j][y+i],
                    ' '
                );
            }
        }
    }
}
