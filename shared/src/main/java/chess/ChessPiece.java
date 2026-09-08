package chess;

import chess.movecalc.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);

        // Bishop Logic
        if (piece.getPieceType() == PieceType.BISHOP) {
            return new BishopMoveCalc().calculate(myPosition, board, pieceColor);
        }

        // King Logic
        if (piece.getPieceType() == PieceType.KING) {
            return new KingMoveCalc().calculate(myPosition, board, pieceColor);
        }

        // Knight Logic
        if (piece.getPieceType() == PieceType.KNIGHT) {
            return new KnightMoveCalc().calculate(myPosition, board, pieceColor);
        }

        // Queen Logic
        if (piece.getPieceType() == PieceType.QUEEN) {
            return new QueenMoveCalc().calculate(myPosition, board, pieceColor);
        }

        // Rook Logic
        if (piece.getPieceType() == PieceType.ROOK) {
            return new RookMoveCalc().calculate(myPosition, board, pieceColor);
        }

        // Pawn Logic
        if (piece.getPieceType() == PieceType.PAWN) {
            return new PawnMoveCalc().calculate(myPosition, board, pieceColor);
        }

        return List.of();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }
}
