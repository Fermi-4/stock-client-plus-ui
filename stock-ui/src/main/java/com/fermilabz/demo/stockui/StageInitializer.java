package com.fermilabz.demo.stockui;
import com.fermilabz.demo.stockui.ChartApplication.StageReadyEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

    @Value("classpath:/chart.fxml")
    private Resource chartResource;


    @Value( value = "${spring.application.ui.title}")
    private String applicationTitle;

    private ApplicationContext applicationContext;


    public StageInitializer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(chartResource.getURL());

            fxmlLoader.setControllerFactory( aClass -> applicationContext.getBean( aClass ));

            Parent parent =  fxmlLoader.load();
            Stage stage = event.getStage();
            stage.setScene(new Scene(parent,800,600));
            stage.setTitle( applicationTitle );
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
