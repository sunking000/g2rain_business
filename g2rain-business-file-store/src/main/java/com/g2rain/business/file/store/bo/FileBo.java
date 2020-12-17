package com.g2rain.business.file.store.bo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import com.g2rain.business.common.utils.FileTypeUtils;
import com.g2rain.business.file.store.enums.FileObjectStatusEnum;
import com.g2rain.business.file.store.enums.FileStoreTypeEnum;
import com.g2rain.business.file.store.po.FileObjectPo;
import com.g2rain.business.file.store.vo.FileObjectVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileBo implements ApplicationListener<ContextRefreshedEvent> {

	@Value("${file.store.dir}")
	private String fileStoreDir;
	@Autowired
	private FileObjectBo fileObjectBo;
	private String fileDir;


	public FileBo() {
		super();
	}

	@PostConstruct
	public void init() {

	}

	public List<FileObjectVo> saveFiles(HttpServletRequest request, MultipartFile[] files, String organId) {

		File file = new File(fileDir);
		if (!file.exists()) {
			file.mkdirs();
		}

		if (files != null && files.length > 0) {
			List<FileObjectPo> fileObjectPos = new ArrayList<>();
			for (int i=0;i<files.length;i++){
				String originalFilename = files[i].getOriginalFilename();
				String prefixPath = getFilePrefixPath();
				String storeFilePath = prefixPath + "_" + System.currentTimeMillis();
				try {
					File storeFile = new File(file, storeFilePath);
					files[i].transferTo(storeFile);
					String fileType = FileTypeUtils.getFileType(storeFile);
					FileObjectPo fileObjectPo = fileObjectBo.createFileObjectPo(originalFilename, fileType,
							FileStoreTypeEnum.NATIVE.name(),
							storeFilePath);
					fileObjectPo.setStatus(FileObjectStatusEnum.SUCCESS.name());
					fileObjectPos.add(fileObjectPo);
				} catch (IllegalStateException | IOException e) {
					log.error(e.getMessage(), e);
				}
			}

			int addRowCount = fileObjectBo.addBatch(fileObjectPos);
			log.debug("添加文件记录数量, addRowCount:{}", addRowCount);
			
			List<FileObjectVo> fileObjectVos = new ArrayList<>();
			for (FileObjectPo item : fileObjectPos) {
				FileObjectVo fileObjectVo = new FileObjectVo(item);
				fileObjectVo.setServerContextPath(fileObjectBo.getServerContextPath());
				fileObjectVos.add(fileObjectVo);
			}

			return fileObjectVos;
		}

		return null;
	}

	public String getFileDir() {
		return fileDir;
	}

	private String getFilePrefixPath() {
		String uuid = UUID.randomUUID().toString().toUpperCase();
		return uuid.replaceAll("-", "");
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (StringUtils.isNotBlank(fileStoreDir) && !".".equals(fileStoreDir)) {
			fileDir = fileStoreDir;
		} else {
			WebApplicationContext webApplicationContext = (WebApplicationContext) event.getApplicationContext();
			ServletContext servletContext = webApplicationContext.getServletContext();
			fileDir = servletContext.getRealPath("/");
		}
	}
}
