## Generate your Kotlin Script from your existing YAML workflow

If you have an existing working GitHub YAML workflow, the **script-generator** can convert it to a Kotlin script !

To use it, clone the repository locally:

```bash
git clone https://github.com/krzema12/github-actions-kotlin-dsl
cd github-actions-kotlin-dsl
```

If you have your workflow available in a local file, run:

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

In any case, the script-generator will output your Kotlin Script:

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

## Run the script

Copy the script to for example `/path/to/.github/workflows/build.main.kts`

Make the script executable and run it:

```bash
cd /path/to/.github/workflows
chmod a+x build.main.kts
./build.main.kts
```

## Compare the YAML files - semantically

Running the script produced a file like `build.yaml`

You cannot compare it directly with the original file because there are lots of way to write in YAML the same content.

Instead, do a Semantic YAML Diff using **https://yamldiff.com/**

<img width="1336" alt="YAML_Diff_-_Semantically_compare_YAML" src="https://user-images.githubusercontent.com/459464/159888285-069cef9c-f35d-4555-93f8-7623c0c73744.png">
