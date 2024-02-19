package sirma.employees_pair1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class FileUploadController {

    private final EmployeeProjectService employeeProjectService;

    @Autowired
    public FileUploadController(EmployeeProjectService employeeProjectService) {
        this.employeeProjectService = employeeProjectService;
    }

    @GetMapping("/")
    public String index() {
        return "upload";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload.");
            return "redirect:/";
        }

        try {
            List<EmployeeProject> employeeProjects = new BufferedReader(new InputStreamReader(file.getInputStream()))
                    .lines()
                    .map(line -> employeeProjectService.parseCsvLineToEmployeeProject(line))
                    .collect(Collectors.toList());


            Map<String, Integer> results = employeeProjectService.calculateWorkingDaysTogether(employeeProjects);

            redirectAttributes.addFlashAttribute("results", results);
            redirectAttributes.addFlashAttribute("message", "File uploaded and processed successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "An error occurred while processing the file.");
        }

        return "redirect:/";
    }
}