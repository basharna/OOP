public class MoveSnapshot {
    private final ConcretePiece[][] boardState;
    private ConcretePiece lastKillingPawn;

    public MoveSnapshot(ConcretePiece[][] boardState) {
        this.boardState = boardState;
    }

    public ConcretePiece[][] getBoardState() {
        return boardState;
    }

    public ConcretePiece getLastKillingPawn() {
        return lastKillingPawn;
    }

    public void setLastKillingPawn(ConcretePiece piece){
        this.lastKillingPawn = piece;
    }
}
