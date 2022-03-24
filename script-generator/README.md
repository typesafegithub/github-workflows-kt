## 🎉 Generate your Kotlin Script from your existing YAML workflow

Create a Github Gist with your existing workflow

Its **url** should look like https://gist.githubusercontent.com/jmfayard/63416a2812475a39618a6b1aab16020b/raw/cd2a5d4dea15c76ca678612a814c59acb6e1b455/build.yml

Then simply run the command:

```bash
export URL=https://raw.githubusercontent.com/jmfayard/refreshVersions/main/.github/workflows/publish-mkdocs-website.yml
./gradlew :script-generator:run --args $URL
```

You will see your Kotlin Script:

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
