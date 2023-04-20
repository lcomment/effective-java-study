# Item9. try-finally보다는 try-with-resources를 사용하라.

- close() 메서드를 호출해 직접 닫아줘야 하는 자원이 많음
  - InputStream, OutputStream, java.sql.Connection
- 닫지 않으면 성능 문제로 이어질 수 있음
  - finalizer를 사용하는 것은 좋지 못한 선택

## I. try-finally

> ### 자원 하나를 사용할 때

```java
static String firstLineOfFile(String path) throws IOException {
    BufferedReaer br = new Bufferredreader(new FileReader(path));

    try {
        return br.readLine();
    } finally {
        br.close();
    }
}
```

<br>

> ### 자원이 2개 이상일 때

```java
static String copy(String src, String dst) throws IOException {
    InputStream in = new FileInputStream(src);

    try {
        OutputStream out = new FileOutputStream(dst);

        try {
            byte[] buf = new byte[BUFFER_SIZE];
            int n;
            while((n = in.read(buf)) >= 0) { out.write(buf, 0, n); }
        } finally {
            out.close();
        }
    } finally {
        out.close();
    }
}
```

- try문에서 예외가 발생하면 close() 메서드가 실행되지 않음
- 명시적으로 구현해줄 수는 있으나 코드가 지저분해짐

<br>

## II. try-with-resources

- 해당 자원이 `AutoCloseable` 인터페이스를 구현해야 함
  - 단순히 void를 반환하는 `close()` 메서드 하나만 정의한 인터페이스

> ### 단일 자원 처리

```java
static String firstLineOfFile(String path) throws IOException {
    try (BufferedReaer br = new Bufferredreader(new FileReader(path))) {
        return br.readLine();
    }
}
```

<br>

> #### 다일 자원 처리

```java
static String copy(String src, String dst) throws IOException {
    ;

    try (InputStream in = new FileInputStream(src);
         OutputStream out = new FileOutputStream(dst)) {
        byte[] buf = new byte[BUFFER_SIZE];
        int n;
        while((n = in.read(buf)) >= 0) { out.write(buf, 0, n); }
    }
}
```

- 가독성 향상 및 에러 진단 수월
