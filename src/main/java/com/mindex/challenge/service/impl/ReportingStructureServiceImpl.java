package com.mindex.challenge.service.impl;

import java.util.List;
import java.util.HashSet;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.dao.ReportingStructureRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private ReportingStructureRepository rsRepo;

    private HashSet<String> reporterSet;

    @Override
    public ReportingStructure getStructure(String id) {
        LOG.debug("Getting a reporting structure of employee with id [{}]", id);

        Employee requestedEmployee = employeeRepo.findByEmployeeId(id);
        LOG.debug("Found employee " + requestedEmployee.getFirstName());

        if (requestedEmployee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        reporterSet = new HashSet<>();
        int numReports = getReports(requestedEmployee);
        if (reporterSet.contains(id)) {
            numReports--;
        }
        LOG.debug("got the num of reports?");
        ReportingStructure structure = new ReportingStructure();
        structure.setEmployee(requestedEmployee);
        structure.setNumberOfReports(numReports);

        LOG.debug("Found number of reports: " + numReports);
        reporterSet.clear();
        return structure;
    }

    private int getReports(Employee employee) {
        LOG.debug("getReports invoked: " + employee.getFirstName());
        if (reporterSet.contains(employee.getEmployeeId())) {
            return 0;
        }
        List<Employee> reports = employee.getDirectReports();
        LOG.debug("where it will go wrong ");
        if (reports == null || reports.size() == 0) {
            reporterSet.add(employee.getEmployeeId());
            return 1;
        }
        int totalReports = 0;
        LOG.debug("initializing total reports");
        for (Employee reporter : reports) {
            LOG.debug("current top " + employee.getFirstName());
            LOG.debug("got reporter " + reporter.getFirstName());
            reporter = employeeRepo.findByEmployeeId(reporter.getEmployeeId());
            totalReports += getReports(reporter);
        }
        reporterSet.add(employee.getEmployeeId());
        return totalReports + 1;
    }
}
