import java.io.IOException;

public class TradeGUI implements GUI {
    private static final String BACKGROUND = "data/GUI/trade/background.txt";
    private static final String PATH = "data/GUI/trade/";
    private static final int WIDTH = 65, HEIGHT = 31;

    private GUIElement[] elements;
    private GUIElement curSelected;
    private Player player;
    private int x, y;

    private int[] currentOffer, currentDemands;
    private int totalDemands;

    private Player[] others;
    private boolean[] accepted;
    private int mode;

    public TradeGUI(int x, int y) throws IOException {
        this.x = x;
        this.y = y;
        this.player = Board.nextPlayer();
        this.mode = 0;

        others = new Player[3];
        others[0] = Board.nextPlayer();
        others[1] = Board.nextPlayer();
        others[2] = Board.nextPlayer();
        accepted = new boolean[3];

        // return to current player
        while (Board.peekPlayer() != this.player) Board.nextPlayer();

        this.currentOffer = new int[5];

        this.currentDemands = new int[5];
        this.totalDemands = 0;

        GUIElement[] elements = {
            new GUIElement(0, x+24 , y+2, PATH+"plus_normal.txt", PATH+"plus_selected.txt"),
            new GUIElement(1, x+24, y+6, PATH+"plus_normal.txt", PATH+"plus_selected.txt"),
            new GUIElement(2, x+24, y+10, PATH+"plus_normal.txt", PATH+"plus_selected.txt"),
            new GUIElement(3, x+24, y+14, PATH+"plus_normal.txt", PATH+"plus_selected.txt"),
            new GUIElement(4, x+24, y+18, PATH+"plus_normal.txt", PATH+"plus_selected.txt"),
            new GUIElement(5, x+36 , y+2, PATH+"plus_normal.txt", PATH+"plus_selected.txt"),
            new GUIElement(6, x+36, y+6, PATH+"plus_normal.txt", PATH+"plus_selected.txt"),
            new GUIElement(7, x+36, y+10, PATH+"plus_normal.txt", PATH+"plus_selected.txt"),
            new GUIElement(8, x+36, y+14, PATH+"plus_normal.txt", PATH+"plus_selected.txt"),
            new GUIElement(9, x+36, y+18, PATH+"plus_normal.txt", PATH+"plus_selected.txt"),
            new GUIElement(10, x+4, y+3, PATH+"minus_normal.txt", PATH+"minus_selected.txt"),
            new GUIElement(11, x+4, y+7, PATH+"minus_normal.txt", PATH+"minus_selected.txt"),
            new GUIElement(12, x+4, y+11, PATH+"minus_normal.txt", PATH+"minus_selected.txt"),
            new GUIElement(13, x+4, y+15, PATH+"minus_normal.txt", PATH+"minus_selected.txt"),
            new GUIElement(14, x+4, y+19, PATH+"minus_normal.txt", PATH+"minus_selected.txt"),
            new GUIElement(15, x+56, y+3, PATH+"minus_normal.txt", PATH+"minus_selected.txt"),
            new GUIElement(16, x+56, y+7, PATH+"minus_normal.txt", PATH+"minus_selected.txt"),
            new GUIElement(17, x+56, y+11, PATH+"minus_normal.txt", PATH+"minus_selected.txt"),
            new GUIElement(18, x+56, y+15, PATH+"minus_normal.txt", PATH+"minus_selected.txt"),
            new GUIElement(19, x+56, y+19, PATH+"minus_normal.txt", PATH+"minus_selected.txt"),
            new GUIElement(20, x+21, y+24, PATH+"arrow_normal.txt", PATH+"arrow_selected.txt"),
            new GUIElement(21, x+18, y+2, PATH+"num_normal.txt", null, PATH+"num/0.txt", "\u001b[38;5;0m"),
            new GUIElement(22, x+18, y+6, PATH+"num_normal.txt", null, PATH+"num/0.txt", "\u001b[38;5;0m"),
            new GUIElement(23, x+18, y+10, PATH+"num_normal.txt", null, PATH+"num/0.txt", "\u001b[38;5;0m"),
            new GUIElement(24, x+18, y+14, PATH+"num_normal.txt", null, PATH+"num/0.txt", "\u001b[38;5;0m"),
            new GUIElement(25, x+18, y+18, PATH+"num_normal.txt", null, PATH+"num/0.txt", "\u001b[38;5;0m"),
            new GUIElement(26, x+42, y+2, PATH+"num_normal.txt", null, PATH+"num/0.txt", "\u001b[38;5;0m"),
            new GUIElement(27, x+42, y+6, PATH+"num_normal.txt", null, PATH+"num/0.txt", "\u001b[38;5;0m"),
            new GUIElement(28, x+42, y+10, PATH+"num_normal.txt", null, PATH+"num/0.txt", "\u001b[38;5;0m"),
            new GUIElement(29, x+42, y+14, PATH+"num_normal.txt", null, PATH+"num/0.txt", "\u001b[38;5;0m"),
            new GUIElement(30, x+42, y+18, PATH+"num_normal.txt", null, PATH+"num/0.txt", "\u001b[38;5;0m"),
            new GUIElement(31, x+4 , y+24, PATH+"player_normal.txt", PATH+"player_selected.txt"),
            new GUIElement(32, x+24, y+24, PATH+"player_normal.txt", PATH+"player_selected.txt"),
            new GUIElement(33, x+44, y+24, PATH+"player_normal.txt", PATH+"player_selected.txt")
        };

        for (int i=0; i<5; i++) {
            elements[i].setLeft(elements[i+10]);
            elements[i+10].setRight(elements[i]);

            elements[i+5].setLeft(elements[i]);
            elements[i].setRight(elements[i+5]);

            elements[i+15].setLeft(elements[i+5]);
            elements[i+5].setRight(elements[i+15]);
        }

        for (int i=0; i<4; i++) {
            for (int j=0; j<4; j++) {
                elements[i+j*5].setDown(elements[i+1+j*5]);
                elements[i+1+j*5].setUp(elements[i+j*5]);
            }
        }

        elements[4].setDown(elements[20]);
        elements[9].setDown(elements[20]);
        elements[14].setDown(elements[20]);
        elements[19].setDown(elements[20]);
        
        elements[20].setUp(elements[4]);
        elements[20].setLeft(elements[14]);
        elements[20].setRight(elements[19]);

        elements[31].setRight(elements[32]);
        elements[32].setRight(elements[33]);
        elements[32].setLeft(elements[31]);
        elements[33].setLeft(elements[32]);

        if (Catan.PLAYERS == 2) {
            elements[31] = elements[32];
            elements[33] = elements[32];

            elements[32].setLeft(null);
            elements[32].setRight(null);
            elements[32].setID(31);
        }
        else if (Catan.PLAYERS == 3) {
            elements[33] = elements[32];
            elements[32].setRight(null);

            elements[31].setX(elements[31].getX() + 10);
            elements[32].setX(elements[32].getX() + 10);
        }

        this.elements = elements;
        this.curSelected = elements[20];
    }

