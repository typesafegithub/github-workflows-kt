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
./gradlew :script-generator:run --args https://gist.githubusercontent.com/jmfayard/dba8b5195292cac0e5f83c42de7cc3c2/raw/ca6143d70a8a34eea5ea64871f87cfec69443ab1/build.yml
```

## Run the script

Whether you use an URL or a path to a local file, the script-generator should show something like:

```
Kotlin script written to update-gradle-wrapper.main.kts
Run it with:
  ./build.main.kts
The resulting YAML file with be available at build.yml
```

## Compare the YAML files - semantically

Running the script produced a file like `build.yaml`

You cannot compare it directly with the original file because there are lots of way to write in YAML the same content.

Instead, do a Semantic YAML Diff using **https://yamldiff.com/**

<img width="1336" alt="YAML_Diff_-_Semantically_compare_YAML" src="https://user-images.githubusercontent.com/459464/159888285-069cef9c-f35d-4555-93f8-7623c0c73744.png">
