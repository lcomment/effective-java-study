# Item6. 불필요한 객체 생성을 피하라.

## I. 재사용은 빠르고 세련되다!

```java
String s = new String("komment");
String s = "komment";
```

- new 키워드로 인스턴스를 새로 만든 경우
  - 불필요한 객체가 여러 개 생성
  - 여러 개의 객체가 사실은 하나의 기능을 함
- 리터럴로 생성
  - 하나의 인스턴스만 생성
  - 이후, `String s1 = "komment"`로 다시 생성해도 s와 s1은 같은 객체
  - s를 사용하는 모든 코드에서 `같은 객체 재사용`이 보장됨

<br>

```java
Boolean b1 = new Boolean("true");
Boolean b2 = Boolean.valueOf("true");
```

- Boolean 생성자
  - 호출할 때마다 새로운 객체 생성
  - Java9에서 deprecated 및 removal 됨
- Boolean의 valueOf() → 정적 팩터리 메서드
  - 불필요한 객체 생성을 피할 수 있음

<br>

## II. 불변 객체뿐만 아니라 가변 객체라 해도 사용 중에 변경되지 않는다면 재사용 가능하다!

**_정규식으로 문자열 형태를 확인하는 정적 메서드_**

```java
static boolean isRmanNumeral(String s) {
    return s.matches("^(?=.)M*(C[MD]|D?C{0,3})"
        + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");
}
```

- String.matches 메서드는 `Pattern 인스턴스`를 생성해줌
- 생성된 Pattern 인스턴스는 한번 쓰고 `가비지 컬렉션 대상`이 됨
- Pattern은 유한 상태 머신을 만들기 때문에 생성 비용이 높음
- 해당 정적 메서드는 성능이 안 좋을 수밖에 없음

<br>

**_객체 재사용을 통한 성능 향상_**

```java
public class RomanNumerals {
    private static final Pattern ROMAN = Pattern.compile(
        "^(?=.)M*(C[MD]|D?C{0,3})"
        + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");

    static boolean isRomanNumeral(String s) {
        return ROMAN.matcher(s).matches();
    }
}
```

- 성능 개선
- 코드 명확성 개선
- 만약 메서드를 한 번도 호출하지 않는다면?
  - ROMAN 필드는 `쓸데없이` 초기화된 꼴
  - `지연 초기화`로 해결? → 역효과를 낼 확률이 높음

<br>

## III. Map 인터페이스 keySet 메서드

> 33P : keySet을 호출할 때마다 새로운 Set 인스턴스가 만들어지리라고 순진하게 생각할 수도 있지만, 사실은 `매번 같은 Set 인스턴스`를 반환할지도 모른다

```java
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapTest {
    public static void main(String[] args) {
        Map<Integer, String> map = new HashMap<>();

        map.put(1, "k");
        map.put(2, "o");

        Set<Integer> set1 = map.keySet();
        System.out.println(System.identityHashCode(set1));

        map.put(3, "m");
        map.put(4, "m");

        Set<Integer> set2 = map.keySet();
        System.out.println(System.identityHashCode(set2));
    }
}
```

위의 코드와 같이 테스트 해본 결과,

```shell
1175962212
1175962212
```

같은 값이 나왔다. 따라서 map의 keySet 메서드로 초기화 된 set1과 set2는 같은 공간을 `공유`한다.

<br>

## IV. 의도치 않은 오토박싱을 피하라.

> 오토박싱은 기본 타입과 그에 대응하는 박싱된 기본 타입의 구분을 흐려주지만, 완전히 없애주는 것은 아니다.

```java
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

```

위와 같이 테스트 해본 결과, 결과는 다음과 같다.

```shell
primitive type: 761
reference type: 3602
```
