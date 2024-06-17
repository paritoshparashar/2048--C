package ttfe.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import ttfe.MoveDirection;
import ttfe.SimulatorInterface;
import ttfe.TTFEFactory;

/**
 * This class provides a very simple example of how to write tests for this project.
 * You can implement your own tests within this class or any other class within this package.
 * Tests in other packages will not be run and considered for completion of the project.
 */
public class SimpleTests {

	private SimulatorInterface game;
	

	@Before
	public void setUp() {
		game = TTFEFactory.createSimulator(4, 4, new Random(0));
	}
    
    /*
     * Tests for Add Piece
     */
    @Test
	public void testAddPieceAtEmpty() {
		int currentTiles = game.getNumPieces();
		game.addPiece();
		assertEquals( currentTiles + 1, game.getNumPieces());
	}

    @Test
    public void testAddPieceValue() {
        for (int i = 0; i < 14; i++) { 
            game.addPiece(); // Add piece in each iteration

            boolean isTwoOrFour = false;
            for (int x = 0; x < game.getBoardWidth(); x++) {

                for (int y = 0; y < game.getBoardHeight(); y++) {

                    int piece_val = game.getPieceAt(x, y);

                    // Board should not have anything else that 2 || 4 || 0
                    if (piece_val == 2 || piece_val == 4) {
                        isTwoOrFour = true;
                    }
                    assertTrue(piece_val == 0 || piece_val == 2 || piece_val == 4);
                }
            }
            assertTrue("No new piece with value 2 or 4 was added", isTwoOrFour);
        }
    }

    @Test
    public void testAddPieceOnFullBoard () {

        this.setBoardToNum(8);
        
        assertThrows(IllegalStateException.class, () -> {game.addPiece();});

        for (int i = 0; i < game.getBoardWidth(); i++) {
            
            for (int j = 0; j < game.getBoardHeight(); j++) {
                
                assertTrue(game.getPieceAt(i, j) == 8);
            }
        }
    }
    // __________________________________________________ //

    /*
     * Tests for Board Dimensions
     */

    @Test
	public void testInitialBoardHeight() {
		assertTrue("The initial game board did not have correct height",
				4 == game.getBoardHeight());
	}
    @Test
	public void testInitialBoardWidth() {
		assertTrue("The initial game board did not have correct height",
				4 == game.getBoardWidth());
	}

    @Test
    public void testBoardIsSquare () {
        assertTrue (game.getBoardHeight() == game.getBoardWidth());
    }


    // __________________________________________________ //

    /*
     * Tests for Number of Moves
     */

    @Test
    public void testInitialNumOfMoves () {
        assertTrue ("Initial moves not equal to zero", 0 == game.getNumMoves());
    }

    @Test
    public void testNumOfMovesAfterMovingNORTH (){
        if (game.isMovePossible(MoveDirection.NORTH))
        {
            game.performMove (MoveDirection.NORTH);
            assertEquals(1, game.getNumMoves());
        }
    }
    @Test
    public void testNumOfMovesAfterMovingSOUTH (){
        if (game.isMovePossible(MoveDirection.SOUTH))
        {
            game.performMove (MoveDirection.SOUTH);
            assertEquals(1, game.getNumMoves());
        }
    }
    @Test
    public void testNumOfMovesAfterMovingEAST (){
        if (game.isMovePossible(MoveDirection.EAST))
        {
            game.performMove (MoveDirection.EAST);
            assertEquals(1, game.getNumMoves());
        }
    }
    @Test
    public void testNumOfMovesAfterMovingWEST (){
        if (game.isMovePossible(MoveDirection.WEST))
        {
            game.performMove (MoveDirection.WEST);
            assertEquals(1, game.getNumMoves());
        }
    }

    @Test
    public void testNumOfMovesAfterMergingMultipleTiles () {

        this.setBoardToNum(8);
        game.performMove(MoveDirection.EAST);
        game.performMove(MoveDirection.SOUTH);

        assertTrue (2 == game.getNumMoves());
    }
    // __________________________________________________ //

    /*
     * Test for Number of Pieces
     */

