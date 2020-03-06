package com.halcyonmobile.viewmodelfactory.processor

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ViewModelFactoryProcessorGenerationTest(testDescription: String, private val sourceFiles: List<ResourceFile>, private val generatedFileName: String) {

    data class ResourceFile(val sourceFileName: String, val isKotlin: Boolean)

    @Test
    fun checks_the_generated_file_against_the_given() {
        val expectedGeneratedCode = readResourceFileToString(generatedFileName)
        val kotlinSources = sourceFiles.filter { it.isKotlin }.map {
            SourceFile.kotlin(it.sourceFileName, readResourceFileToString(it.sourceFileName))
        }
        val javaSources = sourceFiles.filterNot { it.isKotlin }.map {
            SourceFile.java(it.sourceFileName, readResourceFileToString(it.sourceFileName))
        }

        val result = KotlinCompilation().apply {
            sources = kotlinSources.plus(javaSources)

            annotationProcessors = listOf(ViewModelFactoryProcessor())

            inheritClassPath = true
            messageOutputStream = System.out // see diagnostics in real time
        }.compile()

        System.err.println(result.sourcesGeneratedByAnnotationProcessor)

        val actualGeneratedSource = result.generatedFiles.first().readText()
        Assert.assertEquals(expectedGeneratedCode.removeDuplicateWhiteSpaces(), actualGeneratedSource.removeDuplicateWhiteSpaces().removeMetadata())
        Assert.assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
    }

    companion object {

        private fun String.removeDuplicateWhiteSpaces(): String = replace("[ \t]+".toRegex(), " ")

        private fun String.removeMetadata(): String = replace("@Metadata\\([^@]+@".toRegex(), "@")

        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun parameters(): Collection<Array<out Any?>> = listOf(
            createParameter(testDescription = "Simple MainViewModel test") {
                addInputFile(resourceFileName = "MainViewModel.java", isKotlin = false)
                setExpectedGeneratedFile(resourceFileName = "MainViewModelFactoryBuilder.java")
            },
            createParameter(testDescription = "ViewModel with annotated dependency") {
                addInputFile(resourceFileName = "AnnotatedDependencyViewModel.java", isKotlin = false)
                addInputFile(resourceFileName = "ApplicationContext.java", isKotlin = false)
                setExpectedGeneratedFile(resourceFileName = "AnnotatedDependencyViewModelFactoryBuilder.java")
            },
            createParameter(testDescription = "ViewModel depends on inner class") {
                addInputFile(resourceFileName = "Foo.java", isKotlin = false)
                addInputFile(resourceFileName = "ViewModelDependOnFooBar.java", isKotlin = false)
                setExpectedGeneratedFile(resourceFileName = "ViewModelDependOnFooBarFactoryBuilder.java")
            },
            createParameter(testDescription = "Simple Kotlin MainViewModel test") {
                addInputFile(resourceFileName = "KotlinMainViewModel.kt", isKotlin = true)
                setExpectedGeneratedFile(resourceFileName = "KotlinMainViewModelFactoryBuilder.java")
            },
            createParameter(testDescription = "ViewModel with SavedStateHandler test") {
                addInputFile(resourceFileName = "SavedInstanceStateViewModel.java", isKotlin = false)
                setExpectedGeneratedFile(resourceFileName = "SavedInstanceStateViewModelFactoryBuilder.java")
            }
        )

        /**
         * Simple DSL function to make the Test parameter creation more readable.
         */
        private inline fun createParameter(testDescription: String, setupParameters: Parameter.() -> Unit): Array<Any?> {
            val parameter = ParameterImpl()
            setupParameters(parameter)
            return arrayOf(testDescription, parameter.inputFiles, parameter.expectedGeneratedFile)
        }

        /**
         * Interface used by [createParameter] DSL to setup a paramert for the test.
         */
        private interface Parameter {
             fun addInputFile(resourceFileName: String, isKotlin: Boolean)

            fun setExpectedGeneratedFile(resourceFileName: String)
        }

        /**
         * Concrete implementation of [Parameter] so the [createParameter] can actually access the data given to the DSL.
         */
        private class ParameterImpl : Parameter {
            private var _expectedGeneratedFile: String? = null
            val expectedGeneratedFile : String get() = _expectedGeneratedFile!!
            private val _inputFiles = mutableListOf<ResourceFile>()
            val inputFiles: List<ResourceFile> get() = _inputFiles

            override fun addInputFile(resourceFileName: String, isKotlin: Boolean) {
                _inputFiles.add(ResourceFile(resourceFileName, isKotlin))
            }

            override fun setExpectedGeneratedFile(resourceFileName: String) {
                _expectedGeneratedFile = resourceFileName
            }
        }
    }
}