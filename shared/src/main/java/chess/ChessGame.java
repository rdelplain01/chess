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

    private ChessPosition infiniteMoves(int[] dir, ChessPosition myPosition, TeamColor myColor) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        while (myPosition.moveInbounds(row+dir[0], col+dir[1])) {
            row += dir[0];
            col += dir[1];

            ChessPosition check = new ChessPosition(row, col);
            ChessPiece endPiece = board.getPiece(check);
            if (endPiece != null) {
                if (!(endPiece.getTeamColor().equals(myColor))) {
                    return check;
                }
                if (endPiece.getPieceType() == ChessPiece.PieceType.KING) {
                    continue;
                }
                return null;
            }
        }
        return null;
    }

    private ArrayList<ChessPosition> finiteMove(int[][] moves, ChessPiece.PieceType pieceType, ChessPosition myPosition, TeamColor myColor) {
        ArrayList<ChessPosition> moveList = new ArrayList<>();
        for (var move : moves) {
            if (!myPosition.moveInbounds(myPosition.getRow()+move[0], myPosition.getColumn()+move[1])) { continue; }
            var check = board.getPiece(new ChessPosition(myPosition.getRow()+move[0], myPosition.getColumn()+move[1]));
            if (check != null && check.getTeamColor() != myColor) {
                ChessPiece.PieceType type = check.getPieceType();
                if (type == pieceType) {
                    moveList.add(new ChessPosition(myPosition.getRow()+move[0], myPosition.getColumn()+move[1]));
                }
            }
        }
        return moveList;
    }

    private ArrayList<ChessPosition> pinSet(TeamColor myColor, ChessPosition myPosition) {
        int[][] strait = {
                { 1,  0},
                { 0,  1},
                {-1,  0},
                { 0, -1}
        };
        int[][] knight = {
                { 1, 2},
                { 1,-2},
                { 2, 1},
                {-2, 1},
                {-1, 2},
                {-1,-2},
                { 2,-1},
                {-2,-1}
        };
        int[][] diagonal = {
                { 1,  1},
                { 1, -1},
                {-1,  1},
                {-1, -1}
        };
        int[][] king = {
                { 1,  1},
                { 1, -1},
                {-1,  1},
                {-1, -1},
                { 1,  0},
                { 0,  1},
                {-1,  0},
                { 0, -1}
        };

        int[][] pawn = myColor == TeamColor.WHITE
                ? new int[][] { { 1,  1 }, { 1, -1 } }
                : new int[][] { { -1, 1 }, { -1, -1 } };


        ArrayList<ChessPosition> moveList = new ArrayList<>();
        for (var move : diagonal) {
            var check = infiniteMoves(move, myPosition, myColor);
            if (check != null) {
                ChessPiece.PieceType type = board.getPiece(check).getPieceType();
                if (type == ChessPiece.PieceType.QUEEN || type == ChessPiece.PieceType.BISHOP) {
                    moveList.add(check);
                }
            }
        }
        for (var move : strait) {
            var check = infiniteMoves(move, myPosition, myColor);
            if (check != null) {
                ChessPiece.PieceType type = board.getPiece(check).getPieceType();
                if (type == ChessPiece.PieceType.QUEEN || type == ChessPiece.PieceType.ROOK) {
                    moveList.add(check);
                }
            }
        }
        moveList.addAll(finiteMove(knight, ChessPiece.PieceType.KNIGHT, myPosition, myColor));
        moveList.addAll(finiteMove(king, ChessPiece.PieceType.KING, myPosition, myColor));
        moveList.addAll(finiteMove(pawn, ChessPiece.PieceType.PAWN, myPosition, myColor));
        return moveList;
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        TeamColor myColor = board.getPiece(startPosition).getTeamColor();
        if (isInCheck(myColor)) {
            throw new RuntimeException("Not implemented");
        }

        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> validMovesList = validMoves(move.getStartPosition());
        if (validMovesList.contains(move)) {
            board.movePiece(move, board.getPiece(move.getStartPosition()));
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition myPosition = findKing(teamColor);
        ArrayList<ChessPosition> moveList = pinSet(teamColor, myPosition);
        return !moveList.isEmpty();
    }

    public boolean isPositionInCheck(TeamColor teamColor, ChessPosition myPosition) {
        ArrayList<ChessPosition> moveList = pinSet(teamColor, myPosition);
        return !moveList.isEmpty();
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
