Here's a list of GitHub Actions features supported by the library, and known to be unsupported yet.

Legend:

* ✅ - fully supported
* ✅/❌ - partially supported
* ❌ - not supported

| Feature                                     | Support | Tracking issue                                                             |
|---------------------------------------------|---------|----------------------------------------------------------------------------|
| Conditions                                  | ✅       |                                                                            |
| Continue on error                           | ✅       |                                                                            |
| Concurrency                                 | ✅       |                                                                            |
| Dependent jobs                              | ✅       |                                                                            |
| Different types of triggers                 | ✅       |                                                                            |
| Different types of workers                  | ✅       |                                                                            |
| Environment variables (`env` context)       | ✅       |                                                                            |
| `github` context                            | ✅       |                                                                            |
| Job containers                              | ✅       |                                                                            |
| Job environments                            | ✅       |                                                                            |
| Docker actions                              | ✅       |                                                                            |
| Local actions                               | ✅       |                                                                            |
| `outcome` context                           | ✅       |                                                                            |
| Permissions                                 | ✅       |                                                                            |
| Public actions                              | ✅       |                                                                            |
| `runner` context                            | ✅       |                                                                            |
| Strategy matrix (`matrix` context)          | ✅/❌     | [#368](https://github.com/typesafegithub/github-workflows-kt/issues/368)   |
| Secrets (`secrets` context)                 | ✅       |                                                                            |
| Service containers                          | ✅       |                                                                            |
| Timeouts                                    | ✅       |                                                                            |
| Workflow dispatch inputs (`inputs` context) | ✅/❌     | [#811](https://github.com/typesafegithub/github-workflows-kt/issues/811)   |
