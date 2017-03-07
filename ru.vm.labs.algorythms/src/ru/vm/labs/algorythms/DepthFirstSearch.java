package ru.vm.labs.algorythms;

import java.util.HashMap;
import java.util.Stack;

public class DepthFirstSearch {
		 	
	// The function to do DFS traversal. It uses recursive DFSUtil()
    public static void DFS(adjancyListGraph g, Vertex startNode, Vertex nodeToFind)
    {
        // Mark all the vertices as not visited(set as
        // false by default in java)
        HashMap vistedVertices = new HashMap<>();
    	Vertex[] vertexArr = g.getVertices(); 
    	for (int i=0; i<vertexArr.length; i++)
    	{
    		vistedVertices.put(vertexArr[i], false);
    	}
 
       
    	DFSUtil(startNode, nodeToFind, vistedVertices, g);
    	
    }
	
	// A function used by DFS
    public static void DFSUtil(Vertex currentNode, Vertex nodeToFind, HashMap visited, adjancyListGraph g)
    {
        // Mark the current node as visited and print it
        visited.put(currentNode, true);
        System.out.print(currentNode+" ");
        
        if(currentNode.equals(nodeToFind))
        	System.out.println("Node found!");
 
        // Recur for all the vertices adjacent to this vertex
        Stack<Vertex> adjVertices = g.getAdjacencyListForVertex(currentNode);
        
        // if this vertex hasn´t adjacency list, just return
        if (adjVertices==null)
        	return;
        // otherwise recursion
        for(Vertex vert: adjVertices)
        {
        	// the 1st elem in a list is a vertex itself.
        	if (vert.equals(currentNode))
        		continue;
        	
        	boolean isvisitedNode = (boolean) visited.get(vert);
        	
        	if(!isvisitedNode)
        	{
        		DFSUtil(vert, nodeToFind, visited, g);
        	}
        }       
    }
 
    public static void testMe()
    {
    	System.out.println("Depth First Search: ");
    	
    	adjancyListGraph graph = new adjancyListGraph(6);
    	
    	Vertex v1 = new Vertex("A");
    	Vertex v2 = new Vertex("B");
    	Vertex v3 = new Vertex("C");
    	Vertex v4 = new Vertex("D");
    	Vertex v5 = new Vertex("E");
    	Vertex v6 = new Vertex("F");
    	
    	graph.addVertex(v1);
    	graph.addVertex(v2);
    	graph.addVertex(v3);
    	graph.addVertex(v4);
    	graph.addVertex(v5);
    	graph.addVertex(v6);
    	
    	graph.addEdge(v1, v2);
    	graph.addEdge(v1, v5);
    	graph.addEdge(v2, v3);
    	graph.addEdge(v2, v5);
    	graph.addEdge(v2, v6);
    	graph.addEdge(v3, v4);
    	graph.addEdge(v5, v3);
    	graph.addEdge(v5, v4);
    	graph.addEdge(v6, v4);
    	
    	graph.drawGraph();
    	
    	System.out.println("Start searching...");
    	DFS(graph, v1, v4);
    }

}
