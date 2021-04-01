# DagonParser

[![GitHub issues](https://img.shields.io/github/issues/Dagon0577/DagonParser.svg)](https://github.com/Dagon0577/DagonParser/issues)
[![GitHub forks](https://img.shields.io/github/watchers/Dagon0577/DagonParser.svg)](https://github.com/Dagon0577/DagonParser/watchers)
[![GitHub forks](https://img.shields.io/github/forks/Dagon0577/DagonParser.svg)](https://github.com/Dagon0577/DagonParser/network/members)
[![GitHub stars](https://img.shields.io/github/stars/Dagon0577/DagonParser.svg)](https://github.com/Dagon0577/DagonParser/stargazers)
[![GitHub license](https://img.shields.io/github/license/Dagon0577/DagonParser.svg)](https://github.com/Dagon0577/DagonParser/blob/master/LICENSE)

MySQL高性能解析器，采用byte流解析。

# 快速开始

打开/src/test/java文件夹，任意选择语句，运行单元测试。

# 优化点
- 纯二进制解析，非必要情况不生成String。
- 处理所有支持的SQL，在收到二进制流之后直接生成语法树，避免后续重复工作。
- 语法解析时同步收集SQL摘要等相关信息。

词法分析和语法分析参考MySQL 8.0

# 支持语句
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

# 参考
[cobar](https://github.com/alibaba/cobar)

# 致谢
如果你希望被添加到这个名单中，并且提交过 Issue 或者 PR，请与我联系。  

<a href="https://github.com/Dagon0577">
    <img src="https://avatars0.githubusercontent.com/u/31436836?s=460&v=4" width="75px">
</a>
