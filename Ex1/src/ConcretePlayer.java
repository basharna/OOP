public class ConcretePlayer implements Player{
    private boolean isAttacker;
    int wins;

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
}
