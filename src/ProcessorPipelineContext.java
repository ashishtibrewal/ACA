/**
 * Class that stores intermediate stage values that is used to communicate between different stages of the pipeline.
 */
import pipeline.*;

public class ProcessorPipelineContext implements IPipelineContext
{
  public Register cpuRegisters;        /** Reference to architectural registers */
  public Memory cpuMemory;             /** Reference to main memory */

  public ProcessorPipelineContext(Register cpuRegisters, Memory cpuMemory)
  {
    this.cpuRegisters = cpuRegisters;
    this.cpuMemory = cpuMemory;
  }
  // TODO Add functions and fields to handle outputs from other stages of the pipeline. (I.e. instruction fetch output, instruction decode output etc)
  // TODO WOULD NEED TO HAVE TWO COPIES OF CERTAIN FIELDS, ONE TO STORE THE VALUES FOR THE CURRENT CYCLE AND THE NEXT TO STORE VALUES FOR THE NEXT CYCLE
}