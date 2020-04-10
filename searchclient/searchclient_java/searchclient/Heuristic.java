package searchclient;

import java.util.Comparator;

public abstract class Heuristic
        implements Comparator<State>
{
    private boolean[][] walls;

    private int[] agentGoalRows;
    private int[] agentGoalCols;
    private int numAgents;

    private int[] boxGoalRows;
    private int[] boxGoalCols;
    private char[] boxGoalLetters;
    private int numGoalBoxes;

    public Heuristic(State initialState)
    {
        // Here's a chance to pre-process the static parts of the level.
        walls = initialState.walls;
        agentGoalRows = initialState.agentGoalRows;
        agentGoalCols = initialState.agentGoalCols;
        numAgents = this.agentGoalRows.length;
        boxGoalRows = initialState.boxGoalRows;
        boxGoalCols = initialState.boxGoalCols;
        boxGoalLetters = initialState.boxGoalLetters;
        numGoalBoxes = this.boxGoalRows.length;
    }

    public int h(State s)
    {
        int cost = 0;
        // check how close agents are to their goals
        // euclidean distance for now, just to get an idea
        // would be better to use distance among cells that can be travelled to
        for(int i = 0; i < numAgents; i++){
            int goalRow = agentGoalRows[i];
            int goalCol = agentGoalCols[i];
            int curRow = s.agentRows[i];
            int curCol = s.agentCols[i];
            cost += Math.sqrt(Math.pow((goalRow - curRow), 2) + Math.pow((goalCol - curCol),2));
        }

        // check how close boxes are to their goals
        // euclidean distance for now, just to get an idea
        // would be better to use distance among cells that can be travelled to
        for(int i = 0; i < numGoalBoxes; i++){
            int goalRow = boxGoalRows[i];
            int goalCol = boxGoalCols[i];
            char goalLet = boxGoalLetters[i];
            int[] closestBox = findClosestBox(s,goalRow,goalCol,goalLet);
            int closestBoxRow = closestBox[0];
            int closestBoxCol = closestBox[1];
            cost += Math.sqrt(Math.pow((goalRow - closestBoxRow), 2) + Math.pow((goalCol - closestBoxCol),2));
        }

        return cost;
    }

    private static int[] findClosestBox(State s, int row, int col, char let){
        // check if the row and cal already has a box
        if(s.boxes[row][col] == let){
            return new int[]{row,col};
        }

        int maxRows = s.boxes.length;
        int maxCols = s.boxes[0].length;

        int up = row;
        int down = row;
        int left = col;
        int right = col;

        int foundRow = -1;
        int foundCol = -1;

        boolean found = false;
        while(!found){
            // check left and right border
            for(int i = up; i <= down; i++){
                char leftCell = s.boxes[i][left];
                char rightCell = s.boxes[i][right];
                if(leftCell == let){
                    foundRow = i;
                    foundCol = left;
                    found=true;
                    break;
                }
                if(rightCell == let){
                    foundRow = i;
                    foundCol = right;
                    found=true;
                    break;
                }
            }
            // check top and bottom border
            for(int i = left; i <= right; i++){
                char topCell = s.boxes[up][i];
                char botCell = s.boxes[down][i];
                if(topCell == let){
                    foundRow = up;
                    foundCol = i;
                    found=true;
                    break;
                }
                if(botCell == let){
                    foundRow = down;
                    foundCol = i;
                    found=true;
                    break;
                }
            }

            up = up>0 ? up-1 : up;
            down = down<maxRows-1 ? down+1 : down;
            left = left>0 ? left-1 : left;
            right = right<maxCols-1 ? right+1 : right;
        }
        return new int[]{foundRow,foundCol};
    }

    public abstract int f(State s);

    @Override
    public int compare(State s1, State s2)
    {
        return this.f(s1) - this.f(s2);
    }
}

class HeuristicAStar
        extends Heuristic
{
    public HeuristicAStar(State initialState)
    {
        super(initialState);
    }

    @Override
    public int f(State s)
    {
        return s.g() + this.h(s);
    }

    @Override
    public String toString()
    {
        return "A* evaluation";
    }
}

class HeuristicWeightedAStar
        extends Heuristic
{
    private int w;

    public HeuristicWeightedAStar(State initialState, int w)
    {
        super(initialState);
        this.w = w;
    }

    @Override
    public int f(State s)
    {
        return s.g() + this.w * this.h(s);
    }

    @Override
    public String toString()
    {
        return String.format("WA*(%d) evaluation", this.w);
    }
}

class HeuristicGreedy
        extends Heuristic
{
    public HeuristicGreedy(State initialState)
    {
        super(initialState);
    }

    @Override
    public int f(State s)
    {
        return this.h(s);
    }

    @Override
    public String toString()
    {
        return "greedy evaluation";
    }
}
