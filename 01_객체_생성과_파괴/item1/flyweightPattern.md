# 플라이웨이트 패턴 (Flyweight Pattern)

: 인스턴스를 가능한 한 공유해서 사용함으로써 메모리를 절약하는 패턴

- 하나의 인스턴스를 가지고 여러 개의 `가상 인스턴스`를 제공
- new 연산자를 통한 메모리 낭비를 줄임

> ### 등장인물

1. Flyweight 역할
   - 공유에 사용할 클래스들의 `인터페이스`(API)
2. Concrete Flyweight 역할
   - Flyweight의 내용 `정의`
   - 실제 공유될 객체
3. Flyweight Factory의 역할
   - Flyweight의 인스턴스를 생성 또는 공유해주는 팩토리 역할
4. Client의 역할
   - 해당 패턴의 사용자

<br>

> ### 단점

- 특정 인스턴스만 다른 인스턴스처럼 동작하도록 하는 것은 불가능
- 객체의 값이 변경되면 공유 받는 가상 인스턴스를 사용하는 곳에 영향을 줄 수 있음

---

<br>

### Ref

- [@hoit_98](https://velog.io/@hoit_98/디자인-패턴-Flyweight-패턴)
- [@lee1535](https://lee1535.tistory.com/106)
