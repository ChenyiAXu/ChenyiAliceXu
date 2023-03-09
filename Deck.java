/**
 * Your name here:Chenyi Xu
 * Your McGill ID here:260948311
 **/

import java.util.Random;



public class Deck {
 public static String[] suitsInOrder = {"clubs", "diamonds", "hearts", "spades"};
 public static Random gen = new Random();

 public int numOfCards; // contains the total number of cards in the deck
 public Card head; // contains a pointer to the card on the top of the deck

 /*
  * TODO: Initializes a assignment2.Deck object using the inputs provided
  */
 public Deck(int numOfCardsPerSuit, int numOfSuits) {
  /**** ADD CODE HERE ****/
  //raise illegal aregument exception
  if (numOfCardsPerSuit < 1 || numOfCardsPerSuit > 13) {
   throw new IllegalArgumentException("Number of cads not within the range");
  }

  if (numOfSuits < 1 || numOfSuits > this.suitsInOrder.length) {
   throw new IllegalArgumentException("Number of suits not within the range");
  }
  for(int i =0; i < numOfSuits; i++){
   for(int j = 1; j <= numOfCardsPerSuit; j++){
    this.addCard(this.new PlayingCard(suitsInOrder[i],j));
   }
  }

  // add a red joker and a black joker
  this.addCard(this.new Joker("red"));
  this.addCard(this.new Joker("black"));
 }


 /*
  * TODO: Implements a copy constructor for assignment2.Deck using Card.getCopy().
  * This method runs in O(n), where n is the number of cards in d.
  */
 public Deck(Deck d) {
  /**** ADD CODE HERE ****/
  //create a deck by making a copy of the input deck
  //create a desk by making a deep copy of the input deck
  //use the method getCopy ftom the class Card
  //recreate everything  the same with d
  head = d.head.getCopy();
  addCard(head);

  Card nextToCopy = d.head.next;

  while (nextToCopy != d.head) {
   this.addCard(nextToCopy.getCopy());
   nextToCopy = nextToCopy.next;
  }
 }

 /*
  * For testing purposes we need a default constructor.
  */
 public Deck() {

 }

 /*
  * TODO: Adds the specified card at the bottom of the deck. This
  * method runs in $O(1)$.
  */
 public void addCard(Card c) {
  /**** ADD CODE HERE ****/
  //c is the new one
  if (numOfCards == 0) {
   head = c;
   head.prev = c;
   head.next = c;
  }
  else {
   Card tail = head.prev;

   tail.next = c;
   c.prev = tail;
   c.next = head;

   head.prev = c;
  }
  numOfCards++;
 }


 /*
  * TODO: Shuffles the deck using the algorithm described in the pdf.
  * This method runs in O(n) and uses O(n) space, where n is the total
  * number of cards in the deck.
  */
 public void shuffle() {
  /**** ADD CODE HERE ****/
  //create a new array of card
  Card[] newDeck = new Card[numOfCards];
  //copy all cards inside an array
  Card current = head;

  for (int i = 0; i < numOfCards; i++) {
   newDeck[i] = current;
   current = current.next;
  }

  //shuffle the array -> provided algorithm
  for (int i = this.numOfCards - 1; i >= 1; i--) {
   //generate a random integer
   int j = gen.nextInt(i+1);
   Card temp = newDeck[i];
   newDeck[i] = newDeck[j];
   newDeck[j] = temp;
  }

  clearAll();
  for (Card c : newDeck)
   addCard(c);

 }

 private void clearAll() {
  head = null;
  numOfCards = 0;
 }

