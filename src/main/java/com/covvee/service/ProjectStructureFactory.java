package com.covvee.service;

import com.covvee.entity.File;
import com.covvee.entity.Folder;
import com.covvee.entity.Project;
import com.covvee.enums.Language;
import com.covvee.repository.FileRepository;
import com.covvee.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectStructureFactory {

    private final FolderRepository folderRepository;
    private final FileRepository fileRepository;

    public void initializeStructure(Project project) {
        if (project.getLanguage() == null) return;

        switch (project.getLanguage()) {
            case JAVA -> createJavaStructure(project);
            case JAVASCRIPT -> createJavaScriptStructure(project);
            case PYTHON -> createPythonStructure(project);
            default -> createGenericStructure(project);
        }
    }

    private void createJavaScriptStructure(Project project) {
        // 1. Create package.json in root
        File packageJson = File.builder()
                .name("package.json")
                .content("{\n  \"name\": \"" + project.getName() + "\",\n  \"version\": \"1.0.0\",\n  \"main\": \"index.js\"\n}")
                .language("json")
                .projectId(project.getId())
                .build();
        project.getRootFiles().add(fileRepository.save(packageJson));

        // 2. Create 'src' folder
        Folder src = Folder.builder()
                .name("src")
                .projectId(project.getId())
                .build();
        src = folderRepository.save(src);

        // 3. Create 'index.js' inside 'src'
        File indexJs = File.builder()
                .name("index.js")
                .content("console.log('Hello from " + project.getName() + "');")
                .language("javascript")
                .parentId(src.getId())
                .projectId(project.getId())
                .build();
        indexJs = fileRepository.save(indexJs);

        // 4. Link file to folder
        src.getFiles().add(indexJs);
        folderRepository.save(src);

        // 5. Link folder to project
        project.getFolders().add(src);
    }

    private void createJavaStructure(Project project) {
        // Create root 'src'
        Folder src = folderRepository.save(Folder.builder()
                .name("src")
                .projectId(project.getId())
                .build());

        // Create 'main' inside 'src'
        Folder main = folderRepository.save(Folder.builder()
                .name("main")
                .parentId(src.getId())
                .projectId(project.getId())
                .build());

        // Link main to src
        src.getChildren().add(main);
        folderRepository.save(src);

        // Create Main.java inside 'main'
        File mainJava = fileRepository.save(File.builder()
                .name("Main.java")
                .content("public class Main {\n  public static void main(String[] args) {\n    System.out.println(\"Hello World\");\n  }\n}")
                .language("java")
                .parentId(main.getId())
                .projectId(project.getId())
                .build());

        main.getFiles().add(mainJava);
        folderRepository.save(main);

        project.getFolders().add(src);
    }

    private void createPythonStructure(Project project) {
        File mainPy = fileRepository.save(File.builder()
                .name("main.py")
                .content("if __name__ == '__main__':\n    print('Hello World')")
                .language("python")
                .projectId(project.getId())
                .build());
        project.getRootFiles().add(mainPy);
    }

    private void createGenericStructure(Project project) {
        File readme = fileRepository.save(File.builder()
                .name("README.md")
                .content("# " + project.getName())
                .language("markdown")
                .projectId(project.getId())
                .build());
        project.getRootFiles().add(readme);
    }
}