package item6.autoBoxing;

public class AutoBoxingTest {

    public static void main(String[] args) {
        long num1 = 0;
        Long num2 = 0L;
        // Integer num3 = Integer.valueOf(0);

        long beforeTime = System.currentTimeMillis();

        for(int i=0 ; i<Integer.MAX_VALUE ; i++) {
           num1 += i;
        }

        long afterTime = System.currentTimeMillis();
        System.out.println("primitive type: " + (afterTime - beforeTime));

        beforeTime = System.currentTimeMillis();

        for(int i=0 ; i<Integer.MAX_VALUE ; i++) {
            num2 += i;
        }

        afterTime = System.currentTimeMillis();
        System.out.println("reference type: " + (afterTime - beforeTime));
    }
}
