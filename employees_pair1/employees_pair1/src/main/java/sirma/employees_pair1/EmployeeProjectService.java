package sirma.employees_pair1;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeProjectService {



    public Map<String, Integer> calculateWorkingDaysTogether(List<EmployeeProject> employeeProjects) {
        Map<String, Integer> workTogetherMap = new HashMap<>();

        for (int i = 0; i < employeeProjects.size(); i++) {
            for (int j = i + 1; j < employeeProjects.size(); j++) {
                EmployeeProject first = employeeProjects.get(i);
                EmployeeProject second = employeeProjects.get(j);

                if (first.getProjectId().equals(second.getProjectId())) {
                    LocalDate start1 = first.getDateFrom();
                    LocalDate end1 = first.getDateTo() == null ? LocalDate.now() : first.getDateTo();
                    LocalDate start2 = second.getDateFrom();
                    LocalDate end2 = second.getDateTo() == null ? LocalDate.now() : second.getDateTo();

                    LocalDate laterStart = start1.isAfter(start2) ? start1 : start2;
                    LocalDate earlierEnd = end1.isBefore(end2) ? end1 : end2;

                    if (!laterStart.isAfter(earlierEnd)) {
                        int daysWorkedTogether = (int) ChronoUnit.DAYS.between(laterStart, earlierEnd) ;
                        String key = first.getEmployeeId() + "-" + second.getEmployeeId() + "-" + first.getProjectId();
                        workTogetherMap.merge(key, daysWorkedTogether, Integer::sum);
                    }
                }
            }
        }

        return workTogetherMap;
    }

    public EmployeeProject parseCsvLineToEmployeeProject(String line) {

        String[] fields = line.split(",");
        Long employeeId = Long.parseLong(fields[0].trim());
        Long projectId = Long.parseLong(fields[1].trim());
        LocalDate dateFrom = LocalDate.parse(fields[2].trim());
        LocalDate dateTo = fields[3].trim().equalsIgnoreCase("NULL") ? null : LocalDate.parse(fields[3].trim());

        return new EmployeeProject(employeeId, projectId, dateFrom, dateTo);
    }
}
