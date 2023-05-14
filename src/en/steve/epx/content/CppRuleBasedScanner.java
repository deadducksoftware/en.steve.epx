package en.steve.epx.content;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;
import org.eclipse.ui.PlatformUI;

public class CppRuleBasedScanner extends RuleBasedScanner {
	
	public CppRuleBasedScanner() {
		updateRules();
	}
	
	public void updateRules() {
		ColorRegistry colorRegistry = PlatformUI.getWorkbench()
				.getThemeManager().getCurrentTheme().getColorRegistry(); 
		IToken tokenKeyword = new Token(new TextAttribute(
				colorRegistry.get("en.steve.epx.content.color.keyword"), null, SWT.BOLD));
		IToken tokenPreprocessor = new Token(new TextAttribute(
				colorRegistry.get("en.steve.epx.content.color.preprocessor")));
		IToken tokenComment = new Token(new TextAttribute(
				colorRegistry.get("en.steve.epx.content.color.comment")));
		IToken tokenString = new Token(new TextAttribute(
				colorRegistry.get("en.steve.epx.content.color.string")));		
		WordRule keywordRule = new WordRule(new IWordDetector() {
			
			// Avoid detecting keywords embedded in words
			private boolean prevCharIsNotKeywordPart = true;
			
			public boolean isWordStart(char c) {
				boolean b = isKeywordPart(c) && prevCharIsNotKeywordPart;
				prevCharIsNotKeywordPart = !isKeywordPart(c);
				return b;
			}

			public boolean isWordPart(char c) {
				return isKeywordPart(c);
			}
			
			private boolean isKeywordPart(char c) {
				return Character.isLetterOrDigit(c) || c == '_';
			}			
		});
		for (String s : KEYWORDS)
			keywordRule.addWord(s, tokenKeyword);
		for (String s : KEYWORDS2)
			keywordRule.addWord(s, tokenKeyword);		
		IRule[] rules = new IRule[] {
			new MultiLineRule("/*", "*/", tokenComment),
			new EndOfLineRule("#", tokenPreprocessor),
			new EndOfLineRule("//", tokenComment),
			new SingleLineRule("\"", "\"", tokenString, '\\'),
			new SingleLineRule("'", "'", tokenString, '\\'),
			keywordRule,
			new WhitespaceRule(new IWhitespaceDetector() {
				
				public boolean isWhitespace(char c) {
					return Character.isWhitespace(c);
				}
			})			
		};
		setRules(rules);
	}
	
	private static final String[] KEYWORDS = new String[] {
		"alignas", "alignof", "and", "and_eq", "asm", "atomic_cancel", "atomic_commit",
		"atomic_noexcept", "auto", "bitand", "bitor", "bool", "break", "case", "catch",
		"char", "char8_t", "char16_t", "char32_t", "class", "compl", "concept", "const",
		"consteval", "constexpr", "constinit", "const_cast", "continue", "co_await",
		"co_return", "co_yield", "decltype", "default", "delete", "do", "double", 
		"dynamic_cast", "else", "enum", "explicit", "export", "extern", "false", "float",
		"for", "friend", "goto", "if", "inline", "int", "long", "mutable", "namespace",
		"new", "noexcept", "not", "not_eq", "nullptr", "operator", "or", "or_eq", "private",
		"protected", "public", "reflexpr", "register", "reinterpret_cast", "requires",
		"return", "short", "signed", "sizeof", "static", "static_assert", "static_cast",
		"struct", "switch", "synchronized", "template", "this", "thread_local", "throw",
		"true", "try", "typedef", "typeid", "typename", "union", "unsigned", "using",
		"virtual", "void", "volatile", "wchar_t", "while", "xor", "xor_eq" 
	};

	private static final String[] KEYWORDS2 = new String[] {
		"final", "override", "transaction_safe", "transaction_safe_dynamic", "import", "module"
	};
}
