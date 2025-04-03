module lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires static lombok;


    opens lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject to javafx.fxml;
    exports lk.ijse.gdse.project.theserenitymentalhealththerapycenterproject;
}