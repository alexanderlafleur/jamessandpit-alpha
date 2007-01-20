package com.james.ui.swing.main;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.james.ui.dto.DTO;
import com.james.ui.dto.DiskFileDTO;
import com.james.ui.dto.MP3DTO;
import com.james.ui.swing.node.BaseInfo;
import com.james.ui.swing.node.BaseNodeHelper;
import com.james.ui.swing.node.FileInfo;
import com.james.ui.swing.node.MP3Info;

public class InfoHelper {
	protected final Log logger = LogFactory.getLog(getClass());

	public BaseInfo convert(DTO dto, BaseNodeHelper nodeHelper) {
		BaseInfo node;

		if (dto instanceof DiskFileDTO) {
			DiskFileDTO fileDTO = (DiskFileDTO) dto;
			logger.debug("Converting file: " + fileDTO.getName());

			node = new FileInfo(fileDTO.getId(), fileDTO.getName(), fileDTO,
					nodeHelper);

		} else if (dto instanceof MP3DTO) {
			MP3DTO mp3DTO = (MP3DTO) dto;
			logger.debug("Converting mp3: " + mp3DTO.getName());

			node = new MP3Info(mp3DTO.getId(), mp3DTO.getName(), mp3DTO,
					nodeHelper);

		} else {
			throw new ConversionException("Unknown dto type: "
					+ dto.getClass().getName());
		}

		return node;
	}

	public BaseInfoWrapper wrap(DTO dto, BaseNodeHelper nodeHelper) {
		BaseInfoWrapper node;

		if (dto instanceof DiskFileDTO) {
			DiskFileDTO fileDTO = (DiskFileDTO) dto;

			node = new FileInfoWrapper(fileDTO.getId(), fileDTO.getName(),
					fileDTO, nodeHelper);

		} else if (dto instanceof MP3DTO) {
			MP3DTO mp3DTO = (MP3DTO) dto;

			node = new MP3InfoWrapper(mp3DTO.getId(), mp3DTO.getName(), mp3DTO,
					nodeHelper);

		} else {
			throw new ConversionException("Unknown dto type: "
					+ dto.getClass().getName());
		}

		return node;
	}
}
