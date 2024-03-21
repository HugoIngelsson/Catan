import java.io.IOException;
import java.util.ArrayList;

public class ChatBox {
    public static final String RESET = "\u001b[0m";

    public static int x, y, width, height;
    public static ArrayList<String> messages;

    public static void initialize(int x, int y, int width, int height) {
        ChatBox.x = x;
        ChatBox.y = y;
        ChatBox.width = width;
        ChatBox.height = height;

        messages = new ArrayList<>();

        String startMessage = "Welcome to Catan,";
        for (int i=0; i<Catan.PLAYERS; i++) {
            if (i == Catan.PLAYERS-1) startMessage += " and";

            startMessage += " " + Board.nextPlayer() + RESET;

            if (i != Catan.PLAYERS-1 && Catan.PLAYERS > 2)
                startMessage += ",";
        }
        startMessage += "! To learn the controls of the game, press <ESC>" +
            " and navigate to the \"OPTIONS\" menu.";
        addMessage(startMessage);
    }

    public static void clear() {
        for (int i=0; i<height; i++) {
            for (int j=0; j<width; j++) {
                Terminal.setXY(x+j, y+i);
                Terminal.setPixel(Terminal.ANSI_RESET_COLOR, ' ');
            }
        }
    }

    public static void clear(int deltaW) {
        for (int i=0; i<height; i++) {
            for (int j=-deltaW; j<width+deltaW; j++) {
                Terminal.setXY(x+j, y+i);
                Terminal.setPixel(Terminal.ANSI_RESET_COLOR, ' ');
            }
        }
    }

    public static void displayMessages() throws IOException {
        clear();
        int historyLength = Math.min(height, messages.size());
        for (int i=0; i<historyLength; i++) {
            Terminal.setXY(x, y+height-1-i);
            Terminal.printShallow(Terminal.ANSI_RESET_COLOR, messages.get(i));
        }
    }

