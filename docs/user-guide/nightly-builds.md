# Nightly builds

Sometimes you may want to test a change that has been already merged to `main`, but not yet officially released. In this
case, you can use snapshots published for each commit to the `main` branch.

To use a given "snapshot" version, e.g. `1.3.2-SNAPSHOT`, replace your scripts' preamble with:

```kotlin
@file:Repository("https://central.sonatype.com/repository/maven-snapshots/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.3.2-SNAPSHOT")
```

Remember that requesting version `1.3.2-SNAPSHOT`, if it's being actively developed, may return a different build of the
library each time it's requested. It can also happen occasionally that the snapshot doesn't correspond to any commit on
the `main` branch, and instead some PR that wasn't yet merged. That's why the snapshots are meant to quickly check
something simple that wasn't yet released, not for depending on such unstable version constantly.
