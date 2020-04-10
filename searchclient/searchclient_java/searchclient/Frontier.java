package searchclient;

import java.util.ArrayDeque;
import java.util.Stack;
import java.util.HashSet;
import java.util.PriorityQueue;

public interface Frontier
{
    void add(State state);
    State pop();
    boolean isEmpty();
    int size();
    boolean contains(State state);
    String getName();
}

class FrontierBFS
        implements Frontier
{
    private final ArrayDeque<State> queue = new ArrayDeque<>(65536);
    private final HashSet<State> set = new HashSet<>(65536);

    @Override
    public void add(State state)
    {
        this.queue.addLast(state);
        this.set.add(state);
    }

    @Override
    public State pop()
    {
        State state = this.queue.pollFirst();
        this.set.remove(state);
        return state;
    }

    @Override
    public boolean isEmpty()
    {
        return this.queue.isEmpty();
    }

    @Override
    public int size()
    {
        return this.queue.size();
    }

    @Override
    public boolean contains(State state)
    {
        return this.set.contains(state);
    }

    @Override
    public String getName()
    {
        return "breadth-first search";
    }
}

class FrontierDFS
        implements Frontier
{
    private final ArrayDeque<State> stack = new ArrayDeque<>(65536);
    private final HashSet<State> set = new HashSet<>(65536);

    @Override
    public void add(State state)
    {
        this.stack.addFirst(state);
        this.set.add(state);
    }

    @Override
    public State pop()
    {
        State state = this.stack.pollFirst();
        this.set.remove(state);
        return state;

    }

    @Override
    public boolean isEmpty()
    {
        return this.stack.isEmpty();
    }

    @Override
    public int size()
    {
        return this.stack.size();
    }

    @Override
    public boolean contains(State state)
    {
        return this.set.contains(state);
    }

    @Override
    public String getName()
    {
        return "depth-first search";
    }
}

class FrontierBestFirst
        implements Frontier
{
    // each state has a f; the lowest f is expanded first
    private Heuristic heuristic;
    private PriorityQueue<State> frontier;
    private HashSet<State> set;

    public FrontierBestFirst(Heuristic h)
    {
        this.heuristic = h;
        this.frontier  = new PriorityQueue<>(65536, heuristic);
        this.set = new HashSet<>(65536);
    }

    @Override
    public void add(State state)
    {
        this.frontier.add(state);
        this.set.add(state);
    }

    @Override
    public State pop()
    {
        State state = this.frontier.poll();
        this.set.remove(state);
        return state;
    }

    @Override
    public boolean isEmpty()
    {
        return this.frontier.isEmpty();
    }

    @Override
    public int size()
    {
        return this.frontier.size();
    }

    @Override
    public boolean contains(State state)
    {
        return this.set.contains(state);
    }

    @Override
    public String getName()
    {
        return String.format("best-first search using %s", this.heuristic.toString());
    }
}
