package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;
import model.AuthData;
import model.GameData;
import model.UserData;
import client.ServerFacade;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static ui.EscapeSequences.*;

public class Repl {

    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 8;

    // Padded characters.
    private static final String EMPTY = "   ";

    private static ServerFacade serverFacade;

    public void run(String url) {
        serverFacade = new ServerFacade(url);
        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                System.out.print("Enter your command: ");
                String input = scanner.nextLine();
                String[] command = input.split(" ");
                try {
                    switch (command[0]) {
                        case "register" -> register(command, scanner);
                        case "login" -> login(command, scanner);
                        case "quit" -> running = false;
                        case "help" -> helpPre(false);
                        default -> helpPre(true);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

        }
    }

    private static void register(String[] command, Scanner scanner) {
        if (command.length < 4) {
            helpPre(true);
            return;
        }
        AuthData authData = serverFacade.register(new UserData(command[1], command[2], command[3]));
        System.out.printf("Welcome %s!%n", authData.getUsername());
        postLoginUI(authData);
    }

    private static void login(String[] command, Scanner scanner) {
        if (command.length < 3) {
            helpPre(true);
            return;
        }
        Map<String, String> loginRequest = Map.of(
                "username", command[1],
                "password", command[2]
        );
        AuthData authData = serverFacade.login(loginRequest);
        if (authData != null) {
            System.out.printf("Welcome %s!\n", authData.getUsername());
            postLoginUI(authData);
        }
    }

    private static void postLoginUI(AuthData authData) {
        Scanner scanner = new Scanner(System.in);
        postLoginLoop:
        while (true) {
            System.out.print("Enter your command: ");
            String input = scanner.nextLine();
            String[] command = input.split(" ");
            try {
                switch (command[0]) {
                    case "logout" -> {
                        logout(authData);
                        break postLoginLoop;
                    }
                    case "create" -> create(command, authData);
                    case "list" -> listGames(authData);
                    case "join" -> joinGame(command, authData, scanner);
                    case "observe" -> observeGame(command, authData, scanner);
                    case "help" -> helpPost(false);
                    default -> helpPost(true);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void logout(AuthData authData) {
        serverFacade.logout(authData);
        System.out.println("logged out");
    }

    private static void create(String[] command, AuthData authData) {
        if (command.length < 2) {
            helpPost(true);
            return;
        }
        int gameID = serverFacade.createGame(authData, command[1]);
        System.out.printf("ID: %d    Name: %s \n", gameID, command[1]);
    }

    private static void listGames(AuthData authData) {
        GameData[] gameList = serverFacade.listGames(authData);
        for (var game : gameList) {
            System.out.printf("ID: %d    Name: %s \n", game.getGameID(), game.getGameName());
            System.out.printf("\tWHITE: %s\n",
                    (game.getWhiteUsername() == null) ? "none" : game.getWhiteUsername());
            System.out.printf("\tBLACK: %s\n",
                    (game.getBlackUsername() == null) ? "none" : game.getBlackUsername());
        }
    }

    private static void joinGame(String[] command, AuthData authData, Scanner scanner) {
        if (command.length < 3) {
            helpPost(true);
            return;
        }
        ChessGame game = serverFacade.joinGame(authData, command[2], Integer.parseInt(command[1]));
        playGame(scanner, game, command[2]);
    }

    private static void observeGame(String[] command, AuthData authData, Scanner scanner) {
        if (command.length < 2) {
            helpPost(true);
            return;
        }
        playGame(scanner, new ChessGame(), null);
    }


    private static void helpPre(boolean unknown) {
        if (unknown) {
            System.out.println("Unknown command. Here are the available commands:");
        }
        System.out.println("""
                register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                login <USERNAME> <PASSWORD> - to play chess
                quit - playing chess
                help - with commands
                """);
    }

    private static void helpPost(boolean unknown) {
        if (unknown) {
            System.out.println("Unknown command. Here are the available commands:");
        }
        System.out.println("""
                create <NAME> - a game
                list - games
                join <ID> <WHITE|BLACK> - a game
                observe <ID> - a game
                logout - when you are done
                help - with commands
                """);
    }


    private static void playGame(Scanner scanner, ChessGame game, String color) {
        if (Objects.equals(color, "WHITE")) {
            displayBoard(game, ChessGame.TeamColor.WHITE);
        } else if (Objects.equals(color, "BLACK")) {
            displayBoard(game, ChessGame.TeamColor.BLACK);
        } else {
            displayBoard(game, ChessGame.TeamColor.WHITE);
        }

        if (color != null) {
            System.out.print("What is your move: ");
            String input = scanner.nextLine();
        }
    }

    private static void displayBoard(ChessGame game, ChessGame.TeamColor color) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        printChessBoard(out, game, color);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);

        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
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
            if (!red) {
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
