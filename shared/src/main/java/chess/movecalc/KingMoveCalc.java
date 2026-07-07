package chess.movecalc;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KingMoveCalc {
    public KingMoveCalc() {}

    public Collection<ChessMove> calculate(ChessPosition myPosition, ChessBoard board, ChessGame.TeamColor pieceColor) {
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
            if (!myPosition.moveInbounds(row + ints[0], col + ints[1])) { continue; }
            moves.add(new ChessMove(
                    new ChessPosition(row, col),
                    new ChessPosition(
                            row + ints[0],
                            col + ints[1]),
                    null));
        }

        List<ChessMove> validMoves = new ArrayList<>();
        for (ChessMove move : moves) {
            ChessPiece endPiece = board.getPiece(move.getEndPosition());
            if (endPiece == null || endPiece.getTeamColor() != pieceColor) {
                validMoves.add(move);
            }
        }

        return validMoves;
    }
}
