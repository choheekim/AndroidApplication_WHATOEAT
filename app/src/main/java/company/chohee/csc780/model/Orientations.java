package company.chohee.csc780.model;

/**
 * Created by Chohee on 4/17/17.
 */


public class Orientations {
    public enum Orientation {
        Ordered, Disordered;

        public static Orientation fromIndex(int index) {
            Orientation[] values = Orientation.values();
            if(index < 0 || index >= values.length) {
                throw new IndexOutOfBoundsException();
            }
            return values[index];
        }
    }
}
