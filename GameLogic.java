import java.util.ArrayList;
import java.util.Stack;


public class GameLogic implements PlayableLogic {

    public static int BOARD_SIZE = 11;
    private final ConcretePlayer attacker, defender;
    private ConcretePiece[][] board;
    private Stack<MoveSnapshot> boardHistory;
    private boolean isFinished;
    private boolean attackerTurn = true;
    private int attackerKilled;
    private ConcretePiece lastKillingPawn;

    //Constructor
    public GameLogic() {
        this.attacker = new ConcretePlayer(true);
        this.defender = new ConcretePlayer(false);
        setNewBoard();
    }

    @Override
    public boolean move(Position a, Position b) {
        int x = a.getX();
        int y = a.getY();
        ConcretePiece curr = board[x][y];
        if (curr.getHistory().isEmpty()){
            curr.updatePosition(a);
        }
        Player opponentPlayer = attackerTurn ? defender : attacker;
        boolean isIllegal = isIllegal(a, b);
        if (curr.getOwner() == opponentPlayer || isIllegal) {
            return false;
        }
        saveGameState();
        this.board[b.getX()][b.getY()] = curr;
        curr.updatePosition(b);
        this.board[x][y] = null;
        checkMove(b);
        boardHistory.peek().setLastKillingPawn(lastKillingPawn);
        attackerTurn = !attackerTurn;
        return true;
    }

