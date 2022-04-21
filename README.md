# Java MiGLayout Tester
![Tester](https://i.ibb.co/8bYWQSQ/miglayout.png)

A Tester used to instantly display the effect of a MigLayout constraint
## Insert Object
use ` <text> | <constraints> ` to insert new object into the display
here is a few case to display things other than pure text
 - `[r,g,b]` will display a block of color
 - `[url]` will display the url's image
 - `[path]` will display the path's image
## Global Constraints
Applying global constraints
have the same effect as
```java
container.setLayout(new MigLayout( <constraints> ));
```
