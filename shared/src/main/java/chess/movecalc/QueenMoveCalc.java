package chess.movecalc;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueenMoveCalc {
    public QueenMoveCalc() {}

    public Collection<ChessMove> calculate(ChessPosition myPosition, ChessBoard board, ChessGame.TeamColor pieceColor) {
        // Take Bishop and Rook moves and Combined for Queen
        Collection<ChessMove> rookMoves = new RookMoveCalc().calculate(myPosition, board, pieceColor);
        Collection<ChessMove> bishopMoves = new BishopMoveCalc().calculate(myPosition, board, pieceColor);

        List<ChessMove> moves = new ArrayList<>(rookMoves);
        moves.addAll(bishopMoves);

        return moves;
    }
}
