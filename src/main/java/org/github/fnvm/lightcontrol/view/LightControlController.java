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
  @FXML private Label errorLabel;

  private LightControlViewModel viewModel;

  public void setViewModel(LightControlViewModel viewModel) {
    this.viewModel = viewModel;
    initUi();
    setupListeners();
  }

  private void initUi() {
    brightnessSlider.setValue(viewModel.getBrightness());
    valueTextField.setText(viewModel.format(viewModel.getBrightness()));
    nightModeCheckBox.setSelected(viewModel.isNightMode());
    monitorMenuButton.setText(viewModel.getCurrentScreen());

    monitorMenuButton.getItems().clear();
    viewModel
        .connectedScreensProperty()
        .forEach(
            screen -> {
              MenuItem item = new MenuItem(screen.outputName());
              item.setOnAction(
                  _ -> {
                    viewModel.setCurrentScreen(screen.outputName());
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
              viewModel.setBrightness(normalized);
              valueTextField.setText(viewModel.format(normalized));
            });

    valueTextField.setOnAction(this::handle);
    valueTextField
        .focusedProperty()
        .addListener(
            (_, _, now) -> {
              if (!now) parseField();
            });

    nightModeCheckBox.selectedProperty().addListener((_, _, val) -> viewModel.setNightMode(val));

    applyButton.setOnAction(
        _ -> {
          parseField();
          viewModel.apply();
        });

    errorLabel.textProperty().bind(viewModel.errorMessageProperty());
  }

  private void parseField() {
    try {
      double val = viewModel.parse(valueTextField.getText());
      viewModel.setBrightness(val);
      brightnessSlider.setValue(val);
      valueTextField.setText(viewModel.format(val));
    } catch (Exception _) {
      valueTextField.setText(viewModel.format(viewModel.getBrightness()));
    }
  }

  private void handle(ActionEvent e) {
    parseField();
  }
}
