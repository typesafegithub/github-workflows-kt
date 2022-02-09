import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.TypeSpec

fun Manifest.generateTestFileSpec() =
    FileSpec.builder(coords.ownerPackage(), coords.className() + "Test")
        .indent(DEFAULT_INDENT)
        .allowPublicMethods()
        .addType(generateTestClass())
        .build()

fun Manifest.generateTestClass(): TypeSpec =
    TypeSpec.classBuilder(coords.className() + "Test")
        .superclass(ClassName("io.kotest.core.spec.style", "DescribeSpec"))
        .addSuperclassConstructorParameter("%L", generateTests())
        .build()

fun Manifest.generateTests() : CodeBlock {
    val shouldHaveYamlArguments = MemberName("it.krzeminski.githubactions", "shouldHaveYamlArguments")
    val className = coords.className()

    return CodeBlock.builder()
        .add("{\n")
        .indent()
        .add("it(%S) {\n", "renders with defaults")
        .indent()
        .add("%L() %M linkedMapOf()\n", className, shouldHaveYamlArguments)
        .unindent()
        .add("}\n\n")
        .add("it(%S) {\n", "renders with all parameters")
        .indent()
        .add("%L.example_full_action %M %L.example_full_map\n", className, shouldHaveYamlArguments, className)
        .unindent()
        .add("}\n")
        .unindent()
        .add("}")
        .build()
}

