package io.github.typesafegithub.workflows.yaml

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class ObjectToYamlTest :
    DescribeSpec({
        it("correctly serializes a trivial structure") {
            // given
            val objectToSerialize =
                mapOf(
                    "foo" to 123,
                    "bar" to 456,
                )

            // when
            val yaml = objectToSerialize.toYaml()

            // then
            yaml shouldBe
                """
                foo: 123
                bar: 456

                """.trimIndent()
        }

        it("correctly serializes more complex structure") {
            // given
            val objectToSerialize =
                mapOf(
                    "foo" to
                        listOf(
                            "bar",
                            123,
                            456.789f,
                            11.22,
                            listOf(
                                mapOf("another-foo" to 123),
                                mapOf("key-with-null-value" to null),
                                mapOf("key-with-null-map" to emptyMap<String, String>()),
                                listOf(1, 2, 3),
                                mapOf("yet" to "another-map"),
                            ),
                        ),
                    "bar" to 456,
                )

            // when
            val yaml = objectToSerialize.toYaml()

            // then
            yaml shouldBe
                """
                foo:
                - 'bar'
                - 123
                - 456.789
                - 11.22
                - - another-foo: 123
                  - key-with-null-value: null
                  - key-with-null-map: {}
                  - - 1
                    - 2
                    - 3
                  - yet: 'another-map'
                bar: 456

                """.trimIndent()
        }

        it("correctly serializes duplicated values") {
            // given
            val objectToSerialize =
                mapOf(
                    "foo" to emptyMap<String, String>(),
                    "bar" to emptyMap(),
                    "baz" to emptyMap(),
                )

            // when
            val yaml = objectToSerialize.toYaml()

            // then
            yaml shouldBe
                """
                foo: {}
                bar: {}
                baz: {}

                """.trimIndent()
        }

        it("correctly serializes multiline strings") {
            // given
            val objectToSerialize =
                mapOf(
                    "foo" to
                        """
                        hey
                        hi
                        hello
                        """.trimIndent(),
                    "bar" to "baz",
                )

            // when
            val yaml = objectToSerialize.toYaml()

            // then
            yaml shouldBe
                """
                foo: |-
                  hey
                  hi
                  hello
                bar: 'baz'

                """.trimIndent()
        }

        it("correctly serializes empty strings and null strings") {
            // given
            val objectToSerialize =
                mapOf(
                    "foo" to "",
                    "bar" to null,
                    "baz" to "null",
                )

            // when
            val yaml = objectToSerialize.toYaml()

            // then
            yaml shouldBe
                """
                foo: ''
                bar: null
                baz: 'null'

                """.trimIndent()
        }

        it("correctly serializes string with comment") {
            // given
            val objectToSerialize =
                mapOf(
                    "foo" to "bar",
                    "baz" to StringWithComment("goo", "cool-comment"),
                )

            // when
            val yaml = objectToSerialize.toYaml()

            // then
            yaml shouldBe
                """
                foo: 'bar'
                baz: 'goo' # cool-comment

                """.trimIndent()
        }
    })
