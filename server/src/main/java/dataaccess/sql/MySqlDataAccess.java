package dataaccess.sql;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
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
            throw new DataAccessException("ERROR: " + e.getMessage());
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
            throw new DataAccessException("ERROR: " + e.getMessage());
        }
        return null;
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        String statement = "INSERT INTO games (gameName, gameData) VALUES (\"%s\", \"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1\")"
                .formatted(gameName);
        return update(statement);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        String statement = "SELECT id, whiteUsername, blackUsername, gameName, gameData FROM games WHERE id=\"%d\"".formatted(gameID);
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGames(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("ERROR: " + e.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        String statement = "SELECT id, whiteUsername, blackUsername, gameName, gameData FROM games";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                try (ResultSet rs = ps.executeQuery()) {
                    ArrayList<GameData> games = new ArrayList<>();
                    while (rs.next()) {
                        games.add(readGames(rs));
                    }
                    return games;
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("ERROR: " + e.getMessage());
        }
    }

    private GameData readGames(ResultSet rs) throws SQLException {
        GameData game = new GameData(rs.getString("gameName"), rs.getInt("id"));
        game.setBlackUsername(rs.getString("blackUsername"));
        game.setWhiteUsername(rs.getString("whiteUsername"));
        game.setGame(rs.getString("gameData"));
        return game;
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        String statement = "UPDATE games SET whiteUsername=%s, blackUsername=%s, gameData=\"%s\" WHERE id=%d"
                .formatted(
                        updateString(gameData.getWhiteUsername()),
                        updateString(gameData.getBlackUsername()),
                        decodeFEN(gameData.getGame()),
                        gameData.getGameID()
                );
        update(statement);
    }

    private String updateString(String s) {
        if (s == null) {
            return null;
        } else {
            return "\"" + s + "\"";
        }
    }

    private String decodeFEN(ChessGame game) {
        ChessBoard board = game.getBoard();
        StringBuilder fen = new StringBuilder();
        int space = 0;
        boolean first = true;
        for (int i = 8; i > 0; i--) {
            if (space > 0) {
                fen.append(space);
            }
            if (!first) {
                fen.append('/');
            }
            first = false;
            space = 0;
            for (int j = 1; j < board.squares[0].length + 1; j++) {
                if (board.getPiece(new ChessPosition(i, j)) == null) {
                    space++;
                } else {
                    if (space > 0) {
                        fen.append("/");
                        fen.append(space);
                        fen.append("/");
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
                    fen.append(piece);
                }
            }
        }
        return fen.toString();
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        String statement = "INSERT INTO auth (username, authToken) VALUES (\"%s\", \"%s\")"
                .formatted(authData.getUsername(), authData.getAuthToken());
        update(statement);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        String statement = "SELECT authToken, username FROM auth WHERE authToken=\"%s\""
                .formatted(authToken);
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new AuthData(rs.getString("username"), rs.getString("authToken"));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("ERROR: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteAuth(AuthData authData) throws DataAccessException {
        update("DELETE FROM auth WHERE authToken=\"%s\"".formatted(authData.getAuthToken()));
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
            throw new DataAccessException("ERROR: " + ex.getMessage(), ex);
        }
    }
}
