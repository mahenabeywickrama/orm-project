package lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.config;

import lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.util.Properties;

public class FactoryConfiguration {
    private static FactoryConfiguration factoryConfiguration;
    private final SessionFactory sessionFactory;

    private FactoryConfiguration() throws IOException {
        Configuration configuration = new Configuration();
        Properties properties = new Properties();

        properties.load(Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("hibernate.properties"));

        configuration.setProperties(properties);

        configuration.addAnnotatedClass(Patient.class);
        configuration.addAnnotatedClass(Enrollment.class);
        configuration.addAnnotatedClass(Payment.class);
        configuration.addAnnotatedClass(Therapist.class);
        configuration.addAnnotatedClass(TherapyProgram.class);
        configuration.addAnnotatedClass(TherapySession.class);
        configuration.addAnnotatedClass(User.class);

        sessionFactory = configuration.buildSessionFactory();
    }

    public static FactoryConfiguration getInstance() throws IOException {
        if (factoryConfiguration == null) {
            factoryConfiguration = new FactoryConfiguration();
        }
        return factoryConfiguration;
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }
}
