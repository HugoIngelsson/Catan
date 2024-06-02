import java.io.IOException;

public class MenuGUI implements GUI {
    private static final String PATH = "data/GUI/escape/";
    private static final int WIDTH = 70, HEIGHT = 35;

    private int x, y;
    private int height;

    private GUIElement[] elements;
    private GUIElement curSelected;

    public MenuGUI(int x, int y) throws IOException {
        this.x = x;
        this.y = y;
        this.height = 0;

        GUIElement[] elements = {
            new GUIElement(0, x+5, y+ 3, PATH + "option_normal.txt", PATH + "option_selected.txt"),
            new GUIElement(1, x+5, y+13, PATH + "option_normal.txt", PATH + "option_selected.txt"),
            new GUIElement(2, x+5, y+23, PATH + "option_normal.txt", PATH + "option_selected.txt")
        };

        elements[0].setDown(elements[1]);
        elements[1].setUp(elements[0]);
        elements[1].setDown(elements[2]);
        elements[2].setUp(elements[1]);

        this.curSelected = elements[0];
        this.elements = elements;
    }

    public boolean click() throws IOException {
        if (height == 0 && !Catan.inGame) {
            terminate();
            UI.curGUI = new PlayerSelectGUI(33, 6);
            UI.curGUI.render();
            Catan.CLIMATE = false;

            return false;
        }
        else if (height == 0 && Catan.inGame) {
            terminate();
            Catan.inGame = false;

            Artist.drawFromFile(Catan.HOME_BACKGROUND, 1, 1);
            return true;
        }
        else if (height == 1 && !Catan.inGame) {
            terminate();
            UI.curGUI = new PlayerSelectGUI(33, 6);
            UI.curGUI.render();
            Catan.CLIMATE = true;

            return false;
        }
        
        return false;
    }

    public void moveUp() throws IOException {
        if (height > 0) {
            curSelected.drawNormal();
            curSelected = elements[--height];
            curSelected.drawSelected();
        }
    }

    public void moveDown() throws IOException {
        if (height < 2) {
            curSelected.drawNormal();
            curSelected = elements[++height];
            curSelected.drawSelected();
        }
    }

    public void moveLeft() throws IOException {
        // do nothing
    }

    public void moveRight() throws IOException {
        // do nothing
    }

    public void render() throws IOException {
        if (Catan.inGame)
            Artist.drawFromFileShallow(PATH+"in_game.txt", x, y);
        else
            Artist.drawFromFileShallow(PATH+"not_in_game.txt", x, y);

        for (GUIElement gui : elements)
            gui.drawNormal();

        curSelected.drawSelected();
    }

    public void terminate() throws IOException {
        Artist.restoreSubscreen(x, y, x+WIDTH-1, y+HEIGHT-1);

        if (Catan.inGame) {
            for (Building b : Board.buildings.values()) {
                b.render();
            }
    
            for (Tile t : Board.tiles) {
                t.renderNumber();
            }
    
            Cursor.drawCursor(UI.cursorPos);
            Board.renderPlayerStats();
        }
    }

    public boolean buttonPress(int in) throws IOException {
        if (in == 27) {
            terminate();
            return true;
        }

        return false;
    }
}
