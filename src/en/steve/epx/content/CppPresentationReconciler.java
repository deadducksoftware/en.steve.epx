package en.steve.epx.content;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;

public class CppPresentationReconciler extends PresentationReconciler {
	
	private CppRuleBasedScanner scanner;

	public CppPresentationReconciler() {
		scanner = new CppRuleBasedScanner();
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(scanner);
		setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
	}

	@Override
	public void install(ITextViewer viewer) {
		super.install(viewer);
		InstanceScope.INSTANCE.getNode("org.eclipse.ui.workbench")
			.addPreferenceChangeListener(event -> {
				scanner.updateRules();
				viewer.invalidateTextPresentation();
			});
	}
}
