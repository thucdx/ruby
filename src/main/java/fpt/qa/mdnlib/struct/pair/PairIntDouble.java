/**
 * 
 */
package fpt.qa.mdnlib.struct.pair;

/**
 * @author pxhieu
 *
 */
public class PairIntDouble implements Comparable<Object> {
    public Integer first;
    public Double second;
    
    public PairIntDouble(int f, double s) {
        this.first = f;
        this.second = s;
    }
    
    @Override
    public int compareTo(Object o) {
        double val1 = second.doubleValue();
        double val2 = ((PairIntDouble)o).second.doubleValue();
        
        if (val1 > val2) {
            return 1;
        } else if (val1 == val2) {
            return 0;
        } else {
            return -1;
        }
    }
    
    public void print() {
        System.out.println(this.first + "\t" + this.second);
    }
}
