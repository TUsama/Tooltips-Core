import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.withType
import org.gradle.kotlin.dsl.add
import kotlin.text.split


class CommonPlugin : Plugin<Project> {
    override fun apply(target: Project) {


        target.dependencies {

        }


    }

}