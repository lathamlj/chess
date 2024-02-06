package chess;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard chessBoard;
    private TeamColor currentTeam;
    private ChessMove move;

    public ChessGame() {
        this.currentTeam = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTeam;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.currentTeam = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        //create a temporary chess board to test whether the move will cause check
        ChessBoard tempBoard = new ChessBoard(chessBoard);
        ChessPiece tempPiece = tempBoard.getPiece(startPosition);

        if (tempPiece == null) {
            return null;
        } else {
            Collection<ChessMove> validMoves = new HashSet<>();
            //call pieceMoves function on the tempPiece and add to validMoves
            validMoves.addAll(tempPiece.pieceMoves(tempBoard, startPosition));
            return validMoves;
        }
        //check if move puts king in check by calling isInCheck function
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //if it's not the teams turn to go
        if (chessBoard.getPiece(move.getStartPosition()).getTeamColor() != currentTeam) {
            throw new InvalidMoveException("its not your turn");
        }

        //if the move is not in the hash set returned by the validMoves function above
        if (!validMoves(move.getStartPosition()).contains(move)) {
            throw new InvalidMoveException("not a valid move");
        }

        ChessPiece movedPiece = chessBoard.getPiece(move.getStartPosition());

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = getKingPosition(teamColor);
        Collection<ChessPiece> opposingPieces = opposingPieces(teamColor);

        //check if any opposing pieces have a valid move that puts the king in check
        for (ChessPiece piece : opposingPieces) {
            ChessBoard tempBoard = new ChessBoard(chessBoard);
            Collection<ChessMove> tempMoves = piece.pieceMoves(tempBoard, piece.getPosition(tempBoard));
            for (ChessMove move : tempMoves) {
                if (move.equals(kingPosition)) {
                    return true; //king is in check
                }
            }
        }
        return false;
    }

    //private func to find king position
    private ChessPosition getKingPosition(TeamColor teamColor) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currentPosition = new ChessPosition(i, j);
                ChessPiece pieceThere = chessBoard.getPiece(currentPosition);
                if (pieceThere != null &&
                        (pieceThere.getPieceType() == ChessPiece.PieceType.KING &&
                                pieceThere.getTeamColor() == teamColor)) {
                    return currentPosition;
                }
            }
        }
        return null;
    }

    //private function that returns an array of ChessPiece types of each of the opponent pieces
    private Collection<ChessPiece> opposingPieces(TeamColor teamColor) {
        Collection<ChessPiece> opposingPieces = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currentPosition = new ChessPosition(i, j);
                ChessPiece pieceThere = chessBoard.getPiece(currentPosition);
                if (pieceThere != null &&
                        pieceThere.getTeamColor() != teamColor) {
                    //add that piece to the opposingPieces array
                    opposingPieces.add(pieceThere);
                }
            }
        }
        return opposingPieces;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.chessBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return chessBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return Objects.deepEquals(chessBoard, chessGame.chessBoard) && currentTeam == chessGame.currentTeam && Objects.equals(move, chessGame.move);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chessBoard, currentTeam, move);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "chessBoard=" + chessBoard +
                ", currentTeam=" + currentTeam +
                ", move=" + move +
                '}';
    }
}
