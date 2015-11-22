package pipeline;

import java.util.List;

/**
 * 
 * Context allows different stages to collaborate by sharing data 
 * 
 * Note: The error handling is defined in the pipeline context since 
 * error handling is absolutely  necessary and has to be done in each stage
 * 
 *
 */

public interface IPipelineContext
{
	//public List<Error> getErrors ();
}
