module lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
//    requires com.jfoenix;
//    requires net.sf.jasperreports.core;
//    requires java.mail;

    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.naming;

    opens lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.config to jakarta.persistence;
    opens lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject.entity to org.hibernate.orm.core;

//    opens lk.ijse.gdse.project.ormproject.dto.tm to javafx.base;
//    opens lk.ijse.gdse.project.ormproject.controller to javafx.fxml;

    opens lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject to javafx.fxml;
    exports lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject;
}