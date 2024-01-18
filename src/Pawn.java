public class Pawn extends ConcretePiece{
    private int kills;
    public Pawn(Player owner){
        this.owner = owner;
        this.kills = 0;
        addPieceToArray(this);
    }

    public void incrementKills(){
        this.kills++;
    }

    public void decrementKills(){
        this.kills--;
    }

    public int getKills(){
        return this.kills;
    }
    public String getType() {
        return this.getOwner().isPlayerOne() ? "♙" : "♟";
    }

}
