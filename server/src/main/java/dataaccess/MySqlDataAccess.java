package dataaccess;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.*;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class MySqlDataAccess implements DataAccess {

    public MySqlDataAccess() throws SQLException, DataAccessException {
        configureDatabase();
    }

    @Override
    public void clear() throws DataAccessException {
        update("TRUNCATE TABLE users");
        update("TRUNCATE TABLE auth");
        update("TRUNCATE TABLE games");
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        String statement = "INSERT INTO users (username, password, email) VALUES (\"%s\", \"%s\", \"%s\")"
                .formatted(userData.getUsername(), userData.getPassword(), userData.getEmail());
        System.out.println(statement);
        update(statement);
    }

    private int update(String statement) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
        return 0;
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        String statement = "SELECT username, password, email FROM users WHERE username=\"%s\"".formatted(username);
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        String statement = "INSERT INTO games (gameName, gameData) VALUES (\"%s\", \"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1\")"
                .formatted(gameName);
        System.out.println(statement);
        return update(statement);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        String statement = "SELECT id, whiteUsername, blackUsername, gameName, gameData FROM games WHERE id=\"%d\"".formatted(gameID);
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        GameData game = new GameData(rs.getString("gameName"), rs.getInt("id"));
                        game.setBlackUsername(rs.getString("blackUsername"));
                        game.setWhiteUsername(rs.getString("whiteUsername"));
                        game.setGame(rs.getString("gameData"));
                        return game;
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<GameData> listGames() {
        return null;
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        String statement = "UPDATE games SET whiteUsername=%s, blackUsername=%s, gameData=\"%s\" WHERE id=%d"
                .formatted(gameData.getWhiteUsername(), gameData.getBlackUsername(), decodeFEN(gameData.getGame()), gameData.getGameID());
        update(statement);
    }

    private String decodeFEN(ChessGame game) {
        ChessBoard board = game.getBoard();
        StringBuilder FEN = new StringBuilder();
        int space = 0;
        boolean first = true;
        for (int i = 8; i > 0; i--) {
            if (space > 0) {
                FEN.append(space);
            }
            if (!first) {
                FEN.append('/');
            }
            first = false;
            space = 0;
            for (int j = 1; j < board.squares[0].length + 1; j++) {
                if (board.getPiece(new ChessPosition(i, j)) == null) {
                    space++;
                } else {
                    if (space > 0) {
                        FEN.append("/");
                        FEN.append(space);
                        FEN.append("/");
                        space = 0;
                    }
                    Character piece;
                    switch (board.getPiece(new ChessPosition(i, j)).getPieceType()) {
                        case ROOK -> piece = 'R';
                        case KNIGHT -> piece = 'N';
                        case BISHOP -> piece = 'B';
                        case KING -> piece = 'K';
                        case QUEEN -> piece = 'Q';
                        default -> piece = 'P';
                    }
                    if (board.getPiece(new ChessPosition(i, j)).getTeamColor() == ChessGame.TeamColor.BLACK) {
                        piece = Character.toLowerCase(piece);
                    }
                    FEN.append(piece);
                }
            }
        }
        return FEN.toString();
    }

    @Override
    public void createAuth(AuthData authData) {

    }

    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public void deleteAuth(AuthData authData) {

    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS users (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`),
              INDEX(email)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
            """
            CREATE TABLE IF NOT EXISTS auth (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
            """
            CREATE TABLE IF NOT EXISTS games (
              `id` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256) NOT NULL,
              `gameData` varchar(256) NOT NULL,
              PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Bork", ex);
        }
    }
}
