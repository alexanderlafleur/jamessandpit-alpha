package com.james.ui.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.james.fileItems.Disk;
import com.james.fileItems.Wallet;
import com.james.helper.DiskHelper;
import com.james.helper.WalletHelper;
import com.james.ui.DiskManager;
import com.james.ui.dto.DiskDTO;

public class DiskController extends BaseController implements Controller {
	protected final Log logger = LogFactory.getLog(getClass());

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		logger.info("Entered DiskHandler.handleRequest()");
		Map model = new HashMap();

		String mode = request.getParameter("mode");

		// String strWalletId = request.getParameter("walletId");
		// assert strWalletId != null;

		// int walletId = Integer.parseInt(strWalletId);

		ModelAndView modelAndView;

		// model.put("walletId", strWalletId);

		if ("edit".equals(mode)) {
			String diskId = request.getParameter("diskId");

			DiskDTO disk = loadDisk(diskId);

			model.put("disk", disk);
			modelAndView = new ModelAndView("editDisk", model);

		} else if ("view".equals(mode)) {
			String diskId = request.getParameter("diskId");

			DiskDTO disk = loadDisk(diskId);
			model.put("disk", disk);

			modelAndView = new ModelAndView("disk", model);

		} else if ("new".equals(mode)) {

			String strWalletId = request.getParameter("walletId");

			model.put("walletId", strWalletId);

			modelAndView = new ModelAndView("newDisk", model);

		} else if ("delete".equals(mode)) {
			String diskId = request.getParameter("diskId");

			DiskDTO disk = loadDisk(diskId);

			delete(disk);

			model.put("disk", disk);

			modelAndView = new ModelAndView("manageDisks", model);

		} else if ("save".equals(mode)) {
			WalletHelper walletHelper = (WalletHelper) CONTEXT
					.getBean("walletHelper");
			DiskHelper diskHelper = (DiskHelper) CONTEXT.getBean("diskHelper");

			String description = request.getParameter("diskDescription");

			String strWalletId = request.getParameter("walletId");

			int walletId = Integer.parseInt(strWalletId);
			Wallet wallet = walletHelper.load(walletId);
			model.put("wallet", wallet);

			// Save
			DiskDTO disk = new DiskDTO();
			disk.setLabel(description);

			diskHelper.create(disk);

			// model.put("disks", wallet.getDisks());

			modelAndView = new ModelAndView("manageDisks", model);

		} else if ("load".equals(mode)) {
			DiskHelper diskHelper = (DiskHelper) CONTEXT.getBean("diskHelper");

			String strDiskId = request.getParameter("diskId");

			int diskId = Integer.parseInt(strDiskId);
			Disk disk = diskHelper.load(diskId);

			DiskManager mgr = new DiskManager();

			mgr.read("e:", disk);

			modelAndView = new ModelAndView("manageDisks", model);

		} else {
			modelAndView = null;

		}

		return modelAndView;
	}

	private DiskDTO loadDisk(String diskId) {
		try {
			DiskHelper diskHelper = (DiskHelper) CONTEXT.getBean("diskHelper");

			return diskHelper.loadDTO(Integer.parseInt(diskId));

		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param wallet
	 */
	private void delete(DiskDTO disk) {
		DiskHelper diskHelper = (DiskHelper) CONTEXT.getBean("diskHelper");

		diskHelper.delete(disk.getId());

	}

}