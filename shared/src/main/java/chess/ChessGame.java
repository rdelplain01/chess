package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor turn = TeamColor.WHITE;
    private ChessBoard board = new ChessBoard();
    public ChessGame() {
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ArrayList<ChessPosition> moveList = new ArrayList<>();
        ChessPosition king = new ChessPosition(-1,-1);
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition check = new ChessPosition(i, j);
                if (board.getPiece(check) != null) {
                    if (board.getPiece(check).getPieceType() == ChessPiece.PieceType.KING && board.getPiece(check).getTeamColor() == teamColor) {
                        king = check;
                    }
                    if (board.getPiece(check).getTeamColor() != teamColor) {
                        for (var move : board.getPiece(check).pieceMoves(board, check)) {
                            moveList.add(move.getEndPosition());
                        }
                    }
                }
            }
        }
        return moveList.contains(king);
    }

    public boolean isPositionInCheck(TeamColor teamColor, ChessPosition myPosition) {
        ArrayList<ChessPosition> moveList = new ArrayList<>();
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition check = new ChessPosition(i, j);
                if (board.getPiece(check) != null) {
                    if (board.getPiece(check).getTeamColor() != teamColor) {
                        if (board.getPiece(check).getPieceType() == ChessPiece.PieceType.PAWN) {
                            int dir = -1;
                            if (teamColor == TeamColor.BLACK) { dir = 1; }
                            moveList.add(new ChessPosition(check.getRow()+dir, check.getColumn()+1));
                            moveList.add(new ChessPosition(check.getRow()+dir, check.getColumn()-1));
                        } else {
                            for (var move : board.getPiece(check).pieceMoves(board, check)) {
                                moveList.add(move.getEndPosition());
                            }
                        }
                    }
                }
            }
        }
        return moveList.contains(myPosition);
    }

    private ChessPosition findKing(TeamColor teamColor) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition check = new ChessPosition(i, j);
                if (board.getPiece(check) != null) {
                    if (board.getPiece(check).getPieceType() == ChessPiece.PieceType.KING && board.getPiece(check).getTeamColor() == teamColor) {
                        return check;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ChessPosition kingPosition = findKing(teamColor);
        Collection<ChessMove> kingMoves = board.getPiece(kingPosition).pieceMoves(board, kingPosition);
        if (!isInCheck(teamColor)) {
            return false;
        }
        for (var move : kingMoves) {
            if (!isPositionInCheck(teamColor, move.getEndPosition())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
