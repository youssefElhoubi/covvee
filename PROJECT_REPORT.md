# Covvee Backend — Progress Report

- Scope: Spring Boot 3 + MongoDB backend for a cloud IDE with projects, folders/files, auth, and sandboxed execution.
- Current modules: Auth, Projects, Folders, Files, Execution scaffold, WebSocket endpoints for live updates.
- Security: JWT-based auth (access/refresh), `UserDetails` via src/main/java/com/covvee/security/AppUserDetails.java.
- Execution: Data-backed file tree is materialized to disk, then executed inside a Docker sandbox (see detailed sections below).

## Implemented Features (snapshot)
- Auth: Register/login/refresh with JWT, CustomUserDetailsService, JwtAuthenticationFilter.
- Project/Folder/File: CRUD controllers and services; MapStruct mappers for DTOs; Mongo repositories (ProjectRepository, FolderRepository, FileRepository, UserRepository).
- Validation/Errors: Centralized handling (GlobalExceptionHandler), custom exceptions for business rules and missing resources.
- WebSockets: Channels for file/folder updates.
- Config: CORS/security, Docker client wiring, WebSocket config; app settings in application.yml.

## Execution Pipeline (detailed)
- Project materialization to disk: ProjectMaterializer.materializeProject() loads all folders/files for a project, reconstructs paths with a parent-chain walk, ensures directories exist, and writes file contents to a temp workspace (see src/main/java/com/covvee/utils/ProjectMaterializer.java, lines 17-74).
- Execution orchestration: ExecutionService.runProject() creates a per-run temp directory, calls the materializer to hydrate the project from Mongo onto disk, and is wired to call Docker next; cleanup is noted but not yet implemented (see src/main/java/com/covvee/service/ExecutionService.java, lines 17-52).
- Container sandbox: DockerService.execute() picks a language image, mounts the temp workspace to /app, constrains RAM/CPU, blocks for completion with a 10s timeout, collects logs, and removes the container; supports Python/Node/Java entrypoints (see src/main/java/com/covvee/service/DockerService.java, lines 17-134).
- Result DTO: ExecutionResult captures output, exit code, timeout flag, and error message (see dto/ExecutionResult.java).

## How the pieces fit
1) REST call triggers ExecutionService.runProject(projectId).
2) Temp workspace is created on the host; ProjectMaterializer reconstructs the DB-backed tree onto disk.
3) (Planned) DockerService.execute(tempDir, language) runs the project in an isolated container; logs/timeouts captured.
4) (Planned) Cleanup removes temp workspace; execution metadata can be stored in Execution entity.

## Gaps / Next Steps
1) Wire ExecutionService to actually call dockerService.execute() and return ExecutionResult; add robust cleanup of temp directories and container removal on errors/timeouts.
2) Parameterize language/entrypoint selection (from Project.language or user input); validate presence of required entry file before execution.
3) Persist execution records (start/end times, exit code, logs) to Execution collection; expose endpoints for history.
4) Add defensive checks in ProjectMaterializer for missing parent folders and optional base64 decoding if content is stored encoded.
5) Harden Docker limits (memory/cpu/network), add per-user quotas, and consider streaming logs via WebSocket.
6) Expand tests around materialization and execution flow; add integration tests with a mock Docker daemon or Testcontainers.

## Run/Dev Notes
- Requires Docker daemon running locally; MongoDB on 27017 by default.
- Launch: ./mvnw spring-boot:run (or use mvnw.cmd on Windows).
- Ensure JWT secrets and Mongo URI are set in application.yml or environment variables.
