package chess.movecalc;

import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopMoveCalc {
    public BishopMoveCalc() {}

    public Collection<ChessMove> calculate(ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        List<ChessMove> moves = new ArrayList<>();

        while (row != 1 && col != 1) {
            row--;
            col--;
            moves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(row, col), null));
        }

        row = myPosition.getRow();
        col = myPosition.getColumn();

        while (row != 8 && col != 8) {
            row++;
            col++;
            moves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(row, col), null));
        }

        row = myPosition.getRow();
        col = myPosition.getColumn();

        while (row != 8 && col != 1) {
            row++;
            col--;
            moves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(row, col), null));
        }

        row = myPosition.getRow();
        col = myPosition.getColumn();

        while (row != 1 && col != 8) {
            row--;
            col++;
            moves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(row, col), null));
        }

        return moves;
    }
}
