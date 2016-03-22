package newlang8;

public class Node {
    NodeType type;
    Environment env;
    
    public Node next;

    /** Creates a new instance of Node */
    public Node() {
    }
    public Node(NodeType my_type) {
        type = my_type;
    }
    public Node(Environment my_env) {
        env = my_env;
    }
    
    public NodeType getType() {
        return type;
    }
    
    public boolean Parse() {
        return true;
    }
    
    public Value getValue() {
        return null;
    }
 
    public String toString() {
    	if (type == NodeType.END) return "END";
    	else return "Node";        
    }

}
