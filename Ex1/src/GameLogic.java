import java.util.ArrayList;
import java.util.List;

public class GameLogic implements PlayableLogic {

    public static int BOARD_SIZE = 11;
    private final ConcretePlayer attacker = new ConcretePlayer(true);
    private final ConcretePlayer defender = new ConcretePlayer(false);
    private ConcretePiece[][] board;
    private Position[][] positions;
    private boolean isFinished;
    private boolean attackerTurn = true;

    public GameLogic() {
        this.positions = new Position[BOARD_SIZE][BOARD_SIZE];
        this.reset();
    }
    @Override
    public boolean move(Position a, Position b) {
        int x = a.getX();
        int y = a.getY();
        ConcretePiece temp = this.board[x][y];
        this.board[b.getX()][b.getY()] = temp;
        this.board[x][y] = null;
        return true;
    }

    @Override
    public Piece getPieceAtPosition(Position position) {
        return this.board[position.getX()][position.getY()];
    }

    @Override
    public Player getFirstPlayer() {
        return this.defender;
    }

    @Override
    public Player getSecondPlayer() {
        return this.attacker;
    }

    @Override
    public boolean isGameFinished() {
        return this.isFinished;
    }

    @Override
    public boolean isSecondPlayerTurn() {
        return !this.attackerTurn;
    }

    @Override
    public void reset() {
        this.board = new ConcretePiece[BOARD_SIZE][BOARD_SIZE];
        this.isFinished = false;
        this.attackerTurn = true;

        //set attacker left pieces
        for (int i=3; i<=7; i++){
            this.board[0][i] = new Pawn(attacker);
        }
        this.board[1][5] = new Pawn(attacker);

        //set attacker top pieces
        for (int i=3; i<=7; i++){
            this.board[i][0] = new Pawn(attacker);
        }
        this.board[5][1] = new Pawn(attacker);

        //set attacker right pieces
        for (int i=3; i<=7; i++){
            this.board[10][i] = new Pawn(attacker);
        }
        this.board[9][5] = new Pawn(attacker);

        //set attacker bottom pieces
        for (int i=3; i<=7; i++){
            this.board[i][10] = new Pawn(attacker);
        }
        this.board[5][9] = new Pawn(attacker);

        //set defender pieces
        this.board[5][3] = new Pawn(defender);
        this.board[3][5] = new Pawn(defender);
        this.board[5][7] = new Pawn(defender);
        this.board[7][5] = new Pawn(defender);
        for(int i = 4; i <= 6; i++) {
            for(int j = 4; j <= 6; j++) {
                if (j == 5 && i == 5) {
                    this.board[i][j] = new King(defender);
                } else {
                    this.board[i][j] = new Pawn(defender);
                }
            }
        }

    }

    @Override
    public void undoLastMove() {

    }

    @Override
    public int getBoardSize() {
        return this.BOARD_SIZE;
    }
}
