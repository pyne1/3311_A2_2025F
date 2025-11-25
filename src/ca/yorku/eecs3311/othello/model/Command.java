package ca.yorku.eecs3311.othello.model;

public interface Command {
    
	
	boolean execute();
    
	
	void undo();
}
