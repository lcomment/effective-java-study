# Item2. 생성자에 매개변수가 많다면 빌더를 고려하라.

## I. 서론

&nbsp; [Item1](https://github.com/lcomment/effective-java-study/blob/master/01_객체_생성과_파괴/item1/readme.md)에서 정적 팩터리 메서드를 배우면서, 생성자와 정적 팩터리 메서드의 장단점을 잘 파악하고 적절히 사용하기를 권장했다. 하지만 둘다 똑같은 제약을 한 가지 가지고 있다. 선택적 매개변수가 많을 때 대응하기 힘들다는 것이다. `선택적 매개변수`는 대다수가 디폴트 값을 가지는 경우가 많다. 이를 안전하고 가독성 있게 처리하는 것이 바로 `빌더 패턴`이다. 빌더 패턴을 살펴보기 이전에, `점층적 생성자 패턴`과 `자바 빈즈 패턴`에 대해 먼저 알아보자.

<br>

## II. 점층적 생성자 패턴 (Telescoping Constructor Pattern)

: 생성자를 필수 매개변수 1개만 받는 생성자, 필수 매개변수 1개와 선택 매개변수 1개를 받는 생성자, 선택 매개변수 2개를 받는 생성자 등에 형태로 매개변수 개수만큼 `생성자를 늘리는 방식`

- 매개변수들이 유효한지? → 생성자에서 확인하면 `일관성` 유지 가능
- 매개변수의 수에 따라 생성자의 수가 늘어남
- 클라이언트 코드를 작성하거나 읽기 어려움
- 매개변수의 순서를 바꿔 건네주면 `런타임 오류` 발생

> ### → [실습 코드](https://github.com/lcomment/effective-java-study/blob/master/00_실습코드/src/item2/NutritionFacts_1.java)

<br>

## III. 자바빈즈 패턴 (JavaBeans Pattern)

: 매개변수가 없는 생성자로 객체 생성 후, `Setter`를 호출해 원하는 매개변수의 값을 설정하는 방식

- 점층적 생성자 패턴에 비해 `가독성`이 좋음
- 객체 하나를 만들기 위해 메서드 `여러 개` 호출
- 객체가 완전히 생성되기 전까지 `일관성`이 무너진 상태에 놓이게 됨
  - 클래스를 `불변`으로 만들 수 없음
  - 스레드의 안정성을 위해 추가 작업이 필요

> ### → [실습 코드](https://github.com/lcomment/effective-java-study/blob/master/00_실습코드/src/item2/NutritionFacts_2.java)

<br>

## IV. 빌더 패턴 (Builder Pattern)

: 필수 매개변수는 생성자를 통해 `빌더 객체`를 얻은 후 선택 매개변수는 세터 메서드들로 설정하고 build 메서드를 호출하는 점층적 생성자 패턴의 안정성 + 자바빈즈 패턴의 가독성을 겸비한 패턴

> ### → [실습 코드](https://github.com/lcomment/effective-java-study/blob/master/00_실습코드/src/item2/NutritionFacts_3.java)

- Builder의 Setter 메서드들은 Builder 자신을 반환
  - 연쇄적으로 호출 → Fluent API 또는 Method Chaining
- 명명된 선택적 매개변수를 흉내낸 것
  - C++, Javascript, Python의 선택적 매개변수, 명명된 매개변수와 비슷
- `유효성 검사` 필요
  - 직접 Validator 인터페이스 또는 클래스 구현
  - Spring에서는 의존성 추가 후, `@Valid` 또는 `@Validated` 사용

<br>

> ### Builder 패턴은 계층적으로 설계된 클래스와 함께 쓰기에 좋다.

→ 추상 클래스는 추상 빌더를, 구체 클래스는 구체 빌더를 갖게 함

- 장점
  - 가변인수 매개변수를 여러 개 사용 가능
  - 높은 유연성 (Method Chaining)
  - 공변반환 타이핑 기능 : 하위 클래스의 메서드가 상위 클래스의 메서드가 정의한 반환 타입이 아닌, 그 하위 타입을 반환하는 기능
- 단점
  - Builder를 생성하는 리소스
    - 비용이 크진 않지만 선응이 민감한 상황에서 문제 발생
  - 코드가 긺
    - 매개변수가 4개 이상일 때 값어치를 함

> ### → [실습 코드](https://github.com/lcomment/effective-java-study/blob/master/00_실습코드/src/item2/builderSample)

<br>

## IV. 결론

> ### 생성자나 정적 팩터리가 처리해야 할 매개변수가 많다면 `빌더 패턴`을 사용하도록 하자❗️
