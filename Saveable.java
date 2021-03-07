package Sketchy;

import cs015.fnl.SketchySupport.FileIO;

/*
 * my saveables interfaces only has the method save which saves a saveable object using the io passed in. This interface is implemented by CurvedLine, and both SketchyRectangle and SketchyEllipse
 */
public interface Saveable {

	public void save(FileIO io);

}
