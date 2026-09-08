package chess.movecalc;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnMoveCalc {
    public PawnMoveCalc() {}

    public Collection<ChessMove> calculate(ChessPosition myPosition, ChessBoard board, ChessGame.TeamColor pieceColor) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        List<ChessMove> moves = new ArrayList<>();

        int beginRow;
        int endRow;
        int direction;

        if (pieceColor == ChessGame.TeamColor.WHITE) {
            beginRow = 2;
            endRow = 8;
            direction = 1;
        } else {
            beginRow = 7;
            endRow = 1;
            direction = -1;
        }

        // Double Move
        if (row == beginRow) {
            int mutRow = row + direction*2;
            if ((board.getPiece(new ChessPosition(mutRow, col)) == null) && (board.getPiece(new ChessPosition(row+direction, col)) == null)) {
                moves.add(new ChessMove(myPosition, new ChessPosition(mutRow, col), null));
            }
        }

        // Standard Forward Move
        if (myPosition.moveInbounds(row+direction, col)) {
            ChessPosition endPosition = new ChessPosition(row+direction, col);
            if (board.getPiece(endPosition) == null) {
                // Premotion
                if (endPosition.getRow() == endRow) {
                    moves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.KNIGHT));
                } else {
                    moves.add(new ChessMove(myPosition, endPosition, null));
                }
            }
        }

        // Capture Moves
        int[] captureSet = {1,-1};
        for (var ints : captureSet) {
            if (myPosition.moveInbounds(row+direction, col+ints)) {
                ChessPosition endPosition = new ChessPosition(row+direction, col+ints);

                if (board.getPiece(endPosition) != null && pieceColor != board.getPiece(endPosition).getTeamColor()) {
                    // Premotion
                    if (endPosition.getRow() == endRow) {
                        moves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.KNIGHT));
                    } else {
                        moves.add(new ChessMove(myPosition, endPosition, null));
                    }
                }
            }
        }

        return moves;
    }
}
