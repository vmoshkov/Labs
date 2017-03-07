package ru.vm.labs.algorythms;

public class vmMergeSort {
	
	// main method: returns sorted array
	 public static int[] mergesort(int[] input) {
		    int N = input.length;  //length of an array;
		    
	        if (N <= 1) return input; // only 1 element in array => stop recursion!
	        
	        int[] a = new int[N/2];  // left helper array
	        int[] b = new int[N - N/2]; // right helper array
	        
	        //init helper arrays;	        
	        for (int i = 0; i < a.length; i++)
	            a[i] = input[i];
	        for (int i = 0; i < b.length; i++)
	            b[i] = input[i + N/2];
	        
	        // pre-condition to merge: sub-arrays must be sorted or a single element array.
	        // if so merge of sorted arrays + recursion;
	        return merge(mergesort(a), mergesort(b));
	    }
	
	private static int[] merge(int[] a, int[] b) {
		//the array which  To merge
        int[] c = new int[a.length + b.length];
        
        int i = 0, j = 0;  // i->iterator for the left subarray; j -> iterator for the right subarray;
        
        // k - iterator for the array which  To merge
        for (int k = 0; k < c.length; k++) {
        	// 1) check that i & j not greater than an arrays length;
            if (i >= a.length) c[k] = b[j++];
            else if (j >= b.length) c[k] = a[i++];
            // 2) if i & j less than an arrays length, compare next values and increment iterators;
            // insert at k position of an "array To merge" the smallest value(a[i] or b[j])
            else if 
            	(a[i] <= b[j])  c[k] = a[i++];  // value from a is smaller than value in b and increment i
            else                    
            	c[k] = b[j++]; // value in b is smaller than value in a and increment j
        }
        return c;
    }

	 public static void printNumbers(int[] input) {
         
	        for (int i = 0; i < input.length; i++) {
	            System.out.print(input[i] + ", ");
	        }
	        System.out.println("\n");
	 }


   /***************************************************************************
    *  Check if array is sorted - useful for debugging.
    ***************************************************************************/
    private static boolean isSorted(double[] a) {
        for (int i = 1; i < a.length; i++)
            if (a[i] < a[i-1]) return false;
        return true;
    }


}