    @Test
    public void testInitialNumberOfPieces () {
        assertEquals(2, game.getNumPieces());
    }
    @Test
    public void testNumberOfPiecesFullBoard () {

        this.setBoardToNum(4);
        assertEquals(16, game.getNumPieces());
    }

    // __________________________________________________ //

    /*
     * Tests for the value of pieces (getPieceAt(int x, int y))
     */

    @Test
    public void testInitialValueOfPieces () {

        int nonZeroCount = 0;
        boolean isTwoOrFour = true;

        for (int i = 0; i < game.getBoardHeight(); i++) {
            
            for (int j = 0; j < game.getBoardWidth(); j++) {
                
                if (game.getPieceAt(i, j) != 0) {

                    if (game.getPieceAt(i, j) == 2 || game.getPieceAt(i, j) == 4){
                        ++nonZeroCount;
                    }
                    else {
                        isTwoOrFour = false;
                        break;
                    }
                    
                }
            }
            if (!isTwoOrFour) {
                break;
            }
        }

        assertTrue(nonZeroCount == 2 && isTwoOrFour);
    }

    /*
     * {2, 4, 8, 4},
            {32, 64, 128, 256},
            {2, 1024, 2, 4},
            {8, 16, 32, 8}
     */
    
    @Test
    public void testRandomBoardValues () {

        this.setBoardWithNoMovePossible(); 
        assertEquals(1024, game.getPieceAt(1, 2));
        assertEquals(8, game.getPieceAt(3, 3));
        assertEquals(128, game.getPieceAt(2, 1));
    }

    // __________________________________________________ //

    /*
     * Tests for the number of points
     */

    @Test
    public void testInitialPoints () {
        assertTrue("Get Points should 0 as intial points",0 == game.getPoints());
    }

    @Test
    public void testPointsAfterMove (){

        this.setBoardToNum(0);

        game.setPieceAt(0, 0, 2);
        game.setPieceAt(0, 2, 2);
        
        if (game.isMovePossible(MoveDirection.NORTH)) {
            assertTrue(game.performMove(MoveDirection.NORTH));
        }
        if (game.isMovePossible(MoveDirection.SOUTH)) {
            assertTrue(game.performMove(MoveDirection.SOUTH));
        }
        assertTrue(4 == game.getPoints());

    }

    @Test
    public void testPointsAfterComplexMove (){

        this.setBoardToNum(0);

        game.setPieceAt(0, 0, 16);
        game.setPieceAt(0, 1, 16);
        game.setPieceAt(0, 2, 8);
        game.setPieceAt(0, 3, 8);
        
        game.setPieceAt(1, 0, 16);
        game.setPieceAt(1, 1, 0);
        game.setPieceAt(1, 2, 16);
        game.setPieceAt(1, 3, 4);

        game.setPieceAt(2, 0, 2);
        game.setPieceAt(2, 1, 2);
        game.setPieceAt(2, 2, 2);
        game.setPieceAt(2, 3, 4);

        assertTrue(game.isMovePossible());

        for (MoveDirection direction : MoveDirection.values()) {
            assertTrue (game.isMovePossible(direction)); // Move possible in all directions
        }

            assertTrue(game.performMove(MoveDirection.SOUTH));
            assertTrue(game.performMove(MoveDirection.WEST));

        assertTrue(156 == game.getPoints());

    }

    // __________________________________________________ //

    /*
     * Tests to check if a move is possible
     */

    // Without Direction

    @Test 
    public void testInitialIsMovePossibleWithoutDirection () {

        assertTrue (game.isMovePossible()); // Initially there should be at least one possible move

    }

    @Test
    public void testIsMovePossibleInAFullBoardWithoutDirection () {
        
        this.setBoardWithNoMovePossible();

        assertFalse(game.isMovePossible());
    }

    @Test
    public void testIsMovePossibleInAFullBoardWithoutDirection2 () {

        this.setBoardToNum(2);
        
        assertTrue(game.isMovePossible());
    }


    // __________________With Direction__________________ //

    // @Test(expected = IllegalArgumentException.class)
    // public void testPreconditionError() {

