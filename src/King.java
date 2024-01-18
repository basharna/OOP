public class King extends ConcretePiece{
    public King(Player owner){
        this.owner = owner;
        addPieceToArray(this);
    }
    public String getType() {
        return "♔";
    }
}
