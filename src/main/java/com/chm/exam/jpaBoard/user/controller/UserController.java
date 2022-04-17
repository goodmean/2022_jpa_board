package com.chm.exam.jpaBoard.user.controller;

import com.chm.exam.jpaBoard.article.dao.ArticleRepository;
import com.chm.exam.jpaBoard.user.UserRepository.UserRepository;
import com.chm.exam.jpaBoard.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/usr/user")
public class UserController {
	@Autowired
	private ArticleRepository articleRepository;
	@Autowired
	private UserRepository userRepository;

//	@RequestMapping("test")
//	@ResponseBody
//	public String test(){
//		return "testtest";
//	}

	@RequestMapping("login")
	public String showLogin(HttpSession session, Model model) {
		boolean isLogined = false;
		long loginedUserId = 0;

		if (session.getAttribute("loginedUserId") != null) {
			isLogined = true;
			loginedUserId = (long) session.getAttribute("loginedUserId");
		}

		if (isLogined) {
			model.addAttribute("msg", "이미 로그인 되었습니다.");
			model.addAttribute("historyBack", true);
			return "common/js";
		}

		return "usr/user/login";
	}

	@RequestMapping("doLogin")
	@ResponseBody
	public String doLogin(String email, String password, HttpServletRequest req, HttpServletResponse resp) {

		HttpSession session = req.getSession();

		if(session.getAttribute("loginedUserId") != null){
			return """
                    <script>
                    alert('이미 로그인 상태입니다.');
                    history.back();
                    </script>
                    """;
		}

		if (email == null || email.trim().length() == 0) {
			return """
                    <script>
                    alert('이메일을 입력해주세요.');
                    history.back();
                    </script>
                    """;
		}

		email = email.trim();

//		User user = userRepository.findByEmail(email).orElse(null); 방법1
		Optional<User> user = userRepository.findByEmail(email);

		if (user.isEmpty()) {
			return """
                    <script>
                    alert('일치하는 회원이 존재하지 않습니다.');
                    history.back();
                    </script>
                    """;
		}

		if (password == null || password.trim().length() == 0) {
			return """
                    <script>
                    alert('비밀번호를 입력해주세요.');
                    history.back();
                    </script>
                    """;
		}

		password = password.trim();

		if (user.get().getPassword().equals(password) == false) {
			return """
                    <script>
                    alert('비밀번호가 일치하지 않습니다.');
                    history.back();
                    </script>
                    """;
		}

		session.setAttribute("loginedUserId", user.get().getId());

//		Cookie cookie = new Cookie("loginedUserId", user.get().getId() + "");
//		resp.addCookie(cookie); 쿠키는 해킹당하기 쉽다.

		return """
                <script>
                alert('%s님 환영합니다.');
                history.back();
                </script>
                """.formatted(user.get().getName());

	}

	@RequestMapping("doLogout")
	@ResponseBody
	public String doLogout(HttpSession session) {

		boolean isLogined = false;

		if (session.getAttribute("loginedUserId") != null) {
			isLogined = true;
		}

		if ( isLogined == false ) {
			return "로그인 상태가 아닙니다.";
		}

		session.removeAttribute("loginedUserId");

		return "로그아웃 되었습니다.";

	}

	@RequestMapping("doJoin")
	@ResponseBody
	public String doJoin(String name, String email, String password){

		if( name == null || name.trim().length() == 0 ){
			return "이름을 입력해주세요.";
		}

		name = name.trim();

		if( email == null || email.trim().length() == 0 ){
			return "이메일을 입력해주세요.";
		}

		boolean isDupleEmail = userRepository.existsByEmail(email);

		if( isDupleEmail ){
			return "이미 등록된 이메일입니다.";
		}

		email = email.trim();

		if( password == null || password.trim().length() == 0 ){
			return "비밀번호를 입력해주세요.";
		}

		password = password.trim();

		User user = new User();
		user.setRegDate(LocalDateTime.now());
		user.setUpdateDate(LocalDateTime.now());
		user.setEmail(email);
		user.setPassword(password);
		user.setName(name);

		userRepository.save(user);

		return "%d번 회원이 생성되었습니다.".formatted(user.getId());
	}

	@RequestMapping("me")
	@ResponseBody
	public User showMe(HttpSession session) {
		boolean isLogined = false;
		long loginedUserId = 0;

		if (session.getAttribute("loginedUserId") != null) {
			isLogined = true;
			loginedUserId = (long) session.getAttribute("loginedUserId");
		}

		if ( isLogined == false ) {
			return null;
		}

		Optional<User> user = userRepository.findById(loginedUserId);

		if ( user.isEmpty() ) {
			return null;
		}

		return user.get();
	}
}
