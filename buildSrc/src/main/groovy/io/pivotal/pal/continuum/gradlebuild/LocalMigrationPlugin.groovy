package io.pivotal.pal.continuum.gradlebuild

import org.flywaydb.gradle.FlywayExtension
import org.flywaydb.gradle.task.FlywayCleanTask
import org.flywaydb.gradle.task.FlywayMigrateTask
import org.flywaydb.gradle.task.FlywayRepairTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class LocalMigrationPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.apply plugin: "org.flywaydb.flyway"

        def defaultExtension = project.extensions.findByType(FlywayExtension)
        configureFlywayExtension(defaultExtension, project, "continuum_dev")

        addDbTasks(project, "Application", "continuum_application_test")
        addDbTasks(project, "Integration", "continuum_integration_test")

        project.task "testMigrate", group: "Flyway", description: "Migrate all test databases",
            dependsOn: ["testApplicationMigrate", "testIntegrationMigrate"]

        project.task "testClean", group: "Flyway", description: "Clean all test databases",
            dependsOn: ["testApplicationClean", "testIntegrationClean"]

        project.task "testRepair", group: "Flyway", description: "Repair all test databases",
            dependsOn: ["testApplicationRepair", "testIntegrationRepair"]
    }

    private static addDbTasks(Project project, String name, String dbName) {
        def flywayExtension = configureFlywayExtension(new FlywayExtension(), project, dbName)

        project.task("test${name}Migrate", type: FlywayMigrateTask) { extension = flywayExtension }
        project.task("test${name}Clean", type: FlywayCleanTask) { extension = flywayExtension }
        project.task("test${name}Repair", type: FlywayRepairTask) { extension = flywayExtension }
    }

    private static FlywayExtension configureFlywayExtension(FlywayExtension extension, Project project, String dbName) {
        extension.with {
            url = "jdbc:mysql://localhost:3306/$dbName?useSSL=false&serverTimezone=UTC"
            user = "continuum"
            outOfOrder = false
            locations = ["filesystem:${project.projectDir}"]
        }

        return extension
    }
}
