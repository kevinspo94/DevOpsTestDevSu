package devsu.devops.demo.service;

import java.util.List;

import devsu.devops.demo.service.dto.UserDto;

public interface UserService {

	List<UserDto> findAll();
	UserDto findById(Long id);
	UserDto save(UserDto userDto);
}
