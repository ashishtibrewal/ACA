package pipeline;

import java.util.ArrayList;
import java.util.List;

/**
 * The sequential pipeline executes the stage sequence imitating a try catch finally block.
 * try {
 * 			execute stages
 * }
 * catch (any error){
 *          execute error stages
 * }
 * finally {
 * 			execute finally stages
 * }
 * 
 * // TODO need to think of a way to handle exceptions, either in the parent calller (i.e. higher up in the call chain/stack) or handle it
 * locally in the stage where the error/exception occured
 */
public class SequentialPipeline implements IPipeline
{

	protected List<IStage> p_stages = new ArrayList<IStage> ();
	// protected List<IStage> p_errorStages= new ArrayList<IStage> ();
	// protected List<IStage> p_finalStages= new ArrayList<IStage> ();	
	
	public void addStage(IStage stage)
	{
		p_stages.add(stage);
	}

	// public void addErrorStage(IStage stage)
	// {
	// 	p_errorStages.add(stage);
	// }
	
	// public void addFinalStage(IStage stage)
	// {
	// 	p_finalStages.add(stage);	
	// }
	
	/**
	 * Method to execute/run the pipeline.
	 * @param context Shared object (context object) used throughout all stages in the pipeline
	 */
	public void execute(IPipelineContext context)
	{
		/* execute the stages */
		for (IStage stage:p_stages)		//i.e. for every stage in the pipeline
		{	
			stage.execute(context);			// Execute each stage in the pipeline	
			// if (context.getErrors()!= null && !context.getErrors().isEmpty())
			// {
			// 	break;
			// }
		}
		// /* if any error occurred, execute the error stages*/
		// if (context.getErrors()!= null && !context.getErrors().isEmpty())
		// {
		// 	for (IStage errorStage: p_errorStages)
		// 	{
		// 		errorStage.execute(context);
		// 	}			
		// }
		// //execute the final stages
		// for (IStage finalStage: p_finalStages)
		// {
		// 	finalStage.execute(context);
		// }
	}
}
