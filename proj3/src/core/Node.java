package core;

public class Node implements Comparable<Node>{
    int x;
    int y;
    Node prev;
    public Node(int x, int y, Node prevNode) {
        this.x = x;
        this.y = y;
        this.prev = prevNode;
    }
    public int compareTo(Node other) {

        return (int) (Math.round(this.distance()) - Math.round(other.distance()));
    }
    private double distance() {
        int dx = this.x - World.avatarX;
        int dy = this.y - World.avatarY;
        return Math.sqrt(dx*dx + dy*dy);
    }
}
