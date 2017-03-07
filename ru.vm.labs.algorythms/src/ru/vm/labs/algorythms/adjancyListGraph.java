package ru.vm.labs.algorythms;

import java.util.ArrayList;
import java.util.Stack;

class Vertex
{
	private String vertexData;
	
	public Vertex(String value)
	{
		vertexData = value;
	}
	
	public String toString()
	{
		return vertexData;
	}
}

public class adjancyListGraph {
		
	private int countOfVertices = 0;
	
	// list of vertexes
	private ArrayList<Vertex> list;
	// adjacency list as stack representation
	private Stack<Vertex> adjList[];
	
	public adjancyListGraph(int cnt)
	{
		countOfVertices = cnt;
		list = new ArrayList<>();
		adjList = new Stack[cnt];	
	}
	
	// add new vertex and init new adjacency list
	void addVertex(Vertex vertex)
	{
		if(!list.contains(vertex))
		{
			list.add(vertex);
			
			//search for the first NULL stack for an adjacency list and init it 
			// TODO: just remember the number of next not init element in an adjList array;
    		for(int j=0; j < adjList.length; j++) 
    		{
    			Stack element = adjList[j];
    			
    			if(element==null)
    			{
    				element = new Stack<>();
    				element.add(vertex);
    				    				
    				adjList[j] = element;
    				break; // break the FOR loop
    			}
    		}
		}
	}
	
	//Function to add an edge into the graph
    void addEdge(Vertex vertex,Vertex adjVertex)
    {
    	// if we already define such vertex and adjacency list for it
    	if(list.contains(vertex))
    	{
    		// search for the related adjacency list;
    		// TODO: could be presented as HashMap;
    		for(Stack adj: adjList)
    		{
    			if(adj==null)
    					continue;
    			
    			// if the adjacency list has been define for this vertex;
    			if(adj.get(0).equals(vertex))
    			{
    				adj.add(adjVertex);
    				break; // break the FOR loop
    			}
    			
    		}
    	}    
    }
    
    // illustrate an adjacency list
    public void drawGraph()
    {
    	System.out.println("Print graph:");
    	// for each adjacency list
    	for(Stack adj: adjList)
        {
    		if(adj==null)
    			continue;
    		
    		int size = adj.size();
    		
    		// for each element draw its value
    		for(int i=0; i < size; i++)
    		{
    			Vertex adjVert = (Vertex) adj.get(i);
    			
    			System.out.print(adjVert + " ");
    		}  
    		
    		System.out.print("\n ");
        }
    }
    
    public Vertex[] getVertices()
    {
    	int sizeOfArr = list.size();
    	Vertex[] arr = new Vertex[sizeOfArr];
    	
    	for (int k=0; k<sizeOfArr; k++)
    	{
    		arr[k] = list.get(k);
    	}
    	
    	return arr;    	
    }
    
    public Stack<Vertex> getAdjacencyListForVertex(Vertex v)
    {
    	// search for the related adjacency list;
		for(Stack adj: adjList)
		{
			if(adj==null)
					continue;
			
			// if the adjacency list has been define for this vertex, return it;
			if(adj.get(0).equals(v))
			{
				return adj;
			}			
		}
		
		return null;
    }
    
    public static void testMe()
    {
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
    }

}
