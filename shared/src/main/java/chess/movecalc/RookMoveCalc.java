package chess.movecalc;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RookMoveCalc {
    public RookMoveCalc() {}

    public Collection<ChessMove> calculate(ChessPosition myPosition, ChessBoard board, ChessGame.TeamColor pieceColor) {
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

            while (myPosition.moveInbounds(mutRow+ints[0], mutCol+ints[1])) {
                mutRow += ints[0];
                mutCol += ints[1];

                ChessPiece endPiece = board.getPiece(new ChessPosition(mutRow, mutCol));
                if (endPiece != null) {
                    if (!(endPiece.getTeamColor().equals(pieceColor))) {
                        moves.add(new ChessMove(
                                new ChessPosition(row, col),
                                new ChessPosition(
                                        mutRow,
                                        mutCol),
                                null));
                    }
                    break;
                }

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
