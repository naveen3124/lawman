package org.lawman.command;

public class InsertFileCommand implements Command {

	private String fileName;

	public InsertFileCommand() {
	}

	public InsertFileCommand(final String fileName) {
		this.fileName = fileName;
	}
      
	public String getFileName() {
		return fileName;
	}

	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String execute() {
		System.out.println(fileName);
		return null;
	}
}
