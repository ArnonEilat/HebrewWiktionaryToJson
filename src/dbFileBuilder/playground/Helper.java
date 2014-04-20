package dbFileBuilder.playground;

import java.util.regex.Pattern;

/**
 * Handy playground...
 */
public class Helper {

    public static void printAllHebrewCharter() {
        for (int i = 0X0591; i <= 0x05f4; i++) {
            System.out.printf("%2c %3c %4X\n", i, 0x2502, i);
        }
    }

    public static void stringInDetail(String s) {
        System.out.println(s + "  length: " + s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            System.out.printf("i : %2d \u202B%c\u202B \u202B%4X\u202B \n", i, s.charAt(i), (int) c);// Character.getNumericValue(c)
        }
    }

    /**
     * Convert <tt>String</tt> to its unicode <tt>char</tt>s.
     *
     * @param str <tt>String</tt> to get its unicode chars.
     * @return Unicode representation of <tt>str</tt>.
     */
    public static String unicodeRepresentation(String str) {
        StringBuilder b = new StringBuilder();

        for (char c : str.toCharArray()) {
            String s = Integer.toHexString(c).toUpperCase();
            while (s.length() <= 3) {
                s = '0' + s;
            }
            b.append("\\u").append(s);
        }
        b.append("\"");
        return '\"' + b.toString();
    }
    public static final Pattern jjj
            = Pattern.compile("[^=]={2}\\D([^=])*={2}[^=]", Pattern.UNICODE_CASE);

    public static void main(String args[]) {
        /*
         Pattern p = jjj;
         Matcher matcher = p.matcher("==תרגום==");


         boolean found = false;
         while (matcher.find()) {
         System.out.printf(
         "I found the text"
         + " \"%s\" starting at "
         + "index %d and ending at index %d.%n",
         matcher.group(),
         matcher.start(),
         matcher.end());
         found = true;
         }
         if (found == false) {
         System.out.printf("No match found.%n");
         }*/
        String s = "== כרבלת  ==";
        System.out.println(s.matches("^={2}[^=]*={2}"));
    }
}