 /*
  * TODO: Returns a reference to the joker with the specified color in
  * the deck. This method runs in O(n), where n is the total number of
  * cards in the deck.
  */
 public Joker locateJoker(String color) {
  /**** ADD CODE HERE ****/
  //returns a reference to the joker in the deck with the specific color
  //Joker has value of n, and n-1

  /**/
  if (head == null)
   return null;
  Card cardToCheck = head;
  while (true) {

   if (((cardToCheck instanceof Joker) && ((Joker) cardToCheck).getColor().equalsIgnoreCase(color))) {
    return (Joker) cardToCheck;
   }
   cardToCheck = cardToCheck.next;
   if (cardToCheck == head) {
    // nothing more to check
    break;
   }
  }
  return null;
 }

 /*
  * TODO: Moved the specified Card, p positions down the deck. You can
  * assume that the input Card does belong to the deck (hence the deck is
  * not empty). This method runs in O(p).
  */

 public void moveCard(Card c, int p) {
  /**** ADD CODE HERE ****/
  //movese the card c by p positions down the deck
  //assumes the inpuy card belongs to the deck

  if (numOfCards == 1)
   return;

  int q = p % numOfCards;
  if ( q == 0 ) {
   return;
  }
  if ( numOfCards == 2 ) {
   return;
  }

  if (head == c && q == numOfCards - 1) {
   return;
  }


  Card oldPrev = c.prev;
  Card oldNext = c.next;

  Card newPrev = c;

  for (int i = 0; i < q; i++) {
   newPrev = newPrev.next;
  }

  Card newNext = newPrev.next;

  if ( newNext != c) {
   //unlink
   link( oldPrev, oldNext );

   //new linking
   newPrev.next = c;
   c.prev = newPrev;
   c.next = newNext;
   newNext.prev = c;
  }
 }
 /*
  * TODO: Performs a triple cut on the deck using the two input cards. You
  * can assume that the input cards belong to the deck and the first one is
  * nearest to the top of the deck. This method runs in O(1)
  */
 public void tripleCut(Card firstCard, Card secondCard) {
  /**** ADD CODE HERE ****/
  //triple cut -> swap the cards above the first joker with the cards below the second joker
  //if there are no cards in any of the three sections, just treat it as empty
  //special situation: if firstcard is head

  if ( head != firstCard )
  {
   //  seg 1 is not empty
   Card h1 = head;
   Card t1 = firstCard.prev; //tail of array after triplecut

   if ( secondCard != head.prev)
   {
    // seg 2 not empty
    Card h2 = secondCard.next;
    Card t2 = head.prev;

    link( secondCard, h1 );
    link( t2, firstCard);
    link( t1, h2);
    head = h2;
   }
   else {
    // seg 2 empty
    link( secondCard, h1 );
    // seg 2 is empty, head still be head
    link( t1, firstCard);
    head = firstCard;
   }
  }
  else {
   // seg1 is empty, seg 2 not
   if ( secondCard != head.prev )
   {
    // seg 2 not empty
    Card h2 = secondCard.next;
    Card t2 = head.prev;

    // seg 2 is not empty
    link( t2, firstCard);
//     link( secondCard, h2);
    head = h2;

   }
   // do nothing when both are empty
  }

 }
 /*
  * TODO: Performs a count cut on the deck. Note that if the value of the
  * bottom card is equal to a multiple of the number of cards in the deck,
  * then the method should not do anything. This method runs in O(n).
  */
 public void countCut() {
  /**** ADD CODE HERE ****/
  //o(n), use a for loop here i think
  //look at the value of the bottom card
  //remove that number of cards from the top of the deck and insert them just above the last card in the deck
  if(numOfCards <= 2)
   return;

  Card bottom = head.prev;

  int countValue = bottom.getValue() % numOfCards;

  if(countValue == 0)
   return;

  if(countValue >= numOfCards-1)
   return;

  Card headOfMovedSeg = head;

  Card tailOfMoveSeg = getCardIndex(countValue-1);

  //after
  head = tailOfMoveSeg.next;

  //bottom <- head.prev
  Card cardBeforeOldBottom = bottom.prev;

  link(cardBeforeOldBottom, headOfMovedSeg);
  link(tailOfMoveSeg, bottom);
  link(bottom, head);

 }
 private Card getCardIndex(int p){
  Card c = head;
  for(int i =0; i<p; i++){
   c = c.next;
  }
  return c;
 }

