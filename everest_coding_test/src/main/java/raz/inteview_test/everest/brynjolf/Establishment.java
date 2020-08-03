package raz.inteview_test.everest.brynjolf;

import raz.inteview_test.everest.brynjolf.room.Element;
import raz.inteview_test.everest.brynjolf.room.Room;
import raz.inteview_test.everest.brynjolf.util.MatrixFileConverter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * TODO_done: pay attention to
 * <p>
 * "It's been a few days. You are able to visualize the board up to 4 moves ahead"
 * "Sometimes the game is undecided after 4 moves"
 * <p>
 * idee, fire GameStatus Events like:
 * - game undecided/due to movesCount Limit reached
 * - game won/Bryn has Exited
 * - game lost/Bryn was Caught
 * </p>
 * <p>
 * Attainment/The Goal: ability to perfectly simulate the outcome of a sequence of moves
 * That is:
 * Input:
 * - the room, it is to be read from room.txt
 * - String containing the sequence of moves: l,r,u,d
 * <hr/>
 * <p>
 * Write a commandline application that will take this sequence as input and prints how the room looks. That is:<b>perfectly simulate the outcome of a sequence of moves<b/>
 * </p>
 */
public class Establishment {
    private final static MatrixFileConverter matrixFileConverter = new MatrixFileConverter();

    static Path resourcesDir = Paths.get("src", "main", "resources");

    private final Path roomFilePath;
    private final String movesSequence;
    private final List<Direction> moves;
    private SimulationResult gameResult;

    public SimulationResult getGameResult() {
        return gameResult;
    }

    public Establishment(Path roomFilePath, String movesSequence) {
        this.roomFilePath = roomFilePath;
        this.movesSequence = movesSequence;

        validateInput();
        moves = Direction.parseString(movesSequence);
    }

    private void validateInput() {
        if (!Files.exists(roomFilePath))
            throw new IllegalArgumentException("No valid filePath" + roomFilePath + " expecting 'room.txt' in project location resources/room.txt");

        if (movesSequence == null || movesSequence.isEmpty())
            throw new IllegalArgumentException("movesSequence is required, it can't be empty");
    }

    /**
     * prints how the rooms looks at the end
     */
    String executeMoves() throws IOException {
        Element[][] roomMatrix = matrixFileConverter.loadFromFile(roomFilePath);

        Room brynGame = new Room(roomMatrix);

        gameResult = brynGame.executeMoveSequence(moves);

        String roomOutput = brynGame.prettyPrint();

        return roomOutput;
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 0)
            throw new IllegalArgumentException("Moves Sequence is required!");

        Path filePath = resourcesDir.resolve("room.txt");
        String movesSeq = args[0];

//        System.out.println("Step 0, sequence of moves Program Args >>" + Arrays.toString(args));
//        System.out.println("sequence of moves:" + movesSeq);
//
        String roomStrFormat = Files.readString(filePath);

        System.out.println("Step 1, reading from room.txt the Room String Representation, starting position:");
        System.out.print(roomStrFormat);
        System.out.println("Following movesSequence will be executed:" + movesSeq);

        Establishment phase1 = new Establishment(filePath, movesSeq);

        String outputStrMatrixFormat = phase1.executeMoves();

        System.out.println("Output:");
        System.out.println(outputStrMatrixFormat);

        System.out.println(phase1.getGameResult().getGameStatus() + ": executed " + phase1.getGameResult().getMovesCount() + " moves out of " + phase1.moves.size());
    }

}
