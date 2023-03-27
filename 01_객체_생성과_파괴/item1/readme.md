# Item1. 생성자 대신 정적 팩터리 메서드를 고려하라

> ### 클래스는 생성자와 별도로 `정적 팩터리 메서드`를 제공할 수 있다!

<br>

```java
public static Boolean valueOf(boolean b) {
    return b ? Boolean.TRUE : Boolean.FALSE;
}
```

→ Boxing Class `Boolean`에서 제공하는 정적 팩터리 메서드

<br>

## I. 정적 팩터리 메서드의 장점

> ### i. 이름을 가질 수 있다.

- OOP 측면
  - 객체는 본인에게 주어진 `역할`과 `책임`이 있음
  - 따라서 `역할 수행`이라는 생성 목적에 따라 생성자를 구별해 사용
- 정적 팩터리 메서드를 사용하면 반환될 객체의 특성을 쉽게 `묘사`
- 시그니처 측면
  - 생성자 → 하나의 시그니처로 하나의 생성자만 만들 수 있음
  - 정적 팩터리 메서드 → 하나의 시그니처로 여러 객체를 생성 및 묘사 가능
  - ex. Integer 클래스에서 제공하는 `MAX_VALUE`와 `MIN_VALUE`

```java
class Car {
    private final String name;
    private final int speed;

    // private 생성자
    private Car(String name, int oil) {
        this.name = name;
        this.oil = oil;
    }

    public static Car Sonata() {
        return new Car("Sonata", 265);
    }

    public static Car Bentley() {
        return new Car("Bentley", 335);
    }
}
```

<br>

> ### ii. 호출될 때마다 인스턴스를 새로 생성하지 않아도 된다.

- 불변 클래스 측면
  - 다음과 같은 방법으로 불필요한 객체 생성을 피함
    - 인스턴스를 미리 만들어 놓음
    - 새로 생성한 인스턴스를 캐싱(caching)하여 재활용
  - `플라이웨이트 패턴`(Flyweight Pattern)과 비슷한 기법
- 인스턴스 통제(instance-controlled)
  - 반복되는 요청에 같은 객체를 반환
  - `싱글톤`(singletone)으로 만들거나 `인스턴스화 불가`(noninstaniable)로 만들 수 있음

<br>

> ### iii. 반환 타입의 하위 타입 객체를 반환할 수 있는 능력이 있다.

- Java 8 이전
  - 인터페이스에 정적 메서드를 선언할 수 없음
  - 인터페이스의 `동반 `(Companion Class)를 만들어 정적 메서드 구현

```java
interface Car {
    void run();
}

class SlowCar implements Car {
    public void run() { System.out.println("느리게 달린다"); }
}

class FastCar implements Car {
    public void run() { System.out.println("빠르게 달린다"); }
}

// 동반 클래스
class Cars {
    private Cars() {}
    public static SlowCar getSlowCar() { return new SlowCar(); }
    public static FastCar getFastCar() { return new FastCar(); }
}
```

- Java 8 부터
  - 인터페이스가 `정적 메서드`를 가질 수 있음
    - 인스턴스화 불가 동반 클래스를 둘 필요가 없음
    - 동반 클래스에 두었던 public 정적 멤버 대부분을 인터페이스 자체에 두면 됨
  - `public` 정적 멤버만 허용
    - `package-private` 클래스에 많은 코드를 둘 수밖에 없음
- Java 9
  - private `정적 메서드`까지 허용
  - `정적 필드`와 `정적 멤버 클래스`는 여전히 public만 허용

```java
interface Car {
    public static SlowCar getSlowCar() { return new SlowCar(); }
    public static FastCar getFastCar() { return new FastCar(); }

    void run();
}

class SlowCar implements Car {
    public void run() { System.out.println("느리게 달린다"); }
}

class FastCar implements Car {
    public void run() { System.out.println("빠르게 달린다"); }
}
```

- 구체적인 구현체를 프로그래머에게 공개하지 않음
- API의 개념적인 `무게`가 가벼워짐
- 엄청난 `유연성`

<br>

> ### iv. 입력 매개변수에 따라 매번 다른 클래스의 객체를 반환할 수 있다.

- `하위 타입`이기만 하면 어떤 클래스의 객체든 반환 가능
- 다음 릴리스(release)에서 다른 객체를 반환해도 됨
- 클라이언트는 팩터리가 반환하는 인스턴스에 대해 알 수도, 알 필요도 없음

