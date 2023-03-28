# Item3. private 생성자나 열거 타입으로 싱글턴임을 보증하라

## I. 싱글턴 (singletone)

: 인스턴스를 오직 `하나만` 생성할 수 있는 클래스

> ### 생성 방법

- 생성자는 `private`으로 감춤
- `public static 멤버`로 인스턴스 접근
- 열거 타입 방식

<br>

## II. 싱글턴 이용하기

> ### public static 멤버가 `final` 필드인 방식

```java
public  class TestService {
    public static final TestService SINGLETON_INSTANCE = new TestService();
    private TestService() {}
}
```

- private 생성자는 TestService.SINGLETON_INSTANCE가 초기화될 때 딱 `한번만` 호출
- Reflection API의 `AccessibleObject.setAccessible`을 사용하지 않으면 예외 없음
- 장점
  - 코드의 `간결성`
  - 해당 클래스가 싱글턴임이 `API`에서 명확히 드러남

<br>

> ### 정적 팩터리 메서드 이용

```java
public  class TestService {
    private static final TestService SINGLETON_INSTANCE = new TestService();
    private TestService() {}

    public static TestService getInstance() {return SINGLETON_INSTANCE; }
}
```

- 항상 같은 객체의 참조 반환
- 역시나 Reflection API로 예외 발생 가능
- 장점
  - 언제든 싱글턴이 아니게 변경 가능
  - 원한다면 정적 팩터리를 `제네릭 싱글턴 팩터리`로 만들 수 있음
  - 정적 팩터리의 메서드 참조를 `공급자`(Supplier)로 사용 가능

<br>

> ### 원소가 하나인 열거 타입 선언

```java
public enum TestService {
    SINGLETON_INSTANCE;
}
```

- 원리
  - 열거 타입은 보통 열거 타입 클래스를 만들고 `상수 지정`
  - 그 `상수의 데이터`를 담고 있는 객체는 `Heap` 영역에 있음
  - 열거 상수에는 열거 객체의 `주소`가 담겨져 있음
  - `열거 변수`에는 열거 상수에 담겨져 있는 열거 `객체의 주소값`이 들어감
  - 따라서, 열거 변수를 만드는 것은 Heap 영역에 있는 열거 객체의 주소값을 바로보게 하는 원리
- 대부분의 상황에서 `열거 타입 방식`이 가장 좋은 방법
- 만약 Enum 외의 클래스를 상속해야 한다면, 이 방식은 사용 불가능
