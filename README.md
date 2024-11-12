# Blood donor system
This is a demo application.
The main functionality of the application is to track blood donations.
There are two different types of users, ADMIN and DONOR.
Users can register on their own or ADMIN can register users on-site.

## Table of Contents
- [Installation](#installation)
- [Usage](#usage)
- [Features](#features)
- [Contributing](#contributing)
- [License](#license)

## Installation
1. Clone the repository:
```bash
 git clone https://github.com/javarivanje/blood-donor-system.git
```

2. Install postgres and create database bds inside

3. Install Keycloak with admin set to Milos or change issuer-uri: at application.yml to your admin.
  3.1. If you have changed your admin to something else than Milos then change issuer-uri at application.yml to match you admin
  3.2. Set users priviledge to ADMIN or DONOR

## Usage
To run the project, use the following command:
```bash
./mvnw clean // to clear target folder
./mvnw package // to get .jar file
java -jar target/[name of jar file].jar
```
## Features
Thisapplication is for tracking blood donations.
It has two different type of users, ADMIN and DONOR.

ADMIN users can:
	- organize blood donation event
	- register user without an account
	- monitor the quantity of blood units per blood type
	- can enter blood donation of the DONOR user
	- overview of all DONOR users
	- can confirm each blood donation by entering the amount of blood units

DONOR users can:
	- send a request for URGENT blood donation event
	- can initiate blood donation (ADMIN must confirm it)
	- monitor own blood donations

## Contributing
1. Fork the repository.
2. Create a new branch: `git checkout -b feature-name`.
3. Make your changes.
4. Push your branch: `git push origin feature-name`.
5. Create a pull request.

   
