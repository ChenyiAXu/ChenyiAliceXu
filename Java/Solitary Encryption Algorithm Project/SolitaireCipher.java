/**
 * Your name here:Chenyi Xu
 * Your McGill ID here:260948311
 **/

public class SolitaireCipher {
 public Deck key;

 public SolitaireCipher (Deck key) {
  this.key = new Deck(key); // deep copy of the deck

 }

 /*
  * TODO: Generates a keystream of the given size
  */
 public int[] getKeystream(int size) {
  /**** ADD CODE HERE ****/

  int[] keystreamArray = new int[size];
  for(int i = 0; i< size; i++){
   keystreamArray[i] = key.generateNextKeystreamValue();

  }

  return keystreamArray;

 }

 /*
  * TODO: Encodes the input message using the algorithm described in the pdf.
  */
 public String encode(String msg) {
  /**** ADD CODE HERE ****/
  String msg1 = preprocess(msg);

  int[] keystream = getKeystream(msg1.length());

  StringBuilder builder = new StringBuilder();
  for(int i =0; i< msg1.length(); i++){
   char mchar = msg1.charAt(i);
   final int shiftPos = keystream[i];
   char encoded = mchar;

   for(int j =0; j< shiftPos; j++){
    encoded++;
    if(encoded > 'Z')
     encoded = 'A';
   }
   builder.append((encoded));
  }

  return builder.toString();
 }
 //remove non-letters, convert lower case to upper case
 private String preprocess (String msg){
  StringBuilder builder = new StringBuilder();
  for(int i = 0; i< msg.length(); i++){
   char c = msg.charAt(i);
   if(c >= 'a' && c <= 'z'){
    c = Character.toUpperCase(c);
    builder.append(c);
   }
   else if (c >= 'A' && c <= 'Z'){
    builder.append(c);
   }
  }

  return builder.toString();
 }

 /*
  * TODO: Decodes the input message using the algorithm described in the pdf.
  */
 public String decode(String msg) {
  /**** ADD CODE HERE ****/

  int[] keystreamR = getKeystream(msg.length());

  StringBuilder builder = new StringBuilder();
  for(int i=0; i<msg.length(); i++){
   char mchar = msg.charAt(i);
   final int shiftPos = keystreamR[i];
   char encoded = mchar;
   for (int j=0; j< shiftPos; j++) {
    encoded--;
    if ( encoded < 'A' )
     encoded = 'Z';
   }
   builder.append (encoded);
  }
  return builder.toString();
 }
}
