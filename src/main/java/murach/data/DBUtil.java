package murach.data;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DBUtil {
    // Lazy-initialized EntityManagerFactory to avoid failures during class
    // initialization when environment is not configured.
    private static volatile EntityManagerFactory emf = null;

    public static EntityManagerFactory getEmFactory() {
        if (emf == null) {
            synchronized (DBUtil.class) {
                if (emf == null) {
                    Map<String, String> props = new HashMap<>();

                    String url = System.getenv("DB_URL");
                    String user = System.getenv("DB_USER");
                    String password = System.getenv("DB_PASSWORD");

                    // Some platforms (like Render) expose a DATABASE_URL in the
                    // format: postgres://user:pass@host:port/dbname
                    // Accept that as a fallback and convert it to a JDBC URL.
                    String dbEnv = System.getenv("DATABASE_URL");
                    if ((url == null || url.isEmpty()) && dbEnv != null && !dbEnv.isEmpty()) {
                        url = dbEnv;
                    }

                    if (url != null && (url.startsWith("postgresql://") || url.startsWith("postgres://"))) {
                        try {
                            URI uri = new URI(url);
                            String userInfo = uri.getUserInfo();
                            if ((user == null || user.isEmpty()) && userInfo != null) {
                                String[] parts = userInfo.split(":", 2);
                                if (parts.length > 0) user = parts[0];
                                if (parts.length > 1) password = parts[1];
                            }
                            String host = uri.getHost();
                            int port = uri.getPort();
                            String path = uri.getPath(); // includes leading '/'
                            String jdbc = "jdbc:postgresql://" + host + (port != -1 ? (":" + port) : "") + (path != null ? path : "");
                            url = jdbc;
                        } catch (URISyntaxException e) {
                            System.err.println("DBUtil: unable to parse DATABASE_URL: " + e);
                        }
                    }

                    // Provide sensible local defaults when env vars are not set.
                    // These defaults assume a local PostgreSQL database running
                    // on the typical port and a database named 'murach'. Adjust
                    // as needed for your environment, or set the environment
                    // variables DB_URL, DB_USER and DB_PASSWORD.
                    if (url == null || url.isEmpty() || user == null || user.isEmpty()) {
                        url = "jdbc:postgresql://localhost:5432/murach";
                        user = "murach_user";
                        password = "sesame";
                        System.out.println("DBUtil: using fallback DB settings: " + url + " user=" + user);
                    }

                    props.put("javax.persistence.jdbc.url", url);
                    props.put("javax.persistence.jdbc.user", user);
                    props.put("javax.persistence.jdbc.password", password);
                    props.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
                    props.put("hibernate.hbm2ddl.auto", "update");

                    try {
                        emf = Persistence.createEntityManagerFactory("emailListPU", props);
                    } catch (Throwable t) {
                        // Log the problem and rethrow as a runtime exception so
                        // the server log contains a readable cause.
                        System.err.println("DBUtil: failed to create EntityManagerFactory: " + t);
                        t.printStackTrace();
                        throw new RuntimeException("Could not initialize JPA EntityManagerFactory", t);
                    }
                }
            }
        }
        return emf;
    }
}