    //checks whether a piece should be captured or win game
    private void checkMove(Position b) {
        int x = b.getX();
        int y = b.getY();
        if (isCorner(x, y)) {
            winGame(defender);
        } else {


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
                        ((Pawn)this.board[x][y]).incrementKills();
                        lastKillingPawn = this.board[x][y];
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
                        ((Pawn) this.board[x][y]).incrementKills();
                        lastKillingPawn = this.board[x][y];

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
                        ((Pawn) this.board[x][y]).incrementKills();
                        lastKillingPawn = this.board[x][y];
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
                        ((Pawn) this.board[x][y]).incrementKills();
                        lastKillingPawn = this.board[x][y];
                    }
                }
            }
            if (attackerKilled == 24) {
                winGame(defender);
            }
        }
    }

    private void winGame(ConcretePlayer player) {
        printStats(player);
        this.isFinished = true;
        player.updateWins();
    }

    private void printStats(ConcretePlayer winner) {
        ConcretePlayer loser = winner.isPlayerOne() ? attacker : defender;

        //print pieces history
        Printer.printHistory(ConcretePiece.getPieces(winner));
        Printer.printHistory(ConcretePiece.getPieces(loser));

        //print separator
        for (int i = 0; i < 75; i++) {
            System.out.print("*");
        }
        System.out.println();

        //print pieces kills
        ArrayList<ConcretePiece> mergedPieces = new ArrayList<>(ConcretePiece.getPieces(attacker));
        mergedPieces.addAll(ConcretePiece.getPieces(defender));
        Printer.printKills(mergedPieces, winner);

        //print pieces distance
        Printer.printDist(mergedPieces, winner);

        //print positions
        Printer.printPositions(ConcretePiece.getPiecesOnSquare());

    }

    private boolean checkAdjacentPieces(int currentX, int currentY, int deltaX, int deltaY) {
        ConcretePiece currentPiece = board[currentX][currentY];

        // Check the first adjacent piece
        int firstAdjacentX = currentX + deltaX;
        int firstAdjacentY = currentY + deltaY;
        ConcretePiece firstAdjacentPiece = board[firstAdjacentX][firstAdjacentY];

        if (firstAdjacentPiece == null || firstAdjacentPiece.getOwner() == currentPiece.getOwner()) {
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
        return (isEdge(firstAdjacentX, firstAdjacentY) && !isEdge(currentX, currentY)) ||
                (secondAdjacentPiece != null && secondAdjacentPiece.getOwner() == currentPiece.getOwner()) ||
                (isCorner(secondAdjacentX, secondAdjacentY));
    }

    //checks if a position is on the edge of the board
    private boolean isEdge(int x, int y) {
        return x == 0 || x == BOARD_SIZE - 1 || y == 0 || y == BOARD_SIZE - 1;
    }

    private void isKingSurrounded(int x, int y) {
        boolean atEdge = isEdge(x, y);

        int surroundingCount = 0;

        // Check left side
        if (x != 0 && board[x - 1][y] != null && board[x - 1][y].getOwner() == attacker) {
            surroundingCount++;
        }

        // Check right side
        if (x != 10 && board[x + 1][y] != null && board[x + 1][y].getOwner() == attacker) {
            surroundingCount++;
        }

        // Check top side
        if (y !=  0 && board[x][y - 1] != null && board[x][y - 1].getOwner() == attacker) {
            surroundingCount++;
        }

        // Check bottom side
        if (y != 10 && board[x][y + 1] != null && board[x][y + 1].getOwner() == attacker) {
            surroundingCount++;
        }

        // Check if surrounded on all four sides or on three sides if at the edge
        if ((surroundingCount == 4) || (atEdge && surroundingCount == 3)) {
            winGame(attacker);
        }


    }

    //checks whether a move is illegal
    private boolean isIllegal(Position a, Position b) {
        int aX = a.getX();
        int aY = a.getY();
        int bX = b.getX();
        int bY = b.getY();

        // Check if moving diagonally
        if (aX != bX && aY != bY) {
            return true;
        }

        if (board[bX][bY] != null || (this.isCorner(bX, bY) && this.board[a.getX()][a.getY()] instanceof Pawn)) {
            return true;
        } else {
            //check obstacle
            if (a.getX() == b.getX()) {
                for (int i = Math.min(aY, bY) + 1; i < Math.max(aY, bY); i++) {
                    if (board[aX][i] != null) {
                        return true;
                    }
                }
            } else {
                for (int i = Math.min(aX, bX) + 1; i < Math.max(aX, bX); i++) {
                    if (board[i][aY] != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //checks if a position is in the corner of the board
    private boolean isCorner(int x, int y) {

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
        setNewBoard();
    }

    private void setNewBoard() {
        this.board = new ConcretePiece[BOARD_SIZE][BOARD_SIZE];
        ConcretePiece.resetLists();
        this.isFinished = false;
        this.attackerTurn = true;
        this.attackerKilled = 0;
        this.boardHistory = new Stack<>();

        //set attacker pieces
        for (int i = 3; i <= 7; i++) {
            this.board[0][i] = new Pawn(attacker);
            this.board[i][10] = new Pawn(attacker);
            this.board[i][0] = new Pawn(attacker);
            this.board[10][i] = new Pawn(attacker);
        }
        this.board[1][5] = new Pawn(attacker);
        this.board[5][1] = new Pawn(attacker);
        this.board[5][9] = new Pawn(attacker);
        this.board[9][5] = new Pawn(attacker);

        //set the defender pieces
        this.board[3][5] = new Pawn(defender);
        this.board[7][5] = new Pawn(defender);

        for (int i = 4; i <= 6; i++) {
            this.board[4][i] = new Pawn(defender);
            this.board[6][i] = new Pawn(defender);
        }
        for (int i = 3; i <= 7; i++) {
            if (i != 5) {
                this.board[5][i] = new Pawn(defender);
            }
        }
        this.board[5][5] = new King(defender);

        //set each Piece id
        int attackerId = 1, defenderId = 1;
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[0].length; j++){
                ConcretePiece curr = this.board[j][i];
                if (curr != null) {
                    if (curr.getOwner() == attacker) {
                        curr.setId(attackerId);
                        attackerId++;
                    }else {
                        curr.setId(defenderId);
                        defenderId++;
                    }
                }
            }
        }
    }

    @Override
    public void undoLastMove() {
        if (!boardHistory.isEmpty()) {
            // Pop the previous state from the move history
            MoveSnapshot snapshot = boardHistory.pop();
            ConcretePiece[][] previousState = snapshot.getBoardState();
            ConcretePiece lastKillingPawn = snapshot.getLastKillingPawn();

            if (lastKillingPawn != null) {
                ((Pawn)lastKillingPawn).decrementKills();
            }

            this.board = previousState;

            // Toggle the turn back
            attackerTurn = !attackerTurn;
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
        boardHistory.push(new MoveSnapshot(currentState));
    }

    @Override
    public int getBoardSize() {
        return BOARD_SIZE;
    }
}
