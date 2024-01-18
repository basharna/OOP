import java.util.ArrayList;
import java.util.List;

public class Printer {

    public static void printHistory(ArrayList<ConcretePiece> pieces) {
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

    public static void printKills(ArrayList<ConcretePiece> pieces, ConcretePlayer player){
        Comparators.setWinner(player);

        pieces.sort(new Comparators.KillsComparator());

        for (ConcretePiece piece : pieces){
            if (piece instanceof Pawn) {
                int kills = ((Pawn)piece).getKills();
                if (kills != 0){
                    String ownerLetter = piece.getOwner().isPlayerOne() ? "D" : "A";
                    System.out.print(ownerLetter + piece.getId() + ": ");
                    System.out.println(kills + " kills");
                }
            }
        }

        //print separator
        for (int i = 0; i < 75; i++) {
            System.out.print("*");
        }
        System.out.println();
    }

    public static void printDist(ArrayList<ConcretePiece> pieces, ConcretePlayer player){
        Comparators.setWinner(player);

        pieces.sort(new Comparators.DistComparator());

        for (ConcretePiece piece : pieces){
                int dist = piece.getDistance();
                if (dist != 0){
                    String ownerLetter = !piece.getOwner().isPlayerOne() ? "A" : piece instanceof King ? "K" : "D";
                    System.out.print(ownerLetter + piece.getId() + ": ");
                    System.out.println(dist + " squares");
                }
        }

        //print separator
        for (int i = 0; i < 75; i++) {
            System.out.print("*");
        }
        System.out.println();
    }
}