```java
public static <E extends Enum<E>> EnumSet<E> noneof(Class<E> elementType) {
    Enum<?>[] universe = getUniverse(elementType);

    if(universe == null)
        throw new ClassCastException(elementType + " not an enum");

    if(universe.length <= 64)
        return new RegularEnumSet<>(elementType, universe);
    else
        return new JumboEnumSet<>(elementType, universe);
}
```

<br>

> ### v. 정적 팩터리 메서드를 작성하는 시점에는 반환할 객체의 클래스가 존재하지 않아도 된다.

- 기존의 인터페이스나 클래스를 상속 받는다면, 언제든지 의존성을 주입 받아 사용 가능
  - 클래스가 존재해야 생성자가 존재 가능
  - 정적 팩터리 메서드는 클래스가 존재하지 않아도 존재 가능
- 프로그램 규모가 점점 커지면서 인터페이스를 먼저 생성하는 경우가 많아짐
- 서비스 제공자 프레임워크 (Service Provider Framework)
  - 서비스 인터페이스 - `Connection`
  - 제공자 등록 API - `DriverManager.registerDriver`
  - 서비스 접근 API - `DriverManager.getConnection`
  - 서비스 제공자 인터페이스 - `Driver`

```java
public class Main {
    public static void main(String[] args) {
        TestService service = TestService.getNewInstance();

        service.getName();
    }
}
```

&nbsp; 위는 실습을 진행할 Main 클래스다.

```java
// 서비스 접근 API 구현
public interface TestService {
    void getName();

    static TestService getNewInstance() {
        TestService service = null;

        try {
            Class<?> childClass = Class.forName("item1.TestServiceImpl");
            service = (TestService) childClass.newInstance();
        } catch (ClassNotFoundException e) {
            System.out.println("Not Exist Class");
        } catch (IllegalAccessException e) {
            System.out.println("Class File Access Error");
        } catch (InstantiationException e) {
            System.out.println("Cannot Upload in Memory");
        }

        return service;
    }
}
```

&nbsp; 아직 TestServiceImpl.java를 구현하지 않은 상태다. 클라이언트가 서비스 접근 API를 사용할 때 원하는 구현체의 조건을 명시할 수 있다. 이후 인터페이스의 구현체를 작성해줘도 문제 없게 된다.

```java
package item1;

public class TestServiceImpl implements TestService {

   @Override
   public void getName() {
       System.out.println("Load Success");
   }
}
```

<br>

## II. 정적 팩터리 메서드의 단점

> ### i. 정적 팩터리 메서드만 제공하면 하위 클래스를 만들 수 없다.

- 상속을 위해선 public이나 protected 생성자가 필요
- 정적 팩터리 메서드만 제공하기 위해 private 생성자만 사용하면 상속 불가능

<br>

> ### ii. 정적 팩터리 메서드는 프로그래머가 찾기 어렵다.

- API 설명에 명확히 드러나지 않음
- 직접 찾아내야 함
- 따라서 `명명 규칙`들을 지켜줘야 함

<br>

## III. 명명 규칙

| 메서드 명                 | 설명                                                                                                                                       | 예시                                                       |
| ------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------ | ---------------------------------------------------------- |
| from                      | 매개변수를 `하나` 받아 해당 타입의 인스턴스를 반환하는 형변환 메서드                                                                       | Date d = Date.from(instant)                                |
| of                        | `여러` 매개변수를 받아 적합한 타입의 인스턴스를 반환하는 집계 메서드                                                                       | List&lt;String&gt; list = List.of("A", "B", "C")           |
| valueOf                   | from과 of의 더 자세한 버전                                                                                                                 | BigInteger prime = BigInteger.valueOf(Integer.MAX_VALUE)   |
| •instance<br>•getInstance | (매개변수를 받는다면) 매개변수로 명시한 인스턴스를 반환하지만, 같은 인스턴스임을 `보장하지 않음`                                           | StackWalker luke = StackWalker.getInstance(options)        |
| •create<br>•newInstance   | instance 혹은 getInstance와 같지만, 매번 새로운 인스턴스를 생성해 반환함을 `보장`                                                          | Object newArray = Array.newInstance(classObject, arrayLen) |
| getType                   | getInstance와 같으나, 생성할 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 때 쓴다.<br>Type은 팩터리 메서드가 반환할 객체의 타입이다. | FileStore fs = Files.getFileStore(path);                   |
| newType                   | newInstance와 같으나, 생성할 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 때 쓴다.<br>Type은 팩터리 메서드가 반환할 객체의 타입이다. | BufferedReader br = Files.newBufferedReader(path);         |
| type                      | getType과 newType 축소버전                                                                                                                 | List litany = Collections.list(legacyLitany);              |
