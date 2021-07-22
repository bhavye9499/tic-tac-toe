package enums;

import java.util.HashMap;

public enum Move {

    TOP_LEFT("TL", new int[]{0, 0}),
    TOP_CENTER("TC", new int[]{0, 1}),
    TOP_RIGHT("TR", new int[]{0, 2}),
    MIDDLE_LEFT("ML", new int[]{1, 0}),
    MIDDLE_CENTER("MC", new int[]{1, 1}),
    MIDDLE_RIGHT("MR", new int[]{1, 2}),
    BOTTOM_LEFT("BL", new int[]{2, 0}),
    BOTTOM_CENTER("BC", new int[]{2, 1}),
    BOTTOM_RIGHT("BR", new int[]{2, 2});

    private final String shortName;
    private final int[] position;
    private static final HashMap<String, Move> shortNameToMove;

    static {
        shortNameToMove = new HashMap<>();
        for (Move mv : Move.values()) {
            shortNameToMove.put(mv.getShortName(), mv);
        }
    }

    Move(String shortName, int[] position) {
        this.shortName = shortName;
        this.position = position;
    }

    public String getShortName() {
        return shortName;
    }

    public int[] getPosition() {
        return position;
    }

    public static Move stringToMove(String s) {
        return shortNameToMove.get(s);
    }

}
