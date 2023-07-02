public class Test {
    Connection c = null;
        try {
        Class.forName("org.postgresql.Driver");
        c = DriverManager
                .getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                        "aliiiw", "ali123");
    } catch (Exception e) {
        e.printStackTrace();
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(0);
    }
        System.out.println("Opened database successfully");
}