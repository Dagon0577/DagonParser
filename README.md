# DagonParser

[![GitHub issues](https://img.shields.io/github/issues/Dagon0577/DagonParser.svg)](https://github.com/Dagon0577/DagonParser/issues)
[![GitHub forks](https://img.shields.io/github/watchers/Dagon0577/DagonParser.svg)](https://github.com/Dagon0577/DagonParser/watchers)
[![GitHub forks](https://img.shields.io/github/forks/Dagon0577/DagonParser.svg)](https://github.com/Dagon0577/DagonParser/network/members)
[![GitHub stars](https://img.shields.io/github/stars/Dagon0577/DagonParser.svg)](https://github.com/Dagon0577/DagonParser/stargazers)
[![GitHub license](https://img.shields.io/github/license/Dagon0577/DagonParser.svg)](https://github.com/Dagon0577/DagonParser/blob/master/LICENSE)

- [Chinese documentation](https://github.com/Dagon0577/DagonParser/blob/master/README-ZH.md)

MySQL high-performance parser, use byte stream parsing.

Lexical analysis and grammatical analysis refer to MySQL 8.0

# Quick start

Open the /src/test/java folder, select any statement, and run the unit test.

# Optimization point
- Pure binary analysis, String is not generated if it is not necessary.
- Process all supported SQL and generate syntax tree directly after receiving the binary stream to avoid subsequent duplication of work.
- Synchronous collection of relevant information such as SQL summary during grammar analysis.

# Support statement
- [Select](https://dev.mysql.com/doc/refman/8.0/en/select.html)
- [Update](https://dev.mysql.com/doc/refman/8.0/en/update.html)
- [Delete](https://dev.mysql.com/doc/refman/8.0/en/delete.html)
- [Insert](https://dev.mysql.com/doc/refman/8.0/en/insert.html)
- [Replace](https://dev.mysql.com/doc/refman/8.0/en/replace.html)
- Create
    - [CreateTable](https://dev.mysql.com/doc/refman/8.0/en/create-table.html)
    - [CreateTrigger](https://dev.mysql.com/doc/refman/8.0/en/create-trigger.html)
    - [CreateProcedure & CreateFunction](https://dev.mysql.com/doc/refman/8.0/en/create-procedure.html)
    - [CreateView](https://dev.mysql.com/doc/refman/8.0/en/create-view.html)
    - [CreateEvent](https://dev.mysql.com/doc/refman/8.0/en/create-event.html)
    - [CreateLogFileGroup](https://dev.mysql.com/doc/refman/8.0/en/create-logfile-group.html)
    - [CreateResourceGroup](https://dev.mysql.com/doc/refman/8.0/en/create-resource-group.html)
    - [CreateRole](https://dev.mysql.com/doc/refman/8.0/en/create-role.html)
    - [CreateServer](https://dev.mysql.com/doc/refman/8.0/en/create-server.html)
    - [CreateTablespace](https://dev.mysql.com/doc/refman/8.0/en/create-tablespace.html)
    - [CreateUser](https://dev.mysql.com/doc/refman/8.0/en/create-user.html)
    - [CreateSpatialReferenceSystem](https://dev.mysql.com/doc/refman/8.0/en/create-spatial-reference-system.html)
    - [CreateIndex](https://dev.mysql.com/doc/refman/8.0/en/create-index.html)
    - [CreateDatabase](https://dev.mysql.com/doc/refman/8.0/en/create-database.html)
- Alter
    - [AlterTable](https://dev.mysql.com/doc/refman/8.0/en/alter-table.html)
    - [AlterDatabase](https://dev.mysql.com/doc/refman/8.0/en/alter-database.html)
    - [AlterFunction](https://dev.mysql.com/doc/refman/8.0/en/alter-function.html)
    - [AlterProcedure](https://dev.mysql.com/doc/refman/8.0/en/alter-procedure.html)
    - [AlterTablespace](https://dev.mysql.com/doc/refman/8.0/en/alter-tablespace.html)
    - [AlterView](https://dev.mysql.com/doc/refman/8.0/en/alter-view.html)
    - [AlterEvent](https://dev.mysql.com/doc/refman/8.0/en/alter-event.html)
    - [AlterInstance](https://dev.mysql.com/doc/refman/8.0/en/alter-instance.html)
    - [AlterLogfileGroup](https://dev.mysql.com/doc/refman/8.0/en/alter-logfile-group.html)
    - [AlterResourceGroup](https://dev.mysql.com/doc/refman/8.0/en/alter-resource-group.html)
    - [AlterServer](https://dev.mysql.com/doc/refman/8.0/en/alter-server.html)
    - [AlterTablespace](https://dev.mysql.com/doc/refman/8.0/en/alter-tablespace.html)
    - [AlterUser](https://dev.mysql.com/doc/refman/8.0/en/alter-user.html)

# reference
[cobar](https://github.com/alibaba/cobar)

# Thanks
If you want to be added to this list and have submitted an Issue or PR, please contact me.

<a href="https://github.com/Dagon0577">
    <img src="https://avatars0.githubusercontent.com/u/31436836?s=460&v=4" width="75px">
</a>
