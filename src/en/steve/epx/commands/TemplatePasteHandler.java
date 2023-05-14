package en.steve.epx.commands;

import java.util.regex.Pattern;

import org.eclipse.core.commands.IHandler;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.ui.PlatformUI;

public class TemplatePasteHandler extends UpdateTextHandler implements IHandler {

	@Override
	public boolean isEnabled() {
		return haveSelection() && getClipboardContent() != null;
	}	
	
	@Override
	protected String updateText(String text) {
		String template = getClipboardContent();
		if (template == null) {
			return text;
		}
		String [] placeholders = {
			"${0}", "${1}", "${2}", "${3}", "${4}", "${5}", "${6}", "${7}", "${8}", "${9}"
		};
		String [] values = text.split(Pattern.quote("|"));
		for (int i = 0; i < placeholders.length; i++) {
			template = template.replace(placeholders[i], i < values.length ? values[i] : "");
		}
		return template;
	}
	
	private String getClipboardContent() {
		Clipboard clipboard = new Clipboard(PlatformUI.getWorkbench().getDisplay());
		return (String) clipboard.getContents(TextTransfer.getInstance());
	}
}
