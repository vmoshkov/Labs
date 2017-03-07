package ru.vm.labs.algorythms;

/*
 * Source from http://www.geeksforgeeks.org/convert-min-heap-to-max-heap/
 */
public class vmHeapSort {
	
	// This function basically builds max heap
	public static void convertMaxHeap(int arr[], int size)
	{
	    // Start from bottommost and rightmost
	    // internal mode and heapify all internal
	    // modes in bottom up way
	    for (int i = (size-2)/2; i >= 0; --i)
	        MaxHeapify(arr, i, size);
	}
	
	// to heapify a subtree with root at given index
	private static void MaxHeapify(int arr[], int i, int size)
	{
	    int l = 2*i + 1;
	    int r = 2*i + 2;
	    int largest = i;
	    if (l < size && arr[l] > arr[i])
	        largest = l;
	    if (r < size && arr[r] > arr[largest])
	        largest = r;
	    if (largest != i)
	    {
	        //swap(arr[i], arr[largest]);
	        int temp = arr[i];
	        arr[i] = arr[largest];
	        arr[largest] = temp;
	        MaxHeapify(arr, largest, size);
	    }
	}
	
	// create min heap;
    public static void convertMinHeap(int arr[])

    {
    	int size = arr.length;
    	
        for (int pos = (size-1) /*(size / 2)*/; pos >= 0 ; pos--)
        {
        	// if it is not a leaf
        	if (!(pos >=  (size / 2)  &&  pos <= size))
        		minHeapify(arr, pos);
        }
    }	
	
	private static void minHeapify(int arr[], int pos)
	{
	        int left  = leftChild(pos);
	        int right = rightChild(pos);
	        if (arr[pos] > arr[left] || arr[pos] > arr[right]){
	            if (arr[left] < arr[right]){
	                swap(arr, pos, left);
	                pos = left;
	            } else {
	                swap(arr, pos, right);
	                pos = right;
	            }
	        } else
	           return; 
	}
	
    private static int parent(int pos)
    {
        return pos / 2;
    }

    private static int leftChild(int pos)
    {
        return (2 * pos);
    }
 
    private static int rightChild(int pos)
    {
       return (2 * pos) + 1;
    }
    
    private static void swap(int arr[], int fpos, int spos)
    {
        int tmp;
        tmp = arr[fpos];
        arr[fpos] = arr[spos];
        arr[spos] = tmp;
    }
    
    public void insertToMinHeap(int element)
    {
    	/*
        Heap[++size] = element;
        int current = size;
        while (Heap[current] < Heap[parent(current)])
        {
           swap(current,parent(current));
           current = parent(current);
        }	
        */
    }
	
    public int removeFromMinHeap(int[] arr)
    {
    	/*
        int popped = Heap[FRONT];
        Heap[FRONT] = Heap[size--]; 
        minHeapify(FRONT);
        return popped;
		*/
    	return -1;
    }
    
	
	public static void testMe()
	{
		System.out.println("\nHeap Sort: ");
		
		// array representing Min Heap
	    //int arr[] = {3, 5, 9, 6, 8, 20, 10, 12, 18, 9};
		int arr[] = {4, 16, 12, 4, 8, 3, 11, 1, 19, 14};
	    
	    // print initial array
	    System.out.print(" Initial array: ");
	    for(int i=0; i<arr.length; i++)
	    {
	    	System.out.print(arr[i] + " ");
	    }
	    
	    convertMaxHeap(arr, arr.length);
	    
	    System.out.print("\n Max Heap array : ");
	    for(int i=0; i<arr.length; i++)
	    {
	    	System.out.print(arr[i] + " ");
	    }
	    
	    convertMinHeap(arr);
	    
	    System.out.print("\n Min Heap array : ");
	    for(int i=0; i<arr.length; i++)
	    {
	    	System.out.print(arr[i] + " ");
	    }
	}
}
