package chess.movecalc;

import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KingMoveCalc {
    public KingMoveCalc() {}

    public Collection<ChessMove> calculate(ChessPosition myPosition) {
        int[][] moveSet = {
                { 1,  0},  // right
                { 1,  1},  // down-right
                { 0,  1},  // down
                {-1,  1},  // down-left
                {-1,  0},  // left
                {-1, -1},  // up-left
                { 0, -1},  // up
                { 1, -1}   // up-right
        };

        List<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        for (int[] ints : moveSet) {
            moves.add(new ChessMove(
                    new ChessPosition(row, col),
                    new ChessPosition(
                            row + ints[0],
                            col + ints[1]),
                    null));
        }

        return moves;
    }
}
