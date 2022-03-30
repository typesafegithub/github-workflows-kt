If you have an existing working GitHub YAML workflow, the **script generator** can convert it to a Kotlin script! It may
not produce the most readable or by any means final code (as with every code generator), but it's a good starting point
when you want to save time on migrating your workflows to YAML.

## Generate Kotlin Script from existing YAML

To use it, clone the repository locally:

```bash
git clone https://github.com/krzema12/github-actions-kotlin-dsl
cd github-actions-kotlin-dsl
```

If you have your workflow available in a local file, run:

```bash
./gradlew :script-generator:run --args /path/to/.github/workflows/build.yml
```

If your YAML workflow is available publicly, e.g. on GitHub, use such command referring to the raw file:

```bash
./gradlew :script-generator:run --args https://raw.githubusercontent.com/krzema12/github-actions-kotlin-dsl/0f41e3322a3e7de4199000fae54b398380eace2f/.github/workflows/build.yaml
```

The script generator will communicate a successful generation and hint you what to do next:

```
Kotlin script written to build.main.kts
Run it with:
  ./build.main.kts
The resulting YAML file with be available at build.yml
```

## Compare the YAML files - semantically

Running the script produced a file like `build.yml`. It's recommended to verify if the generator produced a correct
script. You cannot compare it directly with the original file because there are multiple ways to encode the same YAML
content. For example, certain formatting differences are possible, the lists can be expressed either as separate lines
starting with `-` or as values between `[` and `]`, and so on.

Instead, a semantic YAML diff may be helpful, using e.g. **https://yamldiff.com/**. It will show you what really
changed:

<img width="1336" alt="YAML_Diff_-_Semantically_compare_YAML" src="https://user-images.githubusercontent.com/459464/159888285-069cef9c-f35d-4555-93f8-7623c0c73744.png">

## Use the Kotlin script

Now you don't need your old YAML file. The new source of truth about your workflow's logic is the Kotlin script, and the
generated YAML serves only as a preview what actually will be executed by GitHub.

Please refer to [Getting started](getting_started.md) chapter to learn how to use the new script and make it work on
GitHub.
