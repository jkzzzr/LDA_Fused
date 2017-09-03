
package cn.edu.term;
/**
 * Models the concept of a component in a pipeline of term processors. 
 * Classes that implement this interface could be stemming algorithms, 
 * stopwords removers, or acronym expanders just to mention few examples.
  */
public interface TermPipeline
{
	/**
	 * Processes a term using the current term pipeline component and
	 * passes the output to the next pipeline component, if the 
	 * term has not been discarded.
	 * @param t String the term to process.
	 */
	void processTerm(String t);
	
	/**
	 * This method implements the specific rest option needed to implements
	 * query or doc oriented policy.
	 * @return results of the operation
	 */
	boolean reset();
}
