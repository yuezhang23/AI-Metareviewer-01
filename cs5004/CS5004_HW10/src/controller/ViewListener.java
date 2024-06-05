package view;

public interface ViewListener {
  void handleLoadEvent();
  void handleSaveEvent();
  void handleRender(String command);
  void handleBrighten(String cm);
}
