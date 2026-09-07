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

        if (pieceColor == ChessGame.TeamColor.WHITE) {
            if (row == 2) {
                ChessPiece endPiece = board.getPiece(new ChessPosition(row + 2, col));
                if (endPiece == null) {
                    moves.add(new ChessMove(
                            new ChessPosition(row, col),
                            new ChessPosition(row + 2, col),
                            null));
                }
            }

            ChessPiece blockPiece = board.getPiece(new ChessPosition(row + 1, col));
            if (!(row + 1 > 8) && blockPiece == null) {
                if (row == 7) {
                    moves.add(new ChessMove(
                            new ChessPosition(row, col),
                            new ChessPosition(row + 1, col),
                            ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(
                            new ChessPosition(row, col),
                            new ChessPosition(row + 1, col),
                            ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(
                            new ChessPosition(row, col),
                            new ChessPosition(row + 1, col),
                            ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(
                            new ChessPosition(row, col),
                            new ChessPosition(row + 1, col),
                            ChessPiece.PieceType.ROOK));
                } else {
                    moves.add(new ChessMove(
                            new ChessPosition(row, col),
                            new ChessPosition(row + 1, col),
                            null));
                }
            }

            int[][] captureSet = {
                    {1, 1},
                    {1, -1}
            };

            for (int[] ints : captureSet) {
                int mutRow = row + ints[0];
                int mutCol = col + ints[1];
                if (mutRow == 8) {
                    ChessPiece endPiece = board.getPiece(new ChessPosition(mutRow, mutCol));
                    if (endPiece != null && endPiece.getTeamColor() != ChessGame.TeamColor.WHITE) {
                        moves.add(new ChessMove(
                                new ChessPosition(row, col),
                                new ChessPosition(mutRow, mutCol),
                                ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(
                                new ChessPosition(row, col),
                                new ChessPosition(mutRow, mutCol),
                                ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(
                                new ChessPosition(row, col),
                                new ChessPosition(mutRow, mutCol),
                                ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(
                                new ChessPosition(row, col),
                                new ChessPosition(mutRow, mutCol),
                                ChessPiece.PieceType.ROOK));
                    }
                }
                if (mutRow > 7 || mutCol > 7) {
                    continue;
                }
                ChessPiece endPiece = board.getPiece(new ChessPosition(mutRow, mutCol));
                if (endPiece != null && endPiece.getTeamColor() != ChessGame.TeamColor.WHITE) {
                    moves.add(new ChessMove(
                            new ChessPosition(row, col),
                            new ChessPosition(mutRow, mutCol),
                            null));
                }
            }
        }

        if (pieceColor == ChessGame.TeamColor.BLACK) {
            if (row == 7) {
                ChessPiece endPiece = board.getPiece(new ChessPosition(row - 2, col));
                ChessPiece endPieceTwo = board.getPiece(new ChessPosition(row - 1, col));
                if (endPiece == null && endPieceTwo == null) {
                    moves.add(new ChessMove(
                            new ChessPosition(row, col),
                            new ChessPosition(row - 2, col),
                            null));
                }
            }

            ChessPiece blockPiece = board.getPiece(new ChessPosition(row - 1, col));
            if (!(row - 1 < 1) && blockPiece == null) {
                if (row == 2) {
                    moves.add(new ChessMove(
                            new ChessPosition(row, col),
                            new ChessPosition(row - 1, col),
                            ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(
                            new ChessPosition(row, col),
                            new ChessPosition(row - 1, col),
                            ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(
                            new ChessPosition(row, col),
                            new ChessPosition(row - 1, col),
                            ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(
                            new ChessPosition(row, col),
                            new ChessPosition(row - 1, col),
                            ChessPiece.PieceType.ROOK));
                } else {
                    moves.add(new ChessMove(
                            new ChessPosition(row, col),
                            new ChessPosition(row - 1, col),
                            null));
                }
            }

            int[][] captureSet = {
                    {-1, 1},
                    {-1,-1}
            };

            for (int[] ints : captureSet) {
                int mutRow = row + ints[0];
                int mutCol = col + ints[1];
                if (!(myPosition.moveInbounds(mutRow, mutCol))) {
                    continue;
                }
                if (mutRow == 1) {
                    ChessPiece endPiece = board.getPiece(new ChessPosition(mutRow, mutCol));
                    if (endPiece != null && endPiece.getTeamColor() != ChessGame.TeamColor.BLACK) {
                        moves.add(new ChessMove(
                                new ChessPosition(row, col),
                                new ChessPosition(mutRow, mutCol),
                                ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(
                                new ChessPosition(row, col),
                                new ChessPosition(mutRow, mutCol),
                                ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(
                                new ChessPosition(row, col),
                                new ChessPosition(mutRow, mutCol),
                                ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(
                                new ChessPosition(row, col),
                                new ChessPosition(mutRow, mutCol),
                                ChessPiece.PieceType.ROOK));
                    }
                }
                if (mutRow < 2 || mutCol < 2) {
                    continue;
                }
                ChessPiece endPiece = board.getPiece(new ChessPosition(mutRow, mutCol));
                if (endPiece != null && endPiece.getTeamColor() != ChessGame.TeamColor.BLACK) {
                    moves.add(new ChessMove(
                            new ChessPosition(row, col),
                            new ChessPosition(mutRow, mutCol),
                            null));
                }
            }
        }

        return moves;
    }
}
