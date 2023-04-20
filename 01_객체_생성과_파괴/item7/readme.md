# Item7. 다 쓴 객체 참조를 해제하라

- C나 C++은 메모리를 직접 관리해야 함
- Java는 `가비지 컬렉터`가 메모리 관리를 해줌
- 하지만! 메모지 누수에 신경써줘야 함

<br>

## I. 자료구조를 생성하여 사용하면서 메모리 해제를 하지 않은 경우

```java
public class Stack {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public Object pop() {
        if (size == 0)
            throw new EmptyStackException();
        return elements[--size];
    }

    private void ensureCapacity() {
        if (elements.length == size)
            elements = Arrays.copyOf(elements, 2 * size + 1);
    }
}
```

- `pop()`
  - 스택에서 꺼내진 객체 → 카비지 컬렉터가 회수하지 않음
  - why? → 스택이 그 객체들의 `다 쓴 참조`를 여전히 가지고 있음
- 단 몇 개의 객체가 매우 많은 객체를 회수되지 못하게 할 수 있음
- 해당 참조를 다 썼을 때 `null 처리`(참조 해제) 하면 됨

```java
public class Stack {
    . . .
    public Object pop() {
        if (size == 0)
            throw new EmptyStackException();
        Object result = elements[--size];
        elements[size] = null; // 다 쓴 참조 해제
        return result;
    }
}
```

- null 처리를 하면서, 다른 참조를 사용하려 할 때 `NullPointerException` 발생
- But. 객체 참조를 null 처리하는 것은 `예외적인 경우`여야 함
  - 다 쓴 참조를 해제하는 가장 좋은 방법 → 참조를 담은 변수를 유효 범위 밖으로 밀어내는 것
  - [Item57: 지역변수의 범위를 최소화하라.](https://github.com/lcomment/effective-java-study/tree/master/08_일반적인_프로그래밍_원칙/item57)

<br>

## II. null 처리는 언제 해야 할까?

→ 메모리 직접 관리

- Stack 클래스가 문제가 됐던 이유는 메모리를 `직접 관리`했기 때문!
- 프로그래머 → 비활성 영역을 null 처리해야 함

<br>

## III. 캐시 역시 메모리 누수를 일으키는 주범이다.

```java
Object key = new Object("key");
Object value = new Object("value");

Map<Object, Object> cache = new HashMap<>();
cache.put(key, value);
```

→ 객체 참조를 캐시에 넣고, 다 쓴 뒤 놔두면 메모리 누수 말생

```java
Map<Object, Object> cache = new WeakHashMap<>();
cache.put(key, value);
```

- WeakHashMap
  - 다 쓴 엔트리를 즉시 자동으로 제거 가능
- 백그라운드 스레드 활용 or 캐시에 새 엔트리 추가 시 부수 작업 수행
  - LinkedHashMap → removeEldestEntry 메서드를 통해 후자의 방식으로 처리

<br>

## IV. 리스너와 콜백

→ 콜백을 약한 참조(weak reference)로 저장하자!

- WeakHashMap에 저장하면 GC가 즉시 수거
- 클라이언트에서 null 할당
