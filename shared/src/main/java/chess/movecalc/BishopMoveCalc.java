package chess.movecalc;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopMoveCalc {
    public BishopMoveCalc() {}

    public List<ChessMove> calculate(ChessPosition myPosition, ChessBoard board, ChessGame.TeamColor pieceColor) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        List<ChessMove> moves = new ArrayList<>();

        while (row != 1 && col != 1) {
            row--;
            col--;
            ChessPiece piece = board.getPiece(new ChessPosition(row, col));
            if (piece != null) {
                if (piece.getTeamColor() != pieceColor) {
                    moves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(row, col), null));
                }
                break;
            }
            moves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(row, col), null));
        }

        row = myPosition.getRow();
        col = myPosition.getColumn();

        while (row != 8 && col != 8) {
            row++;
            col++;
            ChessPiece piece = board.getPiece(new ChessPosition(row, col));
            if (piece != null) {
                if (piece.getTeamColor() != pieceColor) {
                    moves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(row, col), null));
                }
                break;
            }
            moves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(row, col), null));
        }

        row = myPosition.getRow();
        col = myPosition.getColumn();

        while (row != 8 && col != 1) {
            row++;
            col--;
            ChessPiece piece = board.getPiece(new ChessPosition(row, col));
            if (piece != null) {
                if (piece.getTeamColor() != pieceColor) {
                    moves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(row, col), null));
                }
                break;
            }
            moves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(row, col), null));
        }

        row = myPosition.getRow();
        col = myPosition.getColumn();

        while (row != 1 && col != 8) {
            row--;
            col++;
            ChessPiece piece = board.getPiece(new ChessPosition(row, col));
            if (piece != null) {
                if (piece.getTeamColor() != pieceColor) {
                    moves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(row, col), null));
                }
                break;
            }
            moves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(row, col), null));
        }

        return moves;
    }
}
