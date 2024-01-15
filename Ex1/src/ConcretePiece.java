import java.util.ArrayList;
import java.util.List;

public abstract class ConcretePiece implements Piece{
    protected Player owner;
    private final List<Position> history;

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

    @Override
    public abstract String getType();
}