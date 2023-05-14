package en.steve.epx.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.ITextEditor;

public abstract class UpdateTextHandler implements IHandler {
	
	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		return replaceSelection(event, true);
	}

	@Override
	public boolean isEnabled() {
		return haveSelection();
	}

	@Override
	public boolean isHandled() {
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
	}	

	protected boolean haveSelection() {
		IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (editorPart instanceof ITextEditor) {
			ITextEditor textEditor = (ITextEditor) editorPart;
			ISelection selection = textEditor.getSelectionProvider().getSelection();
			if (selection instanceof ITextSelection) {
				ITextSelection text = (ITextSelection) selection;
				return text.getLength() > 0;
			}
		}
		return false;
	}
	
	protected ITextSelection replaceSelection(ExecutionEvent event, boolean keepSelection) {
		IEditorPart editorPart = HandlerUtil.getActiveEditor(event);
		if (editorPart instanceof ITextEditor) {
			ITextEditor textEditor = (ITextEditor) editorPart;
			ISelection selection = textEditor.getSelectionProvider().getSelection();
			if (selection instanceof ITextSelection) {
				ITextSelection text = (ITextSelection) selection;
				String selectedText = updateText(text.getText());
				IDocument document = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
				try {
					document.replace(text.getOffset(), text.getLength(), selectedText);
					if (keepSelection) {
						textEditor.selectAndReveal(text.getOffset(), selectedText.length());
					}
				} catch (BadLocationException e) {
					// Do nothing
				}
			}
		}		
		return null;
	}
	
	protected abstract String updateText(String text);
}
