public class driver {
    public static void main(String[] args) {
//        create_run_arrays(2);
//        create_run_arrays(4);
//        create_run_arrays(16);
//        create_run_arrays(32);
//        create_run_arrays(64);
//        create_run_arrays(128);
//        create_run_arrays(256);
//        create_run_arrays(512);
//        create_run_arrays(1024);
//        create_run_arrays(2048);
        create_run_arrays(3000);
    }

    private static void create_run_arrays(int n) {
        System.out.println("n = " + n);
        int[][] a;
        int[][] b;
        a = fillArr(n);
        b = fillArr(n);
//        printArr(a);
//        printArr(b);
        avgTimeNano(a, b);
    }
    private static int[][] fillArr(int n) {
        int content = 1;
        int[][] a = new int[n][n];
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                a[i][j] = content;
                content += 1;
                if (content >= 25)
                    content = 0;
            }
        }
        return a;
    }
    private static void printArr(int[][] a) {
        for(int i = 0; i < a.length; i++) {
            for(int j = 0; j < a.length; j++) {
                System.out.print(a[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
    private static int[][] multArr(int[][] a, int[][] b) {
        int[][] c = new int[a.length][a.length];
        for(int i = 0; i < a.length; i++) { // rows
            for(int j = 0; j < a.length; j++) { // columns
                c[i][j] = 0;
                for(int k = 0; k < a.length; k++) {
                    c[i][j] = c[i][j] + a[i][k] * b[k][j];
                }
            }
        }
//        printArr(c);
        return c;
    }
    private static void avgTimeNano(int[][] a, int[][] b) {
        int i = 0;
        int[][] result;
        long totalTime = 0;
        long startTime, endTime;
        while(i != 5) {
            System.out.println("Run #: " + i);
            startTime = System.nanoTime();
            result = multArr(a, b);
            endTime = System.nanoTime();
            totalTime += endTime-startTime;
            i++;
        }

        double seconds = totalTime/5.0;
        seconds = seconds / 1_000_000_000;
        System.out.printf("Average Runtime: %.9f", seconds);
        System.out.print("s\n");
    }
}

