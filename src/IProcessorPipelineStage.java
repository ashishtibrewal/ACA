import pipeline.*;

/**
 * A basic work unit in the processor pipeline. 
 *
 */

public interface IProcessorPipelineStage extends IStage
{
  /**
   * Method to flush the contents of a stage (contained in the pipeline). Only used when a branch instruction is executed, 
   * i.e. when the fetched/decoded instructions were from the incorrect locations due to the incorrect PC value being used.
   */
  public void flush(IPipelineContext context);
}
