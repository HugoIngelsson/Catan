import java.io.IOException;

public interface GUI {
    public boolean click() throws IOException;
    public boolean buttonPress(int in) throws IOException;

    public void moveUp() throws IOException;
    public void moveDown() throws IOException;
    public void moveLeft() throws IOException;
    public void moveRight() throws IOException;

    public void render() throws IOException;
    public void terminate() throws IOException;
}
