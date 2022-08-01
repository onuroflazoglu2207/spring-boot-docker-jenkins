package com.example.demo.service;

import com.example.demo.dto.UserRequestDTO;
import com.example.demo.mapper.UserRequestMapper;
import com.example.demo.mapper.UserResponseMapper;
import com.example.demo.model.UserModel;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserResponseMapper responseMapper;
    private final UserRequestMapper requestMapper;

    @Override
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(responseMapper.toUserResponseDTOs(repository.findAll()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getById(Long identity) {
        Optional<UserModel> optional = repository.findById(identity);
        UserModel model = optional.isEmpty() ? null : optional.get();
        if (model != null) {
            return new ResponseEntity<>(responseMapper.toUserResponseDTO(model), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User is not found!", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> save(UserRequestDTO dto) {
        try {
            UserModel model = requestMapper.toUserModel(dto);
            model = repository.save(model);
            return new ResponseEntity<>(responseMapper.toUserResponseDTO(model), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Have unacceptable field!", HttpStatus.CONFLICT);
        }
    }

    @Override
    public ResponseEntity<?> update(UserRequestDTO dto, Long identity) {
        try {
            Optional<UserModel> optional = repository.findById(identity);
            UserModel model = optional.isEmpty() ? null : optional.get();
            if (model == null)
                return new ResponseEntity<>("User is not found!", HttpStatus.NOT_FOUND);
            UserModel temp = requestMapper.toUserModel(dto);
            temp.setIdentity(identity);
            repository.save(temp);
            return new ResponseEntity<>(responseMapper.toUserResponseDTO(model), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Have unacceptable field!", HttpStatus.CONFLICT);
        }
    }

    @Override
    public ResponseEntity<?> delete(Long identity) {
        Optional<UserModel> optional = repository.findById(identity);
        UserModel model = optional.isEmpty() ? null : optional.get();
        if (model != null) {
            repository.deleteById(identity);
            return new ResponseEntity<>(responseMapper.toUserResponseDTO(model), HttpStatus.OK);
        } else return new ResponseEntity<>("User is not found!", HttpStatus.NOT_FOUND);
    }
}
