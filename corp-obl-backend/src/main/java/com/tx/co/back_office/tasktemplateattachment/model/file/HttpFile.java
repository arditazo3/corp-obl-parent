package com.tx.co.back_office.tasktemplateattachment.model.file;

import static com.tx.co.common.constants.ApiConstants.*;
import static org.springframework.util.ObjectUtils.isEmpty;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.Tika;

import com.tx.co.security.exception.GeneralException;

public class HttpFile {

	private static final Logger logger = LogManager.getLogger(HttpFile.class);

	private final String name;
	private final String submittedFileName;
	private String fileType;
	private String filePath;
	private long size;
	private final Map<String, String> parameters;
	private final InputStream stream;

	public HttpFile(String name, String submittedFileName, Map<String, String> parameters, InputStream stream) {

		super();
		this.name = name;
		this.submittedFileName = submittedFileName;
		this.parameters = parameters;
		this.stream = stream;
		if(!isEmpty(stream)) {
			try {
				byte[] bytes = IOUtils.toByteArray(getStream());
				long fileSize = Long.valueOf(bytes.length);
				if(BigInteger.valueOf(fileSize).divide(FileUtils.ONE_MB_BI).compareTo(new BigInteger(FILE_MAX_SIZE)) > 0) {
					throw new GeneralException("Maximum file size is " + FILE_MAX_SIZE + " MB");
				}
				this.size = fileSize;
				
				
				Tika tika = new Tika();
			    String contentType = tika.detect(stream);
			    this.fileType = contentType;
			} catch (IOException e) {
				logger.error("", e);
			}
		}
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