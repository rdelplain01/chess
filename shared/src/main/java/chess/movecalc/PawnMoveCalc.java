package chess.movecalc;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnMoveCalc {
    public PawnMoveCalc() {}

    public Collection<ChessMove> calculate(ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        List<ChessMove> moves = new ArrayList<>();

        if (pieceColor == ChessGame.TeamColor.WHITE) {
            moves.add(new ChessMove(
                    new ChessPosition(row, col),
                    new ChessPosition(
                            row+1,
                            col),
                    null));
        } else {
            moves.add(new ChessMove(
                    new ChessPosition(row, col),
                    new ChessPosition(
                            row-1,
                            col),
                    null));
        }



        return moves;
    }
}
