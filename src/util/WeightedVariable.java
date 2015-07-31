/**
 * 
 */
package util;

/**
 * A numeric variable with an associated, constant weight.
 * 
 * @param V The type of the value to store.
 * @param W The type of the associated weight.
 * @author James Giller 109466711
 */
public class WeightedVariable<V extends Number, W extends Number> {
	/**
	 * The value of the variable.
	 */
    protected V value;
    /**
     * The weight.
     */
    protected final W weight;
    
    public WeightedVariable( V initialValue, W weight ) {
    	this.value = initialValue;
    	this.weight = weight;
    }

	/**
	 * @return the value
	 */
	public V getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(V value) {
		this.value = value;
	}

	/**
	 * @return the weight
	 */
	public W getWeight() {
		return weight;
	}
	
	@Override
	public boolean equals( Object o ) {
		if ( o instanceof WeightedVariable<?, ?> ) {
			WeightedVariable<?, ?> wv = (WeightedVariable<?, ?>) o;
			return weight.equals( wv.getWeight() ) && value.equals( wv.getValue() );
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return value.hashCode() + 31 * weight.hashCode();
	}
	
	@Override
	public String toString() {
		return "(Value = " + value + ", Weight = " + weight + ")";
	}
}
