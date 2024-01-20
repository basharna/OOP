import java.util.*;

public abstract class ConcretePiece implements Piece{
    protected Player owner;
    private final ArrayList<Position> history;
    private static final Map<Position, Set<ConcretePiece>> piecesOnSquare = new HashMap<>();
    private static final ArrayList<ConcretePiece> attackerPieces = new ArrayList<>();
    private static final ArrayList<ConcretePiece> defenderPieces = new ArrayList<>();

    private int id;
    private int distance = 0;

    protected ConcretePiece() {
        history = new ArrayList<>();
    }

    @Override
    public Player getOwner() {
        return this.owner;
    }

    public void updatePosition(Position p){
        calculateDist(p);
        this.history.add(p);
        updatePiecesOnSquare(p);
    }

    private void calculateDist(Position p) {
        if (!history.isEmpty()) {
            Position previousPosition = history.get(history.size() - 1);

            int deltaX = p.getX() - previousPosition.getX();
            int deltaY = p.getY() - previousPosition.getY();

            int distance = (int) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            this.distance += distance;
        }
    }

    public ArrayList<Position> getHistory(){
        return this.history;
    }

    @Override
    public abstract String getType();

    public int getId() {
        return this.id;
    }

    public void setId(int x) {
        this.id = x;
    }

    public static ArrayList<ConcretePiece> getPieces(ConcretePlayer player){
        return player.isPlayerOne() ? defenderPieces : attackerPieces;
    }

    public static void addPieceToArray(ConcretePiece piece){
        if (piece.getOwner().isPlayerOne()){
            defenderPieces.add(piece);
        }else {
            attackerPieces.add(piece);
        }
    }

    public static void resetLists() {
        attackerPieces.clear();
        defenderPieces.clear();
        piecesOnSquare.clear();
    }

    public int getDistance() {
        return this.distance;
    }

    public static Map<Position, Set<ConcretePiece>> getPiecesOnSquare() {
        return piecesOnSquare;
    }

    private void updatePiecesOnSquare(Position p) {
        piecesOnSquare.computeIfAbsent(p, k -> new HashSet<>()).add(this);

    }
}