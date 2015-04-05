/* CS 146 Programming Project 2
 * Creates and manipulates a minmax heap based on file input.
 * Matthew Sziklay
 */
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;


public class minMaxHeap {

	    private static int[] heap;
	    private static int i=0;
	    private static int next = 1;
	    private static Scanner lineScan;
	    private static int [] toInsert;
	    private static int j = 0;
		public static void main(String[] args) {
		heap = new int[100];
		String fName = args[0];
	//		String fName= "test.txt"; Used for testing, no longer necessary.
			System.out.println("Retrieving file " + fName);
			try{
			Scanner file = new Scanner(new FileReader(fName)); 
			while(file.hasNextLine()){ 
				String line = file.nextLine();
				lineScan = new Scanner(line);
				while(lineScan.hasNext()){ //Scan through each line for specific inputs.
				String nextInput = lineScan.next();
				if(nextInput.equalsIgnoreCase("buildMinMaxHeap")){
					System.out.println("Building MinMaxHeap");
					heap = new int[100];
					lineScan.next(); //Dump the : 
					toInsert = new int [100]; 
					j=0;
					while(lineScan.hasNext()){
						 toInsert[j] = Integer.parseInt(lineScan.next().toString().replace(",", ""));
						j++;
					}
					buildMinMaxHeap(toInsert);
				}
				
				if(nextInput.equalsIgnoreCase("peekMin")){
					if(next!=1)
					System.out.println("Peeking at min: " + peekMin());
					else{
						System.err.println("ERROR: Heap is empty.");
						System.exit(-1);
					}
				}
				
				if(nextInput.equalsIgnoreCase("peekMax")){
					if(next!=1)
						System.out.println("Peeking at max: " + peekMax());
					else{
						System.err.println("ERROR: Heap is empty.");
						System.exit(-1);
					}
				}
				
				if(nextInput.equalsIgnoreCase("deleteMin")){
					if(next !=1) //Nonempty case
						System.out.println("Deleting min: " + deleteMin());
					else{
						System.err.println("ERROR: Heap is empty.");
						System.exit(-1);
					}
				}
				
				if(nextInput.equalsIgnoreCase("deleteMax")){
					if(next !=1)
						System.out.println("Deleting max: " + deleteMax());
					else{
						System.err.println("ERROR: Heap is empty.");
						System.exit(-1);
					}
				}
				
				if(nextInput.equalsIgnoreCase("Insert")){
					insert(lineScan.nextInt());
				}
				
				if(nextInput.equalsIgnoreCase("printMinMaxHeap")){
					if(next !=1){
					System.out.println("Printing heap");
					printMinMaxHeap();
					}
					else{
						System.err.println("ERROR: No heap to print.");
						System.exit(-1);
					}
				}
			}
				lineScan.close();
			}
			file.close();
			}catch(FileNotFoundException i){
				System.err.println("File not found.");
		}
		}
		
		public static void buildMinMaxHeap(int [] array){
			for(int k=0;k<j;k++){
				int nextInt = array[k];
				insert(nextInt);
			}
		}



	    public static int size() {
	        return next - 1;
	    }

	    public static boolean insert(int s) {
	    	System.out.println("Inserting " + s);
	        if (next < 100) { //If heap still has space
	            heap[next++] = s;
	            percolateUp(next-1);
	            return true;
	        } else
	        	return false; //Do nothing if heap has no space.
	    }

	    public static int peekMax() {
	        if(next==1) //Heap empty.
	        	return -1;
	        else if (next == 2) //1 element; return it
	        	return heap[1];
	        else if (next >= 3) //2 or more elements. Return the larger of 2 or 3
	        	if(heap[2] > heap[3])
	        		return heap[2];
	        	else
	        		return heap[3];
	        else
	        	return -1;
	    }

	    public static int peekMin() {
	        if(next == 1) ///Heap empty. Should never occur with added check for length.
	        	return -1;
	        else
	        	return heap[1];
	    }

