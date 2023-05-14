package en.steve.epx.build;

import java.io.File;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.IHyperlink;
import org.eclipse.ui.console.IPatternMatchListenerDelegate;
import org.eclipse.ui.console.PatternMatchEvent;
import org.eclipse.ui.console.TextConsole;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.ITextEditor;

public class GccMessagePatternMatcher implements IPatternMatchListenerDelegate {
	
	private TextConsole console;

	@Override
	public void connect(TextConsole console) {
		this.console = console;
	}

	@Override
	public void disconnect() {
		this.console = null;
	}

	@Override
	public void matchFound(PatternMatchEvent event) {
		try {
			IDocument document = console.getDocument();
			String matchText = document.get(event.getOffset(), event.getLength());
			String[] text = matchText.split(":");
			if (text.length < 2)
				return;
			File filePath = findFile(text[0]);
			if (filePath == null)
				return;
			int linkLength = text[0].length();
			int lineNumber = Integer.parseInt(text[1]);
			console.addHyperlink(new IHyperlink() {
				
				@Override
				public void linkExited() {
				}

				@Override
				public void linkEntered() {
				}

				@Override
				public void linkActivated() {
					try {
						IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
						IFileStore fileStore = EFS.getStore(filePath.toURI());
						IEditorPart editorPart = IDE.openEditorOnFileStore(activePage, fileStore);
						if (editorPart instanceof ITextEditor) {
							ITextEditor textEditor = (ITextEditor) editorPart;
							IDocument document = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
							IRegion region = document.getLineInformation(lineNumber - 1);
							textEditor.selectAndReveal(region.getOffset(), region.getLength());
						}
					}
					catch (Exception e) {
					}
				}
			}, event.getOffset(), linkLength);
		}
		catch (BadLocationException e) {
		}
	}
	
	private File findFile(String filePath) {
		File file = new File(filePath);
		if (file.exists())
			return file;
		for (IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			if (project.isOpen()) {
				file = new File(project.getLocation().toFile(), filePath);
				if (file.exists())
					return file;
			}
		}
		return null;
	}
}
	