package com.mindex.challenge.service.impl;

import java.util.List;
import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);

        String id = compensation.getEmployee().getEmployeeId();
        compensation.setEmployee(employeeRepository.findByEmployeeId(id));
        compensationRepository.insert(compensation);

        return compensation;
    }

    @Override
    public List<Compensation> read(String id) {
        LOG.debug("Reading compensation of employee with id [{}]", id);

        List<Compensation> compensation = compensationRepository.findByEmployee_EmployeeId(id);

        if (compensation == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return compensation;
    }
}