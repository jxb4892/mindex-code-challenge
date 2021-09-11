package com.mindex.challenge.service.impl;

import java.util.*;
import java.util.stream.Collectors;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
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

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {
    private String employeeUrl;
    private String compensationUrl;
    private String compensationIdUrl;

    @Autowired
    private CompensationService compensationService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationIdUrl = "http://localhost:" + port + "/compensation/{id}";
    }

    @Test
    public void testCreateRead() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        testEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();
        assertNotNull(testEmployee.getEmployeeId());

        Compensation testCompensation = new Compensation();
        testCompensation.setEmployee(testEmployee);
        testCompensation.setSalary(80000.00);
        Calendar date = Calendar.getInstance();
        date.set(2023, 12, 31);
        testCompensation.setEffectiveDate(date);

        // create checks
        Compensation createdCompensation = restTemplate.postForEntity(compensationUrl, testCompensation, Compensation.class).getBody();

        assertNotNull(createdCompensation.getEmployee());
        assertCompensationEquivalence(testCompensation, createdCompensation);

        // read checks
        Compensation[] readCompensations = restTemplate.getForEntity(compensationIdUrl, Compensation[].class, testEmployee.getEmployeeId()).getBody();
        List<Compensation> compList = Arrays
                .stream(readCompensations)
                .map(comp -> {
                    Compensation a = new Compensation();
                    a.setEmployee(comp.getEmployee());
                    a.setSalary(comp.getSalary());
                    a.setEffectiveDate(comp.getEffectiveDate());
                    return a;
                }).collect(Collectors.toList());
        assertNotEquals(compList.size(), 0);
        assertEquals(createdCompensation.getEmployee().getEmployeeId(), compList.get(0).getEmployee().getEmployeeId());
        assertCompensationEquivalence(createdCompensation, compList.get(0));
    }

    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEquals(expected.getEmployee().getEmployeeId(), actual.getEmployee().getEmployeeId());
        assertEquals(expected.getSalary(), actual.getSalary(), 0.0);
        assertEquals(expected.getEffectiveDate().get(Calendar.YEAR), actual.getEffectiveDate().get(Calendar.YEAR));
        assertEquals(expected.getEffectiveDate().get(Calendar.MONTH), actual.getEffectiveDate().get(Calendar.MONTH));
        assertEquals(expected.getEffectiveDate().get(Calendar.DATE), actual.getEffectiveDate().get(Calendar.DATE));
    }
}