    public static void addMessage(String s) {
        String[] parts = s.split(" ");
        String cur = "";

        for (String next : parts) {
            if (nonEscapeLength(cur) + 1 + nonEscapeLength(next) <= width) {
                if (cur.length() != 0) cur += " ";
                cur += next;
            }
            else {
                messages.add(0, cur);
                cur = "    " + next;
            }
        }

        messages.add(0, cur);
        while (messages.size() > height)
            messages.remove(messages.size()-1);

        try {
            displayMessages();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int nonEscapeLength(String s) {
        int l = 0;
        boolean isInEsc = false;

        for (char c : s.toCharArray()) {
            if (isInEsc) {
                if (c == 'm') isInEsc = false;
            }
            else if (c == 27) isInEsc = true;
            else l++;
        }

        return l;
    }

    public static void buildTown(Player p) {
        addMessage(p.toString() + RESET + " built a town!");
    }

    public static void buildSecondTown(Player p) {
        int partCount = 0;
        if (p.getBrick() > 0) partCount++;
        if (p.getOre() > 0) partCount++;
        if (p.getWheat() > 0) partCount++;
        if (p.getWood() > 0) partCount++;
        if (p.getSheep() > 0) partCount++;

        if (partCount == 0) {
            addMessage(p.toString() + RESET + " built a town! This was their second town, so they " +
                        "would have received some resources. However, they built their town next to a " +
                        "desert, so they receive nothing... (WHY????)");
            return;
        }

        String message = p.toString() + RESET + " built a town! Because this was their second town, they receive ";
        int[] resources = {p.getBrick(), p.getOre(), p.getWheat(), p.getWood(), p.getSheep()};
        addMessage(message + formatReturns(resources, partCount) + ".");
    }

    public static void buildCity(Player p) {
        addMessage(p.toString() + RESET + " upgraded a town into a city!");
    }

    public static void buildRoad(Player p) {
        addMessage(p.toString() + RESET + " built a road!");
    }

    public static void buildLongestRoad(Player p) {
        addMessage(p.toString() + RESET + " built a road." + 
           " They now have the \u001b[38;5;214mlongest \u001b[38;5;214mroad!");
    }

    public static void hasLargestArmy(Player p) {
        addMessage(p.toString() + RESET + " now has the " +
                    "\u001b[38;5;214mlargest \u001b[38;5;214marmy!");
    }

    public static void buyDevCard(Player p, Card c) {
        addMessage(p.toString() + RESET + " bought a development card! They received a " +
            c.toString());
    }

    public static void monopoly(Player p, Resource resource) {
        String message = p.toString() + RESET + " made a monopoly on ";

        switch (resource) {
            case BRICK:
                message += "\u001b[38;5;88mBRICK" + RESET;
                break;
            case ORE:
                message += "\u001b[38;5;250mORE" + RESET;
                break;
            case WHEAT:
                message += "\u001b[38;5;221mWHEAT" + RESET;
                break;
            case WOOD:
                message += "\u001b[38;5;130mLOGS" + RESET;
                break;
            default:
                message += "\u001b[38;5;82mSHEEP" + RESET;
                break;
        }
        message += ". They now have ";
        switch (resource) {
            case BRICK:
                message += "\u001b[38;5;88m" + p.getBrick() + " ";
                message += "\u001b[38;5;88mBRICK" + RESET;
                if (p.getBrick() > 1) message += "\u001b[38;5;88mS" + RESET;
                break;
            case ORE:
                message += "\u001b[38;5;250m" + p.getOre() + " ";
                message += "\u001b[38;5;250mORE" + RESET;
                if (p.getOre() > 1) message += "\u001b[38;5;250mS" + RESET;
                break;
            case WHEAT:
                message += "\u001b[38;5;221m" + p.getWheat() + " ";
                message += "\u001b[38;5;221mWHEAT" + RESET;
                break;
            case WOOD:
                message += "\u001b[38;5;130m" + p.getWood() + " ";
                message += "\u001b[38;5;130mLOG" + RESET;
                if (p.getWood() > 1) message += "\u001b[38;5;130mS" + RESET;
                break;
            default:
                message += "\u001b[38;5;82m" + p.getSheep() + " ";
                message += "\u001b[38;5;82mSHEEP" + RESET;
                break;
        }

        message += ".";
        addMessage(message);
    }

    public static void yearOfPlenty(Player p, int[] resources) {
        String message = p.toString() + RESET + " had a plentiful year! They received an extra ";
        
        int partCount = 0;
        for (int i=0; i<5; i++) if (resources[i] > 0) partCount++;
        message += formatReturns(resources, partCount) + ".";

        addMessage(message);
    }

    public static void victory(Player p) {
        String message = "Extra! Extra! " + p.toString() + RESET + " has been announced the " + 
                    "\u001b[38;5;214mruler \u001b[38;5;214mof \u001b[38;5;214mCatan " + RESET +
                    "after ending their turn with \u001b[38;5;214m" + Catan.VP_REQUIREMENT + 
                    " \u001b[38;5;214mVICTORY " + "\u001b[38;5;214mPOINTS" + RESET + "!";

        addMessage(message);
    }

    public static void receiveDiceRoll(Player p, int r1, int r2) {
        String message = p.toString() + RESET + " rolled a " + (r1 + r2) + 
                            " (" + r1 + " + " + r2 + ")";
        if (r1 + r2 == 7) message += ", triggering the robber!";

        addMessage(message);
    }

    public static void receiveDiceRoll(Player p) {
        int[] resources = p.getResourcesThisTurn();

        int partCount = 0;
        for (int i=0; i<5; i++) if (resources[i] > 0) partCount++;

        if (partCount > 0) {
            String message = "~ " + p.toString() + RESET + " received " + 
                                formatReturns(resources, partCount) + ".";

            addMessage(message);
        }
    }

    public static void stealResources(Player to, Player from, Resource resource) {
        String message = to.toString() + RESET + " stole a";

        switch (resource) {
            case BRICK:
                message += " \u001b[38;5;88mBRICK " + RESET;
                break;
            case ORE:
                message += "n \u001b[38;5;250mORE " + RESET;
                break;
            case WHEAT:
                message += " \u001b[38;5;221mWHEAT " + RESET;
                break;
            case WOOD:
                message += " \u001b[38;5;130mLOG " + RESET;
                break;
            default:
                message += " \u001b[38;5;82mSHEEP " + RESET;
                break;
        }

        message += "from " + from.toString() + RESET + ".";
        addMessage(message);
    }

    public static void trade(Player active, Player acceptor, int[] offered, int[] received) {
        String message = active.toString() + RESET + " gave ";
        if (acceptor == null) message += "the bank";
        else message += acceptor.toString() + RESET;

        int partCount = 0;
        for (int i=0; i<5; i++) if (offered[i] > 0) partCount++;

        message += " " + formatReturns(offered, partCount) + " in exchange for ";

        partCount = 0;
        for (int i=0; i<5; i++) if (received[i] > 0) partCount++;

        message += formatReturns(received, partCount) + ".";
        addMessage(message);
    }

    public static void discardResources(Player p, int[] discarded) {
        int partCount = 0;
        for (int i=0; i<5; i++) if (discarded[i] > 0) partCount++;

        addMessage("~ " + p.toString() + RESET + " had to discard " + formatReturns(discarded, partCount) + ".");
    }

    private static String formatReturns(int[] resourceNums, int partCount) {
        String message = "";

        int added = 0;
        if (resourceNums[0] > 0) {
            message += "\u001b[38;5;88m" + resourceNums[0] + " \u001b[38;5;88mBRICK" + RESET;

            added++;
            if (added == partCount);
            else if (added != partCount-1 && partCount > 2) message += ", ";
            else if (added == partCount-1 && partCount > 2) message += ", and ";
            else message += " and ";
        }
        if (resourceNums[1] > 0) {
            message += "\u001b[38;5;250m" + resourceNums[1] + " \u001b[38;5;250mORE" + RESET;

            added++;
            if (added == partCount);
            else if (added != partCount-1 && partCount > 2) message += ", ";
            else if (added == partCount-1 && partCount > 2) message += ", and ";
            else message += " and ";
        }
        if (resourceNums[2] > 0) {
            message += "\u001b[38;5;221m" + resourceNums[2] + " \u001b[38;5;221mWHEAT" + RESET;

            added++;
            if (added == partCount);
            else if (added != partCount-1 && partCount > 2) message += ", ";
            else if (added == partCount-1 && partCount > 2) message += ", and ";
            else message += " and ";
        }
        if (resourceNums[3] > 0) {
            message += "\u001b[38;5;130m" + resourceNums[3] + " \u001b[38;5;130mLOG" + RESET;
            if (resourceNums[3] > 1) message += "\u001b[38;5;130mS" + RESET;

            added++;
            if (added == partCount);
            else if (added != partCount-1 && partCount > 2) message += ", ";
            else if (added == partCount-1 && partCount > 2) message += ", and ";
            else message += " and ";
        }
        if (resourceNums[4] > 0) {
            message += "\u001b[38;5;82m" + resourceNums[4] + " \u001b[38;5;82mSHEEP" + RESET;

            added++;
            if (added == partCount);
            else if (added != partCount-1 && partCount > 2) message += ", ";
            else if (added == partCount-1 && partCount > 2) message += ", and ";
            else message += " and ";
        }

        return message;
    }
}
