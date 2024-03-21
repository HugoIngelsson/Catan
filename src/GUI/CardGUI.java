import java.io.IOException;

public class CardGUI implements GUI {
    private static final int WIDTH = 24, HEIGHT = 21;

    private int x, y;
    private Card card;

    public CardGUI(int x, int y, Card card) throws IOException {
        this.x = x;
        this.y = y;
        this.card = card;
    }

    public boolean click() throws IOException {
        terminate();
        return true;
    }

    public void moveUp() throws IOException {
        // do nothing
    }

    public void moveDown() throws IOException {
        // do nothing
    }

    public void moveLeft() throws IOException {
        // do nothing
    }

    public void moveRight() throws IOException {
        // do nothing
    }

    public void render() throws IOException {
        card.renderFront(x, y);
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
        terminate();
        return true;
    }
}
