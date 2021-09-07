package com.fundamentos.springboot.fundamentos;

import com.fundamentos.springboot.fundamentos.bean.MyBean;
import com.fundamentos.springboot.fundamentos.bean.MyBeanWithDependency;
import com.fundamentos.springboot.fundamentos.bean.MyBeanWithProperties;
import com.fundamentos.springboot.fundamentos.component.ComponentDependency;
import com.fundamentos.springboot.fundamentos.entity.User;
import com.fundamentos.springboot.fundamentos.pojo.UserPojo;
import com.fundamentos.springboot.fundamentos.repository.UserRepository;
import com.fundamentos.springboot.fundamentos.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class FundamentosApplication implements CommandLineRunner {

	private final Log LOGGER = LogFactory.getLog(FundamentosApplication.class);

	private ComponentDependency componentDependency;
	private MyBean myBean;
	private MyBeanWithDependency myBeanWithDependency;
	private MyBeanWithProperties myBeanWithProperties;
	private UserPojo userPojo;
	private UserRepository userRepository;
	private UserService userService;
	@Autowired
	public FundamentosApplication(@Qualifier("componentTwoImplement") ComponentDependency componentDependency,
								  MyBean myBean, MyBeanWithDependency myBeanWithDependency,
								  MyBeanWithProperties myBeanWithProperties,UserPojo userPojo,
								  UserRepository userRepository,UserService userService){
		this.componentDependency = componentDependency;
		this.myBean = myBean;
		this.myBeanWithDependency = myBeanWithDependency;
		this.myBeanWithProperties = myBeanWithProperties;
		this.userPojo = userPojo;
		this.userRepository = userRepository;
		this.userService = userService;
	}

	public static void main(String[] args) {
		SpringApplication.run(FundamentosApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//ejemplosAnteriores();
		saveUsersInDataBase();
		getInfoJpqlFromUser();
		saveWithErrorTransactional();
	}

	private void saveWithErrorTransactional(){
		User test1 = new User("TestTransactional1", "TestTransactional1@domain.com", LocalDate.now());
		User test2 = new User("TestTransactional2", "TestTransactional2@domain.com", LocalDate.now());
		User test3 = new User("TestTransactional3", "TestTransactional1@domain.com", LocalDate.now());
		User test4 = new User("TestTransactional4", "TestTransactional4@domain.com", LocalDate.now());

		List<User> users = Arrays.asList(test1,test2,test3,test4);

		try {
			userService.saveTransactional(users);
		}catch (Exception e){
			LOGGER.error("Esta es una excepcion en el transactional " + e);
		}
		userService.getAllUsers().stream()
				.forEach(user -> LOGGER.info("Este es el usuario dentro del metodo transactional "+ user));
	}

	private void getInfoJpqlFromUser(){
		/*LOGGER.info("Usuario con el metodo findByUserEmail"+
				userRepository.findByUserEmail("user5@gmail.com")
						.orElseThrow(()-> new RuntimeException("No se encontro el usuario")));
		userRepository.findAndSort("user", Sort.by("id").descending())
				.stream()
				.forEach(user -> LOGGER.info("Usuario con metodo sort:"+user));

		userRepository.findByName("John")
				.stream()
				.forEach(user -> LOGGER.info("Usuario con query method " + user));

		LOGGER.info("Usuario con query method"+userRepository.findByEmailAndName("daniela@gmail.com","Daniela")
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado")));
		userRepository.findByNameLike("%user%")
				.stream()
				.forEach(user -> LOGGER.info("Usuario findByNameLike" + user));

		userRepository.findByNameOrEmail(null,"user4@gmail.com")
				.stream()
				.forEach(user -> LOGGER.info("Usuario findByNameOrEmail" + user));*/
		userRepository.findByBirthDateBetween(LocalDate.of(2021,5,1),LocalDate.of(2021,10,1))
				.stream()
				.forEach(user -> LOGGER.info("Usuario findByBirthDate " + user));

		userRepository.findByNameLikeOrderByIdDesc("%user%")
				.stream()
				.forEach(user -> LOGGER.info("Usuario encontrado con like y order" + user));

		LOGGER.info(userRepository.getAllByBirthDateAndEmail(LocalDate.of(2021,6,9),"daniela@gmail.com")
				.orElseThrow(() -> new RuntimeException("No se encontro el usuario a partir del named parameter")));
	}
	private void saveUsersInDataBase(){
		User user1 = new User("John","jonh@gmail.com", LocalDate.of(2021,07,9));
		User user2 = new User("Julie","julie@gmail.com", LocalDate.of(2021,8,9));
		User user3 = new User("Daniela","daniela@gmail.com", LocalDate.of(2021,6,9));
		User user4 = new User("user4","user4@gmail.com", LocalDate.of(2021,11,9));
		User user5 = new User("user5","user5@gmail.com", LocalDate.of(2021,12,9));
		User user6 = new User("user6","user6@gmail.com", LocalDate.of(2021,10,9));
		User user7 = new User("user7","user7@gmail.com", LocalDate.of(2021,04,9));
		User user8 = new User("user8","user8@gmail.com", LocalDate.of(2021,03,9));
		User user9 = new User("user9","user9@gmail.com", LocalDate.of(2021,02,9));
		User user10 = new User("user10","user10@gmail.com", LocalDate.of(2021,01,9));
		List<User> list = Arrays.asList(user1,user2,user3,user4,user5,user6,user7,user8,user9,user10);
		list.stream().forEach(userRepository::save);
	}

	private void ejemplosAnteriores(){
		componentDependency.saludar();
		myBean.print();
		myBeanWithDependency.printWithDependency();
		System.out.println(myBeanWithProperties.function());
		System.out.println(userPojo.getEmail() + "-" + userPojo.getPassword());
		LOGGER.error("Esto es un error");
	}
}
