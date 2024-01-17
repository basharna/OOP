import java.util.ArrayList;
import java.util.List;

public class Printer {

    public static void printBoard(ArrayList<ConcretePiece> pieces) {
        // Sort the pieces using the custom comparator
        pieces.sort(new Comparators.HistoryComparator());

        // Iterate over the sorted pieces and print their step history
        for (ConcretePiece piece : pieces) {
            List<Position> stepHistory = piece.getHistory();
            if (!stepHistory.isEmpty()) {
                String ownerLetter = !piece.getOwner().isPlayerOne() ? "A" : piece instanceof King ? "K" : "D";
                System.out.print(ownerLetter + piece.getId() + ": ");

                // Print step history in the format (x, y)
                System.out.print("[");
                for (int i = 0; i < stepHistory.size(); i++) {
                    Position position = stepHistory.get(i);
                    System.out.print("(" + position.getX() + ", " + position.getY() + ")");
                    if (i < stepHistory.size() - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println("]");
            }
        }
    }
}
