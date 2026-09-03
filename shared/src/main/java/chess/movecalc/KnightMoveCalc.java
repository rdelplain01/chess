package chess.movecalc;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KnightMoveCalc {
    public KnightMoveCalc() {}

    public Collection<ChessMove> calculate(ChessPosition myPosition, ChessBoard board, ChessGame.TeamColor pieceColor) {
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
            ChessMove move = new ChessMove(
                    new ChessPosition(row, col),
                    new ChessPosition(
                            mutRow,
                            mutCol),
                    null);
            ChessPiece endPiece = board.getPiece(move.getEndPosition());
            if (endPiece == null || endPiece.getTeamColor() != pieceColor) {
                moves.add(move);
            }
        }

        return moves;
    }
}