 private void link(Card p, Card n){
  p.next = n;
  n.prev = p;
 }

 /*
  * TODO: Returns the card that can be found by looking at the value of the
  * card on the top of the deck, and counting down that many cards. If the
  * card found is a Joker, then the method returns null, otherwise it returns
  * the Card found. This method runs in O(n).
  */
 public Card lookUpCard() {
  /**** ADD CODE HERE ****/
  //returns a reference to the card that can be found by looking at the value of the card
  //on top of the decl, continue count dn that many cards
  //if it is a joker, return null
  //return the card found otherwise
  //O(n)
  int num = head.getValue();
  Card current = head;
  for (int i= 0; i < num; i++) {
   current = current.next;
  }
  if (current instanceof Joker) {
   return null;
  } else {
   return current;
  }

 }



 /*
  * TODO: Uses the Solitaire algorithm to generate one value for the keystream
  * using this deck. This method runs in O(n).
  */
 private Integer indexOfCard( Card c )
 {
  int p = 0;
  Card looking = head;
  while ( true )
  {
   if ( c == looking )
    return p;
   p++;
   looking = looking.next;
   if ( looking == head )
   {
    return null;
   }
  }
 }

 public int generateNextKeystreamValue() {
  /**** ADD CODE HERE ****/

  while (true) {
   moveAndMove();
   Card theCardToGiveValue = lookUpCard();
   if (theCardToGiveValue != null) {

    return theCardToGiveValue.getValue();
   }
  }
 }

 private void moveAndMove() {
  //step 1
  Card redJoker = locateJoker("red");
  moveCard(redJoker, 1);
  //step 2
  Card blackJoker = locateJoker("black");

  moveCard(blackJoker, 2);

  //step 3

  if (indexOfCard(blackJoker ) > indexOfCard(redJoker)) {
   tripleCut(redJoker, blackJoker);
  } else {
   tripleCut(blackJoker, redJoker);
  }

  //step 4
  countCut();

 }

 public abstract class Card {
  public Card next;
  public Card prev;

  public abstract Card getCopy();
  public abstract int getValue();

 }

 public class PlayingCard extends Card {
  public String suit;
  public int rank;

  public PlayingCard(String s, int r) {
   this.suit = s.toLowerCase();
   this.rank = r;
  }

  public String toString() {
   String info = "";
   if (this.rank == 1) {
    //info += "Ace";
    info += "A";
   } else if (this.rank > 10) {
    String[] cards = {"Jack", "Queen", "King"};
    //info += cards[this.rank - 11];
    info += cards[this.rank - 11].charAt(0);
   } else {
    info += this.rank;
   }
   //info += " of " + this.suit;
   info = (info + this.suit.charAt(0)).toUpperCase();
   return info;
  }

  public PlayingCard getCopy() {
   return new PlayingCard(this.suit, this.rank);
  }

  public int getValue() {
   int i;
   for (i = 0; i < suitsInOrder.length; i++) {
    if (this.suit.equals(suitsInOrder[i]))
     break;
   }

   return this.rank + 13*i;
  }

 }

 public class Joker extends Card{
  public String redOrBlack;

  public Joker(String c) {
   if (!c.equalsIgnoreCase("red") && !c.equalsIgnoreCase("black"))
    throw new IllegalArgumentException("Jokers can only be red or black");

   this.redOrBlack = c.toLowerCase();
  }

  public String toString() {
   //return this.redOrBlack + " Joker";
   return (this.redOrBlack.charAt(0) + "J").toUpperCase();
  }

  public Joker getCopy() {
   return new Joker(this.redOrBlack);
  }

  public int getValue() {
   return numOfCards - 1;
  }

  public String getColor() {
   return this.redOrBlack;
  }
 }

}


