package com.kh.toy.common.file;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.kh.toy.common.code.Config;
import com.kh.toy.common.code.ErrorCode;
import com.kh.toy.common.exception.HandlableException;
import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;

public class FileUtil {

	private static final int MAX_SIZE = 1024 * 1024 * 10;
	// multipart 요청 도착
	// -> multipartParser를 사용해, 파일업로드 + 요청파라미터 저장 + FileDTO 생성

	public MultiPartParams fileUpload(HttpServletRequest request) {
		Map<String, List> res = new HashMap<String, List>();
		List<FileDTO> fileDTOs = new ArrayList<FileDTO>();
		try {
			MultipartParser parser = new MultipartParser(request, MAX_SIZE);
			parser.setEncoding("UTF-8");
			Part part = null;
			
			while((part = parser.readNextPart()) != null) {
				if(part.isFile()) {
					FilePart filePart = (FilePart) part;
					FileDTO fileDTO = createFileDTO(filePart);
					filePart.writeTo(new File(getSavePath() + fileDTO.getRenameFileName())); //파일저장
					fileDTOs.add(fileDTO);
				}else {
					ParamPart paramPart = (ParamPart) part;
					setParameterMap(paramPart, res);
				}
			}
			
			res.put("com.kh.toy.files",fileDTOs);
			
		} catch (IOException e) {
			throw new HandlableException(ErrorCode.FAILED_FILE_UPLOAD_ERROR,e);
		}
		
		return new MultiPartParams(res);
	}

	private String getSavePath() {
		// 2. 저장경로를 웹어플리케이션 외부로 지정
		String subPath = getSubPath();
		String savePath = Config.UPLOAD_PATH.DESC + subPath;

		File dir = new File(savePath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return savePath;
	}

	private String getSubPath() {
//		저장경로를 외부경로 + /연/월/일 형태로 작성
		Calendar today = Calendar.getInstance();
		int year = today.get(Calendar.YEAR);
		int month = today.get(Calendar.MONTH) + 1;
		int date = today.get(Calendar.DATE);
		return year + "\\" + month + "\\" + date + "\\";
	}

	private FileDTO createFileDTO(FilePart filePart) {

		// 1. 유니크한 파일명 생성
		FileDTO fileDTO = new FileDTO();
		String originFileName = filePart.getFileName();
		String renameFileName = UUID.randomUUID().toString();
		String savePath = getSubPath();

		// 3. FileDTO 생성
		fileDTO.setOriginFileName(originFileName);
		fileDTO.setRenameFileName(renameFileName);
		fileDTO.setSavePath(savePath);

		return fileDTO;
	}

	private void setParameterMap(ParamPart paramPart, Map<String, List> res) throws UnsupportedEncodingException {
		if (res.containsKey(paramPart.getName())) {
			res.get(paramPart.getName()).add(paramPart.getStringValue());
			// 2. 해당 파라미터명으로 처음 파라미터값이 저장되는 경우
		} else {
			List<String> param = new ArrayList<String>();
			param.add(paramPart.getStringValue());
			res.put(paramPart.getName(), param);
		}
	}
}
