package Game.Engine;

import Database.SQLManager;
import Game.Player;
import Game.Util.FieldManager;
import Game.Util.WinnerChecker;

import java.sql.Connection;
import java.util.Scanner;

public class GameEngine {
    private static String[][] field = {{" ", " ", " "},
            {" ", " ", " "},
            {" ", " ", " "}};
    private static final Scanner scanner = new Scanner(System.in);
    private static int x;
    private static int y;
    private static Player player1;
    private static Player player2;
    private static SQLManager sqlManager;

    public GameEngine(Player player1, Player player2, Connection connection) {
        GameEngine.player1 = player1;
        GameEngine.player2 = player2;
        sqlManager = new SQLManager(connection);
    }

    public void start() {
        System.out.println(player1.getName() + " will use \"x\", " + player2.getName() + " will use \"o\"");
        System.out.println("Let the show begin!\n");

        WinnerChecker winnerChecker = new WinnerChecker(field);
        FieldManager fieldManager = new FieldManager(field);
        boolean result;

        for (int i = 1; i <= 9; ) {

            fieldManager.printField();
            String cell = i % 2 != 0 ? "x" : "o";
            Player player = i % 2 != 0 ? player1 : player2;

            System.out.print(player.getName() + " is choosing -> ");
            result = scanVariables();
            System.out.println();

            if (result) {
                field[x - 1][y - 1] = cell;
                if (winnerChecker.checkWinner(cell)) {
                    fieldManager.printField();
                    System.out.println(player.getName() + " won!");
                    sqlManager.insertGameResult(player.getId(), player1.getId(), player2.getId());
                    break;
                }
            } else i--;

            i++;
            if (i > 9) {
                fieldManager.printField();
                System.out.println("Draw!");
                sqlManager.insertGameResult(-1, player1.getId(), player2.getId());
            }
        }
        fieldManager.clearField();
    }

    private static boolean scanVariables() {
        String[] s = scanner.nextLine().split("\s++");
        try {
            x = Integer.parseInt(s[0]);
            y = Integer.parseInt(s[1]);
        } catch (NumberFormatException e) {
            System.out.println("Incorrect input");
            return false;
        }
        return isValidInput();
    }

    private static boolean isValidInput() {
        if (x < 1 || x > 3 || y < 1 || y > 3) {
            System.out.println("Incorrect input!");
            return false;
        }
        if (!field[x - 1][y - 1].equals(" ")) {
            System.out.println("Oops, this cell is busy");
            return false;
        }
        return true;
    }
}
