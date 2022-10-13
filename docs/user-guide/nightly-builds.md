# Nightly builds

Sometimes you may want to test a change that has been already merged to `main` or other branch, but not yet officially
released. In this case, you can use JitPack to request building the desired version of the library on demand:

https://jitpack.io/#krzema12/github-workflows-kt

To use the newest, bleeding-edge version from `main` branch, replace your scripts' preamble with:

```kotlin
@file:Repository("https://jitpack.io")
@file:DependsOn("com.github.krzema12:github-actions-kotlin-dsl:main-SNAPSHOT")
```

Remember that the version `main-SNAPSHOT` may return a different build of the library each time because `main` branch
gets new commits over time. If it's important to you to have more stability, consider e.g. pinning your dependency to a
specific commit. See the above link on JitPack to learn multiple ways how JitPack can refer to the library's versions.

JitPack lazily builds the library on demand, so it may happen that you will wait for the dependency resolution until the
build is done. It usually takes up to several minutes, and you can preview the progress on JitPack.
