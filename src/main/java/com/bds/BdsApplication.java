package com.bds;

import com.bds.user.BloodType;
import com.bds.user.Role;
import com.bds.user.Users;
import com.bds.user.UsersRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class BdsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BdsApplication.class, args);
	}


	@Bean
	CommandLineRunner runner(UsersRepository usersRepository) {
		return args -> {
			Users milos = new Users(
					2L,
					"milos",
					"bacetic",
					"milos@gmail.com",
					Role.ADMIN,
					BloodType.ABNeg
			);

			/*Users uros = new Users(
					2L,
					"uros",
					"bacetic",
					"uros@gmail.com",
					Role.DONOR,
					BloodType.APos
			);*/
			List<Users> users = List.of(milos/*, uros*/);
			usersRepository.saveAll(users);
			usersRepository.findByRoleIs(Role.DONOR);
		};
	}
}
