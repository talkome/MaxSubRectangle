import java.util.Arrays;
import java.util.Comparator;
//311148902

public class MaxSubRectangle {
    boolean doSwap = false;
    int EMPTY_BLOCK;
    int[][] inputMat; // input mat
    int[][] currMat; // our mat
    int i; // row coordinate
    int j; // col coordinate
    int v; // value
    int maxSum;
    int iLeft;
    int jLeft;
    int iRight;
    int jRight;
    int sizeOfMat, secondSizeOfMat;

    public MaxSubRectangle(int[][] data) {
        this.inputMat = data;
        this.i = 0;
        this.j = 0;
        this.v = 0;
        this.maxSum = 0;
        this.iLeft = 0;
        this.jLeft = 0;
        this.iRight = 0;
        this.jRight = 0;
        this.EMPTY_BLOCK = -10000;
        currMat = createMat();
        printMat(currMat);
        findMaxSubRec();
    }

    private void findMin(){
        int min = EMPTY_BLOCK;
        Arrays.sort(inputMat,Comparator.comparingInt(o -> o[2]));
        int length = (int)(Math.log10(10*inputMat[0][2])+1);
        if(length >= 9){
            throw new RuntimeException("illegals input");
        }
        if (min >= inputMat[0][2]) {
            min = inputMat[0][2];
            EMPTY_BLOCK = 10 * min;
        }
    }

    public int[][] createMat(){
        Arrays.sort(inputMat,Comparator.comparingInt(o -> o[0]));
        int maxI = inputMat[inputMat.length-1][0];
        Arrays.sort(inputMat,Comparator.comparingInt(o -> o[1]));
        int maxJ = inputMat[inputMat.length-1][1];
        findMin();
        int[][] newMat;
        if (maxI <= maxJ) { //check n > m for better complexity
            doSwap = true;
            newMat = new int[maxJ+1][maxI+1];
            sizeOfMat = newMat[0].length;
            secondSizeOfMat = newMat.length;
            for (int[] row : newMat) {
                Arrays.fill(row,EMPTY_BLOCK);
            }
            for (int[] ints : inputMat) {
                i = ints[0];
                j = ints[1];
                v = ints[2];
                newMat[j][i] = v;
            }

        } else{
            newMat = new int[maxI+1][maxJ+1];
            sizeOfMat = newMat[0].length;
            secondSizeOfMat = newMat.length;
            for (int[] row : newMat) {
                Arrays.fill(row,EMPTY_BLOCK);
            }
            for (int[] ints : inputMat) {
                i = ints[0];
                j = ints[1];
                v = ints[2];
                newMat[i][j] = v;
            }
        }
        return newMat;
    }

    /**
     * The maximum sub-matrix problem:
     * finding the sub-matrix which has the largest sum.
     * Optimal solution - Super Best
     * Complexity: O(n^3) = minimum(O(n^2*m),O(m^2*n)) = O(minimum^2(m*n)*max(m,n))
     */
    public void findMaxSubRec(){
        maxSum = currMat[0][0];
        int[] helpArr, result;
        int sum;
        for (int k = 0; k < sizeOfMat; k++) {
            for (int l = k; l < sizeOfMat; l++) {
                helpArr = getHelpArr(k,l);
                result = findMaxSumSubArr(helpArr);
                sum = result[3];
                if (sum > maxSum) {
                    maxSum = sum;
                    iLeft = result[0];// beginMax
                    jLeft = k;
                    iRight = result[1];// endMax
                    jRight = l;
                    //System.out.println("left[i=" +iLeft+ ",j=" +jLeft+ "] -> right[i="+iRight+",j="+jRight+"]: H = "+Arrays.toString(helpArr)+ ", max sum = "+maxSum);
                }
            }
        }
    }

    private int[] getHelpArr(int start, int end) {
        int[] helpArr = new int[secondSizeOfMat];
        for (int i = 0; i < helpArr.length; i++) {
            for (int j = start; j <= end; j++) {
                helpArr[i] += currMat[i][j];
            }
        }
        return helpArr;
    }

