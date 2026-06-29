package chess.movecalc;

import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KnightMoveCalc {
    public KnightMoveCalc() {}

    public Collection<ChessMove> calculate(ChessPosition myPosition) {
        int[][] moveSet = {
                { 1, 2},
                { 1,-2},
                { 2, 1},
                {-2, 1},
                {-1, 2},
                {-1,-2},
                { 2,-1},
                {-2,-1}
        };

        List<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        for (int[] ints : moveSet) {
            int mutRow = row + ints[0];
            int mutCol = col + ints[1];

            if (mutRow < 1 || mutRow > 8 || mutCol < 1 || mutCol > 8) {
                continue;
            }

            moves.add(new ChessMove(
                    new ChessPosition(row, col),
                    new ChessPosition(
                            mutRow,
                            mutCol),
                    null));
        }

        return moves;
    }
}
