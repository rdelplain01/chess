package chess.movecalc;

import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RookMoveCalc {
    public RookMoveCalc() {}

    public Collection<ChessMove> calculate(ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        List<ChessMove> moves = new ArrayList<>();

        int[][] moveSet = {
                { 1,  0},  // right
                { 0,  1},  // down
                {-1,  0},  // left
                { 0, -1},  // up
        };

        for (int[] ints : moveSet) {
            int mutRow = row;
            int mutCol = col;

            while (!(mutRow <= 1 || mutRow >= 8 || mutCol <= 1 || mutCol >= 8)) {
                mutRow += ints[0];
                mutCol += ints[1];

                moves.add(new ChessMove(
                        new ChessPosition(row, col),
                        new ChessPosition(
                                mutRow,
                                mutCol),
                        null));
            }
        }

        return moves;
    }
}
