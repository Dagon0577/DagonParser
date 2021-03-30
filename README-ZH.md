# DagonParser

[![GitHub issues](https://img.shields.io/github/issues/Dagon0577/DagonParser.svg)](https://github.com/Dagon0577/DagonParser/issues)
[![GitHub forks](https://img.shields.io/github/watchers/Dagon0577/DagonParser.svg)](https://github.com/Dagon0577/DagonParser/watchers)
[![GitHub forks](https://img.shields.io/github/forks/Dagon0577/DagonParser.svg)](https://github.com/Dagon0577/DagonParser/network/members)
[![GitHub stars](https://img.shields.io/github/stars/Dagon0577/DagonParser.svg)](https://github.com/Dagon0577/DagonParser/stargazers)
[![GitHub license](https://img.shields.io/github/license/Dagon0577/DagonParser.svg)](https://github.com/Dagon0577/DagonParser/blob/master/LICENSE)

MySQL高性能解析器，采用byte流解析。

# 优化点
- 纯二进制解析，非必要情况不生成String。
- 处理所有支持的SQL，在收到二进制流之后直接生成语法树，避免后续重复工作。
- 语法解析时同步收集SQL摘要等相关信息。

词法分析和语法分析参考MySQL 8.0

# 支持语句
- [Select](https://dev.mysql.com/doc/refman/8.0/en/select.html)
- [Create](https://dev.mysql.com/doc/refman/8.0/en/create-table.html)
- [Update](https://dev.mysql.com/doc/refman/8.0/en/update.html)
- [Delete](https://dev.mysql.com/doc/refman/8.0/en/delete.html)
- [Insert](https://dev.mysql.com/doc/refman/8.0/en/insert.html)
- [Replace](https://dev.mysql.com/doc/refman/8.0/en/replace.html)

# 参考
[cobar](https://github.com/alibaba/cobar)
