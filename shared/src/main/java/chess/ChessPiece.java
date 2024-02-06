package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

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
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    public void setPieceType(PieceType pieceType) {
        this.type = pieceType;
    }

    //method that returns the position of a given piece
    public ChessPosition getPosition(ChessBoard board) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition currentPosition = new ChessPosition(row, col);
                ChessPiece pieceAtCurrentPosition = board.getPiece(currentPosition);
                if (pieceAtCurrentPosition != null && pieceAtCurrentPosition.equals(this)) {
                    return currentPosition;
                }
            }
        }
        return null;
    }


    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> pieceMoves = new HashSet<>();

        if (type == PieceType.PAWN) {
            int direction = (pieceColor == ChessGame.TeamColor.WHITE) ? 1 : -1;
            int newRow = myPosition.getRow() + direction;
            int newCol = myPosition.getColumn();

            //check if the new position is within the boundaries
            if (isValidPosition(newRow, myPosition.getColumn())) {
                //check if the position in front is empty
                if (board.getPiece(new ChessPosition(newRow, myPosition.getColumn())) == null) {
                    //check if promotion is result of move
                    if ((pieceColor == ChessGame.TeamColor.WHITE && myPosition.getRow() == 7) ||
                            (pieceColor == ChessGame.TeamColor.BLACK && myPosition.getRow() == 2)) {
                        pieceMoves.add(new ChessMove(myPosition, new ChessPosition(newRow, newCol), ChessPiece.PieceType.QUEEN));
                        pieceMoves.add(new ChessMove(myPosition, new ChessPosition(newRow, newCol), ChessPiece.PieceType.BISHOP));
                        pieceMoves.add(new ChessMove(myPosition, new ChessPosition(newRow, newCol), ChessPiece.PieceType.ROOK));
                        pieceMoves.add(new ChessMove(myPosition, new ChessPosition(newRow, newCol), ChessPiece.PieceType.KNIGHT));
                    } else {
                        pieceMoves.add(new ChessMove(myPosition, new ChessPosition(newRow, newCol), null));
                    }

                    //if it's the pawn's first move, it can move two squares forward
                    if ((pieceColor == ChessGame.TeamColor.WHITE && myPosition.getRow() == 2) ||
                            (pieceColor == ChessGame.TeamColor.BLACK && myPosition.getRow() == 7)) {
                        int doubleMoveRow = myPosition.getRow() + 2 * direction;
                        if (isValidPosition(doubleMoveRow, myPosition.getColumn()) &&
                                board.getPiece(new ChessPosition(doubleMoveRow, newCol)) == null) {
                            pieceMoves.add(new ChessMove(myPosition, new ChessPosition(doubleMoveRow, newCol), null));
                        }
                    }
                }

                //check for capturing opponents diagonally
                int leftCaptureCol = myPosition.getColumn() - 1;
                int rightCaptureCol = myPosition.getColumn() + 1;

                if (isValidPosition(newRow, leftCaptureCol)) {
                    ChessPiece leftCapturePiece = board.getPiece(new ChessPosition(newRow, leftCaptureCol));
                    if (leftCapturePiece != null && leftCapturePiece.getTeamColor() != getTeamColor()) {
                        //check if promotion is result of move
                        if ((pieceColor == ChessGame.TeamColor.WHITE && myPosition.getRow() == 7) ||
                                (pieceColor == ChessGame.TeamColor.BLACK && myPosition.getRow() == 2)) {
                            pieceMoves.add(new ChessMove(myPosition, new ChessPosition(newRow, leftCaptureCol), ChessPiece.PieceType.QUEEN));
                            pieceMoves.add(new ChessMove(myPosition, new ChessPosition(newRow, leftCaptureCol), ChessPiece.PieceType.BISHOP));
                            pieceMoves.add(new ChessMove(myPosition, new ChessPosition(newRow, leftCaptureCol), ChessPiece.PieceType.ROOK));
                            pieceMoves.add(new ChessMove(myPosition, new ChessPosition(newRow, leftCaptureCol), ChessPiece.PieceType.KNIGHT));
                        } else {
                            pieceMoves.add(new ChessMove(myPosition, new ChessPosition(newRow, leftCaptureCol), null));
                        }
                    }
                }

                if (isValidPosition(newRow, rightCaptureCol)) {
                    ChessPiece rightCapturePiece = board.getPiece(new ChessPosition(newRow, rightCaptureCol));
                    if (rightCapturePiece != null && rightCapturePiece.getTeamColor() != getTeamColor()) {
                        //check if promotion is result of move
                        if ((pieceColor == ChessGame.TeamColor.WHITE && myPosition.getRow() == 7) ||
                                (pieceColor == ChessGame.TeamColor.BLACK && myPosition.getRow() == 2)) {
                            pieceMoves.add(new ChessMove(myPosition, new ChessPosition(newRow, rightCaptureCol), ChessPiece.PieceType.QUEEN));
                            pieceMoves.add(new ChessMove(myPosition, new ChessPosition(newRow, rightCaptureCol), ChessPiece.PieceType.BISHOP));
                            pieceMoves.add(new ChessMove(myPosition, new ChessPosition(newRow, rightCaptureCol), ChessPiece.PieceType.ROOK));
                            pieceMoves.add(new ChessMove(myPosition, new ChessPosition(newRow, rightCaptureCol), ChessPiece.PieceType.KNIGHT));
                        } else {
                            pieceMoves.add(new ChessMove(myPosition, new ChessPosition(newRow, rightCaptureCol), null));
                        }
                    }
                }
            }
        }

        if (type == PieceType.KING) {
            int[] rowDirections = {-1, -1, -1, 0, 0, 1, 1, 1};
            int[] colDirections = {-1, 0, 1, -1, 1, -1, 0, 1};

            for (int i = 0; i < rowDirections.length; i++) {
                int newRow = myPosition.getRow() + rowDirections[i];
                int newCol = myPosition.getColumn() + colDirections[i];

                if (isValidPosition(newRow, newCol)) {
                    ChessPiece occupyingPiece = board.getPiece(new ChessPosition(newRow, newCol));
                    if (occupyingPiece == null || occupyingPiece.getTeamColor() != getTeamColor()) {
                        pieceMoves.add(new ChessMove(myPosition, new ChessPosition(newRow, newCol), null));
                    }
                }
            }
        }

        if (type == PieceType.KNIGHT) {
            int[] rowDirections = {-2, -2, -1, -1, 1, 1, 2, 2};
            int[] colDirections = {-1, 1, -2, 2, -2, 2, -1, 1};

            for (int i = 0; i < rowDirections.length; i++) {
                int newRow = myPosition.getRow() + rowDirections[i];
                int newCol = myPosition.getColumn() + colDirections[i];

                if (isValidPosition(newRow, newCol)) {
                    ChessPiece occupyingPiece = board.getPiece(new ChessPosition(newRow, newCol));
                    if (occupyingPiece == null || occupyingPiece.getTeamColor() != getTeamColor()) {
                        pieceMoves.add(new ChessMove(myPosition, new ChessPosition(newRow, newCol), null));
                    }
                }
            }
        }

        if (type == PieceType.BISHOP) {
            // Diagonal moves: starting position to top-right
            for (int i = 1; isValidPosition(myPosition.getRow() + i, myPosition.getColumn() + i); i++) {
                int newRow = myPosition.getRow() + i;
                int newCol = myPosition.getColumn() + i;
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece occupyingPiece = board.getPiece(newPosition);

                if (occupyingPiece != null) {
                    if (occupyingPiece.getTeamColor() != getTeamColor()) {
                        pieceMoves.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;
                }

                pieceMoves.add(new ChessMove(myPosition, newPosition, null));
            }
            // Diagonal moves: starting position to top-left
            for (int i = 1; isValidPosition(myPosition.getRow() + i, myPosition.getColumn() - i); i++) {
                int newRow = myPosition.getRow() + i;
                int newCol = myPosition.getColumn() - i;
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece occupyingPiece = board.getPiece(newPosition);

                if (occupyingPiece != null) {
                    if (occupyingPiece.getTeamColor() != getTeamColor()) {
                        pieceMoves.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;
                }

                pieceMoves.add(new ChessMove(myPosition, newPosition, null));
            }
            // Diagonal moves: starting position to bottom-left
            for (int i = 1; isValidPosition(myPosition.getRow() - i, myPosition.getColumn() - i); i++) {
                int newRow = myPosition.getRow() - i;
                int newCol = myPosition.getColumn() - i;
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece occupyingPiece = board.getPiece(newPosition);

                if (occupyingPiece != null) {
                    if (occupyingPiece.getTeamColor() != getTeamColor()) {
                        pieceMoves.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;
                }

                pieceMoves.add(new ChessMove(myPosition, newPosition, null));
            }
            // Diagonal moves: starting position to bottom-right
            for (int i = 1; isValidPosition(myPosition.getRow() - i, myPosition.getColumn() + i); i++) {
                int newRow = myPosition.getRow() - i;
                int newCol = myPosition.getColumn() + i;
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece occupyingPiece = board.getPiece(newPosition);

                if (occupyingPiece != null) {
                    if (occupyingPiece.getTeamColor() != getTeamColor()) {
                        pieceMoves.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;
                }

                pieceMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        if (type == PieceType.ROOK) {
            //straight up
            for (int i = 1; isValidPosition(myPosition.getRow() + i, myPosition.getColumn()); i++) {
                int newRow = myPosition.getRow() + i;
                int newCol = myPosition.getColumn();
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece occupyingPiece = board.getPiece(newPosition);

                if (occupyingPiece != null) {
                    //rook cannot move beyond a square occupied by an opponent
                    if (occupyingPiece.getTeamColor() != getTeamColor()) {
                        //rook can take the opponent on that square
                        pieceMoves.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break; //rook cannot move to a square occupied by its own team
                }

                pieceMoves.add(new ChessMove(myPosition, newPosition, null));
            }
            //straight down
            for (int i = 1; isValidPosition(myPosition.getRow() - i, myPosition.getColumn()); i++) {
                int newRow = myPosition.getRow() - i;
                int newCol = myPosition.getColumn();
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece occupyingPiece = board.getPiece(newPosition);

                if (occupyingPiece != null) {
                    if (occupyingPiece.getTeamColor() != getTeamColor()) {
                        pieceMoves.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;
                }

                pieceMoves.add(new ChessMove(myPosition, newPosition, null));
            }
            //right
            for (int i = 1; isValidPosition(myPosition.getRow(), myPosition.getColumn() + i); i++) {
                int newRow = myPosition.getRow();
                int newCol = myPosition.getColumn() + i;
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece occupyingPiece = board.getPiece(newPosition);

                if (occupyingPiece != null) {
                    if (occupyingPiece.getTeamColor() != getTeamColor()) {
                        pieceMoves.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;
                }

                pieceMoves.add(new ChessMove(myPosition, newPosition, null));
            }
            //left
            for (int i = 1; isValidPosition(myPosition.getRow(), myPosition.getColumn() - i); i++) {
                int newRow = myPosition.getRow();
                int newCol = myPosition.getColumn() - i;
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece occupyingPiece = board.getPiece(newPosition);

                if (occupyingPiece != null) {
                    if (occupyingPiece.getTeamColor() != getTeamColor()) {
                        pieceMoves.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;
                }

                pieceMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        if (type == PieceType.QUEEN) {
            // Diagonal moves: starting position to top-right
            for (int i = 1; isValidPosition(myPosition.getRow() + i, myPosition.getColumn() + i); i++) {
                int newRow = myPosition.getRow() + i;
                int newCol = myPosition.getColumn() + i;
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece occupyingPiece = board.getPiece(newPosition);

                if (occupyingPiece != null) {
                    if (occupyingPiece.getTeamColor() != getTeamColor()) {
                        pieceMoves.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;
                }

                pieceMoves.add(new ChessMove(myPosition, newPosition, null));
            }

            // Diagonal moves: starting position to top-left
            for (int i = 1; isValidPosition(myPosition.getRow() + i, myPosition.getColumn() - i); i++) {
                int newRow = myPosition.getRow() + i;
                int newCol = myPosition.getColumn() - i;
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece occupyingPiece = board.getPiece(newPosition);

                if (occupyingPiece != null) {
                    if (occupyingPiece.getTeamColor() != getTeamColor()) {
                        pieceMoves.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;
                }

                pieceMoves.add(new ChessMove(myPosition, newPosition, null));
            }

            // Diagonal moves: starting position to bottom-left
            for (int i = 1; isValidPosition(myPosition.getRow() - i, myPosition.getColumn() - i); i++) {
                int newRow = myPosition.getRow() - i;
                int newCol = myPosition.getColumn() - i;
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece occupyingPiece = board.getPiece(newPosition);

                if (occupyingPiece != null) {
                    if (occupyingPiece.getTeamColor() != getTeamColor()) {
                        pieceMoves.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;
                }

                pieceMoves.add(new ChessMove(myPosition, newPosition, null));
            }

            // Diagonal moves: starting position to bottom-right
            for (int i = 1; isValidPosition(myPosition.getRow() - i, myPosition.getColumn() + i); i++) {
                int newRow = myPosition.getRow() - i;
                int newCol = myPosition.getColumn() + i;
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece occupyingPiece = board.getPiece(newPosition);

                if (occupyingPiece != null) {
                    if (occupyingPiece.getTeamColor() != getTeamColor()) {
                        pieceMoves.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;
                }

                pieceMoves.add(new ChessMove(myPosition, newPosition, null));
            }

            //straight up
            for (int i = 1; isValidPosition(myPosition.getRow() + i, myPosition.getColumn()); i++) {
                int newRow = myPosition.getRow() + i;
                int newCol = myPosition.getColumn();
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece occupyingPiece = board.getPiece(newPosition);

                if (occupyingPiece != null) {
                    //rook cannot move beyond a square occupied by an opponent
                    if (occupyingPiece.getTeamColor() != getTeamColor()) {
                        //rook can take the opponent on that square
                        pieceMoves.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break; //rook cannot move to a square occupied by its own team
                }

                pieceMoves.add(new ChessMove(myPosition, newPosition, null));
            }

            //straight down
            for (int i = 1; isValidPosition(myPosition.getRow() - i, myPosition.getColumn()); i++) {
                int newRow = myPosition.getRow() - i;
                int newCol = myPosition.getColumn();
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece occupyingPiece = board.getPiece(newPosition);

                if (occupyingPiece != null) {
                    if (occupyingPiece.getTeamColor() != getTeamColor()) {
                        pieceMoves.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;
                }

                pieceMoves.add(new ChessMove(myPosition, newPosition, null));
            }

            //right
            for (int i = 1; isValidPosition(myPosition.getRow(), myPosition.getColumn() + i); i++) {
                int newRow = myPosition.getRow();
                int newCol = myPosition.getColumn() + i;
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece occupyingPiece = board.getPiece(newPosition);

                if (occupyingPiece != null) {
                    if (occupyingPiece.getTeamColor() != getTeamColor()) {
                        pieceMoves.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;
                }

                pieceMoves.add(new ChessMove(myPosition, newPosition, null));
            }

            //left
            for (int i = 1; isValidPosition(myPosition.getRow(), myPosition.getColumn() - i); i++) {
                int newRow = myPosition.getRow();
                int newCol = myPosition.getColumn() - i;
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece occupyingPiece = board.getPiece(newPosition);

                if (occupyingPiece != null) {
                    if (occupyingPiece.getTeamColor() != getTeamColor()) {
                        pieceMoves.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;
                }

                pieceMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        return pieceMoves;
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
