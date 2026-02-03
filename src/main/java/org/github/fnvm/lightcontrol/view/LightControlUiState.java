package org.github.fnvm.lightcontrol.view;

public class LightControlUiState {

  private double brightness;
  private boolean nightMode;
  private String screen;

  public double getBrightness() {
    return brightness;
  }

  public void setBrightness(double brightness) {
    this.brightness = brightness;
  }

  public boolean isNightMode() {
    return nightMode;
  }

  public void setNightMode(boolean nightMode) {
    this.nightMode = nightMode;
  }

  public String getScreen() {
    return screen;
  }

  public void setScreen(String screen) {
    this.screen = screen;
  }
}
