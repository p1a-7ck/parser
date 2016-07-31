# parser
Java Lab #19, Task #2

The Parser-project shows the usage of Composite design pattern, Iterable and Iterator interface implementation, Collections' sorting method. Parser algorithm based on Recursive parser, which tries to find pattern of nearest text-part of any type. Each type of text-part defined in a 'rules.properties'-file.


***
#parser algorithm

1. Checks for defined startsWith-type for parent type, if startsWith-type is defined and it's starting index equals current index, then relevant text-part of startsWith-type added to the parent composite. If startsWith-type is defined and it's starting index greater than current index, then algorithm exits current recursion method.

2. Checks for defined endsWith-type for parent type, if endsWith-type is defined and it's starting index equals current index, then relevant text-part of endsWith-type added to the parent composite and algorithm exits current recursion method.

3. Checks for defined endsWith-type for all parent types, if there are any endsWith-type is defined and it's starting index equals current index, then algorithm exits resursion method up to the exact parent, which endsWith-type' starting index equals current index.

4. Checks for defined subTypes contained in parent type, if any subType contains another subTypes, then method should be called recursively for that subType. If subType don't have another subTypes, then it means that subType have search patterns and it's possible to find some relevant text-part. If subType starting index equals current index, then relevant text-part of subType added to the parent composite.

5. If any of type found, then algorithm loops from step #2, otherwise, exits current recursion method.


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
