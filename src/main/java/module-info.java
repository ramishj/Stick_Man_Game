module com.example.firstjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media; // Add this line to include the media module


    opens com.example.firstjavafx to javafx.fxml;
    exports com.example.firstjavafx;
}