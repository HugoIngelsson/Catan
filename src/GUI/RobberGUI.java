import java.io.IOException;

public class RobberGUI implements GUI {
    private static final String BACKGROUND = "data/GUI/robber/background.txt";
    private static final String PATH = "data/GUI/robber/";
    private static final int WIDTH = 55, HEIGHT = 23;

    private GUIElement[] elements;
    private GUIElement curSelected;
    private Player player;
    private int x, y;

    private int[] currentOffer;
    private int totalOffer;

    public RobberGUI(int x, int y, Player player) throws IOException {
        this.x = x;
        this.y = y;
        this.player = player;

        this.currentOffer = new int[5];
        this.totalOffer = 0;

        GUIElement[] elements = {
            new GUIElement(0, x+5 , y+4, PATH+"plus_normal.txt", PATH+"plus_selected.txt"),
            new GUIElement(1, x+15, y+4, PATH+"plus_normal.txt", PATH+"plus_selected.txt"),
            new GUIElement(2, x+25, y+4, PATH+"plus_normal.txt", PATH+"plus_selected.txt"),
            new GUIElement(3, x+35, y+4, PATH+"plus_normal.txt", PATH+"plus_selected.txt"),
            new GUIElement(4, x+45, y+4, PATH+"plus_normal.txt", PATH+"plus_selected.txt"),
            new GUIElement(5, x+5 , y+16, PATH+"minus_normal.txt", PATH+"minus_selected.txt"),
            new GUIElement(6, x+15, y+16, PATH+"minus_normal.txt", PATH+"minus_selected.txt"),
            new GUIElement(7, x+25, y+16, PATH+"minus_normal.txt", PATH+"minus_selected.txt"),
            new GUIElement(8, x+35, y+16, PATH+"minus_normal.txt", PATH+"minus_selected.txt"),
            new GUIElement(9, x+45, y+16, PATH+"minus_normal.txt", PATH+"minus_selected.txt"),
            new GUIElement(10, x+19, y+18, PATH+"done_normal.txt", PATH+"done_selected.txt", PATH+"done_mask.txt", "\u001b[38;5;255m"),
            new GUIElement(11, x+ 5, y+12, PATH+"num_normal.txt", null, PATH+"num/0.txt", "\u001b[38;5;0m"),
            new GUIElement(12, x+15, y+12, PATH+"num_normal.txt", null, PATH+"num/0.txt", "\u001b[38;5;0m"),
            new GUIElement(13, x+25, y+12, PATH+"num_normal.txt", null, PATH+"num/0.txt", "\u001b[38;5;0m"),
            new GUIElement(14, x+35, y+12, PATH+"num_normal.txt", null, PATH+"num/0.txt", "\u001b[38;5;0m"),
            new GUIElement(15, x+45, y+12, PATH+"num_normal.txt", null, PATH+"num/0.txt", "\u001b[38;5;0m")
        };

        for (int i=0; i<4; i++) {
            elements[i].setRight(elements[i+1]);
            elements[i+1].setLeft(elements[i]);

            elements[i+5].setRight(elements[i+6]);
            elements[i+6].setLeft(elements[i+5]);

            elements[i].setDown(elements[i+5]);
            elements[i+5].setUp(elements[i]);

            elements[i+5].setDown(elements[10]);
        }

        elements[4].setDown(elements[9]);
        elements[9].setUp(elements[4]);

        elements[9].setDown(elements[10]);
        elements[10].setUp(elements[7]);
        elements[10].setLeft(elements[5]);
        elements[10].setRight(elements[9]);

        this.elements = elements;
        this.curSelected = elements[0];
    }

    public boolean click() throws IOException {
        switch (curSelected.getID()) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                clickPlus(curSelected.getID());
                break;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                clickMinus(curSelected.getID());
                break;
            default: 
                if (totalOffer == player.getTotalResources()/2) {
                    terminate();
                    return true;
                }
                break;
        }

        return false;
    }

    public void clickPlus(int i) throws IOException {
        int[] resources = {
            player.getBrick(),
            player.getOre(),
            player.getWheat(),
            player.getWood(),
            player.getSheep()
        };

        if (currentOffer[i] < resources[i]) {
            currentOffer[i]++;
            totalOffer++;

            elements[i+11].setMask(PATH+"num/"+currentOffer[i]+".txt");
            elements[i+11].drawNormal();
        }
    }

    public void clickMinus(int i) throws IOException {
        if (currentOffer[i-5] > 0) {
            currentOffer[i-5]--;
            totalOffer--;

            elements[i+6].setMask(PATH+"num/"+currentOffer[i-5]+".txt");
            elements[i+6].drawNormal();
        }
    }

    public void moveUp() throws IOException {
        GUIElement next = curSelected.getUp();

        if (next != null) {
            curSelected.drawNormal();
            next.drawSelected();

            curSelected = next;
        }
    }

    public void moveDown() throws IOException {
        GUIElement next = curSelected.getDown();

        if (next != null) {
            curSelected.drawNormal();
            next.drawSelected();

            curSelected = next;
        }
    }

    public void moveLeft() throws IOException {
        GUIElement next = curSelected.getLeft();

        if (next != null) {
            curSelected.drawNormal();
            next.drawSelected();

            curSelected = next;
        }
    }

    public void moveRight() throws IOException {
        GUIElement next = curSelected.getRight();

        if (next != null) {
            curSelected.drawNormal();
            next.drawSelected();

            curSelected = next;
        }
    }

    public void render() throws IOException {
        Artist.drawColorBox(x, y, x+WIDTH-1, y+HEIGHT-1, player.getColor());
        Artist.drawFromFileShallow(BACKGROUND, x, y);

        Terminal.setXY(x+3, y+1);
        Terminal.printShallow("\u001b[48;5;230m", "You're being robbed! Choose " +
                    (player.getTotalResources()/2) + " resources to lose!");

        for (GUIElement elem : elements) {
            elem.drawNormal();
        }

        if (curSelected != null)
            curSelected.drawSelected();
    }

    public void terminate() throws IOException {
        Artist.restoreSubscreen(x, y, x+WIDTH-1, y+HEIGHT-1);

        for (Building b : Board.buildings.values()) {
            b.render();
        }

        for (Tile t : Board.tiles) {
            t.renderNumber();
        }

        Cursor.drawCursor(UI.cursorPos);

        player.decreaseResource(Resource.BRICK, currentOffer[0]);
        player.decreaseResource(Resource.ORE  , currentOffer[1]);
        player.decreaseResource(Resource.WHEAT, currentOffer[2]);
        player.decreaseResource(Resource.WOOD , currentOffer[3]);
        player.decreaseResource(Resource.SHEEP, currentOffer[4]);
        ChatBox.discardResources(player, currentOffer);

        Board.renderPlayerStats();
    }

    public boolean buttonPress(int in) {
        return false;
    }
}
