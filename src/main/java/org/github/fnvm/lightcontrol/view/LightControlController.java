package org.github.fnvm.lightcontrol.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.github.fnvm.lightcontrol.viewmodel.LightControlViewModel;

public class LightControlController {

  @FXML private TextField valueTextField;
  @FXML private Slider brightnessSlider;
  @FXML private MenuButton monitorMenuButton;
  @FXML private CheckBox nightModeCheckBox;
  @FXML private Button applyButton;

  private LightControlViewModel viewModel;
  private LightControlUiState uiState;

  public void setViewModel(LightControlViewModel viewModel) {
    this.viewModel = viewModel;
    this.uiState = new LightControlUiState();
    initUi();
    setupListeners();
  }

  private void initUi() {
    uiState.setBrightness(viewModel.getBrightness());
    uiState.setNightMode(viewModel.isNightMode());
    uiState.setScreen(viewModel.getCurrentScreen());

    brightnessSlider.setValue(uiState.getBrightness());
    valueTextField.setText(viewModel.format(uiState.getBrightness()));
    nightModeCheckBox.setSelected(uiState.isNightMode());
    monitorMenuButton.setText(uiState.getScreen());

    monitorMenuButton.getItems().clear();
    viewModel
        .connectedScreensProperty()
        .forEach(
            screen -> {
              MenuItem item = new MenuItem(screen.outputName());
              item.setOnAction(
                  _ -> {
                    uiState.setScreen(screen.outputName());
                    monitorMenuButton.setText(screen.outputName());
                  });
              monitorMenuButton.getItems().add(item);
            });
  }

  private void setupListeners() {
    valueTextField.setTextFormatter(
        new TextFormatter<>(
            change -> change.getControlNewText().matches("\\d*(\\.\\d{0,2})?") ? change : null));

    brightnessSlider
        .valueProperty()
        .addListener(
            (_, _, val) -> {
              double normalized = viewModel.normalize(val.doubleValue());
              uiState.setBrightness(normalized);
              valueTextField.setText(viewModel.format(normalized));
            });

    valueTextField.setOnAction(this::handle);
    valueTextField
        .focusedProperty()
        .addListener(
            (_, _, now) -> {
              if (!now) parseField();
            });

    nightModeCheckBox.selectedProperty().addListener((_, _, val) -> uiState.setNightMode(val));

    applyButton.setOnAction(
        _ -> {
          parseField();
          viewModel.apply(uiState);
        });
  }

  private void parseField() {
    try {
      double val = viewModel.parse(valueTextField.getText());
      uiState.setBrightness(val);
      brightnessSlider.setValue(val);
      valueTextField.setText(viewModel.format(val));
    } catch (Exception ex) {
      valueTextField.setText(viewModel.format(uiState.getBrightness()));
    }
  }

  private void handle(ActionEvent e) {
    parseField();
  }
}
