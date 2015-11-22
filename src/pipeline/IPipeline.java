package pipeline;

/**
 * Pipeline holds a number of stages which will be executed.
 *
 */

public interface IPipeline extends IStage
{
	/**
	 * appends a stage to the pipeline
	 * @param stage
	 */
	 public void addStage (IStage stage);
	 
	 // /**
	 //  * adds a stage to the error handling sequence of stages
	 //  * @param stage
	 //  */
	 // public void addErrorStage (IStage stage);
	 
	 // /**
	 //  * adds a stage to the "final" sequence of stages
	 //  * The final sequence of stages will be executed even if there is any error
	 //  * @param stage
	 //  */
	 // public void addFinalStage (IStage stage);	 
}
