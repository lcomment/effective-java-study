package item1;

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
