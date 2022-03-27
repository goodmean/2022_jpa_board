package com.chm.exam.jpaBoard.article.controller;

import com.chm.exam.jpaBoard.article.dao.ArticleRepository;
import com.chm.exam.jpaBoard.article.domain.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/usr/article")
public class ArticleController {
	@Autowired
	private ArticleRepository articleRepository;

	@RequestMapping("test")
	@ResponseBody
	public String test(){
		return "test";
	}

	@RequestMapping("list")
	@ResponseBody
	public List<Article> showList() {
		return articleRepository.findAll();
	}
}
