import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public abstract class ConcretePiece implements Piece{
    protected Player owner;
    private final ArrayList<Position> history;
    private static final ArrayList<ConcretePiece> attackerPieces = new ArrayList<>();
    private static final ArrayList<ConcretePiece> defenderPieces = new ArrayList<>();
    private int id;

    protected ConcretePiece() {
        history = new ArrayList<>();

    }

    @Override
    public Player getOwner() {
        return this.owner;
    }

    public void updatePosition(Position p){
        this.history.add(p);
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
}