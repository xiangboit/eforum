package org.eforum.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.eforum.constant.Constants;
import org.eforum.entity.Article;
import org.eforum.exception.ServiceException;
import org.eforum.service.ArticleService;
import org.eforum.service.FileService;
import org.eforum.util.EntityUtil;
import org.eforum.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ArticleServiceImpl extends BaseServiceImpl implements ArticleService {
	@Autowired
	private FileService fileService;

	@SuppressWarnings("unchecked")
	@Override
	public List<Article> listArticle(int pageNumber, int pageSize) {
		return dao.pagingQuery(Article.class, pageNumber, pageSize);
	}

	@Override
	public Article findArticleById(Long id) {
		Article article = dao.get(Article.class, id);
		if (null == article) {
			throw new ServiceException("未找到该帖子，或者该帖子已被删除！");
		}
		return article;
	}

	@Override
	public List<Article> findSuggestionArticle(int pageSize) {
		return null;
	}

	@Override
	public Article saveOrUpdate(Article article) {
		Article willSaveArticle = null;
		if (StringUtils.isNullOrEmpty(article.getTitle()) || StringUtils.isNullOrEmpty(article.getContent())
				|| "<p><br></p>".equals(article.getContent())) {
			throw new ServiceException("标题或内容不能是空的！");
		}
		if (!article.isNew()) {
			willSaveArticle = dao.get(Article.class, article.getId());
			if (null == willSaveArticle) {
				throw new ServiceException("未找到id为【" + article.getId() + "】的帖子");
			}
			EntityUtil.copyPropertiesOfEntity(article, willSaveArticle);
		} else {
			willSaveArticle = article;
		}
		dao.save(willSaveArticle);
		return willSaveArticle;
	}

	@Override
	public List<String> saveImagesOfArticle(MultipartFile[] images) {
		List<String> imageNames = fileService.saveImages(images);
		List<String> imageRequestPaths = new ArrayList<String>();
		for (String imageName : imageNames) {
			imageRequestPaths.add(Constants.IMAGE_REQUEST_PATH + imageName);
		}
		return imageRequestPaths;
	}

	@Override
	public void downloadImageOfArticle(String imageName, HttpServletResponse response) {
		File file = fileService.getImageFileByName(imageName);
		try {
			InputStream is = new FileInputStream(file);
			OutputStream os = new BufferedOutputStream(response.getOutputStream());
			byte[] data = new byte[1024];
			int length;
			while ((length = is.read(data)) != -1) {
				os.write(data, 0, length);
			}
			is.close();
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
}