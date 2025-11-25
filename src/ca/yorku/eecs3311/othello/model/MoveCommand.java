package ca.yorku.eecs3311.othello.model;

public class MoveCommand implements Command {

    private Othello othello;
    private int row;
    private int col;

    private OthelloBoard beforeBoard;
    private char beforeWhosTurn;
    private int beforeNumMoves;

    public MoveCommand(Othello othello, int row, int col) {
        this.othello = othello;
        this.row = row;
        this.col = col;
        this.beforeBoard = othello.copyBoard();
        this.beforeWhosTurn = othello.getWhosTurn();
        this.beforeNumMoves = othello.getNumMoves();
    }

    @Override
    public boolean execute() {
        return othello.move(row, col);
    }

    @Override
    public void undo() {
        othello.restoreState(beforeBoard.copy(), beforeWhosTurn, beforeNumMoves);
    }
}
