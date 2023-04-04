# Item5. 자원을 직접 명시하지 말고 의존 객체 주입을 사용하라.

## I. 의존성 주입의 안 좋은 예

**_소개할 두 방법에 대해여_**

- 멀티스레드 환경에서 사용할 수 없음
- 사용하는 자원에 따라 동작이 달라질 경우 정적 유틸리티 클래스나 싱글턴 방식이 적합하지 않음

> ### 정적 유틸리티 클래스 → private 생성자 사용

```java
public  class SpellCheck {
    private static final Lexicon dictionary = ...;

    private SpellChecker() {}

    public static boolean isValid(String word) { ... }
    public static List<String> suggestions(String typo) { ... }
}
```

유연하지 않고 테스트하기 어렵다.

<br>

> ### Singletone 사용

```java
public  class SpellCheck {
    private final Lexicon dictionary = ...;

    private SpellChecker(...) {}
    public static SpellChecker INSTANCE = new SpellChecker(...);

    public static boolean isValid(String word) { ... }
    public static List<String> suggestions(String typo) { ... }
}
```

마찬가지로, 유연하지 않고 테스트하기 어렵다.

<br>

## II. 의존 주입

- 클래스가 여러 인스턴스를 지원 및 클라이언트가 원하는 자원을 사용해야 할 때 적합
- 인스턴스를 생성할 때 `생성자`에 필요한 자원을 넘겨줌
- `불변` 보장
- 생성자, 정적 팩터리, 빌더 모두에 똑같이 응용 가능
- 유연성, 재사용성, 테스트 용이성 향상

```java
public class SpellChecker {
    private final Lexicon dictionary;

    public SpellChecker(Lexicon dictionary) {
        this.dictionary = Objects.requireNonNull(dictionary);
    }

    public boolean isValid(String word) { ... }
    public List<String> suggestions(String typo) { ... }
}
```
