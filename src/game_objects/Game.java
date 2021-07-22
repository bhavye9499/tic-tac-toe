package game_objects;

import enums.ConsoleColors;
import enums.Move;
import enums.Symbol;
import io.ClientHandler;
import io.Response;
import constants.Constant;

import java.util.*;

public class Game implements Runnable {

    private static int idCtr;

    private final int id;
    private final Board board;
    private ArrayList<Player> players;
    private Symbol turn;

    static {
        Game.idCtr = 1;
    }

    public Game (ArrayList<ClientHandler> clientHandlers) {
        this.id = Game.idCtr++;
        this.board = new Board();
        this.players = new ArrayList<>();
        this.createPlayers(clientHandlers);
        this.turn = Symbol.CROSS;

    }

    private void createPlayers(ArrayList<ClientHandler> clientHandlers) {
        this.players.add(new Player(Symbol.CROSS, clientHandlers.get(0)));
        this.players.add(new Player(Symbol.NOUGHT, clientHandlers.get(1)));
    }

    private void exitAllPlayers() {
        for (Player player : this.players) {
            player.getClientHandler().sendResponseToClient(new Response("over", false));
        }
    }

    private boolean isValidMove(Move move) {
        return move != null && this.board.isPositionEmpty(move.getPosition());
    }

    private void flipTurn() {
        this.turn = (this.turn == Symbol.CROSS) ? Symbol.NOUGHT : Symbol.CROSS;
    }

    private void gameLoop() {
        Player turnPlayer;
        while (true) {
            turnPlayer = this.getTurnPlayer();
            for (Player player : players) {
                if (!player.equals(turnPlayer)) {
                    this.printBoardState(player);
                    this.printWaitingMessage(player, "Awaiting for other player to make their move... \n");
                }
            }
            Move move = this.takeInputMove();
            this.makeMove(move);
            if (this.board.checkWinningState(this.turn) || this.board.checkDrawState()) {
                break;
            }
            this.flipTurn();
        }
    }

    private String getResult(Player player) {
        StringBuilder result = new StringBuilder();
        if (this.board.checkDrawState()) {
            result.append(String.format("Game %d :: It's a Draw! \n", this.id));
        }
        else {
            result.append(this.getWinningLosingResult(player));
        }
        return ConsoleColors.YELLOW.getCode() + result.toString() + ConsoleColors.RESET.getCode();
    }

    private String getWinningLosingResult(Player player) {
        if (player.equals(this.getTurnPlayer())) {
            return String.format("Game %d :: Congratulations! You Won! \n", this.id);
        }
        else {
            return String.format("Game %d :: Sorry! You Lost! \n", this.id);
        }
    }

    private Player getTurnPlayer() {
        return (this.turn == Symbol.CROSS) ? this.players.get(0) : this.players.get(1);
    }

    private void makeMove(Move move) {
        this.board.fillPosition(move.getPosition(), this.turn);
    }

    private void printBoardState(Player player) {
        StringBuilder boardState = this.board.getBoardState();
        Response response = new Response(Constant.CLEAR_SCREEN + boardState.toString(), false);
        player.getClientHandler().sendResponseToClient(response);
    }

    private void printMovePrompt() {
        Player turnPlayer = this.getTurnPlayer();
        String movePrompt = String.format("Game %d :: Your turn (%s): ", this.id, turnPlayer.getSymbol().name());
        Response response = new Response(movePrompt, true);
        turnPlayer.getClientHandler().sendResponseToClient(response);
    }

    private void printNamePrompt(Player player) {
        String namePrompt = String.format("Enter your name (%s %s): ", player.getSymbol().name(), player.getSymbol());
        Response response = new Response(namePrompt, true);
        player.getClientHandler().sendResponseToClient(response);
    }

    private void printWaitingMessage(Player player, String message) {
        Response response = new Response(message, false);
        player.getClientHandler().sendResponseToClient(response);
    }

    private void printExitScreen() {
        for (Player player : this.players) {
            this.printBoardState(player);
            player.getClientHandler().sendResponseToClient(new Response(this.getResult(player), false));
        }
    }

    @Override
    public void run() {
        this.runGame();
    }

    public void runGame() {
        ArrayList<String> playerNames = this.takePlayerNames();
        this.updatePlayerNames(playerNames);
        this.gameLoop();
        this.printExitScreen();
        this.exitAllPlayers();
        System.out.println("A game completed...");
    }

    private Move takeInputMove() {
        Player turnPlayer = this.getTurnPlayer();
        Move move = null;
        do {
            this.printBoardState(turnPlayer);
            this.printMovePrompt();
            Response response = turnPlayer.getClientHandler().receiveResponseFromClient();
            String strMove = response.getMessage().toUpperCase();
            move = Move.stringToMove(strMove);
        } while (!this.isValidMove(move));
        return move;
    }

    private ArrayList<String> takePlayerNames() {
        ArrayList<String> playerNames = new ArrayList<>();
        for (int i = 1; i < this.players.size(); i++) {
            this.printWaitingMessage(players.get(i), "Awaiting for other player to enter their name... \n");
        }
        for (int i = 0; i < this.players.size(); i++) {
            this.printNamePrompt(players.get(i));
            Response response = players.get(i).getClientHandler().receiveResponseFromClient();
            String playerName = response.getMessage();
            playerNames.add(playerName);
            if (i < this.players.size() - 1) {
                this.printWaitingMessage(players.get(i), "Awaiting for other player to enter their name... \n");
            }
        }
        return playerNames;
    }

    private void updatePlayerNames(ArrayList<String> playerNames) {
        for (Player player : this.players) {
            player.setName(playerNames.get(0));
        }
    }

}
