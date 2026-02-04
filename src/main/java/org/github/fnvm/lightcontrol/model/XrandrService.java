package org.github.fnvm.lightcontrol.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class XrandrService implements DisplayService {
  private double currentBrightness = 1.0;
  private String currentScreen = "Not found";

  public XrandrService() {}

  public double getCurrentBrightness() {
    return currentBrightness;
  }

  public String getCurrentScreen() {
    return currentScreen;
  }

  public void setBrightness(String output, double brightnessValue, String gamma)
      throws IOException {
    var processBuilder = new ProcessBuilder();
    processBuilder.command(
        "xrandr",
        "--output",
        output,
        "--brightness",
        Double.toString(brightnessValue),
        "--gamma",
        gamma);
    processBuilder.start();
    currentBrightness = brightnessValue;
  }

  @Override
  public List<Screen> getConnectedScreens() throws IOException {
    var processBuilder = new ProcessBuilder();
    processBuilder.command("xrandr", "--listactivemonitors");

    var process = processBuilder.start();
    var out = new BufferedReader(new InputStreamReader(process.getInputStream()));

    List<Screen> connectedScreens = new ArrayList<>();

    int currentScreenId = 0;
    String line;
    while ((line = out.readLine()) != null) {
      line = line.trim();
      if (line.startsWith("Monitors")) continue;

      String[] parts = line.split("\\s+");
      String outputName = parts[parts.length - 1];

      if (line.contains("+*")) {
        currentScreen = outputName;
      }

      connectedScreens.add(new Screen(currentScreenId, outputName));
    }
    return connectedScreens;
  }
}