	    public static int deleteMax() {
	        if (next == 1) return -1; //Empty heap; return error.

	        if (next == 2) { //1 element; return heap[1]
	            --next;
	            i--;
	            return heap[1];
	        }

	        if (next == 3) { //2 elements; return 2nd.
	            --next;
	            i--;
	            return heap[2];
	        }

	        if (heap[2] > heap[3]) { //Delete 2, insert last item into hole, percolate down
	            int max = heap[2];
	            heap[2] = heap[--next];
	            i--;
	            percolateDownMax(2);
	            return max;
	        } else { //Delete 3, insert last item into hole, percolate down.
	            int max = heap[3];
	            heap[3] = heap[--next];
	            i--;
	            percolateDownMax(3);
	            return max;
	        }
	    }


	    public static int deleteMin() {
	        if (next == 1) 
	        	return -1; //Empty heap. Return error.

	        if (next == 2) {
	            next = 1; //1 element. Return 1.
	            return heap[1]; 
	        }

	        int min = heap[1]; //Store 1 to return later. 
	        heap[1] = heap[--next]; //Put last item in heap at top and percolate it down.
	        percolateDownMin(1);
	        return min;
	    }

	    public static void percolateUp(int index) {
	        if (!hasParent(index)) return; //Do nothing if empty.
	        int parent = parent(index);
	        if (onMinLevel(index)) {
	            if (heap[index] > heap[parent]) { //If item is greater than parent
	                swap(index,parent); //swap them
	                percolateUpMax(parent);
	            } else {
	                percolateUpMin(index);
	            }
	        } else {  // on max level
	            if (heap[index] < heap[parent]) {
	                swap(index,parent);
	                percolateUpMin(parent);
	            } else {
	                percolateUpMax(index);
	            }
	        }
	    }

	    public static void percolateUpMin(int index) {//Percolate up only through min levels
	        while (true) {
	            if (!hasParent(index)) return;
	            int parent = parent(index);
	            if (!hasParent(parent)) return;
	            int grandparent = parent(parent);
	            if (heap[index]
	                >= heap[grandparent]) return;
	            swap(index,grandparent);
	            index = grandparent;
	        }
	    }

	    public static void percolateUpMax(int index) {//Percolate up only through max levels
	        while (true) {
	            if (!hasParent(index)) return;
	            int parent = parent(index);
	            if (!hasParent(parent)) return;
	            int grandparent = parent(parent);
	            if (heap[index]
	                <= heap[grandparent]) return;
	            swap(index,grandparent);
	            index = grandparent;
	        }
	    }


		public static boolean onMinLevel(int index){ //True for min level
			if(index >= 64 || index == 1 || (index>=16 && index <=31) 
					|| (index>=4 && index <=7))
				return true;
			else return false;
		}

	   public static void percolateDown(int index) {
	        if (noChildren(index)) return;
	        if (onMinLevel(index))
	            percolateDownMin(index);
	        else
	            percolateDownMax(index);
	    }

	   public static void percolateDownMin(int index) { //Percolate down through min levels only.
	        while (leftChild(index) < next) { // has children
	            int minDescendent = minDescendent(index);
	            if (isChild(index,minDescendent)) {
	                if (heap[minDescendent] < heap[index])
	                    swap(minDescendent,index);
	                return;
	            } else {  // is grand child
	                if (heap[minDescendent] >= heap[index])
	                    return;
	                swap(minDescendent,index);
	                int parent = parent(minDescendent);
	                if (heap[minDescendent] > heap[parent])
	                    swap(minDescendent,parent);
	                index = minDescendent;
	            }
	        }
	    }

	    public static void percolateDownMax(int index) { //Percolate down through max levels only.
	        while (leftChild(index) < next) {
	            int maxDescendentIndex = maxDescendent(index);
	            if (isChild(index,maxDescendentIndex)) {
	                if (heap[maxDescendentIndex] > heap[index])
	                    swap(maxDescendentIndex,index);
	                return;
	            } else {  // is grand child
	                if (heap[maxDescendentIndex] <= heap[index]){
	                	return;
	                }
	                swap(maxDescendentIndex,index);
	                int parent = parent(maxDescendentIndex);
	                if (heap[maxDescendentIndex] < heap[parent])
	                    swap(maxDescendentIndex,parent);
	                index = maxDescendentIndex;
	            }
	        }
	    }

