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
        ChessMove move;
//        int mutRow;
//        int mutCol;
        int[][] moveSet;
        List<ChessMove> moves = new ArrayList<>();

        if (row == 2) {
            moves.add(new ChessMove(
                new ChessPosition(row, col),
                new ChessPosition(row + 2, col),
                null));
        }

        ChessPiece blockPiece = board.getPiece(new ChessPosition(row+1, col));
        if (!(row+1 > 8) && blockPiece == null) {
            if (row == 7) {
                moves.add(new ChessMove(
                        new ChessPosition(row, col),
                        new ChessPosition(row + 1, col),
                        ChessPiece.PieceType.QUEEN));
            } else {
                moves.add(new ChessMove(
                        new ChessPosition(row, col),
                        new ChessPosition(row + 1, col),
                        null));
            }
        }

        int[][] captureSet = {
                { 1, 1},
                { 1,-1}
        };

        for (int[] ints : captureSet) {
            int mutRow = row + ints[0];
            int mutCol = col + ints[1];
            if(mutRow > 7 || mutCol > 7) {
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

//        if (pieceColor == ChessGame.TeamColor.WHITE) {
//            moveSet = new int[][]{
//                    { 1, 0},
//                    { 1, 1},
//                    { 1,-1}
//            };
//
//        } else {
//            moveSet = new int[][]{
//                    {-1, 0},
//                    {-1, 1},
//                    {-1,-1}
//            };
//        }
//
//        for (int[] ints : moveSet) {
//            int mutRow = row + ints[0];
//            int mutCol = col + ints[1];
//        }

//        // Normal Move up
//        if (!(mutRow < 2 || mutRow > 7 || mutCol < 1 || mutCol > 8)) {
//            move = new ChessMove(
//                    new ChessPosition(row, col),
//                    new ChessPosition(mutRow, mutCol),
//                    null);
//            ChessPiece endPiece = board.getPiece(move.getEndPosition());
//            if (endPiece == null) {
//                moves.add(move);
//            }
//        }
//
//        //Premotion
//        if (mutRow == 1 || mutRow == 8) {
//            move = new ChessMove(
//                    new ChessPosition(row, col),
//                    new ChessPosition(mutRow, mutCol),
//                    ChessPiece.PieceType.QUEEN);
//            ChessPiece endPiece = board.getPiece(move.getEndPosition());
//            if (endPiece == null) {
//                moves.add(move);
//            }
//        }
//
//        //Capture Diagnal
//        mutCol = col + 1;
//        // Normal Move up
//        if (!(mutRow < 2 || mutRow > 7 || mutCol < 1 || mutCol > 8)) {
//            move = new ChessMove(
//                    new ChessPosition(row, col),
//                    new ChessPosition(mutRow, mutCol),
//                    null);
//            ChessPiece endPiece = board.getPiece(move.getEndPosition());
//            if (endPiece != null) {
//                moves.add(move);
//            }
//        }
//
//        //Premotion
//        if (mutRow == 1 || mutRow == 8) {
//            move = new ChessMove(
//                    new ChessPosition(row, col),
//                    new ChessPosition(mutRow, mutCol),
//                    ChessPiece.PieceType.QUEEN);
//            ChessPiece endPiece = board.getPiece(move.getEndPosition());
//            if (endPiece != null) {
//                moves.add(move);
//            }
//        }
//
//        //Capture Diagnal
//        mutCol = col - 1;
//        // Normal Move up
//        if (!(mutRow < 2 || mutRow > 7 || mutCol < 1 || mutCol > 8)) {
//            move = new ChessMove(
//                    new ChessPosition(row, col),
//                    new ChessPosition(mutRow, mutCol),
//                    null);
//            ChessPiece endPiece = board.getPiece(move.getEndPosition());
//            if (endPiece != null) {
//                moves.add(move);
//            }
//        }
//
//        //Premotion
//        if (mutRow == 1 || mutRow == 8) {
//            move = new ChessMove(
//                    new ChessPosition(row, col),
//                    new ChessPosition(mutRow, mutCol),
//                    ChessPiece.PieceType.QUEEN);
//            ChessPiece endPiece = board.getPiece(move.getEndPosition());
//            if (endPiece != null) {
//                moves.add(move);
//            }
//        }
//
//
//
//
//        move = new ChessMove(
//                new ChessPosition(row, col),
//                new ChessPosition(mutRow, mutCol),
//                null);

        return moves;
    }
}
