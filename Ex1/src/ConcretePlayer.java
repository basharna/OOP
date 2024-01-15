public class ConcretePlayer implements Player{
    private final boolean isAttacker;
    private int wins;

    public ConcretePlayer(boolean isAttacker) {
        this.isAttacker = isAttacker;
        this.wins = 0;
    }
    @Override
    public boolean isPlayerOne() {
        return !this.isAttacker;
    }

    @Override
    public int getWins() {
        return this.wins;
    }

    public void updateWins(){
        this.wins++;
    }
}
