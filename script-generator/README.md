## 🎉 Generate your Kotlin Script from your existing YAML workflow

If you have an existing working GitHub YAML workflow, the script-generator can convert it to a Kotlin script !

To use it, clone the repository locally:

```bash
git clone https://github.com/krzema12/github-actions-kotlin-dsl
cd github-actions-kotlin-dsl
```

If you have your workflow available locally, run:

```bash
./gradlew :script-generator:run --args /path/to/.github/workflows/build.yml
```

If your YAML workflow is available publicly on GitHub, run:

```bash
./gradlew :script-generator:run --args https://raw.githubusercontent.com/krzema12/github-actions-kotlin-dsl/0f41e3322a3e7de4199000fae54b398380eace2f/.github/workflows/build.yaml
```

You can also create a GitHub Gist at https://gist.github.com/ 

```bash
./gradlew :script-generator:run --args https://raw.githubusercontent.com/jmfayard/refreshVersions/main/.github/workflows/publish-mkdocs-website.yml
```

In any case, the script-generator will show your Kotlin Script:

```kotlin
public val workflowPublishMkDocsWebsiteToGitHubPages: Workflow = workflow(
      name = "Publish MkDocs website to GitHub pages",
      on = listOf(
        Push(
          branches = listOf("release"),
        ),
        ),
      sourceFile = Paths.get("publish-mkdocs-website-to-github-pages.main.kts"),
      targetFile = Paths.get("publish-mkdocs-website-to-github-pages.yml"),
    ) {
      job("deploy", UbuntuLatest) {
        uses(
          name = "CheckoutV2",
          action = CheckoutV2(),
        )
        run(
          name = "./docs/DocsCopier.main.kts",
          command = "./docs/DocsCopier.main.kts",
        )
        uses(
          name = "SetupPythonV2",
          action = SetupPythonV2(
            pythonVersion = "3.x",
          ),
        )
       // ...
    }
}
```

You simply have to add for example `workflowPublishMkDocsWebsiteToGitHubPages.writeToFile()`

## Integration tests

You can add a working YAML workflow to the folder `yaml-input`, for example `yaml-input/all-triggers.yml` 

The test `GenerateKotlinScripts.kt` will generate its Kotlin script inside `src/test/kotlin/actual`

Once you have validated that its content is right, it will be in `src/test/kotlin/generated`

From there, 
- open the test `RunKotlinScripts.kt`
- add your workflow to the list `allWorkflows`
- run the test

This will produce a YAML file inside `yaml-output/all-triggers.yml`

You cannot compare directly the YAML files because there are lots of way to write in YAML the same content.

Instead do a sementically diff witth https://yamldiff.com/

<img width="1336" alt="YAML_Diff_-_Semantically_compare_YAML" src="https://user-images.githubusercontent.com/459464/159888285-069cef9c-f35d-4555-93f8-7623c0c73744.png">
