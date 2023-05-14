package en.steve.epx.commands;

public class ToggleBlockCommentHandler extends UpdateTextHandler {

	@Override
	protected String updateText(String text) {
		if (text.startsWith("/*")) {
			text = text.substring(2);
			text = replacelLast(text);
		}
		else {
			text = "/*" + text + "*/";
		}
		return text;
	}
	
	private String replacelLast(String s) {
		int i = s.lastIndexOf("*/");
		if (i != -1) {
			s = s.substring(0, i) + s.substring(i + 2); 
		}
		return s;
	}
}
