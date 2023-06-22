package Game.Util;

import static Game.Util.FieldManager.FIELD_SIZE;

public class WinnerChecker {
    private static String[][] field;
    private static String cell;


    public WinnerChecker(String[][] field) {
        WinnerChecker.field = field;
    }

    public boolean checkWinner(String cell) {
        WinnerChecker.cell = cell;
        boolean result;
        for (int i = 0; i < FIELD_SIZE; i++) {
            result = checkRow(i);
            if (result) return true;
            result = checkColumn(i);
            if (result) return true;
        }
        result = checkDiagonals();
        return result;
    }

    private static boolean checkRow(int row) {
        boolean result = true;
        for (int i = 0; i < FIELD_SIZE; i++) {
            if (!field[row][i].equals(cell)) return false;
        }
        return result;
    }

    private static boolean checkColumn(int column) {
        boolean result = true;
        for (int i = 0; i < FIELD_SIZE; i++) {
            if (!field[i][column].equals(cell)) return false;
        }
        return result;
    }

    private static boolean checkDiagonals() {
        boolean diagonal1 = true;
        for (int i = 0; i < FIELD_SIZE; i++) {
            if (!field[i][i].equals(cell)) {
                diagonal1 = false;
                break;
            }
        }
        boolean diagonal2 = true;
        for (int i = 0; i < FIELD_SIZE; i++) {
            if (!field[i][FIELD_SIZE - i - 1].equals(cell)) {
                diagonal2 = false;
                break;
            }
        }
        return diagonal1 || diagonal2;
    }
}
