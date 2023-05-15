# en.steve.epx
A simple Eclipse plugin to add basic C++ editing features to the stock Eclipse Platform:

- Basic syntax colouring for .cpp and .h source files.
- File view icons for .cpp and .h source files.
- Location parsing and hyperlink navigation for GCC compiler messages.
- Commands to toggle line and block comments on text selections.
- Template Paste (see below).

Extends the IDE functionality provided by the basic Eclipse Platform for C++ development with the GNU G++ compiler.

## Template Paste ##
Template Paste allows a snippet of text containing placeholders (the template) to be pasted over text selections which are used to populate the placeholders in the final text.

For example:

> Define the template text: **This is the ${0} example.**

Copy this to the clipboard, highlight the target text and 'template paste' over it:

> Target text: **first** 

> Highlight and paste over: **This is the first example.**

> Target text: **second**

> Highlight and paste over: **This is the second example.**

And so on.

A placeholder can appear multiple times in the template.

Up to ten placeholders can be defined: **${0}** to **${9}**.

These correspond to equivalent positions in the target text, separated by '|'.

For example:

> Target text: **This|is|a|test**

> Template: **${0} ${1} not ${2} very ${3}ing ${3}.**

> Result: **This is not a very testing test.**

Unfilled placeholders will be removed from the text.
