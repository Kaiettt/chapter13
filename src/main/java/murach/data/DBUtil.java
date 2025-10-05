package murach.data;
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DBUtil {
    private static final EntityManagerFactory emf;

    static {
        Map<String, String> props = new HashMap<>();
        props.put("javax.persistence.jdbc.url", System.getenv("DB_URL"));
        props.put("javax.persistence.jdbc.user", System.getenv("DB_USER"));
        props.put("javax.persistence.jdbc.password", System.getenv("DB_PASSWORD"));
        props.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
        props.put("hibernate.hbm2ddl.auto", "update");

        emf = Persistence.createEntityManagerFactory("emailListPU", props);
    }

    public static EntityManagerFactory getEmFactory() {
        return emf;
    }
}