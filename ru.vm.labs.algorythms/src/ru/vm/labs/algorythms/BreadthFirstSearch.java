package ru.vm.labs.algorythms;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

public class BreadthFirstSearch {
	
    // prints BFS traversal from a given source s
    public static void BFS(adjancyListGraph g, Vertex sourceNode, Vertex nodeToFind)
    {
        // Mark all the vertices as not visited(By default
        // set as false)
    	HashMap vistedVertices = new HashMap<>();
     	Vertex[] vertexArr = g.getVertices(); 
     	for (int i=0; i<vertexArr.length; i++)
     	{
     		vistedVertices.put(vertexArr[i], false);
     	}
 
        // Create a queue for BFS
        LinkedList<Vertex> queue = new LinkedList<Vertex>();
 
        // Mark the current node as visited and enqueue it
        vistedVertices.put(sourceNode, true);
        
        queue.add(sourceNode);
        
        int loop = 1;
 
        while (queue.size() != 0)
        {
        	System.out.print("; loop#" + loop++ + " -> ");
        	
            // Dequeue a vertex from queue and print it
            Vertex currentNode = queue.poll();
            
            System.out.print(currentNode +" ");
 
            // Get all adjacent vertices of the dequeued vertex s
           Stack<Vertex> adjVertices = g.getAdjacencyListForVertex(currentNode);
           
           // check vertices
           for(Vertex vert: adjVertices)
           {
        	   // the 1st elem in a list is a vertex itself, not adjacent vertices.
           		if (vert.equals(currentNode))
           			continue;
           	
           		boolean isvisitedNode = (boolean) vistedVertices.get(vert);
           	
           		// if an adjacent node is not visited add it to a queue
	           	if(!isvisitedNode)
	           	{	
	           		//handle this node (for instance, compare values and mark it as visited
	           		if (vert.equals(nodeToFind))
	           		{
	           			System.out.println("Node " + vert + " found!");
	           		}
	           		vistedVertices.put(vert, true);
	           		queue.add(vert);
	           	}
           }            
        }
    }
    
    public static void testMe()
    {
    	System.out.println("Breadth First Search: ");
    	
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
    	
    	BFS(graph, v1, v6);
    }


}
