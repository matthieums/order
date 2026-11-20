package com.switchfully.eurder.eurder.repository;

import com.switchfully.eurder.eurder.domain.Eurder;
import com.switchfully.eurder.eurder.domain.user.Admin;
import com.switchfully.eurder.eurder.domain.user.Customer;
import com.switchfully.eurder.eurder.domain.user.User;
import com.switchfully.eurder.eurder.domain.user.UserType;
import com.switchfully.eurder.eurder.exception.CustomerAlreadyExistsException;
import com.switchfully.eurder.eurder.exception.CustomerDoesNotExistException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email); 
}
