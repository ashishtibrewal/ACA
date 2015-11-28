package pipeline;

/**
 * A basic work unit in the pipeline. 
 *
 */

public interface IStage 
{
	/**
	 * executes the work to be performed.
	 * The input will be read from the context
	 * The output will be stored in the context	 
	 * @param context - context object which keeps shared state 
	 */
	public void execute(IPipelineContext context);
}
