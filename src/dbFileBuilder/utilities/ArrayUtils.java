package dbFileBuilder.utilities;

/**
 * Operations on arrays.
 */
public final class ArrayUtils {

    /**
     * Removes the element at the specified position from the specified array.
     * All subsequent elements are shifted to the left (subtracts one from their
     * indices).
     *
     * This method returns a new array with the same elements of the input array
     * except the element on the specified position. The component type of the
     * returned array is always the same as that of the input array.
     *
     * @param array the array to remove the element from, may not be
     * {@code null}.
     * @param indexOfElement the position of the element to be removed.
     * @return A new array containing the existing elements except the element
     * at the specified position.
     */
    public static String[] removeEltement(String[] array, int indexOfElement) {
        //TODO: generic
        String[] newArr = new String[array.length - 1];
        System.arraycopy(array, 0, newArr, 0, indexOfElement);
        System.arraycopy(array, indexOfElement + 1, newArr, indexOfElement, array.length - indexOfElement - 1);
        return newArr;
    }

    /**
     * Returns a string containing the string representation of each of
     * lines.<br>
     * The connection is done by using the <tt>glue</tt> between each line.<br>
     * After the last line the <tt>glue</tt> is not append.<br>
     * If the length of <tt>lines</tt> is one - it returned as {@link String}
     * without the <tt>glue</tt>.
     *
     * @return String containing the string representation of each of lines from
     * {@code  lines}
     * @param lines to connect.
     * @param glue to connect the lines with.
     */
    public static String join(String[] lines, String glue) {
        if (lines.length == 1) {
            return lines[0];
        }
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < lines.length - 1; i++) {
            out.append(lines[i]).append(glue);
        }
        out.append(lines[lines.length - 1]);
        return out.toString();
    }
}
