import java.io.IOException;
import java.io.InputStreamReader;

public class Catan {
    public static final String HOME_BACKGROUND = "data/home_page.txt";

    public static int PLAYERS = 4;
    public static int VP_REQUIREMENT = 10;
    public static int starter;
    public static boolean inGame = true;

    public static void main(String[] args) throws IOException, InterruptedException {
        inGame = false;

        Terminal.initialize();
        Artist.drawFromFile(HOME_BACKGROUND, 1, 1);
        
        Terminal.setXY(Terminal.WIDTH, Terminal.HEIGHT);
        Terminal.flush();

        int[] buffer = new int[100];
        InputStreamReader isr = new InputStreamReader(System.in);
        while (true) {
            int bufferID = 0;
            while (isr.ready()) {
                buffer[bufferID++] = isr.read();
            }

            if (buffer[0] != 0) {
                if (!inGame) {
                    if (UI.clearMenuBuffer(buffer)) break;

                    Terminal.flush();
                }
                else if (starter < PLAYERS*4) {
                    if (UI.clearBufferAtStart(buffer)) break;

                    if (starter < PLAYERS*2-1) {
                        if (starter % 2 == 1) {
                            Board.nextPlayer();
                            Artist.drawPlayerEmblem(Board.peekPlayer().getColor());
                            starter++;
                        }
                    }
                    else if (starter % 2 == 1) {
                        for (int i=0; i<PLAYERS-1; i++)
                            Board.nextPlayer();
                        Artist.drawPlayerEmblem(Board.peekPlayer().getColor());
                        starter++;

                        if (starter == PLAYERS*2 || starter == PLAYERS*4) {
                            Board.nextPlayer();
                            Artist.drawPlayerEmblem(Board.peekPlayer().getColor());
                        }
                    }

                    Terminal.flush();
                }
                else if (UI.clearBuffer(buffer)) break;
                else Terminal.flush();
            }
            
            Thread.sleep(33);
        }

        Terminal.close();
    }
}
