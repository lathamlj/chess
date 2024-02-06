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
        this.chessBoard = new ChessBoard();
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
        Collection<ChessMove> validMoves = new HashSet<>();
        ChessPiece piece = chessBoard.getPiece(startPosition);

        if (piece == null) {
            return null;
        } else {
            Collection<ChessMove> allMoves = piece.pieceMoves(chessBoard, startPosition);

            for (ChessMove move : allMoves) {
                ChessBoard tempBoard = new ChessBoard(chessBoard);
                ChessPiece tempPiece = tempBoard.getPiece(startPosition);


                if (move.getPromotionPiece() != null) {
                    tempBoard.addPiece(new ChessPosition(move.getEndPosition().getRow(), move.getEndPosition().getColumn()),
                            new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
                } else {
                    tempBoard.addPiece(new ChessPosition(move.getEndPosition().getRow(), move.getEndPosition().getColumn()), tempPiece);
                }
                tempBoard.addPiece(new ChessPosition(move.getStartPosition().getRow(), move.getStartPosition().getColumn()), null);
                if (!isInCheck(tempBoard, tempPiece.getTeamColor())) {
                    validMoves.add(move);
                }
            }
        }
        return validMoves;
    }

    //not using this now but could be emplyed for better efficiency in the future
    private boolean isPromotionMove(ChessPosition position, TeamColor teamColor) {
        if (teamColor == TeamColor.WHITE) {
            return position.getRow() == 8;
        } else {
            return position.getRow() == 1;
        }
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

        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if (validMoves == null || !validMoves.contains(move)) {
            throw new InvalidMoveException("not a valid move");
        }

        ChessPiece pieceToMove = chessBoard.getPiece(move.getStartPosition());
        //add piece to endPosition
        if (move.getPromotionPiece() != null) {
            chessBoard.addPiece(new ChessPosition(move.getEndPosition().getRow(), move.getEndPosition().getColumn()),
                    new ChessPiece(pieceToMove.getTeamColor(), move.getPromotionPiece()));
        } else {
            chessBoard.addPiece(new ChessPosition(move.getEndPosition().getRow(), move.getEndPosition().getColumn()), pieceToMove);
        }
        //erase piece in start position
        chessBoard.addPiece(new ChessPosition(move.getStartPosition().getRow(), move.getStartPosition().getColumn()), null);

        //taking turns
        currentTeam = (currentTeam == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return isInCheck(chessBoard, teamColor);
    }

    public boolean isInCheck(ChessBoard chessBoard, TeamColor teamColor) {
        ChessPosition kingPosition = getKingPosition(chessBoard, teamColor);
        Collection<ChessMove> opposingPiecesMoves = opposingPiecesMoves(chessBoard, teamColor);

        //check if any opposing pieces have a valid move that puts the king in check
        for (ChessMove move : opposingPiecesMoves) {
            if (move.getEndPosition().equals(kingPosition)) {
                return true;
            }
        }
        return false;
    }

    //private func to find king position
    private ChessPosition getKingPosition(ChessBoard chessBoard, TeamColor teamColor) {
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

    //private function that returns an array of ChessMove types of each of the opponent pieces moves
    //this function should call the pieceMoves function in the ChessPiece class
    private Collection<ChessMove> opposingPiecesMoves(ChessBoard chessBoard, TeamColor teamColor) {
        Collection<ChessMove> opposingPiecesMoves = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currentPosition = new ChessPosition(i, j);
                ChessPiece pieceThere = chessBoard.getPiece(currentPosition);
                if (pieceThere != null &&
                        pieceThere.getTeamColor() != teamColor) {
                    Collection<ChessMove> moves = pieceThere.pieceMoves(chessBoard, currentPosition);
                    opposingPiecesMoves.addAll(moves);
                }
            }
        }
        return opposingPiecesMoves;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheckmate(chessBoard, teamColor);
    }

    public boolean isInCheckmate(ChessBoard chessBoard, TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }

        Collection<ChessPiece> teamPieces = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currentPosition = new ChessPosition(i, j);
                ChessPiece piece = chessBoard.getPiece(currentPosition);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    teamPieces.add(piece);
                }
            }
        }

        for (ChessPiece piece : teamPieces) {
            //call validMoves function here on all the pieces of the teamColor
            Collection<ChessMove> teamPieceMoves = validMoves(piece.getPosition(chessBoard));
            if (!teamPieceMoves.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        Collection<ChessPiece> teamPieces = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currentPosition = new ChessPosition(i, j);
                ChessPiece piece = chessBoard.getPiece(currentPosition);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    teamPieces.add(piece);
                }
            }
        }

        for (ChessPiece piece : teamPieces) {
            //call validMoves function here on all the pieces of the teamColor
            Collection<ChessMove> teamPieceMoves = validMoves(piece.getPosition(chessBoard));
            if (!teamPieceMoves.isEmpty()) {
                return false;
            }
        }
        return true;
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
