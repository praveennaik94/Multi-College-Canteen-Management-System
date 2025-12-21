package com.canteen.multi_canteen.service;

import com.canteen.multi_canteen.model.College;
import com.canteen.multi_canteen.repository.CollegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollegeService {

    @Autowired
    private CollegeRepository collegeRepository;

    public List<College> getAllColleges() {
        return collegeRepository.findAll();
    }
}