    /**
     * Complexity: O(n)
     * @param A - our mat
     * @return ans - array with beginMax, endMax, countMax and maxSum
     */
    private static int[] findMaxSumSubArr(int[] A){
        int i = 0;
        while (i < A.length && A[i] <= 0){i++;}
        if (i == A.length){
            int index = 0;
            for (int j = 0; j < A.length; j++) {
                if (A[j] > A[index])
                    index = j;
            }
            int[] ans = {index, index, 1, A[index]};
            return ans;
        } else {
            int sum = 0, maxSum = A[i], beginMax = i, endMax = i, count = 0, countMax = 1;
            while (i < A.length){
                sum += A[i];
                count++;
                if (sum < 0){
                    sum = 0;
                    count = 0;
                } else if (sum > maxSum){
                    maxSum = sum;
                    endMax = i;
                    countMax = count;
                }
                i++;
            }
            beginMax = endMax + 1- countMax;
            int[] ans = {beginMax, endMax, countMax, maxSum};
            return ans;
        }
    }

    /**
     * return max sum of the rectangle
     * @return maxSum
     */
    public int getMaxSum(){ return maxSum; }

    /**
     * return row index of the top left point
     * @return iLeft
     */
    public int getILeft(){
        if (doSwap)
            return jLeft;
        return iLeft;
    }

    /**
     * return col index of the top left point
     * @return jLeft
     */
    public int getJLeft(){
        if (doSwap)
            return iLeft;
        return jLeft;
    }

    /**
     * return row index of bottom right point
     * @return iRight
     */
    public int getIRight(){
        if (doSwap)
            return jRight;
        return iRight;
    }

    /**
     * return col index of bottom right point
     * @return jRight
     */
    public int getJRight(){
        if (doSwap)
            return iRight;
        return jRight;
    }

    /**
     * print matrix
     * @param M - our matrix
     */
    public static void printMat(int[][] M) {
        for (int[] row : M)
            System.out.println(Arrays.toString(row));
    }

