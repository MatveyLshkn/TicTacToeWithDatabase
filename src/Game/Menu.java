package Game;

import Database.ConnectionManager;
import Database.SQLManager;
import Game.Engine.GameEngine;

import java.sql.Connection;
import java.util.Scanner;

public class Menu {

    private static final Connection connection = ConnectionManager.open();
    private static final SQLManager sqlManager = new SQLManager(connection);
    private static final Scanner scanner = new Scanner(System.in);

    public static void start() {
        System.out.println("\nWelcome to TicTacToe game!\n");
        Player player1, player2;
        while (true) {
            System.out.println("1-st player choose the option: 1 - sign in, 2 - register");
            String option = scanner.nextLine();
            player1 = optionMenu(option);
            if (player1 != null) break;
            else System.out.println("Wrong option");
        }

        while (true) {
            System.out.println("\n2-nd player choose the option: 1 - sign in, 2 - register");
            String option = scanner.nextLine();
            player2 = optionMenu(option);
            if (player2 != null) break;
            else System.out.println("Wrong option!");
        }

        System.out.println("\n\n1-st player name: " + player1.getName() + "\n2-nd player name: " + player2.getName());
        GameEngine game = new GameEngine(player1, player2, connection);

        while (true) {
            game.start();
            System.out.println("\nDo you want to rematch?");
            while (true) {
                System.out.println("1 - yes, 2 - no");
                String option = scanner.nextLine();
                if (option.equals("1")) break;
                else if (option.equals("2")) return;
                else System.out.println("Wrong option!");
            }
        }
    }

    private static Player optionMenu(String option) {
        if (option.equals("1")) {
            while (true) {
                System.out.print("Enter your username -> ");
                String name = scanner.nextLine();
                System.out.print("\nEnter your password -> ");
                String password = scanner.nextLine();

                if (sqlManager.isValidPassword(name, password)) {
                    Player player = new Player(name, sqlManager.findId(name));
                    sqlManager.showStats(player);
                    return player;
                } else System.out.println("Invalid username or password\n");
            }
        } else if (option.equals("2")) {
            while (true) {
                System.out.println("Account registration:");
                System.out.print("Enter your username -> ");
                String name = scanner.nextLine();
                System.out.print("\nEnter your password -> ");
                String password = scanner.nextLine();

                if (sqlManager.createAccount(name, password)) {
                    System.out.println("\nAccount successfully created");
                    return new Player(name, sqlManager.findId(name));
                } else System.out.println("This name already exists\n");
            }
        } else return null;
    }
}
