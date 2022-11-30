package utp.agile.kerplank.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import utp.agile.kerplank.model.enumerate.ProjectStatus
import utp.agile.kerplank.response.BaseResponse
import java.time.Instant


@Document
data class Project(
    @Id val id: String? = null,
    val title: String,
    val description: String,
    val dateTimeCreation: Instant = Instant.now(),
    val dateTimeDelivery: Instant,
    val status: ProjectStatus = ProjectStatus.ACTIVE,
    val users: MutableCollection<String> = mutableListOf(),
    val files: MutableCollection<String> = mutableListOf(), //paths = /notes/test.txt
    val tasks: MutableCollection<Task> = mutableListOf(), // tasks ids
) {
    fun appendTask(task: Task) = this.apply { tasks.add(task) }
    fun appendUser(userId: String) = this.apply { users.add(userId) }
    fun appendUsers(userList: List<String>) = this.apply { users.addAll(userList) }
    fun appendFile(filePath: String) = this.apply { files.add(filePath) }
    fun appendFiles(filePathList: List<String>) = this.apply { files.addAll(filePathList) }
}


data class ProjectCreateRequest(
    val title: String,
    val description: String,
    val dateTimeDelivery: Instant,
    var users: MutableCollection<String> = mutableListOf()
) {
    fun createProject(): Project {
        return Project(
            title = this.title,
            description = this.description,
            dateTimeDelivery = this.dateTimeDelivery,
            users = users
        )
    }
}

data class ProjectResponse(val project: Project) : BaseResponse()

data class ProjectListResponse(val projects: List<Project>) : BaseResponse()


data class ProjectUpdateRequest(
    val files: List<String>?,
    val users: List<String>?,
)

