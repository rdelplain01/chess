package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    public ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {

    }
     public ChessBoard(ChessBoard board) {
        this.squares = board.squares;
     }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    public void movePiece(ChessMove move, ChessPiece piece) {
        squares[move.getStartPosition().getRow()-1][move.getStartPosition().getColumn()-1] = null;
        squares[move.getEndPosition().getRow()-1][move.getEndPosition().getColumn()-1] = piece;
    }

    public void fenBoard(String fen) {
        ChessGame.TeamColor color;
        ChessPiece.PieceType type = ChessPiece.PieceType.PAWN;
        int i = 8;
        int j = 1;
        boolean breakFor = false;
        for (int g = 0; g < fen.length(); g++) {
            char place = fen.charAt(g);
            switch (Character.toUpperCase(place)) {
                case 'R' -> type = ChessPiece.PieceType.ROOK;
                case 'N' -> type = ChessPiece.PieceType.KNIGHT;
                case 'B' -> type = ChessPiece.PieceType.BISHOP;
                case 'K' -> type = ChessPiece.PieceType.KING;
                case 'Q' -> type = ChessPiece.PieceType.QUEEN;
                case 'P' -> type = ChessPiece.PieceType.PAWN;
                case '/' -> { i--; j=1; continue;}
                case ' ' -> breakFor = true;
                default -> {
                    j += Character.getNumericValue(place);
                    continue;
                }
            }
            if (breakFor) {
                break;
            }
            if (Character.isUpperCase(place)) {
                color = ChessGame.TeamColor.WHITE;
            } else {
                color = ChessGame.TeamColor.BLACK;
            }
            addPiece(new ChessPosition(i,j), new ChessPiece(color, type));
            j++;
        }
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        fenBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        StringBuilder board = new StringBuilder();
        for (int i = 8; i > 0; i--) {
            board.append('\n');
            for (int j = 1; j < squares[0].length+1; j++) {
                if (getPiece(new ChessPosition(i, j)) == null) {
                    board.append(" ");
                } else {
                    Character piece;
                    switch (getPiece(new ChessPosition(i, j)).getPieceType()) {
                        case ROOK -> piece = 'R';
                        case KNIGHT -> piece = 'N';
                        case BISHOP -> piece = 'B';
                        case KING -> piece = 'K';
                        case QUEEN -> piece = 'Q';
                        default -> piece = 'P';
                    }
                    if (getPiece(new ChessPosition(i, j)).getTeamColor() == ChessGame.TeamColor.BLACK) {
                        piece = Character.toLowerCase(piece);
                    }
                    board.append(piece);
                }
                board.append('|');
            }
        }
        return board.toString();
    }
}
