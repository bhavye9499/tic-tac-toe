package game_objects;

import enums.Symbol;
import io.ClientHandler;

public class Player {

    private static int idCtr;

    private int id;
    private String name;
    private final Symbol symbol;
    private final ClientHandler clientHandler;

    static {
        Player.idCtr = 0;
    }

    public Player (Symbol symbol, ClientHandler clientHandler) {
        this.symbol = symbol;
        this.clientHandler = clientHandler;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Symbol getSymbol() {
        return this.symbol;
    }

    public ClientHandler getClientHandler() {
        return this.clientHandler;
    }

}
