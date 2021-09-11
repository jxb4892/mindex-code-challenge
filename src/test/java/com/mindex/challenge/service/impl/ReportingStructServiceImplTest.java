package com.mindex.challenge.service.impl;

import java.util.ArrayList;
import java.util.List;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import com.mindex.challenge.dao.EmployeeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructServiceImplTest {

    private String reportingStructUrl;
    private String employeeUrl;
    private String employeeIdUrl;

    @Autowired
    private ReportingStructureService rsService;

    @Autowired
    private EmployeeRepository employeeRepo;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        this.employeeUrl = "http://localhost:" + port + "/employee";
        this.employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        this.reportingStructUrl = "http://localhost:" + port + "/reportingStructure/{id}";
    }

    @Test
    public void testGetStructure() {
        Employee empl1 = new Employee();
        empl1.setFirstName("Michael");
        empl1.setLastName("Scott");
        empl1.setPosition("Regional Manager");

        Employee empl2 = new Employee();
        empl2.setFirstName("Dwight");
        empl2.setLastName("Schrute");
        empl2.setDepartment("Sales");
        empl2.setPosition("Salesman");

        Employee empl3 = new Employee();
        empl3.setFirstName("Jim");
        empl3.setLastName("Halpert");
        empl3.setDepartment("Sales");
        empl3.setPosition("Salesman");

        empl2 = restTemplate.postForEntity(employeeUrl, empl2, Employee.class).getBody();
        empl3 = restTemplate.postForEntity(employeeUrl, empl3, Employee.class).getBody();

        List<Employee> reporters = new ArrayList<>();
        reporters.add(empl2);
        reporters.add(empl3);
        empl1.setDirectReports(reporters);

        empl1 = restTemplate.postForEntity(employeeUrl, empl1, Employee.class).getBody();

        assertNotNull(empl1.getEmployeeId());
        assertNotNull(empl2.getEmployeeId());
        assertNotNull(empl3.getEmployeeId());

        // testing getting reporting structure and number of reports
        ReportingStructure actualStruct = restTemplate.getForEntity(reportingStructUrl, ReportingStructure.class, empl1.getEmployeeId()).getBody();

        ReportingStructure expectedStruct = new ReportingStructure();
        expectedStruct.setEmployee(empl1);
        expectedStruct.setNumberOfReports(2);

        assertNotNull(actualStruct.getEmployee().getEmployeeId());
        assertStructureEquivalence(actualStruct, expectedStruct);
    }

    private static void assertStructureEquivalence(ReportingStructure expected, ReportingStructure actual) {
        assertEquals(expected.getEmployee().getEmployeeId(), actual.getEmployee().getEmployeeId());
        assertEquals(expected.getNumberOfReports(), actual.getNumberOfReports());
    }
}