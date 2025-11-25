package ca.yorku.eecs3311.othello.viewcontroller;

import ca.yorku.eecs3311.othello.model.Move;
import ca.yorku.eecs3311.othello.model.Othello;
import ca.yorku.eecs3311.othello.model.OthelloBoard;
import ca.yorku.eecs3311.othello.model.Player;
import ca.yorku.eecs3311.othello.model.MoveCommand;
import ca.yorku.eecs3311.util.Observer;
import ca.yorku.eecs3311.util.Observable;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.Stack;

public class BoardView extends GridPane implements Observer {

    private Othello othello;
    private Button[][] buttons;

    private Image imgX;
    private Image imgO;
    private Label statusLabel;

    private Player p1Strategy;
    private Player p2Strategy;

    private Stack<MoveCommand> undoStack = new Stack<>();
    private Stack<MoveCommand> redoStack = new Stack<>();

    public BoardView(Othello othello, Label statusLabel, Player p1Strategy, Player p2Strategy) {
        this.othello = othello;
        this.statusLabel = statusLabel;
        this.p1Strategy = p1Strategy;
        this.p2Strategy = p2Strategy;

        int dim = Othello.DIMENSION;
        this.setPadding(new Insets(10));
        this.setHgap(2);
        this.setVgap(2);

        this.imgX = new Image(
            "https://www.citypng.com/public/uploads/preview/transparent-handdrawn-doodle-red-x-close-icon-701751695038240ksyy51nddw.png"
        );

        this.imgO = new Image(
            "https://pngimg.com/uploads/letter_o/letter_o_PNG105.png"
        );

        buttons = new Button[dim][dim];

        for (int r = 0; r < dim; r++) {
            for (int c = 0; c < dim; c++) {
                Button b = new Button();
                b.setPrefSize(50, 50);

                int row = r;
                int col = c;

                b.setOnAction(e -> handleClick(row, col));

                buttons[r][c] = b;
                this.add(b, c, r);
            }
        }

        refresh();
    }

    private boolean executeMove(int row, int col) {
        MoveCommand cmd = new MoveCommand(othello, row, col);
        if (cmd.execute()) {
            undoStack.push(cmd);
            redoStack.clear();
            return true;
        }
        return false;
    }

    private void handleClick(int row, int col) {
        if (othello.isGameOver()) return;

        char turn = othello.getWhosTurn();

        if (turn == OthelloBoard.P1 && p1Strategy == null) {
            if (executeMove(row, col)) {
                runAIMoves();
            }
        } else if (turn == OthelloBoard.P2 && p2Strategy == null) {
            if (executeMove(row, col)) {
                runAIMoves();
            }
        }
    }

    private void runAIMoves() {
        while (!othello.isGameOver()) {
            char turn = othello.getWhosTurn();
            Player strategy = null;
            if (turn == OthelloBoard.P1) {
                strategy = p1Strategy;
            } else if (turn == OthelloBoard.P2) {
                strategy = p2Strategy;
            }

            if (strategy == null) {
                break;
            }

            Move m = strategy.getMove();
            if (m == null) break;
            if (!executeMove(m.getRow(), m.getCol())) break;
        }
    }

    public void undoLastMove() {
        if (undoStack.isEmpty()) return;
        MoveCommand cmd = undoStack.pop();
        cmd.undo();
        redoStack.push(cmd);
    }

    public void redoLastMove() {
        if (redoStack.isEmpty()) return;
        MoveCommand cmd = redoStack.pop();
        if (cmd.execute()) {
            undoStack.push(cmd);
        }
    }

    @Override
    public void update(Observable o) {
        refresh();
    }

    private void refresh() {
        int dim = Othello.DIMENSION;

        for (int r = 0; r < dim; r++) {
            for (int c = 0; c < dim; c++) {
                char token = othello.getToken(r, c);

                if (token == OthelloBoard.P1) {
                    ImageView view = new ImageView(imgX);
                    view.setFitWidth(40);
                    view.setFitHeight(40);
                    view.setPreserveRatio(true);
                    buttons[r][c].setGraphic(view);
                    buttons[r][c].setText("");
                } else if (token == OthelloBoard.P2) {
                    ImageView view = new ImageView(imgO);
                    view.setFitWidth(40);
                    view.setFitHeight(40);
                    view.setPreserveRatio(true);
                    buttons[r][c].setGraphic(view);
                    buttons[r][c].setText("");
                } else {
                    buttons[r][c].setGraphic(null);
                    buttons[r][c].setText("");
                }
            }
        }

        updateStatus();
    }

    private void updateStatus() {
        if (!othello.isGameOver()) {
            char turn = othello.getWhosTurn();
            if (turn == OthelloBoard.P1) {
                statusLabel.setText("Turn: X");
            } else if (turn == OthelloBoard.P2) {
                statusLabel.setText("Turn: O");
            } else {
                statusLabel.setText("");
            }
        } else {
            int p1 = othello.getCount(OthelloBoard.P1);
            int p2 = othello.getCount(OthelloBoard.P2);
            if (p1 > p2) {
                statusLabel.setText("Game over: X wins (" + p1 + " – " + p2 + ")");
            } else if (p2 > p1) {
                statusLabel.setText("Game over: O wins (" + p2 + " – " + p1 + ")");
            } else {
                statusLabel.setText("Game over: tie (" + p1 + " – " + p2 + ")");
            }
        }
    }
}
