import java.util.ArrayList;
import java.util.List;

public class ConcretePiece implements Piece{
    Player owner;
    private final List<Position> history = new ArrayList<>();
    @Override
    public Player getOwner() {
        return this.owner;
    }

    public void updatePosition(Position p){
        this.history.add(p);
    }

    @Override
    public String getType() {
        return null;
    }
}
