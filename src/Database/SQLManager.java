package Database;

import Game.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLManager {

    private static Connection connection;

    public SQLManager(Connection connection) {
        SQLManager.connection = connection;
    }

    public boolean isValidPassword(String username, String password) {
        String sql = """
                SELECT password
                FROM player
                WHERE name = ?;
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();
            result.next();
            String expectedPassword = result.getString("password");
            return expectedPassword.equals(password);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createAccount(String username, String password) {
        String sql = """
                INSERT INTO player(id, name, password) VALUES (COALESCE((SELECT max(id)+1 FROM player),1),?,?);
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public int findId(String name) {
        String sql = """
                SELECT id
                FROM player
                WHERE name = ?;
                """;

        int id = Integer.MIN_VALUE;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            ResultSet result = statement.executeQuery();
            result.next();
            id = result.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public void showStats(Player player) {
        String sql = """
                SELECT (
                           SELECT count(*)--games count
                           FROM game
                           WHERE ? IN (player1_id, player2_id)) AS games,
                       (
                           SELECT count(*)--wins count
                           FROM game
                           WHERE ? IN (player1_id, player2_id) AND result = ?) AS wins,
                       (
                           SELECT count(*)--losses count
                           FROM game
                           WHERE ? IN (player1_id, player2_id)AND NOT result = -1 AND NOT result = ?) AS losses,
                       (
                           SELECT count(*)--draws count
                           FROM game
                           WHERE ? IN (player1_id, player2_id) AND result = -1) AS draws;
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, player.getId());
            statement.setInt(2, player.getId());
            statement.setInt(3, player.getId());
            statement.setInt(4, player.getId());
            statement.setInt(5, player.getId());
            statement.setInt(6, player.getId());
            ResultSet result = statement.executeQuery();
            result.next();
            int games = result.getInt("games");
            int wins = result.getInt("wins");
            int losses = result.getInt("losses");
            int draws = result.getInt("draws");
            System.out.printf("%s-s stats: win rate: %d%%, games: %d, wins: %d, losses: %d, draws: %d\n", player.getName(), (100 * wins / games), games, wins, losses, draws);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param result If draw -1 otherwise winners id
     */
    public void insertGameResult(int result, int player1_id, int player2_id) {
        String sql = """
                INSERT INTO game (id, player1_id, player2_id, date, result)
                VALUES (COALESCE((SELECT max(id)+1 FROM game), 1), ?, ?, current_timestamp, ?);
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, player1_id);
            statement.setInt(2, player2_id);
            statement.setInt(3, result);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
