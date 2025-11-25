package ca.yorku.eecs3311.othello.model;

public class CountVisitor implements OthelloBoardVisitor {

    private char player;
    private int count;

    public CountVisitor(char player) {
        this.player = player;
        this.count = 0;
    }

    @Override
    public void visit(OthelloBoard board) {
        int dim = board.getDimension();
        for (int row = 0; row < dim; row++) {
            for (int col = 0; col < dim; col++) {
                if (board.get(row, col) == player) {
                    count++;
                }
            }
        }
    }

    public int getCount() {
        return count;
    }
}
