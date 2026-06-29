package chess.movecalc;

import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueenMoveCalc {
    public QueenMoveCalc() {}

    public Collection<ChessMove> calculate(ChessPosition myPosition) {
        Collection<ChessMove> rookMoves = new RookMoveCalc().calculate(myPosition);
        Collection<ChessMove> bishopMoves = new BishopMoveCalc().calculate(myPosition);

        List<ChessMove> moves = new ArrayList<>(rookMoves);
        moves.addAll(bishopMoves);

        return moves;
    }
}
