package ca.yorku.eecs3311.othello.model;

public class HasMoveVisitor implements OthelloBoardVisitor {

    private char result = OthelloBoard.EMPTY;

    @Override
    public void visit(OthelloBoard board) {
        int dim = board.getDimension();

        for (int row = 0; row < dim; row++) {
            for (int col = 0; col < dim; col++) {
                for (int drow = -1; drow <= 1; drow++) {
                    for (int dcol = -1; dcol <= 1; dcol++) {
                        if (drow == 0 && dcol == 0) {
                            continue;
                        }
                        char p = hasMove(board, row, col, drow, dcol);
                        if (p == OthelloBoard.P1 && result == OthelloBoard.P2) {
                            result = OthelloBoard.BOTH;
                            return;
                        }
                        if (p == OthelloBoard.P2 && result == OthelloBoard.P1) {
                            result = OthelloBoard.BOTH;
                            return;
                        }
                        if (result == OthelloBoard.EMPTY) {
                            result = p;
                        }
                    }
                }
            }
        }
    }

    private boolean validCoordinate(OthelloBoard board, int row, int col) {
        int dim = board.getDimension();
        return 0 <= row && row < dim && 0 <= col && col < dim;
    }

    private char alternation(OthelloBoard board, int row, int col, int drow, int dcol) {
        if (drow == 0 && dcol == 0) return OthelloBoard.EMPTY;
        char firstToken = board.get(row, col);
        while (true) {
            row += drow;
            col += dcol;
            if (!validCoordinate(board, row, col)) return OthelloBoard.EMPTY;
            char nextToken = board.get(row, col);
            if (nextToken != OthelloBoard.P1 && nextToken != OthelloBoard.P2) return OthelloBoard.EMPTY;
            if (nextToken == OthelloBoard.otherPlayer(firstToken)) return nextToken;
        }
    }

    private char hasMove(OthelloBoard board, int row, int col, int drow, int dcol) {
        if (!validCoordinate(board, row, col) || board.get(row, col) != OthelloBoard.EMPTY) {
            return OthelloBoard.EMPTY;
        }
        return alternation(board, row + drow, col + dcol, drow, dcol);
    }

    public char getResult() {
        return result;
    }
}
