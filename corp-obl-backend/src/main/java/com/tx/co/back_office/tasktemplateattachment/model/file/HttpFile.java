package com.tx.co.back_office.tasktemplateattachment.model.file;

import static com.tx.co.common.constants.ApiConstants.*;
import static org.springframework.util.ObjectUtils.isEmpty;

import java.io.InputStream;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpFile {

	private static final Logger logger = LogManager.getLogger(HttpFile.class);

	private String name;
	private String submittedFileName;
	private String fileType;
	private String filePath;
	private long size;
	private Map<String, String> parameters;
	private InputStream stream;

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