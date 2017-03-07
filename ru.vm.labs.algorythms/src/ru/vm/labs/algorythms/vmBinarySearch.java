package ru.vm.labs.algorythms;


/*
 * Could be applied only to sorted arrays!
 */
public class vmBinarySearch {
	
	
	public static int SearchRecursion(int[] arrA, int low,int high, int number){
		if(low>high){
			return -1;
		}
		
		int mid = low + ((high - low) / 2);
		
		if(arrA[mid]==number) // if arrA[mind] equals number returns a value of mid
				return mid;
		else if (arrA[mid]>number) // if arrA[mind] is greater than number, search right recursivly
				return SearchRecursion(arrA, low,mid-1,number);
		else 
			return SearchRecursion(arrA, mid+1,high,number);
	}
	
	
	public static int Search(int[] a, int b) {
        if (a.length == 0) {
                return -1;
        }
        int low = 0;
        int high = a.length-1;

        while(low <= high ) {
                int middle = (low+high) /2;
                if (b> a[middle] ){
                        low = middle +1;
                } else if (b< a[middle]){
                        high = middle -1;
                } else { // The element has been found
                        return middle;
                }
        }
        return -1;
}

}
