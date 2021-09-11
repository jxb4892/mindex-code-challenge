package com.mindex.challenge.service.impl;

import java.util.List;
import java.util.HashSet;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepo;

    private HashSet<String> reporterSet;

    @Override
    public ReportingStructure getStructure(String id) {
        LOG.debug("Getting a reporting structure of employee with id [{}]", id);

        Employee requestedEmployee = employeeRepo.findByEmployeeId(id);

        if (requestedEmployee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        reporterSet = new HashSet<>();
        int numReports = getReports(requestedEmployee);
        if (reporterSet.contains(id)) {
            numReports--;
        }

        ReportingStructure structure = new ReportingStructure();
        structure.setEmployee(requestedEmployee);
        System.out.println("hello???????");
        System.out.println(structure.getEmployee().getFirstName());
        structure.setNumberOfReports(numReports);

        return structure;
    }

    private int getReports(Employee employee) {
        if (reporterSet.contains(employee.getEmployeeId())) {
            return 0;
        }
        List<Employee> reports = employee.getDirectReports();
        if (reports == null || reports.size() == 0) {
            reporterSet.add(employee.getEmployeeId());
            return 1;
        }
        int totalReports = 0;
        for (Employee reporter : reports) {
            reporter = employeeRepo.findByEmployeeId(reporter.getEmployeeId());
            totalReports += getReports(reporter);
        }
        reporterSet.add(employee.getEmployeeId());
        return totalReports + 1;
    }
}
