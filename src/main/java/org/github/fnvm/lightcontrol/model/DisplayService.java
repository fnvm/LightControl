package org.github.fnvm.lightcontrol.model;

import java.io.IOException;
import java.util.List;

public interface DisplayService {
  List<Screen> getConnectedScreens() throws IOException;

  void setBrightness(String output, double brightnessValue, String gamma) throws IOException;

  double getCurrentBrightness();

  String getCurrentScreen();
}
