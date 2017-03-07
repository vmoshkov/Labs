package ru.vm.labs.algorythms;

/*
 * !!!! http://www.geeksforgeeks.org/top-algorithms-and-data-structures-for-competitive-programming/ -> тут много примеров
 * 
 * Sorting algorythms:
 * 1) Bubble Sort
 * 2) Selection Sort
 * 3) Insertion Sort
 * 4) Quick Sort
 * 5) Merge Sort
 * 6) Heap Sort
 * 
 * Search algorythms:
 * 1) Iterative Binary Search
 * 2) Recursive Binary Search - http://algorithms.tutorialhorizon.com/binary-search/
 * 3) Breadth First Search
 * 4) Depth First Search
 * 
 * 
 * Binary Tree
 * 
 * 1) BST - http://algorithms.tutorialhorizon.com/binary-search-tree-complete-implementation/
 * 
- Count occurrences of a number in a sorted array Java (не понял!)
  		http://algorithms.tutorialhorizon.com/find-the-number-of-occurrences-of-a-number-in-a-given-sorted-array/
- How many times is a sorted array rotated Java (не понял!)
- Search element in a circular sorted array Java (не понял!) 
 
 
 Graph Algorithms

    Breadth First Search (BFS) - http://www.geeksforgeeks.org/breadth-first-traversal-for-a-graph/
    Depth First Search (DFS) - http://www.geeksforgeeks.org/depth-first-traversal-for-a-graph/
    Shortest Path from source to all vertices **Dijkstra** 
    		- http://www.geeksforgeeks.org/greedy-algorithms-set-6-dijkstras-shortest-path-algorithm/  (matrix graph represenation)
    		- http://www.geeksforgeeks.org/greedy-algorithms-set-7-dijkstras-algorithm-for-adjacency-list-representation/
    		- http://www.vogella.com/tutorials/JavaAlgorithmsDijkstra/article.html
    Shortest Path from every vertex to every other vertex **Floyd Warshall**
    		 - http://www.geeksforgeeks.org/dynamic-programming-set-16-floyd-warshall-algorithm/
    
    
 * - Memoization Algorithm Java		http://www.javabrahman.com/gen-java-programs/recursive-fibonacci-in-java-with-memoization/
- DP Algorithm Java http://algorithms.tutorialhorizon.com/introduction-to-dynamic-programming-fibonacci-series/

 - adjacency matrix directed graph java

- Dijkstra Algorythm Java http://www.vogella.com/tutorials/JavaAlgorithmsDijkstra/article.html
- Towers of Hanoi Java http://www.vogella.com/tutorials/JavaAlgorithmsTowersOfHanoi/article.html

- Stack Java
- Stack with a method to search for the smallest element Java
 */

public class Launch {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int[] input = { 4, 2, 9, 6, 23, 12, 34, 0, 1, 55, 2, 4,12, 21 };
        //vmBubbleSort.bubble_srt(input);
        
        
        //vmInsertionSort.insertionSort(input);
        
        long startTime = System.currentTimeMillis();
        
        vmQuickSort.quickSort(input);

        int[] sortedArr = input; //vmMergeSort.mergesort(input);
        
        vmQuickSort.quickSort(input);

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Quick sort: " + elapsedTime);     
        vmMergeSort.printNumbers(sortedArr);
        
        System.out.println("Position of 9 in the sorted array is: " + vmBinarySearch.Search(sortedArr, 9));
        
        System.out.println("Position of 21 in the sorted array is: " + 
        					vmBinarySearch.SearchRecursion(sortedArr, 0, sortedArr.length-1, 21));
        
        vmBinarySearchTree b = new vmBinarySearchTree();
		b.insert(3);b.insert(8);
		b.insert(1);b.insert(4);b.insert(6);b.insert(2);b.insert(10);b.insert(9);
		b.insert(20);b.insert(25);b.insert(15);b.insert(16);
		System.out.println("Original Tree : ");
		b.display(b.root);		
		System.out.println("");
		System.out.println("Check whether Node with value 4 exists : " + b.find(4));
		System.out.println("Delete Node with no children (2) : " + b.delete(2));		
		b.display(b.root);
		System.out.println("\n Delete Node with one child (4) : " + b.delete(4));		
		b.display(b.root);
		System.out.println("\n Delete Node with Two children (10) : " + b.delete(10));		
		b.display(b.root);
		
		System.out.println("\n");
		
		adjancyListGraph.testMe();
		System.out.println("\n");
		
		DepthFirstSearch.testMe();
		System.out.println("\n");
		
		BreadthFirstSearch.testMe();
		
		vmHeapSort.testMe();
		
		FindJob.TestMe();

	}

}
