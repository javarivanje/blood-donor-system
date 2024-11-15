package com.bds;

import com.bds.BloodDonationEvent.BloodDonationEvent;
import com.bds.BloodDonationEvent.BloodDonationEventRepository;
import com.bds.user.BloodType;
import com.bds.user.Role;
import com.bds.user.Users;
import com.bds.user.UsersRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class BdsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BdsApplication.class, args);
	}


	@Bean
	CommandLineRunner runner(
			UsersRepository usersRepository,
			BloodDonationEventRepository bloodDonationEventRepository) {
		return args -> {
			/*Users milos = new Users(
					3L,
					"nemanja",
					"stanojevic",
					"stanoje@gmail.com",
					Role.DONOR,
					BloodType.ABPos
			);

			Users uros = new Users(
					4L,
					"milos",
					"ristic",
					"rile@gmail.com",
					Role.DONOR,
					BloodType.ANeg
			);
			List<Users> users = List.of(milos, uros);
			usersRepository.saveAll(users);
			usersRepository.findByRoleIs(Role.DONOR);
			bloodDonationEventRepository.save(
					new BloodDonationEvent(
							"moj ivent",
							LocalDate.now(),
							BloodType.ONeg,
							5,
							new Users(
									1L,
									"milos",
									"ristic",
									"rile@gmail.com",
									Role.DONOR,
									BloodType.ANeg
							)
					)
			);*/
		};
	}
}
