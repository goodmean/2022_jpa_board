package com.chm.exam.jpaBoard.article.controller;

import com.chm.exam.jpaBoard.article.dao.ArticleRepository;
import com.chm.exam.jpaBoard.article.domain.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/usr/article")
public class ArticleController {
	@Autowired
	private ArticleRepository articleRepository;

//	@RequestMapping("test")
//	@ResponseBody
//	public String test(){
//		return "testtest";
//	}

	@RequestMapping("list")
	@ResponseBody
	public List<Article> showList() {
		return articleRepository.findAll();
	}

	@RequestMapping("detail")
	@ResponseBody
	// EX : http://localhost:8082/usr/article/detail?id=2 => id 매개변수에 2L 이 들어옵니다.
	public Article showDetail(long id) {
		Optional<Article> article = articleRepository.findById(id);
		return article.get();
	}
}