    //     MoveDirection direction = null;

    //     game.isMovePossible(direction);
    // }

    @Test
    public void testInitialIsMovePossibleWithDirection () {

        // Initially, it should be possible to move in atleast one direction
        boolean movePossible = false;

        for (MoveDirection direction : MoveDirection.values()) {
            
            movePossible = game.isMovePossible(direction);
            if (movePossible) {
                break;
            }
        }
        assertTrue (movePossible);
    }

    @Test
    public void testIsMovePossibleInAFullBoardWithDirection () {
        int[][] array = {
            {2, 4, 8, 16},
            {32, 64, 128, 256},
            {512, 1024, 2, 4},
            {8, 16, 32, 64}
        };

        for (int i = 0; i < game.getBoardHeight(); i++) {

            for (int j = 0; j < game.getBoardWidth(); j++) {
                
                game.setPieceAt(i, j, array[i][j]);
            }
        }

        boolean movePossible = true;

        for (MoveDirection direction : MoveDirection.values()) {
            
            movePossible = game.isMovePossible(direction);
            if (movePossible) {
                break;
            }
        }
        
        assertTrue("Game should be over.", !game.isSpaceLeft() && !game.isMovePossible(MoveDirection.NORTH) && 
                   !game.isMovePossible(MoveDirection.SOUTH) && !game.isMovePossible(MoveDirection.WEST) && 
                   !game.isMovePossible(MoveDirection.EAST));
        
        assertFalse(movePossible);
    }

    @Test
    public void testIsMovePossibleInAFullBoardWithDirection2 () {

        this.setBoardToNum(2);

        boolean movePossible = false;
        
        for (MoveDirection direction : MoveDirection.values()) {
            
            movePossible = game.isMovePossible(direction); // Should be able to move in all directions
            
        }
        
        assertTrue(movePossible);
    }

    /*
     * Extra Tests
     */

     @Test
public void testNoMovePossibleOnFullBoard() {
    // Fill the board with tiles that prevent any movement
    game.setPieceAt(0, 0, 4);
    game.setPieceAt(0, 1, 2);
    game.setPieceAt(0, 2, 4);
    game.setPieceAt(0, 3, 2);
    game.setPieceAt(1, 0, 2);
    game.setPieceAt(1, 1, 4);
    game.setPieceAt(1, 2, 2);
    game.setPieceAt(1, 3, 4);
    game.setPieceAt(2, 0, 4);
    game.setPieceAt(2, 1, 2);
    game.setPieceAt(2, 2, 4);
    game.setPieceAt(2, 3, 2);
    game.setPieceAt(3, 0, 2);
    game.setPieceAt(3, 1, 4);
    game.setPieceAt(3, 2, 2);
    game.setPieceAt(3, 3, 4);

    assertFalse("Move is not possible when the board is full with no merges possible", game.isMovePossible());
    assertFalse("No move should be possible to the NORTH", game.isMovePossible(MoveDirection.NORTH));
    assertFalse("No move should be possible to the EAST", game.isMovePossible(MoveDirection.EAST));
    assertFalse("No move should be possible to the SOUTH", game.isMovePossible(MoveDirection.SOUTH));
    assertFalse("No move should be possible to the WEST", game.isMovePossible(MoveDirection.WEST));

    // Attempting to perform a move in any direction should not change the board state
    assertFalse("Performing a move to the NORTH should fail", game.performMove(MoveDirection.NORTH));
    assertFalse("Performing a move to the EAST should fail", game.performMove(MoveDirection.EAST));
    assertFalse("Performing a move to the SOUTH should fail", game.performMove(MoveDirection.SOUTH));
    assertFalse("Performing a move to the WEST should fail", game.performMove(MoveDirection.WEST));

    // Ensure the board state remains unchanged
    assertEquals("Value at (0, 0) should be 4", 4, game.getPieceAt(0, 0));
    assertEquals("Value at (0, 1) should be 2", 2, game.getPieceAt(0, 1));
    // Repeat for all board positions
}


