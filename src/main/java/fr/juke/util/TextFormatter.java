package fr.juke.util;

public class TextFormatter {
  public static TextFormatter INSTANCE = new TextFormatter();

  public static String tablistChars(String string) {
    String output;
    output = string.replaceAll("#TPS", Double.toString(TablistVariables.getTps()));
    output = output.replaceAll("#MSPT", Double.toString(TablistVariables.getMspt()));
    output = output.replaceAll("#UPTIME", TablistVariables.getUptime());
    output = output.replaceAll("#PLAYERCOUNT", Integer.toString(TablistVariables.getPlayerCount()));
    output = output.replaceAll("#N", "\n");
    return output;
  }
}
