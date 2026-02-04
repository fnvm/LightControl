package org.github.fnvm.lightcontrol.viewmodel;

import javafx.animation.PauseTransition;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Duration;
import org.github.fnvm.lightcontrol.model.Screen;
import org.github.fnvm.lightcontrol.model.XrandrService;
import org.github.fnvm.lightcontrol.view.LightControlUiState;

import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Locale;

public class LightControlViewModel {
  private static final Logger logger = System.getLogger(LightControlViewModel.class.getName());

  private static final double MIN_VALUE = 0.1;
  private static final double MAX_VALUE = 1.5;

  private final DoubleProperty brightness = new SimpleDoubleProperty();
  private final BooleanProperty nightMode = new SimpleBooleanProperty();
  private final StringProperty currentScreen = new SimpleStringProperty();
  private final StringProperty errorMessage = new SimpleStringProperty("");
  private final ObservableList<Screen> connectedScreens = FXCollections.observableArrayList();

  public LightControlViewModel() {
    try {
      connectedScreens.setAll(XrandrService.getOutputs());
      currentScreen.set(XrandrService.getCurrentScreen());
      brightness.set(XrandrService.getCurrentBrightness());
    } catch (IOException e) {
      logger.log(Level.ERROR, () -> "Error while accessing xrandr: " + e.getMessage());
      setErrorMessage("Error while accessing xrandr");
    }
  }

  public double normalize(double value) {
    return clamp(truncate(value));
  }

  public double parse(String text) {
    double val = Double.parseDouble(text.replace(',', '.'));
    return normalize(val);
  }

  public String format(double value) {
    return String.format(Locale.US, "%.2f", value);
  }

  public void apply(LightControlUiState state) {
    double uiBrightness = state.getBrightness();
    boolean uiNightMode = state.isNightMode();
    String uiScreen = state.getScreen();
    brightness.set(uiBrightness);
    nightMode.set(uiNightMode);
    currentScreen.set(uiScreen);

    String gamma = "1:1:1";
    if (uiNightMode) gamma = "1:0.85:0.7";
    try {
      XrandrService.setBrightness(uiScreen, uiBrightness, gamma);
    } catch (IOException e) {
      logger.log(Level.ERROR, () -> "Error applying xrandr settings: " + e.getMessage());
      setErrorMessage("Error applying xrandr settings");
    }
  }

  private double truncate(double value) {
    return Math.floor(value * 100) / 100.0;
  }

  private double clamp(double value) {
    return Math.max(MIN_VALUE, Math.min(MAX_VALUE, value));
  }

  public double getBrightness() {
    return brightness.get();
  }

  public String getCurrentScreen() {
    return currentScreen.get();
  }

  public boolean isNightMode() {
    return nightMode.get();
  }

  public ObservableList<Screen> connectedScreensProperty() {
    return connectedScreens;
  }

  public StringProperty errorMessageProperty() {
    return errorMessage;
  }

  private void setErrorMessage(String msg) {
      errorMessage.set(msg);

      PauseTransition delay = new PauseTransition(Duration.seconds(3.0));
      delay.setOnFinished(_ -> errorMessage.set(""));
      delay.play();
  }
}
