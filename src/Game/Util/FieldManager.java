package Game.Util;

public class FieldManager {
    public static final int FIELD_SIZE = 3;
    private static String[][] field;

    public FieldManager(String[][] field) {
        FieldManager.field = field;
    }

    public void printField() {
        String s =  """
                   %s| %s |%s
                  --+---+--
                   %s| %s |%s
                  --+---+--
                   %s| %s |%s
                    """;
        s = s.formatted(field[0][0],field[0][1],field[0][2],
                        field[1][0],field[1][1],field[1][2],
                        field[2][0],field[2][1],field[2][2]);
        System.out.println(s);
    }

    public void clearField() {
        for (int i = 0; i < FIELD_SIZE; i++) {
            for (int j = 0; j < FIELD_SIZE; j++) {
                field[i][j] = " ";
            }
        }
    }
}
