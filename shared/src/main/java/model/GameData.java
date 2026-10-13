package model;

import chess.ChessGame;

public class GameData {
    int gameID;
    String whiteUsername;
    String blackUsername;
    String gameName;
    ChessGame game;

    public GameData(String gameName, int gameID) {
        this.gameName = gameName;
        this.gameID = gameID;
        game = new ChessGame();
    }

    public int getGameID() {
        return gameID;
    }
}
