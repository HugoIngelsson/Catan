import java.io.IOException;

public class GUIElement {
    private int id;
    private int x, y;

    private String normalSprite;
    private String selectedSprite;
    private String mask;
    private String textColor;

    private GUIElement up, down, left, right;

    public GUIElement(int id, int x, int y, String normalSprite, String selectedSprite) {
        this.id = id;
        this.x = x;
        this.y = y;

        this.normalSprite = normalSprite;
        this.selectedSprite = selectedSprite;
    }

    public GUIElement(int id, int x, int y, String normalSprite, 
            String selectedSprite, String mask, String textColor) {
        this.id = id;
        this.x = x;
        this.y = y;

        this.normalSprite = normalSprite;
        this.selectedSprite = selectedSprite;
        this.mask = mask;
        this.textColor = textColor;
    }

    public int getID() {
        return id;
    }

    public void drawNormal() throws IOException {
        if (mask == null)
            Artist.drawFromFileShallow(normalSprite, x, y);
        else   
            drawMask(normalSprite); 
    }

    public void drawSelected() throws IOException {
        if (mask == null)
            Artist.drawFromFileShallow(selectedSprite, x, y);
        else   
            drawMask(selectedSprite); 
    }

    public void drawMask(String bg) throws IOException {
        Artist.drawFromFileShallow(bg, mask, textColor, x, y);
    }

    public void setMask(String s) {
        this.mask = s;
    }

    public void setID(int i) {
        this.id = i;
    }

    public void setUp(GUIElement e) {
        this.up = e;
    }
    
    public void setDown(GUIElement e) {
        this.down = e;
    }

    public void setLeft(GUIElement e) {
        this.left = e;
    }

    public void setRight(GUIElement e) {
        this.right = e;
    }

    public GUIElement getUp() {
        return this.up;
    }

    public GUIElement getDown() {
        return this.down;
    }

    public GUIElement getLeft() {
        return this.left;
    }

    public GUIElement getRight() {
        return this.right;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }
}
