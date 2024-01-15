import java.util.Stack;

public class GameLogic implements PlayableLogic {

    public static int BOARD_SIZE = 11;
    private final ConcretePlayer attacker, defender;
    private ConcretePiece[][] board;
    private Stack<ConcretePiece[][]> boardHistory;
    private boolean isFinished;
    private boolean attackerTurn = true;
    private int attackerKilled;

    public GameLogic() {
        this.boardHistory = new Stack<>();
        this.attacker = new ConcretePlayer(true);
        this.defender = new ConcretePlayer(false);
        this.reset();
    }

    @Override
    public boolean move(Position a, Position b) {
        Piece curr = board[a.getX()][a.getY()];
        Player opponentPlayer = attackerTurn ? defender : attacker;
        if (curr.getOwner() == opponentPlayer || isIllegal(a, b)) {
            return false;
        }
        saveGameState();
        int x = a.getX();
        int y = a.getY();
        ConcretePiece temp = this.board[x][y];
        this.board[b.getX()][b.getY()] = temp;
        temp.updatePosition(b);
        this.board[x][y] = null;
        checkMove(b);
        attackerTurn = !attackerTurn;
        return true;
    }

    private void checkMove(Position b) {
        if (isCorner(b)) {
            winGame(defender);
        } else {
            int x = b.getX();
            int y = b.getY();

            if (board[x][y] instanceof Pawn) {

                //check left side
                if (x != 0 && checkAdjacentPieces(x, y, -1, 0)) {
                    if (board[x - 1][y] instanceof King) {
                        isKingSurrounded(x - 1, y);
                    } else {
                        if (this.board[x - 1][y].getOwner() == attacker) {
                            this.attackerKilled++;
                        }
                        this.board[x - 1][y] = null;
                        ((Pawn)this.board[x][y]).updateKills();
                    }
                }

                //check right side
                if (x != BOARD_SIZE - 1 && checkAdjacentPieces(x, y, 1, 0)) {
                    if (board[x + 1][y] instanceof King) {
                        isKingSurrounded(x + 1, y);
                    } else {
                        if (this.board[x + 1][y].getOwner() == attacker) {
                            this.attackerKilled++;
                        }
                        this.board[x + 1][y] = null;
                        ((Pawn) this.board[x][y]).updateKills();

                    }
                }

                //check top side
                if (y != 0 && checkAdjacentPieces(x, y, 0, -1)) {
                    if (board[x][y - 1] instanceof King) {
                        isKingSurrounded(x, y - 1);
                    } else {
                        if (this.board[x][y - 1].getOwner() == attacker) {
                            this.attackerKilled++;
                        }
                        this.board[x][y - 1] = null;
                        ((Pawn) this.board[x][y]).updateKills();
                    }
                }

                //check bottom side
                if (y != BOARD_SIZE - 1 && checkAdjacentPieces(x, y, 0, 1)) {
                    if (board[x][y + 1] instanceof King) {
                        isKingSurrounded(x, y + 1);
                    } else {
                        if (this.board[x][y + 1].getOwner() == attacker) {
                            this.attackerKilled++;
                        }
                        this.board[x][y + 1] = null;
                        ((Pawn) this.board[x][y]).updateKills();
                    }
                }
            }
            if (attackerKilled == 24) {
                winGame(defender);
            }
        }
    }

    private void winGame(ConcretePlayer player) {
        this.isFinished = true;
        player.updateWins();
    }

    private boolean checkAdjacentPieces(int currentX, int currentY, int deltaX, int deltaY) {
        ConcretePiece currentPiece = board[currentX][currentY];

        // Check the first adjacent piece
        int firstAdjacentX = currentX + deltaX;
        int firstAdjacentY = currentY + deltaY;
        ConcretePiece firstAdjacentPiece = board[firstAdjacentX][firstAdjacentY];

        if (firstAdjacentPiece == null || firstAdjacentPiece.getOwner().isPlayerOne() == currentPiece.getOwner().isPlayerOne()) {
            return false;
        }

        // Check the second adjacent piece
        ConcretePiece secondAdjacentPiece = null;
        int secondAdjacentX = firstAdjacentX + deltaX;
        int secondAdjacentY = firstAdjacentY + deltaY;
        if (secondAdjacentX >= 0 && secondAdjacentX < BOARD_SIZE && secondAdjacentY >= 0 && secondAdjacentY < BOARD_SIZE) {
            secondAdjacentPiece = board[secondAdjacentX][secondAdjacentY];
        }

        // Check conditions for both adjacent pieces
        return (isEdge(firstAdjacentX, firstAdjacentY)) ||
                (secondAdjacentPiece != null && secondAdjacentPiece.getOwner().isPlayerOne() == currentPiece.getOwner().isPlayerOne()) ||
                (isCorner(new Position(secondAdjacentX, currentY)));
    }

