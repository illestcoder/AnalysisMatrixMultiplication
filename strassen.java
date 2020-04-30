public class strassen {
    public static void main(String[] args) {
        create_run_arrays(2);
        create_run_arrays(4);
        create_run_arrays(16);
        create_run_arrays(32);
        create_run_arrays(64);
        create_run_arrays(128);
        create_run_arrays(256);
        create_run_arrays(512);
        create_run_arrays(1024);
    }
    /** Function to create, and fill contents of arrays
     * makes call to avgTimeNano which will run main function */
    private static void create_run_arrays(int n) {
        System.out.println("n = " + n);
        avgTimeNano(fillArr(n), fillArr(n));
    }
    /** Print arrays for debugging */
    private static void printArr(int[][] a) {
        for(int i = 0; i < a.length; i++) {
            for(int j = 0; j < a.length; j++) {
                System.out.print(a[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
    /** Fill contents of array */
    private static int[][] fillArr(int n) {
        int content = 1;
        int[][] A = new int[n][n];
        for(int row = 0; row < n; row++) {
            for(int col = 0; col < n; col++) {
                A[row][col] = content;
//                content += 1;
//                if (content >= 25)
//                    content = 0;
            }
        }
        return A;
    }
    /** Calculate runtime of main function */
    private static void avgTimeNano(int[][] A, int[][] B) {
        int i = 0;
        long totalTime = 0;
        long startTime, endTime;
        while(i != 1) { // run 5 times
            System.out.println("Run #: " + i);
            startTime = System.nanoTime();
            multMat(A, B); // main function here
            endTime = System.nanoTime();
            totalTime += endTime-startTime;
            i++;
        }

        double seconds = totalTime/5.0;
        seconds = seconds / 1_000_000_000;
        System.out.printf("Average Runtime: %.6f", seconds);
        System.out.print("s\n");
    }

    /** MAIN FUNCTION */
    private static int[][] multMat(int[][] A, int[][] B) {
        int n = A.length;
        int[][] F = new int[n][n]; // MAIN, final array

        if(n == 1) { // base case
            F[0][0] = A[0][0] * B[0][0];
        } else {
            /** Creating 4 partitions for each matrix
            * |1   2|
            * |     |
            * |3   4| Quadrants split like so */
            int[][] A1 = new int[n/2][n/2];
            int[][] A2 = new int[n/2][n/2];
            int[][] A3 = new int[n/2][n/2];
            int[][] A4 = new int[n/2][n/2];
            int[][] B1 = new int[n/2][n/2];
            int[][] B2 = new int[n/2][n/2];
            int[][] B3 = new int[n/2][n/2];
            int[][] B4 = new int[n/2][n/2];

            /** partition A & B into 4 sub matrices, already allocated from above */
            partition(A, A1, 0 , 0);       // 1st quadrant
            partition(A, A2, 0 , n/2);     // 2nd quadrant
            partition(A, A3, n/2, 0);      // 3rd quadrant
            partition(A, A4, n/2, n/2);    // 4th quadrant

            partition(B, B1, 0 , 0);       // 1st quadrant
            partition(B, B2, 0 , n/2);     // 2nd quadrant
            partition(B, B3, n/2, 0);      // 3rd quadrant
            partition(B, B4, n/2, n/2);    // 4th quadrant

            /** Strassen's procedure
             * create 7 arrays: P, Q, R, S, T, U, V */
//            int [][] P = multMat(add(A1, A4), add(B1, B4));
//            int [][] Q = multMat(add(A3, A4), B1);
//            int [][] R = multMat(A1, sub(B2, B4));
//            int [][] S = multMat(A4, sub(B3, B1));
//            int [][] T = multMat(add(A1, A2), B4);
//            int [][] U = multMat(sub(A3, A1), add(B1, B2));
//            int [][] V = multMat(sub(A2, A4), add(B3, B4));
//            /** combine 7 arrays into 4 sub arrays before merging to final array F */
//            int [][] C1 = add(sub(add(P, S), T), V);
//            int [][] C2 = add(R, T);
//            int [][] C3 = add(Q, S);
//            int [][] C4 = add(sub(add(P, R), Q), U);

//             instead of allocating space for arrays first, recursively call everything worst idea ever, also eye sore
            int [][] C1 = add(sub(add(multMat(add(A1, A4), add(B1, B4)), multMat(A4, sub(B3, B1))), multMat(add(A1, A2), B4)), multMat(sub(A2, A4), add(B3, B4)));
            int [][] C2 = add(multMat(A1, sub(B2, B4)), multMat(add(A1, A2), B4));
            int [][] C3 = add(multMat(add(A3, A4), B1), multMat(A4, sub(B3, B1)));
            int [][] C4 = add(sub(add(multMat(add(A1, A4), add(B1, B4)), multMat(A1, sub(B2, B4))), multMat(add(A3, A4), B1)), multMat(sub(A3, A1), add(B1, B2)));

            merge(C1, F, 0 , 0);
            merge(C2, F, 0 , n/2);
            merge(C3, F, n/2, 0);
            merge(C4, F, n/2, n/2);
        }
//        printArr(F);
        return F;
    }

     /** Partition PARENT array to CHILD sub arrays
     * iB == column bound for outer loop
     * jB == column bound for inner loop
     * place parents values into child values row priority, then columns **/
    public static void partition(int[][] P, int[][] C, int iB, int jB)
    {
        for(int iR = 0, iC = iB; iR < C.length; iR++, iC++) {
            for(int jR = 0, jC = jB; jR < C.length; jR++, jC++)
                C[iR][jR] = P[iC][jC];
        }
//        printArr(S);
    }
    /** Merge CHILD array back to PARENT array */
    public static void merge(int[][] C, int[][] P, int iB, int jB)
    {
        for(int iR = 0, iC = iB; iR < C.length; iR++, iC++) {
            for(int jR = 0, jC = jB; jR < C.length; jR++, jC++)
                P[iC][jC] = C[iR][jR];
        }
//        printArr(P);
    }

    /** Standard ADD and SUB functions for arrays */
    private static int[][] sub(int[][] A, int[][] B)
    {
        int n = A.length;
        int[][] C = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                C[i][j] = A[i][j] - B[i][j];
        }
        return C;
    }
    private static int[][] add(int[][] A, int[][] B)
    {
        int n = A.length;
        int[][] C = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                C[i][j] = A[i][j] + B[i][j];
        }
        return C;
    }
}
