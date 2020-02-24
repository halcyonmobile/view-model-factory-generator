/*
 * Copyright (c) 2020 Halcyon Mobile.
 * https://www.halcyonmobile.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.halcyonmobile.viewmodelfactory.processor


import com.halcyonmobile.viewmodelfactory.annotation.Provided
import com.halcyonmobile.viewmodelfactory.annotation.ViewModelFactory
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import com.squareup.javapoet.TypeVariableName
import java.io.IOException
import java.util.HashSet
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.inject.Inject
import javax.lang.model.SourceVersion
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic

class ViewModelFactoryProcessor : AbstractProcessor() {

    private lateinit var filer: Filer
    private lateinit var messager: Messager
    private lateinit var elementUtils: Elements

    @Synchronized
    override fun init(processingEnvironment: ProcessingEnvironment) {
        super.init(processingEnvironment)
        filer = processingEnvironment.filer
        messager = processingEnvironment.messager
        elementUtils = processingEnvironment.elementUtils
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        val set = HashSet<String>()
        set.add(ViewModelFactory::class.java.canonicalName)
        return set
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun process(set: Set<TypeElement>, roundEnvironment: RoundEnvironment): Boolean {
        try {
            val annotatedElements = roundEnvironment.getElementsAnnotatedWith(ViewModelFactory::class.java)
            if (annotatedElements.any { element -> element.kind != ElementKind.CLASS }) {
                //todo better error message
                messager.printMessage(Diagnostic.Kind.ERROR, "Cant be applied to class.")
                return true
            }
            val annotatedViewModelClasses = annotatedElements.map { element -> AnnotatedViewModelClass(elementUtils, element as TypeElement) }

            annotatedViewModelClasses.forEach {
                processAnnotatedViewModelClass(it)
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return true
    }

    private fun processAnnotatedViewModelClass(annotatedViewModelClass: AnnotatedViewModelClass) {
        val thereIsMoreThanOneConstructor = annotatedViewModelClass.constructors.size > 1

        // initialize the FactoryBuilder class
        val injectedFieldNames = HashSet<String>()
        val factoryBuilderClass = TypeSpec
            .classBuilder("${annotatedViewModelClass.simpleClassName}$FACTORY_BUILDER_CLASS_NAME")
            .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
            .addAnnotations(annotatedViewModelClass.annotationMirrors.mapToAnnotationSpecsExcept(ViewModelFactory::class.java))
        val factoryBuilderConstructor = MethodSpec
            .constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Inject::class.java)

        // initialize the factory class
        val factoryClass = TypeSpec
            .classBuilder(FACTORY_CLASS_NAME)
            .addSuperinterface(ClassName.get(FACTORY_INTERFACE_PACKAGE, VIEWMODEL_PROVIDER_CLASS_SIMPLE_NAME, FACTORY_INTERFACE_SIMPLE_NAME))
            .addModifiers(Modifier.FINAL, Modifier.PUBLIC, Modifier.STATIC)
            .applyWhen(thereIsMoreThanOneConstructor) {
                addField(Int::class.java, CONSTRUCTOR_MARKER)
            }
        val createMethodStatements = StringBuilder(if (thereIsMoreThanOneConstructor) "switch ($CONSTRUCTOR_MARKER) {\n" else "")

        annotatedViewModelClass.constructors.forEachIndexed { constructorIndex, modelConstructor ->
            // for every constructor of the ViewModel, we have a build method in the factory builder
            val buildMethod = MethodSpec
                .methodBuilder("build")
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get("", FACTORY_CLASS_NAME))
            val buildMethodStatements = StringBuilder("return new $FACTORY_CLASS_NAME(")

            // for every constructor of the ViewModel, we have a constructor for the Factory
            val factoryConstructor = MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .applyWhen(thereIsMoreThanOneConstructor) {
                    addStatement("this.$CONSTRUCTOR_MARKER = $constructorIndex")
                }

            // add case statement for that constructor
            createMethodStatements
                .applyWhen(thereIsMoreThanOneConstructor) {
                    append("\t\tcase $constructorIndex: ")
                }
                .append("return (T) new ${annotatedViewModelClass.className}(")
            if (modelConstructor.parameters.isNotEmpty()) {
                modelConstructor.parameters.forEach { parameter ->
                    val parameterName = parameter.simpleName.toString()
                    val factoryClassFieldName: String
                    if (parameter.getAnnotation(Provided::class.java) == null) {
                        // if the parameter should not be injected with Dagger
                        factoryClassFieldName = "$parameterName${FIELD_SUFFIX}$constructorIndex"

                        factoryClass.addVariableElementAsPrivateField(parameter, factoryClassFieldName)
                        buildMethod.addVariableElementAsParameter(parameter, parameterName)

                        buildMethodStatements.append(parameterName)
                    } else {
                        // if the parameter should be injected with Dagger
                        factoryClassFieldName = getAnnotatedVariableName(parameter)

                        if (!injectedFieldNames.contains(factoryClassFieldName)) {
                            // we only create the fields & parameters if a type like this was not market to inject yet
                            injectedFieldNames.add(factoryClassFieldName)
                            factoryBuilderClass.addVariableElementAsPrivateField(parameter, factoryClassFieldName, Modifier.FINAL)
                            factoryClass.addVariableElementAsPrivateField(parameter, factoryClassFieldName)

                            factoryBuilderConstructor.addVariableElementAsParameter(parameter, factoryClassFieldName)
                            factoryBuilderConstructor.addStatement("this.$factoryClassFieldName = $factoryClassFieldName")
                        }
                        buildMethodStatements.append(factoryClassFieldName)
                    }
                    buildMethodStatements.append(", ")

                    factoryConstructor.addVariableElementAsParameter(parameter, parameterName)
                    factoryConstructor.addStatement("this.$factoryClassFieldName = $parameterName")

                    createMethodStatements.append("$factoryClassFieldName, ")

                }
                //remove unnecessary " ," characters
                createMethodStatements.delete(createMethodStatements.length - 2, createMethodStatements.length)
                buildMethodStatements.delete(buildMethodStatements.length - 2, buildMethodStatements.length)
            }
            createMethodStatements.append(");\n")

            buildMethodStatements.append(");\n")
            factoryBuilderClass.addMethod(
                buildMethod
                    .addCode(buildMethodStatements.toString())
                    .build()
            )

            factoryClass.addMethod(factoryConstructor.build())
        }
        createMethodStatements.applyWhen(thereIsMoreThanOneConstructor) {
            append("\t\tdefault: return null;\n}\n")
        }

        // add the create method to the Factory
        val typeVariableName = TypeVariableName.get("T", ClassName.get(VIEWMODEL_CLASS_PACKAGE, VIEWMODEL_CLASS_SIMPLE_NAME))
        val classOfTypeVariableName = ParameterizedTypeName.get(ClassName.get(Class::class.java), typeVariableName)
        factoryClass.addMethod(
            MethodSpec
                .methodBuilder(METHOD_NAME)
                .addTypeVariable(typeVariableName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override::class.java)
                .returns(typeVariableName)
                .addParameter(ParameterSpec.builder(classOfTypeVariableName, "modelClass").build())
                .addCode(createMethodStatements.toString())
                .build()
        )

        // the factory is a nested class of the factory builder
        factoryBuilderClass.addType(factoryClass.build())

        factoryBuilderClass.addMethod(factoryBuilderConstructor.build())

        JavaFile.builder(annotatedViewModelClass.packageName, factoryBuilderClass.build()).build().writeTo(filer)
    }

    private class AnnotatedViewModelClass(elementUtils: Elements, typeElement: TypeElement) {
        val simpleClassName: String = typeElement.simpleName.toString()
        val packageName: String = elementUtils.getPackageOf(typeElement).toString()
        val constructors: List<ExecutableElement> = typeElement.enclosedElements
            .filter { it.kind == ElementKind.CONSTRUCTOR }
            .map { it as ExecutableElement }
        val className: ClassName = ClassName.get(packageName, simpleClassName)
        val annotationMirrors: List<AnnotationMirror> = typeElement.annotationMirrors
            .filter { it.annotationType.asElement().simpleName.toString() != ViewModelFactory::class.java.simpleName }

    }

    companion object {

        private const val FACTORY_INTERFACE_PACKAGE = "androidx.lifecycle"
        private const val VIEWMODEL_PROVIDER_CLASS_SIMPLE_NAME = "ViewModelProvider"
        private const val FACTORY_INTERFACE_SIMPLE_NAME = "Factory"
        private const val VIEWMODEL_CLASS_PACKAGE = "androidx.lifecycle"
        private const val VIEWMODEL_CLASS_SIMPLE_NAME = "ViewModel"

        private const val CONSTRUCTOR_MARKER = "whichConstructor"
        private const val FIELD_SUFFIX = "_parameterOfConstructor"
        private const val METHOD_NAME = "create"
        private const val FACTORY_BUILDER_CLASS_NAME = "FactoryBuilder"
        private const val FACTORY_CLASS_NAME = "Factory"

        @JvmStatic
        private val IGNORED_ANNOTATIONS_IN_FIELD_NAME =
            arrayOf(
                Provided::class.java,
                org.jetbrains.annotations.Nullable::class.java,
                org.jetbrains.annotations.NotNull::class.java
            )
                .map { TypeName.get(it) }
                .toMutableList()
                .apply {
                    addAll(
                        arrayOf(
                            "androidx.annotation" to "NonNull",
                            "androidx.annotation.Nullable" to "Nullable"
                        )
                            .map { ClassName.get(it.first, it.second) })
                }

        private fun VariableElement.toParameterSpecBuilder(name: String): ParameterSpec.Builder =
            ParameterSpec
                .builder(TypeName.get(asType()), name)
                .addAnnotations(annotationMirrors.mapToAnnotationSpecsExcept(Provided::class.java))


        private fun VariableElement.toFieldSpecBuilder(name: String): FieldSpec.Builder =
            FieldSpec
                .builder(TypeName.get(asType()), name)
                .addAnnotations(annotationMirrors.mapToAnnotationSpecsExcept(Provided::class.java))


        private fun TypeSpec.Builder.addVariableElementAsPrivateField(variableElement: VariableElement, name: String, vararg modifiers: Modifier) =
            addField(
                variableElement.toFieldSpecBuilder(name)
                    .addModifiers(Modifier.PRIVATE)
                    .addModifiers(*modifiers)
                    .build()
            )

        private fun MethodSpec.Builder.addVariableElementAsParameter(variableElement: VariableElement, name: String) =
            addParameter(
                variableElement.toParameterSpecBuilder(name)
                    .build()
            )

        private fun List<AnnotationMirror>.mapToAnnotationSpecsExcept(vararg classes: Class<*>): List<AnnotationSpec> {
            val typeNamesOfClasses = classes.map { TypeName.get(it) }

            return filterNot { typeNamesOfClasses.contains(TypeName.get(it.annotationType)) }
                .map { AnnotationSpec.get(it) }
        }

        private fun <T> T.applyWhen(condition: Boolean, block: T.() -> Unit): T {
            if (condition) {
                block.invoke(this)
            }
            return this
        }

        private fun getAnnotatedVariableName(variableElement: VariableElement): String {
            val nameBuilder = StringBuilder("")
            //remove package name
            var p = 0
            val classNameString = TypeName.get(variableElement.asType()).toString()
            while (p < classNameString.length && Character.isLowerCase(classNameString.codePointAt(p))) {
                p = classNameString.indexOf('.', p) + 1
            }
            nameBuilder.append(
                if (p > 0 && p < classNameString.length) {
                    classNameString.substring(p, classNameString.length)
                } else {
                    classNameString
                }
                    .replace("\\.", "_")
            )

            variableElement.annotationMirrors
                .filterNot { IGNORED_ANNOTATIONS_IN_FIELD_NAME.contains(TypeName.get(it.annotationType)) }
                .forEach {
                    nameBuilder.append("_${it.annotationType.asElement().simpleName}")
                }

            return nameBuilder.toString().decapitalize()
        }
    }
}
