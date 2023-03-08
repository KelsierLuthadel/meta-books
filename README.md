# About

![Build](https://github.com/KelsierLuthadel/meta-books/workflows/Build/badge.svg) ![Tests](https://github.com/KelsierLuthadel/meta-books/blob/main/.github/badges/unit-tests.svg)

[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=KelsierLuthadel_meta-books&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=KelsierLuthadel_meta-books) [![Bugs](https://sonarcloud.io/api/project_badges/measure?project=KelsierLuthadel_meta-books&metric=bugs)](https://sonarcloud.io/summary/new_code?id=KelsierLuthadel_meta-books) [![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=KelsierLuthadel_meta-books&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=KelsierLuthadel_meta-books) [![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=KelsierLuthadel_meta-books&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=KelsierLuthadel_meta-books)

## Overview

This repository is split into two modules, the main module is a REST API framework built upon [DropWizard](https://www.dropwizard.io). 
The reason DropWizard was chosen was that it provides direct exposure to health checks, metrics and a high performant HTTP server. More information
about the framework can be viewed in the [Framework readme](framework/README.md)

## Features

## Installation

#### Installation via maven (recommended)

First you need to checkout this repo:
```bash
git clone https://github.com/KelsierLuthadel/meta-books.git
```

Once you have the code, you should use Maven to build the project:
```bash
cd meta-books
mvn clean install
```

## Quick Start

The server can be started by running the jar and providing a path to the server config (Bookshelf.yaml)
```bash
cd meta-books
java -jar framework/target/framework-1.0-SNAPSHOT.jar server ./config/Bookshelf.yml
```

You can view the APIs by pointing your browser at: http://localhost:2330/swagger/index.html

### Database
The book server requires a database to store book metadata, the recommended database is Postgres. 

#### Creating the database
Stop the bookshelf server, and edit the file `config/Bookshelf.yaml`. Set `databaseEnabled` to be true on line 58:
```bash
databaseEnabled: true
```
Save this file.

Create both a user and a database called `bookshelf`, the default password is `password`. You can change these values
by editing `DatasourceConfiguration.yaml`.

Start the migration process with the following command:
```bash
java -jar framework/target/framework-1.0-SNAPSHOT.jar db migrate ./config/Bookshelf.yml
```

This will populate the database with the default tables.

The bookshelf server can be started again with the following command:

```bash
java -jar framework/target/framework-1.0-SNAPSHOT.jar server ./config/Bookshelf.yml
```

## Requirements
Maven - to build the code locally   
Java - To run the server
Database - To store book metadata (Postgres)



