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
