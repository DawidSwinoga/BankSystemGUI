package banksystem;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import banksystem.model.Account;
import banksystem.model.Address;
import banksystem.model.Database;
import banksystem.view.AccountOverviewController;
import banksystem.view.CreateAccountDialogController;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 *
 * @author Dawid
 */
public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private Database database;

    @Override
    public void start(Stage primaryStage) {
        SessionFactory sessionFactory;
        try {
            sessionFactory = new AnnotationConfiguration()
                    .configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Log exception!
            throw new ExceptionInInitializerError(ex);
        }
        
        
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            Address address = new Address("Bedzelin","Grzybowa","95-040");
            
            session.save(address);
            tx.commit();
        }catch(HibernateException e) {
            if (tx!=null) tx.rollback();
			e.printStackTrace();
        } finally {
            session.close();
        }
        
        
        database = new Database("database");
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("BankSystem");

        initRootLayout();
        ShowAccountOverview();
        this.primaryStage.setOnCloseRequest(e -> database.save());
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ShowAccountOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/AccountOverview.fxml"));
            AnchorPane accountOverview = (AnchorPane) loader.load();
            rootLayout.setCenter(accountOverview);

            AccountOverviewController controler = loader.getController();
            controler.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Account showCreateAccountDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/CreateAccountDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Nowe konto");
            dialogStage.initOwner(primaryStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            CreateAccountDialogController controler = loader.getController();
            controler.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            return controler.getAccount();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Stage getPrimatyStage() {
        return primaryStage;
    }

    public Database getDatabase() {
        return database;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
