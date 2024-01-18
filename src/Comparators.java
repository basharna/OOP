import java.util.Comparator;

public class Comparators {
    private static ConcretePlayer winner;

    public static void setWinner(ConcretePlayer winner) {
        Comparators.winner = winner;
    }


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

    public static class KillsComparator implements Comparator<ConcretePiece> {
        @Override
        public int compare(ConcretePiece piece1, ConcretePiece piece2) {
            int kills1 = piece1 instanceof Pawn ? ((Pawn) piece1).getKills() : 0;
            int kills2 = piece2 instanceof Pawn ? ((Pawn) piece2).getKills() : 0;
            boolean p1 = piece1.getOwner() == winner;
            boolean p2 = piece2.getOwner() == winner;
            int id1 = piece1.getId();
            int id2 = piece2.getId();

            if (kills1 == kills2) {
                if(id1 == id2){
                    return Boolean.compare(p2,p1);
                }
                return Integer.compare(id1, id2);
            }
            return  Integer.compare(kills2, kills1);
        }
    }

    public static class DistComparator implements Comparator<ConcretePiece>{
        @Override
        public int compare(ConcretePiece piece1, ConcretePiece piece2) {
            int dist1 = piece1.getDistance();
            int dist2 = piece2.getDistance();
            boolean p1 = piece1.getOwner() == winner;
            boolean p2 = piece2.getOwner() == winner;
            int id1 = piece1.getId();
            int id2 = piece2.getId();

            if (dist1 == dist2) {
                if(id1 == id2){
                    return Boolean.compare(p2,p1);
                }
                return Integer.compare(id1, id2);
            }
            return  Integer.compare(dist2, dist1);
        }
    }
}