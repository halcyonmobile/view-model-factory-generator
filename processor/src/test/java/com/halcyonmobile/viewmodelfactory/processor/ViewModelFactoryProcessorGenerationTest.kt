package com.halcyonmobile.viewmodelfactory.processor

import com.google.common.truth.Truth
import com.google.testing.compile.JavaFileObjects
import com.google.testing.compile.JavaSourcesSubjectFactory
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import javax.tools.JavaFileObject

@RunWith(Parameterized::class)
class ViewModelFactoryProcessorGenerationTest(testDescription: String, private val sourceFiles: List<ResourceFile>, private val generated: ResourceFile?) {

    data class ResourceFile(val javaFileName: String, val resourceFileName: String)

    @Test
    fun checks_the_generated_file_against_the_given() {
        val input = sourceFiles.map(::toJavaFileObject)
        val output = generated?.let(::toJavaFileObject)

        Truth.assert_()
            .about(JavaSourcesSubjectFactory.javaSources())
            .that(input)
            .processedWith(ViewModelFactoryProcessor())
            .compilesWithoutError()
            .run {
                if (output == null) {
                    this
                } else {
                    and().generatesSources(output)
                }
            }
    }

    companion object {
        private const val DEFAULT_PACKAGE = "com.halcyonmobile.viewmodelfactory.sample"

        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun parameters(): Collection<Array<out Any?>> = listOf(
            createParameter(testDescription = "Simple MainViewModel test") {
                addInputFile(javaFileName = "MainViewModel", resourceFileName = "MainViewModel.java")
                setExpectedGeneratedFile(javaFileName = "MainViewModelFactoryBuilder", resourceFileName = "MainViewModelFactoryBuilder.java")
            },
            createParameter(testDescription = "ViewModel with annotated dependency") {
                addInputFile(javaFileName = "AnnotatedDependencyViewModel", resourceFileName = "AnnotatedDependencyViewModel.java")
                addInputFile(javaFileName = "ApplicationContext", resourceFileName = "ApplicationContext.java")
                setExpectedGeneratedFile(javaFileName = "AnnotatedDependencyViewModelFactoryBuilder", resourceFileName = "AnnotatedDependencyViewModelFactoryBuilder.java")
            },
            createParameter(testDescription = "ViewModel depends on inner class") {
                addInputFile(javaFileName = "ViewModelDependOnFooBar", resourceFileName = "ViewModelDependOnFooBar.java")
                addInputFile(javaFileName = "Foo", resourceFileName = "Foo.java")
                setExpectedGeneratedFile(javaFileName = "ViewModelDependOnFooBarFactoryBuilder", resourceFileName = "ViewModelDependOnFooBarFactoryBuilder.java")
            },
            createParameter(testDescription = "Simple Kotlin MainViewModel test") {
                addInputFile(javaFileName = "KotlinMainViewModel", resourceFileName = "KotlinMainViewModel.kt")
                setExpectedGeneratedFile(javaFileName = "MainViewModelFactoryBuilder", resourceFileName = "MainViewModelFactoryBuilder.java")
            },
            createParameter(testDescription = "ViewModel with SavedStateHandler test") {
                addInputFile(javaFileName = "SavedInstanceStateViewModel", resourceFileName = "SavedInstanceStateViewModel.java")
                setExpectedGeneratedFile(javaFileName = "SavedInstanceStateViewModelFactoryBuilder", resourceFileName = "SavedInstanceStateViewModelFactoryBuilder.java")
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
         * Interface used by [createParameter] DSL to setup a paramer for the test.
         */
        private interface Parameter {
            fun addInputFile(javaFileName: String, resourceFileName: String)

            fun setExpectedGeneratedFile(javaFileName: String, resourceFileName: String)
        }

        /**
         * Concrete implementation of [Parameter] so the [createParameter] can actually access the data given to the DSL.
         */
        private class ParameterImpl : Parameter {
            private var _expectedGeneratedFile: ResourceFile? = null
            val expectedGeneratedFile get() = _expectedGeneratedFile
            private val _inputFiles = mutableListOf<ResourceFile>()
            val inputFiles: List<ResourceFile> get() = _inputFiles

            override fun addInputFile(javaFileName: String, resourceFileName: String) {
                _inputFiles.add(ResourceFile(javaFileName.addPackage(), resourceFileName))
            }

            override fun setExpectedGeneratedFile(javaFileName: String, resourceFileName: String) {
                _expectedGeneratedFile = ResourceFile(javaFileName.addPackage(), resourceFileName)
            }

            private fun String.addPackage() = "$DEFAULT_PACKAGE.$this"
        }

        /**
         * Helper function which converts a [ResourceFile] into a [JavaFileObject]
         */
        private fun toJavaFileObject(resourceFile: ResourceFile) =
            JavaFileObjects.forSourceString(resourceFile.javaFileName, readResourceFileToString(resourceFile.resourceFileName))
    }
}