	    public static int minDescendent(int index) { //Find and return smallest descendent
	        
	        int leftChild = leftChild(index);
	        int minIndex = leftChild;
	        int minScore = heap[leftChild];

	        int rightChild = rightChild(index);
	        if (rightChild >= next) return minIndex;
	        int rightChildScore = heap[rightChild];
	        if (rightChildScore < minScore) {
	            minIndex = rightChild;
	            minScore = rightChildScore;
	        }

	        int grandChild1 = leftChild(leftChild);
	        if (grandChild1 >= next) return minIndex;
	        int grandChild1Score = heap[grandChild1];
	        if (grandChild1Score < minScore) {
	            minIndex = grandChild1;
	            minScore = grandChild1Score;
	        }

	        int grandChild2 = rightChild(leftChild);
	        if (grandChild2 >= next) return minIndex;
	        int grandChild2Score = heap[grandChild2];
	        if (grandChild2Score < minScore) {
	            minIndex = grandChild2;
	            minScore = grandChild2Score;
	        }

	        int grandChild3 = leftChild(rightChild);
	        if (grandChild3 >= next) return minIndex;
	        int grandChild3Score = heap[grandChild3];
	        if (grandChild3Score < minScore) {
	            minIndex = grandChild3;
	            minScore = grandChild3Score;
	        }

	        int grandChild4 = rightChild(rightChild);
	        if (grandChild4 >= next) return minIndex;
	        int grandChild4Score = heap[grandChild4];

	        if(grandChild4Score < minScore)
	        	return grandChild4;
	        else return minIndex;
	    }

	    public static int maxDescendent(int index) {
	        int leftChild = leftChild(index);
	        int maxIndex = leftChild;
	        int maxScore = heap[leftChild];
	        int rightChild = rightChild(index);
	        if (rightChild >= next) return maxIndex;
	        int rightChildContents = heap[rightChild];
	        if (rightChildContents > maxScore) {
	            maxIndex = rightChild;
	            maxScore = rightChildContents;
	        }

	        int grandChild1 = leftChild(leftChild);
	        if (grandChild1 >= next) return maxIndex;
	        int grandChild1Score = heap[grandChild1];
	        if (grandChild1Score > maxScore) {
	            maxIndex = grandChild1;
	            maxScore = grandChild1Score;
	        }

	        int grandChild2 = rightChild(leftChild);
	        if (grandChild2 >= next) return maxIndex;
	        int grandChild2Score = heap[grandChild2];
	        if (grandChild2Score > maxScore) {
	            maxIndex = grandChild2;
	            maxScore = grandChild2Score;
	        }

	        int grandChild3 = leftChild(rightChild);
	        if (grandChild3 >= next) return maxIndex;
	        int grandChild3Score = heap[grandChild3];
	        if (grandChild3Score > maxScore) {
	            maxIndex = grandChild3;
	            maxScore = grandChild3Score;
	        }

	        int grandChild4 = rightChild(rightChild);
	        if (grandChild4 >= next) return maxIndex;
	        int grandChild4Score = heap[grandChild4];

	        if(grandChild4Score > maxScore)
	        	return grandChild4;
	        else return maxIndex;
	    }

	    public static boolean hasParent(int index) {
	        return index > 1;
	    }


	    public static boolean noChildren(int index) {
	        return leftChild(index) >= 100;
	    }

	    public static boolean isChild(int indexParent, int indexDescendant) {
	        return indexDescendant <= rightChild(indexParent);
	    }

	    public static void swap(int index1, int index2) { //Swaps 2 items in heap.
	        int temp = heap[index1];
	        heap[index1] = heap[index2];
	        heap[index2] = temp;
	    }


	    static int parent(int index) { //Returns parent index
	        return index/2;   
	    }

	    static int leftChild(int index) { //Returns left child's index.
	        return 2 * index;
	    }

	    static int rightChild(int index) { //Returns right child's index.
	        return 2 * index + 1;
	    }

	    public static void printMinMaxHeap(){ //Print the resulting heap.
	    	int lineLength = 1;
	    		for(i=1;i<next;i++){
	    			System.out.print(heap[i] + " ");
	    			if(i==lineLength){
	    				System.out.println("");
	    				lineLength += 2*lineLength;
	    			}
	    		}
	    }
}