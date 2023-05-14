package en.steve.epx.commands;

public class ToggleLineCommentsHandler extends UpdateTextHandler {

	@Override
	protected String updateText(String text) {
		if (text.startsWith("//")) {
			text = text.substring(2);
			text = text.replace("\n//", "\n");
		}
		else {
			text = text.replace("\n", "\n//");
			text = "//" + text;
		}
		return text;
	}	
}
