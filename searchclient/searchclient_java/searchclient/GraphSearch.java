package searchclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class GraphSearch {

    public static Action[][] search(State initialState, Frontier frontier)
    {
        boolean outputFixedSolution = false;

        if (outputFixedSolution) {
            //Exercise 1.1:
            //The agents will perform the sequence of actions returned by this method.
            //Try to solve a few levels such as SAD1 and SAD2 by hand and entering them below:

            return new Action[][] {
                {Action.PushSS},
                {Action.PushSS},
                {Action.MoveE},
                {Action.MoveE},
                {Action.MoveE},
                {Action.MoveE},
                {Action.MoveE},
                {Action.MoveE},
                {Action.MoveE},
                {Action.MoveE},
                {Action.MoveE},
                {Action.MoveE},
                {Action.MoveE},
                {Action.MoveE},
                {Action.MoveE},
                {Action.MoveE},
                {Action.MoveE},
                {Action.MoveE},
                {Action.PushSS}
            };
        } else {
            //Exercise 1.2:
            //Now try to implement the Graph-Search algorithm from R&N figure 3.7
            //In the case of "failure to find a solution" you should return null.
            //Some useful methods on the state class which you will need to use are:
            //state.isGoalState() - Returns true if the state is a goal state.
            //state.extractPlan() - Returns the Array of actions used to reach this state.
            //state.getExpandedStates() - Returns an ArrayList<State> containing the states reachable from the current state.
            //You should also take a look at Frontier.java to see which methods the Frontier interface exposes


            int iterations = 0;

            frontier.add(initialState);
            HashSet<State> explored = new HashSet<>();

            while (true) {
                //Print a status message every 10000 iteration
                if (++iterations % 10000 == 0) {
                    printSearchStatus(explored, frontier);
                }
                // If the frontier is empty, all possible states have been checked and none were
                // the goal state. There is no solution.
                if (frontier.isEmpty()) {
                    return null;
                }

                State cur = frontier.pop();
                // If the state we just took from the frontier is the goal state, we have found
                // the solution
                if (cur.isGoalState()) {
                    return cur.extractPlan();
                }

                // If we did not return in the previous statement, the current state is not the
                // goal state. We keep track of it so as not to explore it again later.
                explored.add(cur);
                // We expand the current state to possibilities that follow and add them to the
                // frontier for further exploration if they were not already explored or are not
                // contained in the frontier already.
                ArrayList<State> newStates = cur.getExpandedStates();
                for (State newState : newStates) {
                    if (!frontier.contains(newState) && !explored.contains(newState)) {
                        frontier.add(newState);
                    }
                }
            }

        }
    }

    private static long startTime = System.nanoTime();

    private static void printSearchStatus(HashSet<State> explored, Frontier frontier)
    {
        String statusTemplate = "#Explored: %,8d, #Frontier: %,8d, #Generated: %,8d, Time: %3.3f s\n%s\n";
        double elapsedTime = (System.nanoTime() - startTime) / 1_000_000_000d;
        System.err.format(statusTemplate, explored.size(), frontier.size(), explored.size() + frontier.size(),
                elapsedTime, Memory.stringRep());
    }
}
