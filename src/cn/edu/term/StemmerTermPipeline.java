package cn.edu.term;

public abstract class StemmerTermPipeline implements Stemmer, TermPipeline {

	protected TermPipeline next;
	
	protected StemmerTermPipeline()
	{
		this(null);
	}
	
	/** Make a new StemmerTermPipeline object, with _next being the next object 
	 * in this pipeline.
	 * @param _next Next pipeline object
	 */
	StemmerTermPipeline(TermPipeline _next) {
		this.next = _next;
	}
	
	/**
	 * Stems the given term and passes onto the next object in the term pipeline.
	 * @param t String the term to stem.
	 */
	public void processTerm(String t)
	{
		if (t == null)
			return;
		next.processTerm(stem(t));
	}
	
    /**
	 * Implements the  default operation for all TermPipeline subclasses;
	 * By default do nothing.
	 * This method should be overrided by any TermPipeline that want to implements doc/query
	 * oriented lifecycle.
	 * @return return how the reset has gone
	 */
	public boolean reset() {
		return next!=null ? next.reset() : true;
	}
}
