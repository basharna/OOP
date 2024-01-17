import java.util.Comparator;

public class Comparators {
    public static class HistoryComparator implements Comparator<ConcretePiece> {
        @Override
        public int compare(ConcretePiece piece1, ConcretePiece piece2) {
            // Compare the number of steps taken
            int steps1 = piece1.getHistory().size();
            int steps2 = piece2.getHistory().size();

            // If the number of steps is the same, compare by piece number
            if (steps1 == steps2) {
                return Integer.compare(piece1.getId(), piece2.getId());
            }

            // Sort in ascending order of steps
            return Integer.compare(steps1, steps2);
        }
    }

    public static class PieceComparator implements Comparator<ConcretePiece> {
        @Override
        public int compare(ConcretePiece piece1, ConcretePiece piece2) {
            return 0;
        }
    }
}