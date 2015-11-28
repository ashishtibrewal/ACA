import pipeline.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The sequential pipeline executes the stage sequence imitating a try catch finally block.
 * try {
 *      execute stages
 * }
 * catch (any error){
 *          execute error stages
 * }
 * finally {
 *      execute finally stages
 * }
 * 
 * // TODO need to think of a way to handle exceptions, either in the parent calller (i.e. higher up in the call chain/stack) or handle it
 * locally in the stage where the error/exception occured
 */
public class SequentialProcessorPipeline extends SequentialPipeline
{
  /**
   * Method to flush the pipeline
   * @param context Shared object (context object) used throughout all stages in the pipeline
   */
  public void flush(IPipelineContext context)
  {
    ProcessorPipelineContext pContext = (ProcessorPipelineContext) context;             // Explicitly cast context to ProcessorPipelineContext type. Only need to do this to obtain the branch taken boolean value
    if (pContext.getBranchTaken() == true)    // If a branch is taken, only then flush the pipeline (i.e. only run the flush method of each stage in the pipeline if a branch needs to be taken since whatever is already present currently in these stages is incorrect/irrelevant)
    {
      for (IStage stage:p_stages)   //i.e. for every stage in the pipeline
      { 
        ((IProcessorPipelineStage) stage).flush(context);         // Flush each stage in the pipeline. Note explicitly casting to IProcessorPipelineStage type since all pipeline stages stored in the SequentialPipeline class are are of the IStage type and this doesn't contain a flush() method.
      }
    }
  }
}
