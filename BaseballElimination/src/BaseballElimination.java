import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import java.util.ArrayList;

public class BaseballElimination {
    private final int N;
    private ArrayList<String> t;
    private final int[] w, l, r;
    private final int[][] g;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        N = in.readInt();
        t = new ArrayList<>();
        w = new int[N];
        l = new int[N];
        r = new int[N];
        g = new int[N][N];
        for (int i = 0; i < N; i++) {
            t.add(i, in.readString());
            w[i] = in.readInt();
            l[i] = in.readInt();
            r[i] = in.readInt();
            for (int j = 0; j < N; j++) {
                g[i][j] = in.readInt();
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        System.out.println("dd");
        return N;
    }

    // all teams
    public Iterable<String> teams() {
        return t;
    }

    // number of wins for given team
    public int wins(String team) {
        if (!t.contains(team)) throw new IllegalArgumentException("calling wins method with invalid team name");
        return w[t.indexOf(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (!t.contains(team)) throw new IllegalArgumentException("calling losses method with invalid team name");
        return l[t.indexOf(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (!t.contains(team)) throw new IllegalArgumentException("calling remaining method with invalid team name");
        return r[t.indexOf(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!t.contains(team1) || !t.contains(team2))
            throw new IllegalArgumentException("calling against method with invalid team name");
        return g[t.indexOf(team1)][t.indexOf(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (!t.contains(team)) throw new IllegalArgumentException("calling isEliminated method with invalid team name");
        int trivialElimination = trivialEliminated(team);
        if (trivialElimination != -1) return true;
        ArrayList<String> certificateTeams = nonTrivialEliminated(team);
        if (certificateTeams == null) return false;
        return true;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!t.contains(team))
            throw new IllegalArgumentException("calling certificateOfElimination method with invalid team name");
        ArrayList<String> certificateTeams = new ArrayList<>();
        int trivial = trivialEliminated(team);
        if (trivial != -1) {
            certificateTeams.add(t.get(trivial));
            return certificateTeams;
        }
        certificateTeams = nonTrivialEliminated(team);
        return certificateTeams;
    }

    /**
     * check if the team is trivial eliminated
     * @param team
     * @return
     */
    private int trivialEliminated(String team) {
        int index = t.indexOf(team);
        int maxid = 0;
        int max = w[0];
        for (int i = 0; i < N; i++) {
            if (i != index && w[i] > max) {
                maxid = i;
                max = w[i];
            }
        }
        if (w[index] + r[index] < max) return maxid;
        return -1;
    }

    /**
     * assume the input has already been checked
     *
     * @param team
     * @return
     */
    private ArrayList<String> nonTrivialEliminated(String team) {
        FlowNetwork network = new FlowNetwork((int) (0.5 * N * N - 0.5 * N + 2.0));
        int index = t.indexOf(team);
        int gameVertex;
        for (int team1Index = 0; team1Index < N; team1Index++) {
            if (team1Index == index) continue;
            if (team1Index < index) network.addEdge(new FlowEdge(team1Index + 2, 1, w[index] + r[index] - w[team1Index]));
            if (team1Index > index) network.addEdge(new FlowEdge(team1Index + 1, 1, w[index] + r[index] - w[team1Index]));
            for (int team2Index = team1Index + 1; team2Index < N; team2Index++) {
                if (team2Index == index) continue;
                int gameindex = (int) ((2 * N - 3 - team1Index) * team1Index * 0.5) + team2Index - team1Index-1;
                if (team2Index > index) gameindex--;
                gameVertex = gameindex + (N + 1);
                network.addEdge(new FlowEdge(0, gameVertex, g[team1Index][team2Index]));
                if (team1Index < index) network.addEdge(new FlowEdge(gameVertex, team1Index + 2, Double.POSITIVE_INFINITY));
                if (team1Index > index) network.addEdge(new FlowEdge(gameVertex, team1Index + 1, Double.POSITIVE_INFINITY));
                if (team2Index < index) network.addEdge(new FlowEdge(gameVertex, team2Index + 2, Double.POSITIVE_INFINITY));
                if (team2Index > index) network.addEdge(new FlowEdge(gameVertex, team2Index + 1, Double.POSITIVE_INFINITY));
            }
        }
        FordFulkerson fordFulkerson = new FordFulkerson(network, 0, 1);
        ArrayList<String> certificateTeams = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            if (i != index) {
                if (i < index && fordFulkerson.inCut(i + 2)) certificateTeams.add(t.get(i));
                if (i > index && fordFulkerson.inCut(i + 1)) certificateTeams.add(t.get(i));
            }
        }
        if (certificateTeams.isEmpty()) return null;
        return certificateTeams;
    }

    public static void main(String[] args) {
        ;
    }
}