    private boolean isEdge(int x, int y) {
        return x == 0 || x == BOARD_SIZE - 1 || y == 0 || y == BOARD_SIZE - 1;
    }

    private void isKingSurrounded(int x, int y) {
        boolean atEdge = isEdge(x, y);

        int surroundingCount = 0;

        // Check left side
        if (x > 0 && board[x - 1][y] != null && board[x - 1][y].getOwner() == attacker) {
            surroundingCount++;
        }

        // Check right side
        if (x < BOARD_SIZE - 1 && board[x + 1][y] != null && board[x + 1][y].getOwner() == attacker) {
            surroundingCount++;
        }

        // Check top side
        if (y > 0 && board[x][y - 1] != null && board[x][y - 1].getOwner() == attacker) {
            surroundingCount++;
        }

        // Check bottom side
        if (y < BOARD_SIZE - 1 && board[x][y + 1] != null && board[x][y + 1].getOwner() == attacker) {
            surroundingCount++;
        }

        // Check if surrounded on all four sides or on three sides if at the edge
        if ((surroundingCount == 4) || (atEdge && surroundingCount == 3)) {
            winGame(attacker);
        }


    }

    private boolean isIllegal(Position a, Position b) {
        int aX = a.getX();
        int aY = a.getY();
        int bX = b.getX();
        int bY = b.getY();

        // Check if moving diagonally
        if (aX != bX && aY != bY) {
            return true;
        }

        if (board[bX][bY] != null || (this.isCorner(b) && this.board[a.getX()][a.getY()] instanceof Pawn)) {
            return true;
        } else {
            int min;
            int max;
            if (a.getX() == b.getX()) {
                min = Math.min(aY, bY);
                max = Math.max(aY, bY);
                for (int i = min + 1; i < max; i++) {
                    if (board[aX][i] != null) {
                        return true;
                    }
                }
            } else {
                min = Math.min(aX, bX);
                max = Math.max(aX, bX);
                for (int i = min + 1; i < max; i++) {
                    if (board[i][aY] != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isCorner(Position b) {
        int x = b.getX();
        int y = b.getY();

        boolean isTopLeft = (x == 0 && y == 0);
        boolean isTopRight = (x == 10 && y == 0);
        boolean isBottomLeft = (x == 0 && y == 10);
        boolean isBottomRight = (x == 10 && y == 10);

        return isTopLeft || isTopRight || isBottomLeft || isBottomRight;
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
        return this.attackerTurn;
    }

    @Override
    public void reset() {
        this.board = new ConcretePiece[BOARD_SIZE][BOARD_SIZE];
        this.isFinished = false;
        this.attackerTurn = true;
        this.attackerKilled = 0;
        this.boardHistory = new Stack<>();

        //set attacker pieces
        for (int i=3;i<=7;i++) {
            this.board[0][i] = new Pawn(attacker);
            this.board[i][10] = new Pawn(attacker);
            this.board[i][0] = new Pawn(attacker);
            this.board[10][i] = new Pawn(attacker);
        }
        this.board[1][5]= new Pawn(attacker);
        this.board[5][1]= new Pawn(attacker);
        this.board[5][9]= new Pawn(attacker);
        this.board[9][5]= new Pawn(attacker);

        //set the defender pieces
        this.board[3][5]= new Pawn(defender);
        this.board[7][5]= new Pawn(defender);

        for (int i=4;i<=6;i++){
            this.board[4][i]= new Pawn(defender);
        }
        for (int i=3;i<=7;i++){
            if (i != 5) {
                this.board[5][i] = new Pawn(defender);
            }
        }
        for (int i=4;i<=6;i++){
            this.board[6][i]= new Pawn(defender);
        }
        this.board[5][5]= new King(defender);

    }

    @Override
    public void undoLastMove() {
        if (!boardHistory.isEmpty()) {
            // Pop the previous state from the move history
            // Restore the board and game state to the previous state
            this.board = boardHistory.pop();

            // Toggle the turn back
            attackerTurn = !attackerTurn;

            System.out.println("Undoing last move");
        } else {
            System.out.println("No moves to undo");
        }
    }

    // Save the current state to the move history
    private void saveGameState() {
        ConcretePiece[][] currentState = new ConcretePiece[BOARD_SIZE][BOARD_SIZE];

        // Deep copy the current board state
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.arraycopy(board[i], 0, currentState[i], 0, BOARD_SIZE);
        }

        // Push the current state onto the move history
        boardHistory.push(currentState);
    }

    @Override
    public int getBoardSize() {
        return BOARD_SIZE;
    }
}
