import java.io.IOException;
import java.util.ArrayList;

public class DevGUI implements GUI {
    private static final String BACKGROUND = "data/GUI/dev/GUI_background.txt";
    private static final int WIDTH = 48, HEIGHT = 25;

    private int x, y;
    private int id;

    private ArrayList<Card> cards;

    public DevGUI(int x, int y, Player player) throws IOException {
        this.x = x;
        this.y = y;

        this.cards = player.getCards();
    }

    public boolean click() throws IOException {
        if (cards.get(id).canPlay()) {
            terminate();

            cards.get(id).play();

            UI.cardHasBeenUsed = true;
            cards.remove(id);

            Board.renderPlayerStats();
            return true;
        }

        return false;
    }

    public void moveUp() throws IOException {
        // do nothing
    }

    public void moveDown() throws IOException {
        // do nothing
    }

    public void moveLeft() throws IOException {
        if (id > 0) {
            id--;
            render();
        }
    }

    public void moveRight() throws IOException {
        if (id < cards.size()-1) {
            id++;
            render();
        }
    }

    public void render() throws IOException {
        Artist.drawFromFileShallow(BACKGROUND, x, y);

        if (id > 0) 
            cards.get(id-1).renderBack(x+4, y+4);

        if (id < cards.size()-1) 
            cards.get(id+1).renderBack(x+20, y+4);

        if (cards.size() != 0)
            cards.get(id).renderFront(x+12, y+2);
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

    public boolean buttonPress(int in) throws IOException {
        if (in == 27 || in == 'd') {
            terminate();
            return true;
        }
        return false;
    }
}