    @Test
    public void testIsMovePossibleEmptyBoard() {
        
        this.setBoardToNum(0);
        assertFalse (game.isMovePossible());

        for (MoveDirection direction : MoveDirection.values()) {
            assertFalse(game.isMovePossible(direction));
        }
        
    }

    @Test
    public void testIsMovePossibleWithOnePossibleMove() {

        this.setBoardToNum(2);

        assertTrue("A move should be possible when there is at least one empty space", game.isMovePossible());
        assertTrue("A move should be possible to the NORTH", game.isMovePossible(MoveDirection.NORTH));
        assertTrue("A move should be possible to the EAST", game.isMovePossible(MoveDirection.EAST));
        assertTrue("A move should be possible to the SOUTH", game.isMovePossible(MoveDirection.SOUTH));
        assertTrue("A move should be possible to the WEST", game.isMovePossible(MoveDirection.WEST));
    }

    @Test
    public void testIsMovePossibleWithMergeableTiles() {

        this.setBoardToNum(0);

        // Set up the board so that merges are possible
        game.setPieceAt(0, 0, 2);
        game.setPieceAt(0, 1, 2);
        game.setPieceAt(1, 0, 4);
        game.setPieceAt(1, 1, 4);

        assertTrue("Move is possible when tiles can be merged", game.isMovePossible());
        assertTrue(game.isMovePossible(MoveDirection.NORTH));
        assertTrue(game.isMovePossible(MoveDirection.EAST));
        assertTrue(game.isMovePossible(MoveDirection.SOUTH));
        assertFalse(game.isMovePossible(MoveDirection.WEST));
    }

    // __________________________________________________ //


    /*
     * Tests for an empty space
     */

    @Test
    public void testInitialIsSpaceLeft () {
        assertTrue(game.isSpaceLeft()); // Empty space must be there at starting
    }

    
    @Test
    public void testWithAFullBoard () {

        this.setBoardToNum(8);

        assertTrue (!game.isSpaceLeft());

    }

    // __________________________________________________ //

    /*
     * Tests for Perform Move
     */

    @Test
    public void testInitialPerformMove () {

        // Initially, it should be possible to move in atleast one direction
        boolean movePossible = false;

        for (MoveDirection direction : MoveDirection.values()) {
            
            movePossible = game.performMove(direction);
            if (movePossible) {
                break;
            }
        }
        assertTrue (movePossible);
    }


    @Test
     public void testPerformMoveNorth() {
        // Set up a scenario where a move to NORTH should merge tiles
        game.setPieceAt(0, 0, 2);
        game.setPieceAt(0, 1, 2);
        game.setPieceAt(0, 2, 4);
        assertTrue("Move to the NORTH should be possible", game.performMove(MoveDirection.NORTH));
        assertEquals("Value at (0, 0) should be 4 after merging", 4, game.getPieceAt(0, 0));
        assertEquals("Value at (0, 1) should be 4 after merging", 4, game.getPieceAt(0, 1));
        assertEquals("Value at (0, 2) should be 0 after merging", 0, game.getPieceAt(0, 2));
    }

    @Test
    public void testPerformMoveToEast() {
        // Set up a scenario where a move to EAST should merge tiles
        game.setPieceAt(1, 0, 2);
        game.setPieceAt(2, 0, 2);
        game.setPieceAt(3, 0, 4);
        assertTrue("Move to the EAST should be possible", game.performMove(MoveDirection.EAST));
        assertEquals("Value at (3, 0) should be 4 after merging", 4, game.getPieceAt(3, 0));
        assertEquals("Value at (2, 0) should be 4 after merging", 4, game.getPieceAt(2, 0));
        assertEquals("Value at (1, 0) should be 0 after merging", 0, game.getPieceAt(1, 0));
    }

    @Test
    public void testPerformMoveToSouth() {
        // Set up a scenario where a move to SOUTH should merge tiles
        game.setPieceAt(0, 3, 2);
        game.setPieceAt(0, 2, 2);
        game.setPieceAt(0, 1, 4);
        assertTrue("Move to the SOUTH should be possible", game.performMove(MoveDirection.SOUTH));
        assertEquals("Value at (0, 3) should be 4 after merging", 4, game.getPieceAt(0, 3));
        assertEquals("Value at (0, 2) should be 4 after merging", 4, game.getPieceAt(0, 2));
        assertEquals("Value at (0, 1) should be 0 after merging", 0, game.getPieceAt(0, 1));
    }

