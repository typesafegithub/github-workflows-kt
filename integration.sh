#!/usr/bin/env bash

REPO="https://github.com/jmfayard/github-actions-integration"
DIR="github-actions-integration"
YAML="library/src/test/resources/integration/expected"
TARGET=".github/workflows"
MESSAGE=$(git log --oneline -n 1 --pretty='format:%s%n%nhttps://github.com/krzema12/github-actions-kotlin-dsl/commit/%H')

test ! -d ${DIR} && {
  git clone ${REPO}
  cd ${DIR}
} || {
  echo "Pulling main from ${REPO}"
  cd ${DIR}
  git stash >/dev/null
  git pull
}

echo "Copy files from ${YAML} to ${DIR}/${TARGET}"
rm -rf ${TARGET:?}/*yml
rm -rf ${TARGET:?}/*yaml
cp -f ../${YAML}/* ${TARGET}

git add ${TARGET}
echo "Changes detected, committing"
git commit -m "${MESSAGE}"
echo "Pushing changes to ${REPO}"
git push --force
