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
   * Method to execute/run the pipeline.
   * @param context Shared object (context object) used throughout all stages in the pipeline
   */
  @Override                                         // Overriding since this method exists in the SequentialPipeline class and the IPipeline interface
  public void execute(IPipelineContext context)
  {
    /* execute the stages */
    for (IStage stage:p_stages)   //i.e. for every stage in the pipeline
    { 
      stage.execute(context);     // Execute each stage in the pipeline 
      // if (context.getErrors()!= null && !context.getErrors().isEmpty())
      // {
      //  break;
      // }
    }
    this.shift(context);          // Shift the contents along the pipeline
  }

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
    pContext.flush();     // Flush the pipeline registers (i.e. the current and next value registers that connect the different stages in the pipeline)      
  }

  /**
   * Method to shift the contents of each stage along the pipleine
   * @param context Shared object (context object) used throughout all stages in the pipeline
   */
  public void shift(IPipelineContext context)
  {
    ProcessorPipelineContext pContext = (ProcessorPipelineContext) context;             // Explicitly cast context to ProcessorPipelineContext type.
    // Should actually do all the shifting here by obtaining the values from the pipeline context (i.e. pContext) using its getter methods but for simplicity it's being done in the ProcessorPipelineContext class because it removes the hassle of obtaining each an every single value that needs to be shifted.
    pContext.shiftPipeline();       // Call the shiftPipeline() method on the pContext object
    //System.out.println("SHIFTING CONTENTS IN THE PIPELINE !!!!");
  }
}