    @Test
    public void testPerformMoveToWest() {
        // Set up a scenario where a move to WEST should merge tiles
        game.setPieceAt(3, 0, 2);
        game.setPieceAt(2, 0, 2);
        game.setPieceAt(1, 0, 4);
        assertTrue("Move to the WEST should be possible", game.performMove(MoveDirection.WEST));
        assertEquals("Value at (0, 0) should be 4 after merging", 4, game.getPieceAt(0, 0));
    }
    // __________________________________________________ //

    @Test
    public void isMovePossibleSpecial () {

        this.setBoardToNum(0);

        game.setPieceAt(3, 0, 4);
        game.setPieceAt(3, 1, 2);
        game.setPieceAt(3, 2, 8);
        game.setPieceAt(3, 3, 2);
        game.setPieceAt(2, 3, 4);
        game.setPieceAt(2, 2, 16);
        
        assertTrue( game.isMovePossible());
    }
    @Test
    public void testPerformMoveSpecial1 () {

        this.setBoardToNum(0);
        
        game.setPieceAt(0, 0, 2);
        game.setPieceAt(0, 2, 2);
        game.setPieceAt(0, 3, 8);
        game.setPieceAt(1, 3, 4);

        assertTrue( game.performMove(MoveDirection.SOUTH));
        assertTrue(8 == game.getPieceAt(0, 3));
    }
    @Test
    public void testIsMovePossibleWithDirectionSpecial1 () {

        this.setBoardToNum(0);

        game.setPieceAt(0, 0, 2);
        game.setPieceAt(0, 1, 2);
        game.setPieceAt(0, 2, 16);
        game.setPieceAt(0, 3, 8);

        game.setPieceAt(1, 0, 2);
        game.setPieceAt(1, 1, 4);
        game.setPieceAt(1, 2, 8);
        game.setPieceAt(1, 3, 64);

        assertTrue( game.performMove(MoveDirection.SOUTH));
        assertTrue(4 == game.getPieceAt(0, 1));
    }
    @Test
    public void testPerformMove3sameValueColumn () {

        this.setBoardToNum(0);
        game.setPieceAt(0, 0, 2);
        game.setPieceAt(0, 1, 2);
        game.setPieceAt(0, 2, 2);
        game.setPieceAt(0, 3, 8);

        assertTrue( game.performMove(MoveDirection.SOUTH));
        assertTrue(4 == game.getPieceAt(0, 2));
        assertTrue(2 == game.getPieceAt(0, 1));
    }

    @Test
    public void testGameWin () {

        this.setBoardToNum(0);
        game.setPieceAt(0, 0, 1024);
        game.setPieceAt(0, 1, 1024);
        game.setPieceAt(0, 2, 2);
        game.setPieceAt(0, 3, 8);

        assertTrue( game.performMove(MoveDirection.SOUTH));
        assertTrue(2048 == game.getPieceAt(0, 1));
    }

    public void setBoardToNum (int num) {

        for (int i = 0; i < game.getBoardHeight(); i++) {
            
            for (int j = 0; j < game.getBoardWidth(); j++) {
                
                game.setPieceAt(i, j, num); // Set every piece to be zero

            }
        }
    }

    public void setBoardWithNoMovePossible () {

        int[][] array = {
            {2, 4, 8, 4},
            {32, 64, 128, 256},
            {2, 1024, 2, 4},
            {8, 16, 32, 8}
        };

        for (int i = 0; i < game.getBoardWidth(); i++) {

            for (int j = 0; j < game.getBoardHeight(); j++) {
                
                game.setPieceAt(i, j, array[j][i]);
            }
        }
    }

    public static void main(String[] args){
        SimpleTests test = new SimpleTests();
        test.setUp();
        test.testPerformMove3sameValueColumn();
    }
}
