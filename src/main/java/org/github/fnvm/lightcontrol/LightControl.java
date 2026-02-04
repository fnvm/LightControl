package org.github.fnvm.lightcontrol;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.github.fnvm.lightcontrol.view.LightControlController;
import org.github.fnvm.lightcontrol.viewmodel.LightControlViewModel;

import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

public class LightControl extends Application {
  private static final Logger logger = System.getLogger(LightControl.class.getName());

  @Override
  public void start(Stage primaryStage) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
      Parent root = loader.load();
      LightControlController controller = loader.getController();

      LightControlViewModel viewModel = new LightControlViewModel();
      controller.setViewModel(viewModel);

      Scene scene = new Scene(root);
      scene.getStylesheets().add(getClass().getResource("/MainView.css").toExternalForm());

      primaryStage.setTitle("Light Control");
      primaryStage.getIcons().add(
              new Image(getClass().getResourceAsStream("/icon.png"))
      );
      primaryStage.setScene(scene);
      primaryStage.show();

      Platform.runLater(primaryStage::centerOnScreen);
    } catch (IOException e) {
      logger.log(Level.ERROR, "FXML loading error: {0}", e.getMessage());
      System.exit(1);
    }
  }

  static void main() {
    launch();
  }
}
