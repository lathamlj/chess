package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        //throw new RuntimeException("Not implemented");
        return pieceColor;
    }

//    public ChessBoard.isValidPosition checkIsValidPosition() {
//        //throw new RuntimeException("Not implemented");
//        return isValidPosition();
//    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        //throw new RuntimeException("Not implemented");
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new HashSet<>();

//        if (type == PieceType.PAWN) {
//            int direction = (pieceColor == ChessGame.TeamColor.WHITE) ? 1 : -1;
//            int newRow = myPosition.getRow() + direction;
//
//            // Check if the new position is within the board boundaries
//            if (isValidPosition(newRow, myPosition.getColumn())) {
//                // Check if the position is empty
//                if (board.getPiece(new ChessPosition(newRow, myPosition.getColumn())) == null) {
//                    //validMoves.add(new ChessMove(myPosition, new ChessPosition(newRow, myPosition.getColumn())));
//                }
//
//                // Add logic for capturing opponents diagonally
//                // (You need to implement the actual logic based on the rules for each piece)
//            }
//        }

        if (type == PieceType.BISHOP) {

            // Diagonal moves: starting position to top-right
            for (int i = 1; isValidPosition(myPosition.getRow() + i, myPosition.getColumn() + i); i++) {
                ChessPosition newPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i);
                ChessPiece occupyingPiece = board.getPiece(newPosition);

                if (occupyingPiece != null) {
                    // Bishop cannot move beyond a square occupied by an opponent
                    if (occupyingPiece.getTeamColor() != getTeamColor()) {
                        // Bishop can take the opponent on that square
                        validMoves.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break; // Bishop cannot move to a square occupied by its own team
                }

                validMoves.add(new ChessMove(myPosition, newPosition, null));
            }

            // Diagonal moves: starting position to top-left
            for (int i = 1; isValidPosition(myPosition.getRow() + i, myPosition.getColumn() - i); i++) {
                ChessPosition newPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i);
                ChessPiece occupyingPiece = board.getPiece(newPosition);

                if (occupyingPiece != null) {
                    // Bishop cannot move beyond a square occupied by an opponent
                    if (occupyingPiece.getTeamColor() != getTeamColor()) {
                        // Bishop can take the opponent on that square
                        validMoves.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break; // Bishop cannot move to a square occupied by its own team
                }

                validMoves.add(new ChessMove(myPosition, newPosition, null));
            }

            // Diagonal moves: starting position to bottom-left
            for (int i = 1; isValidPosition(myPosition.getRow() - i, myPosition.getColumn() - i); i++) {
                ChessPosition newPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i);
                ChessPiece occupyingPiece = board.getPiece(newPosition);

                if (occupyingPiece != null) {
                    // Bishop cannot move beyond a square occupied by an opponent
                    if (occupyingPiece.getTeamColor() != getTeamColor()) {
                        // Bishop can take the opponent on that square
                        validMoves.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break; // Bishop cannot move to a square occupied by its own team
                }

                validMoves.add(new ChessMove(myPosition, newPosition, null));
            }

            // Diagonal moves: starting position to bottom-right
            for (int i = 1; isValidPosition(myPosition.getRow() - i, myPosition.getColumn() + i); i++) {
                ChessPosition newPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + i);
                ChessPiece occupyingPiece = board.getPiece(newPosition);

                if (occupyingPiece != null) {
                    // Bishop cannot move beyond a square occupied by an opponent
                    if (occupyingPiece.getTeamColor() != getTeamColor()) {
                        // Bishop can take the opponent on that square
                        validMoves.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break; // Bishop cannot move to a square occupied by its own team
                }

                validMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        if (type == PieceType.KING) {
            int[] rowDirections = {-1, -1, -1, 0, 0, 1, 1, 1};
            int[] colDirections = {-1, 0, 1, -1, 1, -1, 0, 1};

            for (int i=0; i < rowDirections.length; i++) {
                int newRow = myPosition.getRow() + rowDirections[i];
                int newCol = myPosition.getColumn() + colDirections[i];

                if (isValidPosition(newRow, newCol)) {
                    ChessPiece occupyingPiece = board.getPiece(new ChessPosition(newRow, newCol));
                    if (occupyingPiece == null || occupyingPiece.getTeamColor() != getTeamColor()) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(newRow, newCol), null));
                    }
                }
            }
        }

        return validMoves;
    }




    private boolean isValidPosition(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

//    private boolean blockedByTeamPiece(int row, int col) {
//        return true;
//    }
}
