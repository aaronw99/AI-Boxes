package searchclient;

import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

public abstract class Heuristic
        implements Comparator<State>
{
    private boolean[][] walls;
    private int[] boxGoalRows;
    private int[] boxGoalCols;
    private char[] boxGoalLetters;
    private int numGoalBoxes;
    private Set<int[]> boxGoals;
    public Heuristic(State initialState)
    {
        // Here's a chance to pre-process the static parts of the level.
        walls = initialState.walls;
        boxGoalRows = initialState.boxGoalRows;
        boxGoalCols = initialState.boxGoalCols;
        boxGoalLetters = initialState.boxGoalLetters;
        numGoalBoxes = boxGoalRows.length;
        boxGoals = new HashSet<int[]>();
        for(int i = 0; i < numGoalBoxes; i++) {
            int[] arrayToAdd = {boxGoalRows[i], boxGoalCols[i], Character.getNumericValue(boxGoalLetters[i])};
            boxGoals.add(arrayToAdd);
        }
    }

//    public int h(State s)
//    {
//        // first entry in each sublist is box, subsequent are goals
//        List<List<int[]>> graph = new ArrayList<>();
//        for(int i = 0; i < s.boxes.length; i++){
//            for(int j = 0; j < s.boxes[0].length; j++){
//                // once a box is found, add it and each of its goal to map
//                if(Character.isLetter(s.boxes[i][j])){
//                    List<int[]> box_to_goals = new ArrayList<>();
//                    int[] box = new int[]{i,j};
//                    box_to_goals.add(box);
//                    for(int k = 0; k < numGoalBoxes; k++){
//                        if(boxGoalLetters[k] == s.boxes[i][j]){
//                            int[] goal = new int[]{boxGoalRows[k],boxGoalCols[k]};
//                            box_to_goals.add(goal);
//                        }
//                    }
//                    graph.add(box_to_goals);
//                }
//            }
//        }
//
//        int cost = 0;
//        // minimize total costs between boxes and goals,
//        // but make sure boxes aren't aiming for same goals
//        Set<int[]> usedGoals = new HashSet<>();
//        for(List<int[]> cur : graph){
//            int[] box = cur.get(0);
//            int boxRow = box[0];
//            int boxCol = box[1];
//            int minDist = Integer.MAX_VALUE;
//            int[] minGoal = new int[2];
//            for(int i = 1; i < cur.size(); i++){
//                int[] goal = cur.get(i);
//                int goalRow = goal[0];
//                int goalCol = goal[1];
//                int manDist = Math.abs(goalRow-boxRow) + Math.abs(goalCol-boxCol);
//                if(manDist<minDist){
//                    minGoal[0] = goalRow;
//                    minGoal[1] = goalCol;
//                    if(usedGoals.contains(minGoal)){
//                        continue;
//                    }
//                    minDist = manDist;
//                }
//            }
//            cost += minDist;
//            usedGoals.add(minGoal);
//        }
//        return cost;
//    }


    public int h(State s) {
        int cost = 0;
        Set<int[]> boxes = new HashSet<int[]>();
        for (int i = 0; i < s.boxes.length; i++) {
            for (int j = 0; j < s.boxes[0].length; j++) {
                if (Character.isLetter(s.boxes[i][j])) {
                    int[] arrayToAdd = {i, j, Character.getNumericValue(s.boxes[i][j])};
                    boxes.add(arrayToAdd);
                }
            }
        }
        Set<int[]> filledGoals = new HashSet<int[]>(boxes);
        filledGoals.retainAll(boxGoals);
        //remove goals already with a box from the set
        boxGoals.removeAll(filledGoals);
        //remove boxes already in their goal from the set
        boxes.removeAll(filledGoals);

        for (int[] box : boxes) {
            int overallDist = Integer.MAX_VALUE;
            for (int[] boxGoal : boxGoals) {
                if (boxGoal[2] == box[2]) {  //checking if the characters are the same
                    int manDist = Math.abs(box[0] - boxGoal[0]) + Math.abs(box[1] - boxGoal[1]);
                    overallDist = manDist < overallDist ? manDist : overallDist;
                }
            }
            cost += overallDist;
        }
        return cost;
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
