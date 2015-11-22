/**
 * @file IPipelineStage.java
 * @author Ashish Tibrewal
 * @date 29.10.2015
 * @details This file contains the general pipeline stage unit interface declaration. It provides a method called step() to step through
 * the pipeline.
 */

/*
 ********************************************************************************************
 *****            THIS CLASS IS REDUNDANT. DO NOT USE THIS CLASS/FILE!!!                *****
 ********************************************************************************************
*/

package pipeline;
/**
 * IPipelineStage Interface declaration. This interface must be implemented by all the classes that form a stage in the processor pipeline.
 */
public interface IPipelineStage
{
  /**
   * Method that executes a time step for a processor pipeline unit. (i.e Execute and move ahead by one time step).
   */
  public void step();

  /**
   * Method to shift the contents of the processor pipeline forward.
   */
  //void shift();

}