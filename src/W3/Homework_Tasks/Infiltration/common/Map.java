package W3.Homework_Tasks.Infiltration.common;

// Add necessary library imports
import W3.Homework_Tasks.Infiltration.obstacles.Obstacle;
import W3.Homework_Tasks.Infiltration.pathFinding.BFSPathFinder;
import W3.Homework_Tasks.Infiltration.pathFinding.GridPathFinder;
import W3.Homework_Tasks.Infiltration.pathFinding.Path;

import java.util.ArrayList;

/**
 * This class will provide a representation of the map and it obstacles
 */
public class Map {
    private final ArrayList<Obstacle> obstacles = new ArrayList<>();
    private final int PADDING = 2;

    /**
     * This constructor instantiates a new Map object, adding the provided list of obstacles to the
     * obstacles attribute of this class.
     * @param obstacles the obstacles to be placed on the map.
     */
    public Map(ArrayList<Obstacle> obstacles){
        this.obstacles.addAll(obstacles);
    }

    /**
     * Returns the obstacle at the given location, or null if there is no obstacle at the given location.
     * @param x the x coordinate of the location to be checked.
     * @param y the y coordinate of the location to be checked.
     * @return the obstacle at the given location, or null if there is no obstacle at the given location.
     */
    private Obstacle getObstacleAtLocation(int x, int y){
        for (Obstacle obstacle : obstacles){
            if (obstacle.isLocationObstructed(x, y)){
                return obstacle;
            }
        }
        return null;
    }

    /**
     * This method constructs and returns a string representing the state of the map.
     * @param matrix the matrix to be converted into a string format.
     * @return a String representing the state of the map.
     */
    private String matrixToString(char[][] matrix){
        StringBuilder sb = new StringBuilder();
        for (char[] row : matrix){
            for (char symbol : row){
                sb.append(symbol);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * This method returns a string representation of the map with the starting and target
     * positions both marked.
     * @param start the starting position.
     * @param target the target location.
     * @return A string encoding the map, with both an 'S' and 'E' encoding the start and target
     *         positions respectively.
     */
    public String getSolvedMap(Location start, Location target){
        // Find the path
        GridPathFinder pathFinder = new BFSPathFinder(this);
        Path path = pathFinder.findPath(start, target);

        // Define the bounds (including padding) based on the start and target locations
        Location topLeft, bottomRight;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE,
                minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        for (Location location : path) {
            int x = location.getX();
            int y = location.getY();
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
        }
        topLeft = new Location(minX - PADDING, minY - PADDING);
        bottomRight = new Location(maxX + PADDING, maxY + PADDING);

        // Create the map
        // +1 because the bounds are inclusive
        char[][] solvedMap = new char[bottomRight.getY() - topLeft.getY() + 1][bottomRight.getX() - topLeft.getX() + 1];
        for (int y = topLeft.getY(); y <= bottomRight.getY(); y++) {
            for (int x = topLeft.getX(); x <= bottomRight.getX(); x++) {
                // 1. Check location in path
                if (path.isLocationInPath(x, y)) {
                    solvedMap[y - topLeft.getY()][x - topLeft.getX()] = path.getSymbolForLocation(x, y);
                    continue;
                }
                // 2. Check obstruction
                Obstacle obstructingObstacle = getObstacleAtLocation(x, y);
                // Calculate the index in the map 2D array
                int j = y - topLeft.getY();
                int i = x - topLeft.getX();
                if (obstructingObstacle != null) {
                    solvedMap[j][i] = obstructingObstacle.getSymbol();
                    continue;
                }
                // 3. Empty space
                solvedMap[j][i] = '.';
            }
        }
        // Convert the map to a string
        return matrixToString(solvedMap);
    }

    /**
     * Returns a Boolean value based on whether a given location is obstructed by an obstacle or not.
     * @param x the x coordinate of the location.
     * @param y the y coordinate of the location.
     * @return this returns true if the provided location is obstructed by an obstacle, otherwise it returns false.
     */
    public boolean isLocationObstructed(int x, int y){
        return getObstacleAtLocation(x, y) != null;
    }
}