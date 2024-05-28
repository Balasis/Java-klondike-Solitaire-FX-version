This is about creating a klondike solitaire game using JavaFX.

Notes
-Faked the drag by using translate x,y to move the cards ( for the animation part).
-Mouse Events where used instead of Dragevents; reason was the view part, while I tried both I had 1 method blocking the other(there's no concurrency so...)
-had to create Boundary objects related to localScene holding the position of the last cards to create possible destinations to "drop" the cards,then intercept the boundry of the source
(cardMoved) to all possible destinations.
-z order was used with combination of An anchor point because cards were getting hidden by the hierarchy. (anchor point was used to not have differences in view while order change)

P.s found out about checking booleans on Lists using .stream()  ( allMatch, anyMatch, and noneMatch) ...a door to one line iterations < 3
P.s of p.s . javaFX capabilities are a bit sad
