/**
 * Class that stores intermediate stage values that is used to communicate between different stages of the pipeline.
 */
import pipeline.*;

import java.util.*;
import java.util.concurrent.*;

public class ProcessorPipelineContext implements IPipelineContext
{
  // All classs fields/members should be declared public since this makes it easier to access them from outside this class. Note that this class is accessed 
  // by all pipeline stages to read and update values. Having made it public also removes the hassle of creating a method for each functionality the objects
  // declared offers.
  private Register cpuRegisters;                 /** Reference to architectural registers */
  private Memory cpuMemory;                      /** Reference to main memory */
  private Instruction currentInstruction;        /** Reference to the current instruction */
  private Queue<Instruction> instructionQueue;   /** Reference to the current instruction queue */
  // public int opCode;
  // public int sourceReg1;
  // public int sourceReg2;
  // public int destinationReg;
  // public int signedImmediate;

  public ProcessorPipelineContext(Register cpuRegisters, Memory cpuMemory)
  {
    this.cpuRegisters = cpuRegisters;
    this.cpuMemory = cpuMemory;
    instructionQueue = new ArrayBlockingQueue<Instruction>(GlobalConstants.INSTRUCTION_QUEUE_CAPACITY);
  }
  // TODO Add functions and fields to handle outputs from other stages of the pipeline. (I.e. instruction fetch output, instruction decode output etc)
  // TODO WOULD NEED TO HAVE TWO COPIES OF CERTAIN FIELDS, ONE TO STORE THE VALUES FOR THE CURRENT CYCLE AND THE NEXT TO STORE VALUES FOR THE NEXT CYCLE
  
  public Register getCpuRegisters()
  {
    return cpuRegisters;
  }

  public Memory getCpuMemory()
  {
    return cpuMemory;
  }

  public Instruction getCurrentInstruction()
  {
    return currentInstruction;
  }

  public void setCurrentInstruction(Instruction _currentInstruction)
  {
    currentInstruction = _currentInstruction;
  }

  public Queue<Instruction> getInstructionQueue()
  {
    return instructionQueue;
  }
}