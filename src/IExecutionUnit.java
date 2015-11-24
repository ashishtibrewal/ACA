import pipeline.*;

/**
 * A basic execution unit contained in the instruction execution (IE) stage 
 *
 */
public interface IExecutionUnit
{ 
  /**
   * Executes the instruction.
   * The input will be read from the context
   * The output will be stored in the context  
   * @param currentInstruction instruction to be executed by the execution unit
   * @param context            context object which keeps shared state 
   */
  public void execute(Instruction currentInstruction, IPipelineContext context);

  /**
   * Method to flush the contents of the current stage of the pipeline. Only used when a branch instruction is executed and 
   * when the fetched/decoded instructions were from the incorrect locations due to an incorrect branch.
   */
  //public void flush();
}
