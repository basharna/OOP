public class King extends ConcretePiece{
    public King(Player owner){
        this.owner = owner;
    }
    public String getType() {
        return "♔";
    }
}
