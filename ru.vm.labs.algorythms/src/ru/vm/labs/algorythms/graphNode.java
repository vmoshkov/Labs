package ru.vm.labs.algorythms;

import java.util.LinkedList;

public class graphNode {
	
	// value of node
	public String vertexValue;
	
	// Array  of adjacent Nodes
    public LinkedList<graphNode> adjList;
    
    // Constructor
    public graphNode(String value)
    {
    	vertexValue = value;
    	adjList = new LinkedList();        
    }
    
    //Function to add an edge into the graph
    void addEdge(graphNode node)
    {
    	// Add w to v's list.
    }

}
