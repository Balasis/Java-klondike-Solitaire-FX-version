This is an effort to create a klondike solitaire game using JavaFX. Unlike javascript, javaFX drag and drop limitations making it really hard to be achieved.

Notes
-Faked the drag by using translate x,y to move the cards ( for the animation part).
-Mouse Events where used instead of Dragevents; reason was the view part, while I tried both I had 1 method blocking the other(there's no concurrency so...)
-had to create Boundary objects related to localScene holding the position of the last cards to create possible destinations to "drop" the cards,then intercept the boundry of the source
(cardMoved) to all possible destinations.

To be continue...(right now I need to solve the issue of cards hiding behing other cards due to to z-order, fx giving me another headache because unlike javascript it uses the z-index/order;
for the actual viewable order... what I currently think as solution is to replace the parent container of Vboxes to something that doesn't provide an order, therefore I will change z-order
without affecting the view part.)
