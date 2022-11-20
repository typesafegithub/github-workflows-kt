# GitHub Workflows Kt

> _You won't get back to YAML!_

## 🧪 Work in progress - "moving fast" phase

Please expect breaking API changes (they're documented in release notes) and missing features.

---

## 💡 Idea

YAMLs and JSONs surround us more and more frequently. While their syntax is simple and they allow defining hierarchical
data easily, the tendency is also to overuse them for more complicated scenarios where a power of a regular programming
language would be beneficial. This library aims at filling this gap, utilizing Kotlin as a modern general-purpose
language with good internal DSL support.

## ✨ Benefits

* no more confusion about YAML's indent level - Kotlin's syntax doesn't rely on it
* thanks to Kotlin being a compiled language, adds a compilation phase where a number of errors can be caught and
  prevented from getting into your workflows' runtime phase
* superb IDE support: author your workflows in any IDE that supports Kotlin, with auto-completion
* remove duplication: ability to extract common parts to constants or functions
* programmatically generate your workflow's arbitrarily complex logic, you can even call an external service and
  generate your workflow based on the response. Whatever Kotlin and the JVM allows you to do

## 🎥 Video presentation

Here's a detailed presentation of the library by Piotr Krzemiński, along with simple demos:

![type:video](https://www.youtube.com/embed/jrDQXqQicek)