    public boolean click() throws IOException {
        switch (curSelected.getID()) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                clickPlus(curSelected.getID());
                break;
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
                clickMinus(curSelected.getID());
                break;
            case 20:
                if (instantTradePossible()) {
                    terminate(null);
                    return true;
                }

                if (legalTrade())
                    switchMode();
                break;
            default: 
                if (accepted[curSelected.getID()-31]) {
                    terminate(others[curSelected.getID()-31]);
                    return true;
                }
                break;
        }

        return false;
    }

    public void clickPlus(int i) throws IOException {
        if (i > 4) {
            if (currentDemands[i-5] < 19) {
                currentDemands[i-5]++;
                totalDemands++;

                elements[i+21].setMask(PATH+"num/"+currentDemands[i-5]+".txt");
                elements[i+21].drawNormal();
            }

            return;
        }

        int[] resources = {
            player.getBrick(),
            player.getOre(),
            player.getWheat(),
            player.getWood(),
            player.getSheep()
        };

        if (currentOffer[i] < resources[i]) {
            currentOffer[i]++;

            elements[i+21].setMask(PATH+"num/"+currentOffer[i]+".txt");
            elements[i+21].drawNormal();
        }
    }

    public void clickMinus(int i) throws IOException {
        if (i > 14) {
            if (currentDemands[i-15] > 0) {
                currentDemands[i-15]--;
                totalDemands--;

                elements[i+11].setMask(PATH+"num/"+currentDemands[i-15]+".txt");
                elements[i+11].drawNormal();
            }

            return;
        }

        if (currentOffer[i-10] > 0) {
            currentOffer[i-10]--;

            elements[i+11].setMask(PATH+"num/"+currentOffer[i-10]+".txt");
            elements[i+11].drawNormal();
        }
    }

    public boolean instantTradePossible() {
        int[] ratios = this.player.getTrades();
        for (int i=0; i<5; i++) {
            if (currentOffer[i] % ratios[i] != 0)
                return false;
        }

        int totalFeasibleDemands = 0;
        for (int i=0; i<5; i++) {
            totalFeasibleDemands += currentOffer[i] / ratios[i];
        }

        return totalFeasibleDemands == totalDemands;
    }

    public void switchMode() throws IOException {
        Artist.drawColorBox(x+2, 
                            y+23, 
                            x+WIDTH-3, 
                            y+29, 
                            "beige");
        
        if (mode == 0) {
            renderPlayer(0);
            if (Catan.PLAYERS >= 3) renderPlayer(1);
            if (Catan.PLAYERS >= 4) renderPlayer(2);

            curSelected = elements[32];
            if (Catan.PLAYERS != 4) curSelected = elements[31];

            curSelected.drawSelected();

            mode = 1;
        }
        else {
            curSelected = elements[20];
            curSelected.drawSelected();

            accepted = new boolean[3];

            mode = 0;
        }
    }

    public void renderPlayer(int i) {
        GUIElement elementAt = elements[31+i];

        if (accepted[i]) Artist.drawColorBox(elementAt.getX(), 
                                             elementAt.getY(), 
                                             elementAt.getX()+16, 
                                             elementAt.getY()+4, 
                                             "light_green");
        else Artist.drawColorBox(elementAt.getX(), 
                                 elementAt.getY(), 
                                 elementAt.getX()+16, 
                                 elementAt.getY()+4, 
                                 "light_red");

        Artist.drawColorBox(elementAt.getX()+1, 
                            elementAt.getY()+1, 
                            elementAt.getX()+15, 
                            elementAt.getY()+3, 
                            others[i].getColor());
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

            if (curSelected.getID() > 30)
                Artist.drawColorBox(curSelected.getX()+1, 
                                    curSelected.getY()+1, 
                                    curSelected.getX()+15, 
                                    curSelected.getY()+3, 
                                    others[curSelected.getID()-31].getColor());

            curSelected = next;
        }
    }

    public void moveRight() throws IOException {
        GUIElement next = curSelected.getRight();

        if (next != null) {
            curSelected.drawNormal();
            next.drawSelected();

            if (curSelected.getID() > 30)
                Artist.drawColorBox(curSelected.getX()+1, 
                                    curSelected.getY()+1, 
                                    curSelected.getX()+15, 
                                    curSelected.getY()+3, 
                                    others[curSelected.getID()-31].getColor());

            curSelected = next;
        }
    }

    public void render() throws IOException {
        Artist.drawFromFileShallow(BACKGROUND, x, y);

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
        Board.renderPlayerStats();
    }

    public void terminate(Player recipient) throws IOException {
        ChatBox.trade(player, recipient, currentOffer, currentDemands);

        player.increaseResource(Resource.BRICK, currentDemands[0] - currentOffer[0]);
        player.increaseResource(Resource.ORE,   currentDemands[1] - currentOffer[1]);
        player.increaseResource(Resource.WHEAT, currentDemands[2] - currentOffer[2]);
        player.increaseResource(Resource.WOOD,  currentDemands[3] - currentOffer[3]);
        player.increaseResource(Resource.SHEEP, currentDemands[4] - currentOffer[4]);

        if (recipient == null) {
            terminate();
            return;
        }

        recipient.decreaseResource(Resource.BRICK, currentDemands[0] - currentOffer[0]);
        recipient.decreaseResource(Resource.ORE,   currentDemands[1] - currentOffer[1]);
        recipient.decreaseResource(Resource.WHEAT, currentDemands[2] - currentOffer[2]);
        recipient.decreaseResource(Resource.WOOD,  currentDemands[3] - currentOffer[3]);
        recipient.decreaseResource(Resource.SHEEP, currentDemands[4] - currentOffer[4]);

        terminate();
    }

    public boolean buttonPress(int in) throws IOException {
        if (in == 27) {
            if (mode == 0) {
                terminate();
                return true;
            }
            else {
                switchMode();
            }
        }
        else if (in == 't' && mode == 0) {
            terminate();
            return true;
        }
        else if (mode == 1) {
            if (in == '1' && others[0].canAffordTrade(currentDemands)) {
                accepted[0] = !accepted[0];
                renderPlayer(0);
                curSelected.drawSelected();
            }
            else if (Catan.PLAYERS >= 3 && in == '2' && others[1].canAffordTrade(currentDemands)) {
                accepted[1] = !accepted[1];
                renderPlayer(1);
                curSelected.drawSelected();
            }
            else if (Catan.PLAYERS >= 4 && in == '3' && others[2].canAffordTrade(currentDemands)) {
                accepted[2] = !accepted[2];
                renderPlayer(2);
                curSelected.drawSelected();
            }
        }

        return false;
    }

    public boolean legalTrade() {
        return currentOffer[0] * currentDemands[0] == 0 &&
                currentOffer[1] * currentDemands[1] == 0 &&
                currentOffer[2] * currentDemands[2] == 0 &&
                currentOffer[3] * currentDemands[3] == 0 &&
                currentOffer[4] * currentDemands[4] == 0;
    }
}
