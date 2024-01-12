public class Pawn extends ConcretePiece{
    private int kills;
    public Pawn(Player owner){
        this.owner = owner;
        this.kills = 0;
    }

    public void updateKills(){
        this.kills++;
    }

    public int getKills(){
        return this.kills;
    }
    public String getType() {
        return this.getOwner().isPlayerOne() ? "♙" : "♟";
    }

}