    public static void main(String[] args) {

        /*
         * INDEX-CORRECT
         * ---- EXAMPLE FROM LESSON ----
         * [2, 0, 2, -3]
         * [1, 6, -2, 3]
         * [-3, 3, -1, 1]
         * [-4, 4, 4, 0]
         * [5, 1, -5, 3]
         *
         * left[i=0,j=4] -> right[i=0,j=4] : H = [2, 1, -3, -4, 5], max sum = 5
         * left[i=0,j=0] -> right[i=1,j=4] : H = [2, 7, 0, 0, 6], max sum = 15
         * left[i=0,j=0] -> right[i=3,j=4] : H = [1, 8, 0, 4, 4], max sum = 17
         * left[i=1,j=1] -> right[i=3,j=3] : H = [-1, 7, 3, 8, -1], max sum = 18
         * maxSum = 18, iLeft = 1, jLeft = 1, iRight = 3, jRight = 3
         */
        int[][] data5 = {
                {0,0,2},
                {0,1,1},
                {0,2,-3},
                {1,0,0},
                {1,1,6},
                {1,2,3},
                {2,0,2},
                {2,1,-2},
                {2,2,-1},
                {3,0,-3},
                {3,1,3},
                {3,2,1},
        };

        MaxSubRectangle msr5 = new MaxSubRectangle(data5);
        System.out.println("maxSum = "+msr5.getMaxSum()+ ", iLeft = "+msr5.getILeft()+ ", jLeft = "+msr5.getJLeft()+
                ", iRight = "+msr5.getIRight()+ ", jRight = "+msr5.getJRight() );
        System.out.println();

    /*
     * [-10000, -10000, -10000, -10000, -10000, -10000, -10000]
     * [24, 1, -10000, -10000, -10000, -10000, -10000]
     * [-25, 0, -10000, -10000, 706, -10000, -10000]
     * [-4, 4, -504, -10000, -1648, -148, -2000]
     * [-10000, -10000, -6, -10000, -10000, 42, 500]
     * [0, -10000, -72, -10000, -10000, -10000, -10000]
     * [1, -10000, -10000, -10000, -10000, -10000, -10000]
     * left[i=0,j=1] -> right[i=0,j=1]: H = [-10000, 24, -25, -4, -10000, 0, 1], max sum = 24
     * left[i=0,j=1] -> right[i=1,j=1]: H = [-20000, 25, -25, 0, -20000, -10000, -9999], max sum = 25
     * left[i=4,j=2] -> right[i=4,j=2]: H = [-10000, -10000, 706, -1648, -10000, -10000, -10000], max sum = 706
     *
     * maxSum = 706, iLeft = 4, jLeft = 2, iRight = 4, jRight = 2
     */
    int[][] inbal_data1 = {
            {0,1,24},
            {0,2,-25},
            {0,3,-4},
            {0,5,0},
            {0,6,1},
            {1,1,1},
            {1,2,0},
            {1,3,4},
            {2,3,-504},
            {2,4,-6},
            {2,5,-72},
            {4,2,706},
            {4,3,-1648},
            {5,3,-148},
            {5,4,42},
            {6,3,-2000},
            {6,4,500}
    };

    MaxSubRectangle inbal1 = new MaxSubRectangle(inbal_data1);
        System.out.println();
        System.out.println("maxSum = "+inbal1.getMaxSum()+ ", iLeft = "+inbal1.getILeft()+ ", jLeft = "+inbal1.getJLeft()+
                ", iRight = "+inbal1.getIRight()+ ", jRight = "+inbal1.getJRight() );
        System.out.println();

        /*
         * [-10000, -10000, -10000]
         * [406, 7, -10000]
         * [-1, 9, -10000]
         * [504, -10000, -1]
         * [6, -10000, 406]
         * [3, -10000, -10000]
         * left[i=0,j=1] -> right[i=0,j=5]: H = [-10000, 406, -1, 504, 6, 3], max sum = 918
         *
         * maxSum = 918, iLeft = 0, jLeft = 1, iRight = 0, jRight = 5
         */
        int[][] inbal_data2 = {
                {0,1,406},
                {0,2,-1},
                {0,3,504},
                {0,4,6},
                {0,5,3},
                {1,1,7},
                {1,2,9},
                {2,3,-1},
                {2,4,406}
        };

        MaxSubRectangle inbal2 = new MaxSubRectangle(inbal_data2);
        System.out.println();
        System.out.println("maxSum = "+inbal2.getMaxSum()+ ", iLeft = "+inbal2.getILeft()+ ", jLeft = "+inbal2.getJLeft()+
                ", iRight = "+inbal2.getIRight()+ ", jRight = "+inbal2.getJRight() );
        System.out.println();

        /*
         * [E, 3]
         * [-1, 4]
         * left[i=0,j=1] -> right[i=0,j=1]: H = [-10000, -1], max sum = -1
         * left[i=0,j=1] -> right[i=1,j=1]: H = [-9997, 3], max sum = 3
         * left[i=1,j=0] -> right[i=1,j=1]: H = [3, 4], max sum = 7
         *
         * maxSum = 7, iLeft = 1, jLeft = 0, iRight = 1, jRight = 1
         */
        int[][] tal1_data = {
                {0,1,-1},
                {1,0,3},
                {1,1,4}
        };

        MaxSubRectangle tal1 = new MaxSubRectangle(tal1_data);
        System.out.println();
        System.out.println("maxSum = "+tal1.getMaxSum()+ ", iLeft = "+tal1.getILeft()+ ", jLeft = "+tal1.getJLeft()+
                ", iRight = "+tal1.getIRight()+ ", jRight = "+tal1.getJRight() );
        System.out.println();

        /*
         * INDEX-CORRECT
         * ---- EXAMPLE A ----
         * n < m
         * Complexity: O(n^2*m)
         *
         * [  N,  N,  N,  1,  N,  N,   N]
         * [  N,  N,  1,  2, -4,  N,   N]       ==>>
         * [  N,  2, -3,  3,  3,  1,   N]
         * [ -2, 10,  9, -2,  4,  5, -11]
         *
         * [ E,  E,  E,  -2]
         * [ E,  E,  2,  10]
         * [ E,  1, -3,   9]
         * [ 1,  2,  3,  -2]
         * [ E, -4,  3,   4]
         * [ E,  E,  1,   5]
         * [ E,  E,  E, -11]
         *
         * maxSum = 32, iLeft = 2, jLeft = 1, iRight = 3, jRight = 5
         *
         */
        int[][] data3 = {
                {3,5,5},
                {1,3,2},
                {3,3,-2},
                {2,5,1},
                {1,4,-4},
                {2,1,2},
                {0,3,1},
                {3,2,9},
                {2,2,-3},
                {3,1,10},
                {2,4,3},
                {3,0,-2},
                {2,3,3},
                {1,2,1},
                {3,4,4},
                {3,6,-11}
        };

        MaxSubRectangle msr3 = new MaxSubRectangle(data3);
        System.out.println();
        System.out.println("maxSum = "+msr3.getMaxSum()+ ", iLeft = "+msr3.getILeft()+ ", jLeft = "+msr3.getJLeft()+
                ", iRight = "+msr3.getIRight()+ ", jRight = "+msr3.getJRight() );
        System.out.println();

        /*
         * INDEX-CORRECT
         * ---- EQUAL COLUMN AND ROW CASE ----
         * n = m
         * Complexity: O(n^3)
         *
         * [-3,  3,   E,  E,   E]
         * [ E,  E,  -7, 16,   E]   ==>>
         * [ E, -5,   2, 43, -45]
         * [ E,  E,   4,  4, -17]
         * [ E, 31, -40,  7,   6]
         *
         * [-3, E, E, E, E]
         * [3, E, -5, E, 31]
         * [E, -7, 2, 4, -40]
         * [E, 16, 43, 4, 7]
         * [E, E, -45, -17, 6]
         *
         * left[i=0,j=1] -> right[i=0,j=1]: H = [-3, 3, -10000, -10000, -10000], max sum = 3
         * left[i=1,j=3] -> right[i=1,j=3]: H = [-10000, -10000, -7, 16, -10000], max sum = 16
         * left[i=1,j=3] -> right[i=2,j=3]: H = [-20000, -10005, -5, 59, -10045], max sum = 59
         * left[i=1,j=3] -> right[i=3,j=3]: H = [-30000, -20005, -1, 63, -10062], max sum = 63
         * left[i=1,j=3] -> right[i=4,j=3]: H = [-40000, -19974, -41, 70, -10056], max sum = 70
         *
         * maxSum = 70, iLeft = 1, jLeft = 3, iRight = 4, jRight = 3
         *
         *
         */
        int[][] data1 = {
                {4,4,6},
                {3,3,4},
                {2,1,-5},
                {0,1,3},
                {2,4,-45},
                {0,0,-3},
                {3,4,-17},
                {1,2,-7},
                {2,2,2},
                {2,3,43},
                {3,2,4},
                {4,3,7},
                {1,3,16},
                {4,1,31},
                {4,2,-40}
        };

        MaxSubRectangle msr1 = new MaxSubRectangle(data1);
        System.out.println("maxSum = "+msr1.getMaxSum()+ ", iLeft = "+msr1.getILeft()+ ", jLeft = "+msr1.getJLeft()+
                ", iRight = "+msr1.getIRight()+ ", jRight = "+msr1.getJRight() );
        System.out.println();

        /*
         * n > m
         * [11000, -10000, 12000]
         * [-100000, -100000, -100000]
         * [-100000, 8342, 4657]
         * [-100000, -10000, 12999]
         * left[i=0,j=0] -> right[i=2,j=0]: H = [13000, -300000, -87001, -97001], max sum = 13000
         * left[i=1,j=2] -> right[i=2,j=3]: H = [2000, -200000, 12999, 2999], max sum = 15998
         * left[i=2,j=2] -> right[i=2,j=3]: H = [12000, -100000, 4657, 12999], max sum = 17656
         *
         * maxSum = 17656, iLeft = 2, jLeft = 2, iRight = 3, jRight = 2
         */
        int[][] inbal_data3 = {
                {0,0,11000},
                {0,1,-10000},
                {0,2,12000},
                {2,1,8342},
                {2,2,4657},
                {3,1,-10000},
                {3,2,12999}
        };

        MaxSubRectangle inbal3 = new MaxSubRectangle(inbal_data3);
        System.out.println();
        System.out.println("maxSum = "+inbal3.getMaxSum()+ ", iLeft = "+inbal3.getILeft()+ ", jLeft = "+inbal3.getJLeft()+
                ", iRight = "+inbal3.getIRight()+ ", jRight = "+inbal3.getJRight() );
        System.out.println();

        /*
         * [-10000, 1456, -500, 600]
         * [-10000, -10000, -10000, -10000]
         * [-10000, 624, -10000, -10000]
         * [-10000, -10000, -500, 705]
         * [-10000, 1555, -10000, -10000]
         * left[i=0,j=4] -> right[i=1,j=4]: H = [-8544, -20000, -9376, -20000, -8445], max sum = -8445
         * left[i=0,j=0] -> right[i=3,j=0]: H = [-8444, -40000, -29376, -19795, -28445], max sum = -8444
         * left[i=1,j=4] -> right[i=1,j=4]: H = [1456, -10000, 624, -10000, 1555], max sum = 1555
         * left[i=1,j=0] -> right[i=3,j=0]: H = [1556, -30000, -19376, -9795, -18445], max sum = 1556
         *
         * maxSum = 1556, iLeft = 0, jLeft = 1, iRight = 0, jRight = 3
         */
        int[][] inbal_data4 = {
                {0,1,1456},
                {0,2,-500},
                {0,3,600},
                {2,1,624},
                {3,2,-500},
                {3,3,705},
                {4,1,1555}
        };

        MaxSubRectangle inbal4 = new MaxSubRectangle(inbal_data4);
        System.out.println();
        System.out.println("maxSum = "+inbal4.getMaxSum()+ ", iLeft = "+inbal4.getILeft()+ ", jLeft = "+inbal4.getJLeft()+
                ", iRight = "+inbal4.getIRight()+ ", jRight = "+inbal4.getJRight() );
        System.out.println();

        /*
         * [-788880, -788880, -788880, -788880]
         * [-788880, -45667, -10000, -56666]
         * [-788880, -788880, -788880, -788880]
         * [-788880, -788880, -10000, -788880]
         * [-788880, -788880, -788880, -788880]
         * [-788880, -78888, -42222, -56666]
         * left[i=1,j=1] -> right[i=1,j=1]: H = [-788880, -45667, -788880, -788880, -788880, -78888], max sum = -45667
         * left[i=2,j=1] -> right[i=2,j=1]: H = [-788880, -10000, -788880, -10000, -788880, -42222], max sum = -10000
         *
         * maxSum = -10000, iLeft = 1, jLeft = 2, iRight = 1, jRight = 2
         */
        int[][] inbal_data5 = {
                {1,1,-45667},
                {1,2,-10000},
                {1,3,-56666},
                {3,2,-10000},
                {5,1,-78888},
                {5,2,-42222},
                {5,3,-56666}
        };

        MaxSubRectangle inbal5 = new MaxSubRectangle(inbal_data5);
        System.out.println();
        System.out.println("maxSum = "+inbal5.getMaxSum()+ ", iLeft = "+inbal5.getILeft()+ ", jLeft = "+inbal5.getJLeft()+
                ", iRight = "+inbal5.getIRight()+ ", jRight = "+inbal5.getJRight() );
        System.out.println();

        /*
         * [-100000]
         * [-10000]
         * left[i=0,j=1] -> right[i=0,j=1]: H = [-100000, -10000], max sum = -10000
         *
         * maxSum = -10000, iLeft = 0, jLeft = 1, iRight = 0, jRight = 1
         */
        int[][] inbal_data6 = {
                {0,1,-10000}
        };

        MaxSubRectangle inbal6 = new MaxSubRectangle(inbal_data6);
        System.out.println();
        System.out.println("maxSum = "+inbal6.getMaxSum()+ ", iLeft = "+inbal6.getILeft()+ ", jLeft = "+inbal6.getJLeft()+
                ", iRight = "+inbal6.getIRight()+ ", jRight = "+inbal6.getJRight() );
        System.out.println();

        /*
         * [-10000]
         *
         * maxSum = -10000, iLeft = 0, jLeft = 0, iRight = 0, jRight = 0
         */
        int[][] inbal_data7 = {
                {0,0,-10000}
        };

        MaxSubRectangle inbal7 = new MaxSubRectangle(inbal_data7);
        System.out.println();
        System.out.println("maxSum = "+inbal7.getMaxSum()+ ", iLeft = "+inbal7.getILeft()+ ", jLeft = "+inbal7.getJLeft()+
                ", iRight = "+inbal7.getIRight()+ ", jRight = "+inbal7.getJRight() );
        System.out.println();

        /*
         * [E, 2, E]
         * [4, 6, 8]
         * [5, 6, 7]
         * [E, 1, 9]
         *
         * left[i=0,j=1] -> right[i=0,j=2]: H = [-10000, 4, 5, -10000], max sum = 9
         * left[i=0,j=1] -> right[i=1,j=2]: H = [-9998, 10, 11, -9999], max sum = 21
         * left[i=0,j=1] -> right[i=2,j=2]: H = [-19998, 18, 18, -9990], max sum = 36
         * left[i=1,j=1] -> right[i=2,j=3]: H = [-9998, 14, 13, 10], max sum = 37
         *
         * maxSum = 37, iLeft = 1, jLeft = 1, iRight = 2, jRight = 3
         */
        int[][] inbal_data8 = {
                {0,1,4},
                {0,2,5},
                {1,0,2},
                {1,1,6},
                {1,2,6},
                {1,3,1},
                {2,1,8},
                {2,2,7},
                {2,3,9}
        };

        MaxSubRectangle inbal8 = new MaxSubRectangle(inbal_data8);
        System.out.println();
        System.out.println("maxSum = "+inbal8.getMaxSum()+ ", iLeft = "+inbal8.getILeft()+ ", jLeft = "+inbal8.getJLeft()+
                ", iRight = "+inbal8.getIRight()+ ", jRight = "+inbal8.getJRight() );
        System.out.println();

        /*
         * [E, E]
         * [E, E]
         * [-1000000, -3000000]
         * [-2000000, -4000000]
         *
         * left[i=0,j=2] -> right[i=0,j=2]: H = [-40000000, -40000000, -1000000, -2000000], max sum = -1000000
         *
         * maxSum = -1000000, iLeft = 0, jLeft = 2, iRight = 0, jRight = 2
         */
        int[][] data8 = {
                {0,2,-1000000},
                {0,3,-2000000},
                {1,2,-3000000},
                {1,3,-4000000},
        };

        MaxSubRectangle msr8 = new MaxSubRectangle(data8);
        System.out.println("maxSum = "+msr8.getMaxSum()+ ", iLeft = "+msr8.getILeft()+ ", jLeft = "+msr8.getJLeft()+
                ", iRight = "+msr8.getIRight()+ ", jRight = "+msr8.getJRight() );
        System.out.println();

        /*
         * INDEX-CORRENCT
         * [E, 3, -3, 17]
         * [36, 6, 9, -4]
         *
         * [E,  36]
         * [3,   6]
         * [-3,  9]
         * [17, -4]
         *
         * left[i=0,j=1] -> right[i=0,j=3] : H = [-100000000, 3, -3, 17], max sum = 17
         * left[i=0,j=1] -> right[i=1,j=3] : H = [-99999964, 9, 6, 13], max sum = 28
         * left[i=1,j=0] -> right[i=1,j=2] : H = [36, 6, 9, -4], max sum = 51
         *
         * maxSum = 51, iLeft = 1, jLeft = 0, iRight = 1, jRight = 2
         */
        int[][] data7 = {
                {0,1,3},
                {0,2,-3},
                {0,3,17},
                {1,0,36},
                {1,1,6},
                {1,2,9},
                {1,3,-4},
        };

        MaxSubRectangle msr7 = new MaxSubRectangle(data7);
        System.out.println("maxSum = "+msr7.getMaxSum()+ ", iLeft = "+msr7.getILeft()+ ", jLeft = "+msr7.getJLeft()+
                ", iRight = "+msr7.getIRight()+ ", jRight = "+msr7.getJRight() );
        System.out.println();

        /*
         * INDEX-CORRECT
         * [E, 3, -3, 17]
         * [8, 6, 9, -4]
         *
         * [E,   8]
         * [3,   6]
         * [-3,  9]
         * [17, -4]
         *
         * left[i=0,j=1] -> right[i=0,j=3] : H = [-100000000, 3, -3, 17], max sum = 17
         * left[i=0,j=1] -> right[i=1,j=3] : H = [-99999992, 9, 6, 13], max sum = 28
         *
         * maxSum = 28, iLeft = 0, jLeft = 1, iRight = 1, jRight = 3
         */
        int[][] data6 = {
                {0,1,3},
                {0,2,-3},
                {0,3,17},
                {1,0,8},
                {1,1,6},
                {1,2,9},
                {1,3,-4},
        };

        MaxSubRectangle msr6 = new MaxSubRectangle(data6);
        System.out.println("maxSum = "+msr6.getMaxSum()+ ", iLeft = "+msr6.getILeft()+ ", jLeft = "+msr6.getJLeft()+
                ", iRight = "+msr6.getIRight()+ ", jRight = "+msr6.getJRight() );
        System.out.println();

        /*
         * ---- ALL NEGATIVE NUMBERS CASE ----
         * n > m
         * Complexity: O(m^2*n)
         *
         * [E,      E,         E,     E]
         * [E,      E,   -122234,     E]
         * [E, -11111,     -4232, -5333]
         * [E, -52463, -22222222,     E]
         * [E, -24668, -11111111,     E]
         * [E,      E,    -52345,     E]
         *
         * left[i=1,j=2] -> right[i=1,j=2]: H = [-222222220, -222222220, -11111, -52463, -24668, -222222220], max sum = -11111
         * left[i=2,j=2] -> right[i=2,j=2]: H = [-222222220, -122234, -4232, -22222222, -11111111, -52345], max sum = -4232
         *
         * maxSum = -4232, iLeft = 2, jLeft = 2, iRight = 2, jRight = 2
         */
        int[][] data2 = {
                {3,2,-22222222},
                {2,1,-11111},
                {4,1,-24668},
                {2,3,-5333},
                {5,2,-52345},
                {3,1,-52463},
                {2,2,-4232},
                {4,2,-11111111},
                {1,2,-122234}
        };

        MaxSubRectangle msr2 = new MaxSubRectangle(data2);
        System.out.println("maxSum = "+msr2.getMaxSum()+ ", iLeft = "+msr2.getILeft()+ ", jLeft = "+msr2.getJLeft()+
                ", iRight = "+msr2.getIRight()+ ", jRight = "+msr2.getJRight() );
        System.out.println();

        /*
         *  ---- EXAMPLE B----
         * n < m
         * Complexity: O(n^2*m)
         *
         * [E,   E,   E, E, E,   E,   E,   E,   E,  E]
         * [E,   E,  20, E, E,   E,   E,   E,   E,  E]
         * [E, -10,  21, 2, E,   E,   E,   8,   E,  E]
         * [E,   E,   3, E, E,   E,   7,   9,  10,  E]
         * [E,   E,   E, E, E,   6, -44,   5,   7, 11]
         * [E,   E,   E, E, E,   5,  10,  33, -28,  7]
         * [E,   E,   E, E, E,   4,  13,  -4,   1,  6]
         * [E,   E,   E, E, E, -10,  17,  15,   4,  5]
         * [E,   E,   E, E, E,   3,   2, -60,   2,  3]
         *
         * [ E,  E,   E,  E,   E,   E,  E,   E,   E]
         * [ E,  E, -10,  E,   E,   E,  E,   E,   E]
         * [ E, 20,  21,  3,   E,   E,  E,   E,   E]
         * [ E,  E,   2,  E,   E,   E,  E,   E,   E]
         * [ E,  E,   E,  E,   E,   E,  E,   E,   E]
         * [ E,  E,   E,  E,   6,   5,  4, -10,   3]
         * [ E,  E,   E,  7, -44,  10, 13,  17,   2]
         * [ E,  E,   8,  9,   5,  33, -4,  15, -60]
         * [ E,  E,   E, 10,   7, -28,  1,   4,   2]
         * [ E,  E,   E,  E,  11,   7,  6,   5,   3]
         *
         * left[i=0,j=2] -> right[i=1,j=2]: H = [-200, -200, -80, -200, -200, -200, -200, -200, -200, -200], max sum = -80
         * left[i=0,j=2] -> right[i=2,j=2]: H = [-300, -210, -59, -198, -300, -300, -300, -192, -300, -300], max sum = -59
         * left[i=0,j=2] -> right[i=3,j=2]: H = [-400, -310, -56, -298, -400, -400, -293, -183, -290, -400], max sum = -56
         * left[i=1,j=2] -> right[i=1,j=2]: H = [-100, -100, 20, -100, -100, -100, -100, -100, -100, -100], max sum = 20
         * left[i=1,j=2] -> right[i=2,j=2]: H = [-200, -110, 41, -98, -200, -200, -200, -92, -200, -200], max sum = 41
         * left[i=1,j=2] -> right[i=3,j=2]: H = [-300, -210, 44, -198, -300, -300, -193, -83, -190, -300], max sum = 44
         * left[i=2,j=7] -> right[i=5,j=7]: H = [-400, -310, -176, -298, -400, -189, -127, 55, -111, -182], max sum = 55
         * left[i=2,j=7] -> right[i=7,j=7]: H = [-600, -510, -376, -498, -600, -195, -97, 66, -106, -171], max sum = 66
         * left[i=5,j=6] -> right[i=7,j=7]: H = [-300, -300, -300, -300, -300, -1, 40, 44, -23, 18], max sum = 84
         *
         * maxSum = 84, iLeft = 5, jLeft = 6, iRight = 7, jRight = 7
         *
         */
        int[][] data4 = {
                {3,6,7},
                {6,7,-4},
                {2,3,2},
                {7,6,17},
                {3,7,9},
                {3,8,10},
                {4,5,6},
                {4,6,-44},
                {4,8,7},
                {4,9,11},
                {8,8,2},
                {2,1,-10},
                {5,5,5},
                {5,6,10},
                {5,7,33},
                {8,9,3},
                {5,9,7},
                {6,5,4},
                {8,6,2},
                {4,7,5},
                {6,8,1},
                {6,9,6},
                {7,5,-10},
                {2,7,8},
                {7,7,15},
                {2,2,21},
                {3,2,3},
                {6,6,13},
                {7,8,4},
                {7,9,5},
                {8,5,3},
                {5,8,-28},
                {8,7,-60},
                {1,2,20}
        };

        MaxSubRectangle msr4 = new MaxSubRectangle(data4);
        System.out.println();
        System.out.println("maxSum = "+msr4.getMaxSum()+ ", iLeft = "+msr4.getILeft()+ ", jLeft = "+msr4.getJLeft()+
                ", iRight = "+msr4.getIRight()+ ", jRight = "+msr4.getJRight() );
        System.out.println();
    }
}
