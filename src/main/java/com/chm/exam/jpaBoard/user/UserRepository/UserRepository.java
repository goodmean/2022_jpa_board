package com.chm.exam.jpaBoard.user.UserRepository;

import com.chm.exam.jpaBoard.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByEmail(String email);
}
