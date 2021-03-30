# DagonParser

[![GitHub issues](https://img.shields.io/github/issues/Dagon0577/DagonParser.svg)](https://github.com/Dagon0577/DagonParser/issues)
[![GitHub forks](https://img.shields.io/github/watchers/Dagon0577/DagonParser.svg)](https://github.com/Dagon0577/DagonParser/watchers)
[![GitHub forks](https://img.shields.io/github/forks/Dagon0577/DagonParser.svg)](https://github.com/Dagon0577/DagonParser/network/members)
[![GitHub stars](https://img.shields.io/github/stars/Dagon0577/DagonParser.svg)](https://github.com/Dagon0577/DagonParser/stargazers)
[![GitHub license](https://img.shields.io/github/license/Dagon0577/DagonParser.svg)](https://github.com/Dagon0577/DagonParser/blob/master/LICENSE)

MySQL high-performance parser, use byte stream parsing.

Lexical analysis and grammatical analysis refer to MySQL 8.0

# Optimization point
- Pure binary analysis, String is not generated if it is not necessary.
- Process all supported SQL and generate syntax tree directly after receiving the binary stream to avoid subsequent duplication of work.
- Synchronous collection of relevant information such as SQL summary during grammar analysis.

# Support statement
- [Select](https://dev.mysql.com/doc/refman/8.0/en/select.html)
- [Create](https://dev.mysql.com/doc/refman/8.0/en/create-table.html)
- [Update](https://dev.mysql.com/doc/refman/8.0/en/update.html)
- [Delete](https://dev.mysql.com/doc/refman/8.0/en/delete.html)
- [Insert](https://dev.mysql.com/doc/refman/8.0/en/insert.html)
- [Replace](https://dev.mysql.com/doc/refman/8.0/en/replace.html)

# reference
[cobar](https://github.com/alibaba/cobar)
