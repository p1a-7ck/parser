# parser
Java Lab #19, Task #2

The Parser-project shows the usage of Composite design pattern, Iterable and Iterator interface implementation, Collections' sorting method. Parser algorithm based on Recursive parser, which tries to find nearest text-part's type pattern. Each type of text-part defined in a 'rules.properties'-file.


***
#parser algorithm

1. Checks for defined startsWith text-parts, if startsWith defined and starting index equals current index, then algorithm moves to the next step. Otherwise, if startsWith starting index greater than current index, then algorithm exits.

2. Checks for defined endsWith text-parts, if endsWith defined and starting index equals current index, then algorithm exits. Otherwise, if endsWith starting index greater than current index, then algorithm moves to the next step.

3. Checks for defined endsWith text-parts of the parent text-parts, if there are any endsWith text-part starting index equals current index, then algorithm exits to the parent.

4. Checks for defined contains text-parts, if text-part contains another text-parts, then method should be called recursively. Otherwise, searching any contains text-parts, which starts from current index.

5. If any of text-parts found, then algorithm goes to the step 2, otherwise, exits method


***
#rules.properties
'rules.properties'-file should have root-element with next value:

{text},{.contains},{.startsWith},{.endsWith},{.regexStart},{.regexEnd}

{text} - upper composite element (root of the tree);

{.contains},{.startsWith},{.endsWith},{.regexStart},{.regexEnd} - sub-names for keys;


***
Example:

Text=text // name for the text-part

Text.contains=Paragraph

Text.Paragraph=paragraph

Text.Paragraph.contains=Whitespace,Sentence

Text.Paragraph.endsWith=NewLineFeed // could be defined several types devided by ','-mark

Text.Paragraph.NewLineFeed=new line feed

Text.Paragraph.NewLineFeed.regexStart=[\\n]

Text.Paragraph.NewLineFeed.regexEnd=[^\\n]|$
