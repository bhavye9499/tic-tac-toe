package enums;

public enum Symbol {

    NONE(' '),
    CROSS('X'),
    NOUGHT('O');

    private final char symbol;

    Symbol(char symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString () {
        return Character.toString(this.symbol);
    }

}