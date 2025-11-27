package com.jiocoders.java.jiofamily.service;

import com.jiocoders.java.jiofamily.dto.UserPreferenceDTO;
import com.jiocoders.java.jiofamily.entity.Interest;
import com.jiocoders.java.jiofamily.entity.User;
import com.jiocoders.java.jiofamily.repository.AdminRepository;
import com.jiocoders.java.jiofamily.repository.InterestRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserPreferenceService {

    private final AdminRepository userRepository;
    private final InterestRepository interestRepository;

    public UserPreferenceService(AdminRepository userRepository,
            InterestRepository interestRepository) {
        this.userRepository = userRepository;
        this.interestRepository = interestRepository;
    }

    @Transactional
    public void registerUser(UserPreferenceDTO dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmailId(dto.getEmailId());
        user.setInterestIdList(dto.getInterestIds());
        userRepository.save(user);
    }

    public UserPreferenceDTO getUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));

        List<Interest> interests = interestRepository.findAllById(user.getInterestIdList());

        UserPreferenceDTO dto = new UserPreferenceDTO();
        dto.setFirstName(user.getFirstName());
        dto.setEmailId(user.getEmailId());
        dto.setInterestIds(user.getInterestIdList());
        dto.setInterestNames(interests.stream().map(Interest::getName).toList());
        return dto;
    }
}
