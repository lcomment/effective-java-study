package item6.romanTest;

public class Main {
    public static void main(String[] args) {
        long beforeTime = System.currentTimeMillis();

        for(int i=0 ; i<100 ; i++) {
            StaticMethod.isRmanNumeral("komment");
        }

        long afterTime = System.currentTimeMillis();
        System.out.println("정적 메서드: " + (afterTime - beforeTime));

        beforeTime = System.currentTimeMillis();

        for(int i=0 ; i<100 ; i++) {
            RecycleClass.isRomanNumeral("komment");
        }

        afterTime = System.currentTimeMillis();
        System.out.println("클래스 재사용: " + (afterTime - beforeTime));

        beforeTime = System.currentTimeMillis();

        for(int i=0 ; i<100 ; i++) {
            LazyInitClass.isRomanNumeral("komment");
        }

        afterTime = System.currentTimeMillis();
        System.out.println("지연 초기화: " + (afterTime - beforeTime));
    }
}
