package com.mindex.challenge;

import java.util.List;
import java.text.SimpleDateFormat;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.Compensation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataBootstrapTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompensationRepository compensationRepository;

    @Test
    public void test() {
        Employee employee = employeeRepository.findByEmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f");
        assertNotNull(employee);
        assertEquals("John", employee.getFirstName());
        assertEquals("Lennon", employee.getLastName());
        assertEquals("Development Manager", employee.getPosition());
        assertEquals("Engineering", employee.getDepartment());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        List<Compensation> compensation = compensationRepository.findByEmployee_EmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f");
        assertNotEquals(compensation.size(), 0);
        assertEquals(89565.45, compensation.get(0).getSalary(), 0.0);
        String actualDate = formatter.format(compensation.get(0).getEffectiveDate().getTime());
        assertEquals("2023/12/31", actualDate);
    }
}