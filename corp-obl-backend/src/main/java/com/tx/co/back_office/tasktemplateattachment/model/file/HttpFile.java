package com.tx.co.back_office.tasktemplateattachment.model.file;

import java.io.InputStream;
import java.util.Map;

public class HttpFile {

    private final String name;
    private final String submittedFileName;
    private final String fileType;
    private String filePath;
    private final long size;
    private final Map<String, String> parameters;
    private final InputStream stream;
    
	public HttpFile(String name, String submittedFileName, String fileType, long size, Map<String, String> parameters,
			InputStream stream) {
		super();
		this.name = name;
		this.submittedFileName = submittedFileName;
		this.fileType = fileType;
		this.size = size;
		this.parameters = parameters;
		this.stream = stream;
	}

	public String getName() {
		return name;
	}

	public String getSubmittedFileName() {
		return submittedFileName;
	}

	public String getFileType() {
		return fileType;
	}

	public long getSize() {
		return size;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public InputStream getStream() {
		return stream;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}