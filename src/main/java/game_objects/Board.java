package game_objects;

import enums.Symbol;

import java.util.Arrays;

public class Board {

    private final Symbol[][] state;

    public Board () {
        this.state = new Symbol[3][3];
        for (Symbol[] symbols : this.state) {
            Arrays.fill(symbols, Symbol.NONE);
        }
    }

    public boolean checkDrawState() {
        boolean flag = true;
        for (Symbol[] row : this.state) {
            for (Symbol symbol : row) {
                flag = (flag && symbol != Symbol.NONE);
            }
        }
        return flag;
    }

    public boolean checkWinningState(Symbol symbol) {
        boolean flag = false;
        for (int i = 0; i < this.state.length; i++) {
            flag = (flag || this.isRowWinning(i, symbol) || this.isColumnWinning(i, symbol));
        }
        flag = (flag || this.isDiagonalWinning(symbol));
        return flag;
    }

    private boolean isColumnWinning(int col, Symbol symbol) {
        boolean flag = true;
        for (Symbol[] symbols : this.state) {
            flag = (flag && symbols[col] == symbol);
        }
        return flag;
    }

    private boolean isDiagonalWinning(Symbol symbol) {
        boolean flag = true;
        for (int i = 0; i < this.state.length; i++) {
            flag = (flag && this.state[i][i] == symbol);
        }
        if (!flag) {
            flag = true;
            for (int i = 0; i < this.state.length; i++) {
                flag = (flag && this.state[this.state.length - i - 1][i] == symbol);
            }
        }
        return flag;
    }

    public boolean isPositionEmpty(int[] position) {
        return this.state[position[0]][position[1]] == Symbol.NONE;
    }

    private boolean isRowWinning(int row, Symbol symbol) {
        boolean flag = true;
        for (int j = 0; j < this.state[0].length; j++) {
            flag = (flag && this.state[row][j] == symbol);
        }
        return flag;
    }

    public StringBuilder getBoardState() {
        StringBuilder boardState = new StringBuilder();
        boardState.append("======== TIC TAC TOE ======== \n");
        for (Symbol[] symbols : this.state) {
            boardState.append("\t+---+---+---+ \n");
            boardState.append("\t");
            for (int j = 0; j < this.state[0].length; j++) {
                boardState.append(String.format("| %s ", symbols[j]));
            }
            boardState.append("| \n");
        }
        boardState.append("\t+---+---+---+ \n");
        boardState.append("============================= \n");
        return boardState;
    }

    public void fillPosition(int[] position, Symbol symbol) {
        this.state[position[0]][position[1]] = symbol;
    }

}
