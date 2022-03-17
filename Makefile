SHELL := bash
.ONESHELL:
.SHELLFLAGS := -eu -o pipefail -c
.DELETE_ON_ERROR:
MAKEFLAGS += --warn-undefined-variables
MAKEFLAGS += --no-builtin-rules

.PHONY: help start_services grade test run

# Globals
JAVA_PKG_NAMESPACE := org.example

# help: @ Lists available make tasks
help:
	@egrep -oh '[0-9a-zA-Z_\.\-]+:.*?@ .*' $(MAKEFILE_LIST) | \
	awk 'BEGIN {FS = ":.*?@ "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}' | sort

# clean: @ Clean the build
clean:
	mvn clean

# test: @ Run all tests
test:
	mvn test

# install: @ Install the application locally
install:
	mvn install -DskipTests

# run: @ Run the application locally
run:
	mvn exec:java -Dexec.mainClass="$(JAVA_PKG_NAMESPACE).WordleSolverServer"
