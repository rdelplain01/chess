package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;
import model.AuthData;
import model.UserData;
import server.ServerFacade;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {

    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 8;

    // Padded characters.
    private static final String EMPTY = "   ";

    private static ServerFacade serverFacade;

    public static void main(String[] args) {
        serverFacade = new ServerFacade("http://localhost:8080");
        try (Scanner scanner = new Scanner(System.in)) {
            mainLoop:
            while (true) {
                System.out.print("Enter your command: ");
                String input = scanner.nextLine();
                String[] command = input.split(" ");
                switch (command[0]) {
                    case "register": {
                        UserData user = new UserData(command[1], command[2], command[3]);
                        AuthData authData = serverFacade.register(user);
                        if (authData != null) {
                            postLoginUI();
                        }
                        break;
                    }
                    case "login": {
                        Map<String, String> loginRequest = Map.of(
                                "username", command[1],
                                "password", command[2]
                        );
                        AuthData authData = serverFacade.login(loginRequest);
                        if (authData != null) {
                            System.out.printf("Welcome %s!\n", authData.getUsername());
                            postLoginUI();
                        }
                        break;
                    }
                    case "quit": {
                        break mainLoop;
                    }
                    case "help": {
                        System.out.println("""
                                register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                                login <USERNAME> <PASSWORD> - to play chess
                                quit - playing chess
                                help - with commands
                                """);
                        break;
                    }
                }
            }

        }
    }

    private static void postLoginUI() {
        try (Scanner scanner = new Scanner(System.in)) {
            postLoginLoop:
            while (true) {
                System.out.print("Enter your command: ");
                String input = scanner.nextLine();
                String[] command = input.split(" ");
                switch (command[0]) {
                    case "logout": {
                        //
                    }
                    case "create": {
                        //
                    }
                    case "list": {
                        //
                    }
                    case "join": {
//                        var color = ChessGame.TeamColor.WHITE;
//                        displayBoard(scanner, color);
                    }
                    case "quit": {
                        break postLoginLoop;
                    }
                    case "help": {
                        System.out.println("""
                                register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                                login <USERNAME> <PASSWORD> - to play chess
                                quit - playing chess
                                help - with commands
                                """);
                        break;
                    }
                }
            }

        }
    }


    private void playGame() {
        ChessBoard board = new ChessBoard();

        // Do stuff
    }

    private static void displayBoard(Scanner scanner, ChessGame.TeamColor color) {
        ChessGame game = new ChessGame();

        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        printChessBoard(out, game, color);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);

    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setRed(PrintStream out) {
        out.print(SET_BG_COLOR_RED);
        out.print(SET_TEXT_COLOR_RED);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setBlue(PrintStream out) {
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void printPlayer(PrintStream out, String player) {
        out.print(SET_TEXT_COLOR_BLACK);

        out.print(" ");
        out.print(player);
        out.print(" ");
    }

    private static void drawSquare(PrintStream out, ChessBoard board, int row, int col) {
        if (board.getPiece(new ChessPosition(row, col)) == null) {
            printPlayer(out, " ");
            return;
        }
        String letter;
        switch (board.getPiece(new ChessPosition(row, col)).getPieceType()) {
            case PAWN -> letter = "p";
            case KING -> letter = "k";
            case QUEEN -> letter = "q";
            case ROOK -> letter = "r";
            case BISHOP -> letter = "b";
            case KNIGHT -> letter = "n";
            default -> letter = " ";
        }

        if (board.getPiece(new ChessPosition(row, col)).getTeamColor() == ChessGame.TeamColor.WHITE) {
            letter = letter.toUpperCase();
        }

        printPlayer(out, letter);
    }

    private static void printCross(PrintStream out, boolean red, int row, ChessGame game) {
        ChessBoard board = game.getBoard();
        int col = 1;
        for (int i = 0; i < BOARD_SIZE_IN_SQUARES / 2; i++) {
            if (red) {
                setRed(out);
                drawSquare(out, board, row, col);
                col++;
                setWhite(out);
                drawSquare(out, board, row, col);
                col++;
            } else {
                setWhite(out);
                drawSquare(out, board, row, col);
                col++;
                setRed(out);
                drawSquare(out, board, row, col);
                col++;
            }
        }
    }

    private static void printChessBoard(PrintStream out, ChessGame game, ChessGame.TeamColor color) {
        List<String> letters = List.of(new String[]{"a", "b", "c", "d", "e", "f", "g", "h"});
        List<String> revLetters = List.of(new String[]{"h", "g", "f", "e", "d", "c", "g", "h"});

        // Header
        setBlue(out);
        out.print(EMPTY.repeat(1));

        boolean red = (color == ChessGame.TeamColor.WHITE);
        if (red) {
            for (var letter : letters) {
                printPlayer(out, letter);
            }

            setBlue(out);
            out.print(EMPTY.repeat(1));
            setBlack(out);
            out.println();

            for (int i = BOARD_SIZE_IN_SQUARES; i > 0; i--) {
                printLines(out, red, i, game);
                red = !red;
            }

            setBlue(out);
            out.print(EMPTY.repeat(1));

            for (var letter : letters) {
                printPlayer(out, letter);
            }
        } else {
            for (var letter : revLetters) {
                printPlayer(out, letter);
            }

            setBlue(out);
            out.print(EMPTY.repeat(1));
            setBlack(out);
            out.println();

            for (int i = 1; i < BOARD_SIZE_IN_SQUARES + 1; i++) {
                printLines(out, red, i, game);
                red = !red;
            }

            setBlue(out);
            out.print(EMPTY.repeat(1));

            for (var letter : revLetters) {
                printPlayer(out, letter);
            }
        }

        setBlue(out);
        out.print(EMPTY.repeat(1));
        setBlack(out);
        out.println();
    }

    private static void printLines(PrintStream out, boolean red, int i, ChessGame game) {
        setBlue(out);
        printPlayer(out, "%d".formatted(i));

        printCross(out, red, i, game);

        setBlue(out);
        printPlayer(out, "%d".formatted(i));

        setBlack(out);
        out.println();
    }
}
