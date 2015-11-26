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

  /**
   * Method to flush the contents of the current stage of the pipeline. Only used when a branch instruction is executed and 
   * when the fetched/decoded instructions were from the incorrect locations due to an incorrect branch.
   */
  public void flush(IPipelineContext context);
}
