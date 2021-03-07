package Sketchy;

/*
 * the command interface is implemented by each class that models a command of sketchy. It has the undo and redo methods because each command must be undoable and redoable but in their own ways
 */
public interface Command {
	public void redo();

	public void undo();
}
