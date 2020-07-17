package parser.syntax;

import parser.ast.expression.Expression;
import parser.ast.expression.QueryExpression;
import parser.ast.expression.primary.Identifier;
import parser.ast.expression.primary.literal.Literal;
import parser.ast.expression.primary.literal.LiteralNumber;
import parser.ast.expression.primary.literal.LiteralString;
import parser.ast.fragment.SubpartitionDefinition;
import parser.ast.fragment.ddl.*;
import parser.ast.stmt.SQLStatement;
import parser.ast.stmt.compound.condition.Characteristic;
import parser.ast.stmt.dal.account.AuthOption;
import parser.ast.stmt.dal.account.DALCreateRoleStatement;
import parser.ast.stmt.dal.account.DALCreateUserStatement;
import parser.ast.stmt.dal.resource.DALCreateResourceGroupStatement;
import parser.ast.stmt.ddl.*;
import parser.ast.stmt.ddl.alter.Algorithm;
import parser.ast.stmt.ddl.alter.Lock;
import parser.lexer.Lexer;
import parser.token.IntervalUnit;
import parser.token.Keywords;
import parser.token.Token;
import parser.util.BytesUtil;
import parser.util.Pair;
import parser.util.Tuple3;

import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Dagon0577
 * @date 2020/7/17
 */
public class MySQLDDLParser extends AbstractParser {

    private ExprParser exprParser;

    private boolean unsigned = false;
    private boolean zerofill = false;
    private boolean binary = false;

    private static final Set<Integer> possibleToken = new HashSet<>();

    static {
        possibleToken.add(Token.KW_NOT);
        possibleToken.add(Token.LITERAL_NULL);
        possibleToken.add(Token.KW_DEFAULT);
        possibleToken.add(Token.KW_ON);
        possibleToken.add(Token.KW_UNIQUE);
        possibleToken.add(Token.KW_PRIMARY);
        possibleToken.add(Token.KW_KEY);
        possibleToken.add(Token.KW_COLLATE);
        possibleToken.add(Token.KW_GENERATED);
        possibleToken.add(Token.KW_AS);
        possibleToken.add(Token.KW_VIRTUAL);
        possibleToken.add(Token.KW_STORED);
        possibleToken.add(Token.IDENTIFIER);
        possibleToken.add(Token.KW_REFERENCES);
        possibleToken.add(Token.KW_CONSTRAINT);
        possibleToken.add(Token.KW_CHECK);
    }

    private static final Set<Integer> possibleTokens = new HashSet<Integer>();

    static {
        possibleTokens.add(Token.KW_UNSIGNED);
        possibleTokens.add(Token.IDENTIFIER);
        possibleTokens.add(Token.KW_ZEROFILL);
    }

    public MySQLDDLParser(Lexer lexer, ExprParser exprParser) {
        super(lexer);
        this.exprParser = exprParser;
    }

    public SQLStatement create() throws SQLSyntaxErrorException {
        switch (lexer.nextToken()) {
            case Token.IDENTIFIER:
                switch (lexer.parseKeyword()) {
                    case Keywords.TEMPORARY:
                        lexer.nextToken();
                        return createTable(true);
                    case Keywords.DEFINER:
                        lexer.nextToken();
                        match(Token.OP_EQUALS);
                        Expression definer = exprParser.expression();
                        if (lexer.token() == Token.KW_TRIGGER) {
                            return createTrigger(definer);
                        } else if (lexer.token() == Token.KW_PROCEDURE) {
                            return createProcedure(definer);
                        } else if (lexer.token() == Token.KW_FUNCTION) {
                            return createFunction(definer);
                        } else if (lexer.token() == Token.IDENTIFIER) {
                            if (lexer.parseKeyword() == Keywords.VIEW) {
                                return createView(definer, false);
                            } else if (lexer.parseKeyword() == Keywords.EVENT) {
                                return createEvent(definer);
                            }
                        }
                    case Keywords.ALGORITHM:
                    case Keywords.VIEW:
                        return createView(null, false);
                    case Keywords.EVENT:
                        return createEvent(null);
                    case Keywords.LOGFILE:
                        return createLogFileGroup();
                    case Keywords.RESOURCE:
                        return createResourceGroup();
                    case Keywords.ROLE:
                        return createRole();
                    case Keywords.SERVER:
                        return createServer();
                    case Keywords.TABLESPACE:
                        return createTablespace(false);
                    case Keywords.USER:
                        return createUser();
                }
            case Token.KW_OR:
                if (lexer.nextToken() == Token.KW_REPLACE) {
                    lexer.nextToken();
                }
                if (lexer.token() == Token.KW_SPATIAL) {
                    lexer.nextToken();
                    return createSpatialReferenceSystem(true);
                } else {
                    return createView(null, true);
                }
            case Token.KW_SQL:
                return createView(null, false);
            case Token.KW_UNIQUE:
                return createIndex(DDLCreateIndexStatement.UNIQUE);
            case Token.KW_FULLTEXT:
                return createIndex(DDLCreateIndexStatement.FULLTEXT);
            case Token.KW_SPATIAL:
                if (lexer.nextToken() == Token.KW_INDEX) {
                    return createIndex(DDLCreateIndexStatement.SPATIAL);
                } else {
                    return createSpatialReferenceSystem(false);
                }
            case Token.KW_TABLE:
                return createTable(false);
            case Token.KW_INDEX:
                return createIndex(null);
            case Token.KW_TRIGGER:
                return createTrigger(null);
            case Token.KW_PROCEDURE:
                return createProcedure(null);
            case Token.KW_FUNCTION:
                return createFunction(null);
            case Token.KW_DATABASE:
            case Token.KW_SCHEMA:
                return createDatabase();
            case Token.KW_UNDO:
                lexer.nextToken();
                return createTablespace(true);
        }
        throw new SQLSyntaxErrorException("unsupported DDL");
    }

    public SQLStatement createTable(boolean temporarys) throws SQLSyntaxErrorException {
        match(Token.KW_TABLE);
        boolean ifNotExists = false;
        if (lexer.token() == Token.KW_IF) {
            lexer.nextToken();
            match(Token.KW_NOT);
            match(Token.KW_EXISTS);
            ifNotExists = true;
        }
        Identifier tblName = identifier(true);
        if (lexer.token() != Token.KW_LIKE) {
            DDLCreateTableStatement stmt = new DDLCreateTableStatement(temporarys, ifNotExists, tblName, null);
            if (lexer.token() == Token.PUNC_LEFT_PAREN) {
                createDefinitions(stmt);
            }
            if (lexer.token() == Token.KW_LIKE) {
                lexer.nextToken();
                Identifier like = identifier();
                match(Token.PUNC_RIGHT_PAREN);
                stmt = new DDLCreateTableStatement(temporarys, ifNotExists, tblName, like);
                return stmt;
            }
            TableOptions options = new TableOptions();
            stmt.setTableOptions(options);
            tableOptions(options);
            PartitionOptions partitionOptions = partionOptions();
            stmt.setPartitionOptions(partitionOptions);
            Boolean isIgnore = null;
            Boolean isReplace = null;
            QueryExpression queryExpression = null;
            if (lexer.token() == Token.KW_IGNORE) {
                lexer.nextToken();
                isIgnore = true;
            } else if (lexer.token() == Token.KW_REPLACE) {
                lexer.nextToken();
                isReplace = true;
            }
            if (lexer.token() == Token.KW_AS) {
                lexer.nextToken();
            }
            if (lexer.token() == Token.KW_SELECT) {
                queryExpression = new MySQLDMLSelectParser(lexer, exprParser).selectUnion(false);
            }
            stmt.setIsIgnore(isIgnore);
            stmt.setIsReplace(isReplace);
            stmt.setQueryExpression(queryExpression);
            return stmt;
        } else {
            match(Token.KW_LIKE);
            Identifier like = identifier();
            DDLCreateTableStatement stmt = new DDLCreateTableStatement(temporarys, ifNotExists, tblName, like);
            return stmt;
        }
    }

    public SQLStatement createTrigger(Expression definer) throws SQLSyntaxErrorException {
        Identifier name = null;
        boolean before = true;
        int event = 0;
        Identifier table = null;
        Boolean follows = null;
        Identifier otherName = null;
        match(Token.KW_TRIGGER);
        name = identifier();
        if (lexer.token() == Token.KW_BEFORE) {
            // before = true;
        } else if (lexer.parseKeyword() == Keywords.AFTER) {
            before = false;
        } else {
            throw new SQLSyntaxErrorException("unexpected trigger_time");
        }
        lexer.nextToken();
        if (lexer.token() == Token.KW_INSERT) {
            event = DDLCreateTriggerStatement.INSERT;
        } else if (lexer.token() == Token.KW_UPDATE) {
            event = DDLCreateTriggerStatement.UPDATE;
        } else if (lexer.token() == Token.KW_DELETE) {
            event = DDLCreateTriggerStatement.DELETE;
        } else {
            throw new SQLSyntaxErrorException("unexpected trigger_event");
        }
        lexer.nextToken();
        match(Token.KW_ON);
        table = identifier(true);
        match(Token.KW_FOR);
        match(Token.KW_EACH);
        match(Token.KW_ROW);
        if (lexer.token() == Token.IDENTIFIER) {
            int key = lexer.parseKeyword();
            if (key == Keywords.FOLLOWS) {
                follows = true;
                lexer.nextToken();
                otherName = identifier();
            } else if (key == Keywords.PRECEDES) {
                follows = false;
                lexer.nextToken();
                otherName = identifier();
            }
        }
        List<SQLStatement> stmt = Parser.parse(lexer);
        return new DDLCreateTriggerStatement(definer, name, before, event, table, follows, otherName, stmt.get(0));
    }

    public SQLStatement createProcedure(Expression definer) throws SQLSyntaxErrorException {
        match(Token.KW_PROCEDURE);
        Identifier name = identifier(false);
        List<Tuple3<Integer, Identifier, DataType>> parameters = new ArrayList<>();
        List<Characteristic> characteristics = new ArrayList<>();
        SQLStatement stmt = null;
        match(Token.PUNC_LEFT_PAREN);
        parameters.add(getProcParameter());
        while (lexer.token() == Token.PUNC_COMMA) {
            lexer.nextToken();
            parameters.add(getProcParameter());
        }
        match(Token.PUNC_RIGHT_PAREN);
        Characteristic Characteristic = getCharacteristics();
        while (Characteristic != null) {
            characteristics.add(Characteristic);
            Characteristic = getCharacteristics();
        }
        List<SQLStatement> stmts = Parser.parse(lexer);
        if (stmts.size() > 0) {
            stmt = stmts.get(0);
        }
        return new DDLCreateProcedureStatement(definer, name, parameters, characteristics, stmt);
    }

    public SQLStatement createFunction(Expression definer) throws SQLSyntaxErrorException {
        match(Token.KW_FUNCTION);
        Identifier name = identifier(false);
        List<Pair<Identifier, DataType>> parameters = new ArrayList<>();
        DataType returns = null;
        List<Characteristic> characteristics = new ArrayList<>();
        SQLStatement stmt = null;
        match(Token.PUNC_LEFT_PAREN);
        parameters.add(getFuncParameter());
        while (lexer.token() == Token.PUNC_COMMA) {
            lexer.nextToken();
            parameters.add(getFuncParameter());
        }
        match(Token.PUNC_RIGHT_PAREN);
        matchKeywords(Keywords.RETURNS);
        returns = dataType();
        Characteristic Characteristic = getCharacteristics();
        while (Characteristic != null) {
            characteristics.add(Characteristic);
            Characteristic = getCharacteristics();
        }
        List<SQLStatement> stmts = Parser.parse(lexer);
        if (stmts.size() > 0) {
            stmt = stmts.get(0);
        }
        return new DDLCreateFunctionStatement(definer, name, parameters, returns, characteristics, stmt);
    }

    public SQLStatement createView(Expression definer, boolean isOrReplace) throws SQLSyntaxErrorException {
        Integer algorithm = null;
        Boolean sqlSecurityDefiner = null;
        Identifier name = null;
        List<Identifier> columns = null;
        SQLStatement stmt = null;
        boolean withCheckOption = false;
        Boolean cascaded = null;
        if (lexer.token() == Token.KW_OR) {
            lexer.nextToken();
            match(Token.KW_REPLACE);
            isOrReplace = true;
        }
        if (lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.ALGORITHM) {
            lexer.nextToken();
            match(Token.OP_EQUALS);
            if (lexer.token() == Token.IDENTIFIER) {
                int key = lexer.parseKeyword();
                if (key == Keywords.UNDEFINED) {
                    algorithm = DDLCreateViewStatement.UNDEFINED;
                    lexer.nextToken();
                } else if (key == Keywords.MERGE) {
                    algorithm = DDLCreateViewStatement.MERGE;
                    lexer.nextToken();
                } else {
                    matchKeywords(Keywords.TEMPTABLE);
                    algorithm = DDLCreateViewStatement.TEMPTABLE;
                }
            }
        }
        if (lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.DEFINER) {
            lexer.nextToken();
            match(Token.OP_EQUALS);
            definer = exprParser.expression();
        }
        if (lexer.token() == Token.KW_SQL) {
            lexer.nextToken();
            matchKeywords(Keywords.SECURITY);
            if (lexer.token() == Token.IDENTIFIER) {
                if (lexer.parseKeyword() == Keywords.DEFINER) {
                    lexer.nextToken();
                    sqlSecurityDefiner = true;
                } else {
                    matchKeywords(Keywords.INVOKER);
                    sqlSecurityDefiner = false;
                }
            }
        }
        matchKeywords(Keywords.VIEW);
        name = identifier(false);
        if (lexer.token() == Token.PUNC_LEFT_PAREN) {
            lexer.nextToken();
            columns = new ArrayList<>();
            Identifier column = identifier(false);
            columns.add(column);
            while (lexer.token() == Token.PUNC_COMMA) {
                lexer.nextToken();
                column = identifier(false);
                columns.add(column);
            }
            match(Token.PUNC_RIGHT_PAREN);
        }
        match(Token.KW_AS);
        stmt = new MySQLDMLSelectParser(lexer, exprParser).select();
        if (lexer.token() == Token.KW_WITH) {
            if (lexer.nextToken() == Token.IDENTIFIER) {
                int key = lexer.parseKeyword();
                if (key == Keywords.CASCADED) {
                    cascaded = true;
                    lexer.nextToken();
                } else if (key == Keywords.LOCAL) {
                    cascaded = false;
                    lexer.nextToken();
                }
            }
            if (lexer.token() == Token.KW_CHECK) {
                lexer.nextToken();
                match(Token.KW_OPTION);
                withCheckOption = true;
            }
        }
        return new DDLCreateViewStatement(isOrReplace, algorithm, definer, sqlSecurityDefiner, name, columns, stmt,
            withCheckOption, cascaded);
    }

    public SQLStatement createEvent(Expression definer) throws SQLSyntaxErrorException {
        boolean ifNotExist = false;
        Identifier event = null;
        ScheduleDefinition schedule = null;
        Boolean preserve = null;
        Integer enableType = null;
        LiteralString comment = null;
        SQLStatement eventBody = null;
        matchKeywords(Keywords.EVENT);
        if (lexer.token() == Token.KW_IF) {
            lexer.nextToken();
            match(Token.KW_NOT);
            match(Token.KW_EXISTS);
            ifNotExist = true;
        }
        event = identifier(false);
        match(Token.KW_ON);
        matchKeywords(Keywords.SCHEDULE);
        schedule = scheduleDefinition();
        if (lexer.token() == Token.KW_ON) {
            lexer.nextToken();
            matchKeywords(Keywords.COMPLETION);
            preserve = true;
            if (lexer.token() == Token.KW_NOT) {
                lexer.nextToken();
                preserve = false;
            }
            matchKeywords(Keywords.PRESERVE);
        }
        if (lexer.token() == Token.IDENTIFIER) {
            int key = lexer.parseKeyword();
            if (key == Keywords.ENABLE) {
                lexer.nextToken();
                enableType = DDLCreateEventStatement.ENABLE;
            } else if (key == Keywords.DISABLE) {
                lexer.nextToken();
                enableType = DDLCreateEventStatement.DISABLE;
                if (lexer.token() == Token.KW_ON) {
                    lexer.nextToken();
                    matchKeywords(Keywords.SLAVE);
                    enableType = DDLCreateEventStatement.DISABLE_ON_SLAVE;
                }
            }
        }
        if (lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.COMMENT) {
            lexer.nextToken();
            comment = exprParser.parseString();
        }
        matchKeywords(Keywords.DO);
        eventBody = Parser.parse(lexer).get(0);
        return new DDLCreateEventStatement(definer, ifNotExist, event, schedule, preserve, enableType, comment,
            eventBody);
    }

    public SQLStatement createLogFileGroup() throws SQLSyntaxErrorException {
        Identifier name = null;
        LiteralString undoFile = null;
        Expression initialSize = null;
        Expression undoBufferSize = null;
        Expression redoBufferSize = null;
        Expression nodeGroupId = null;
        boolean isWait = false;
        LiteralString comment = null;
        Identifier engine = null;
        matchKeywords(Keywords.LOGFILE);
        match(Token.KW_GROUP);
        name = identifier(false);
        match(Token.KW_ADD);
        matchKeywords(Keywords.UNDOFILE);
        undoFile = exprParser.parseString();
        while (lexer.token() == Token.IDENTIFIER) {
            switch (lexer.parseKeyword()) {
                case Keywords.INITIAL_SIZE:
                    if (lexer.nextToken() == Token.OP_EQUALS) {
                        lexer.nextToken();
                    }
                    initialSize = exprParser.expression();
                    break;
                case Keywords.UNDO_BUFFER_SIZE:
                    if (lexer.nextToken() == Token.OP_EQUALS) {
                        lexer.nextToken();
                    }
                    undoBufferSize = exprParser.expression();
                    break;
                case Keywords.REDO_BUFFER_SIZE:
                    if (lexer.nextToken() == Token.OP_EQUALS) {
                        lexer.nextToken();
                    }
                    redoBufferSize = exprParser.expression();
                    break;
                case Keywords.NODEGROUP:
                    if (lexer.nextToken() == Token.OP_EQUALS) {
                        lexer.nextToken();
                    }
                    nodeGroupId = exprParser.expression();
                    break;
                case Keywords.WAIT:
                    lexer.nextToken();
                    isWait = true;
                    break;
                case Keywords.COMMENT:
                    if (lexer.nextToken() == Token.OP_EQUALS) {
                        lexer.nextToken();
                    }
                    comment = exprParser.parseString();
                    break;
                case Keywords.ENGINE:
                    if (lexer.nextToken() == Token.OP_EQUALS) {
                        lexer.nextToken();
                    }
                    engine = identifier(false);
                    break;
                default:
                    throw new SQLSyntaxErrorException("unexpected  SQL!");
            }
        }
        return new DDLCreateLogfileGroupStatement(name, undoFile, initialSize, undoBufferSize, redoBufferSize,
            nodeGroupId, isWait, comment, engine);
    }

    public SQLStatement createResourceGroup() throws SQLSyntaxErrorException {
        Identifier name = null;
        boolean systemOrUser = false;
        List<Expression> vcpus = null;
        Long threadPriority = null;
        Boolean enable = null;
        matchKeywords(Keywords.RESOURCE);
        match(Token.KW_GROUP);
        name = identifier(false);
        matchKeywords(Keywords.TYPE);
        match(Token.OP_EQUALS);
        if (lexer.token() == Token.KW_SYSTEM) {
            systemOrUser = true;
            lexer.nextToken();
        } else {
            matchKeywords(Keywords.USER);
        }
        if (lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.VCPU) {
            lexer.nextToken();
            if (lexer.token() == Token.OP_EQUALS) {
                lexer.nextToken();
            }
            vcpus = new ArrayList<>();
            vcpus.add(exprParser.expression());
            while (lexer.token() == Token.PUNC_COMMA) {
                lexer.nextToken();
                vcpus.add(exprParser.expression());
            }
        }
        if (lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.THREAD_PRIORITY) {
            if (lexer.nextToken() == Token.OP_EQUALS) {
                lexer.nextToken();
            }
            threadPriority = exprParser.longValue();
        }
        if (lexer.token() == Token.IDENTIFIER) {
            if (lexer.parseKeyword() == Keywords.ENABLE) {
                lexer.nextToken();
                enable = true;
            } else {
                matchKeywords(Keywords.DISABLE);
                enable = false;
            }
        }
        return new DALCreateResourceGroupStatement(name, systemOrUser, vcpus, threadPriority, enable);
    }

    public SQLStatement createRole() throws SQLSyntaxErrorException {
        boolean ifNotExists = false;
        List<Expression> roles = null;
        matchKeywords(Keywords.ROLE);
        if (lexer.token() == Token.KW_IF) {
            lexer.nextToken();
            match(Token.KW_NOT);
            match(Token.KW_EXISTS);
            ifNotExists = true;
        }
        roles = new ArrayList<>();
        roles.add(exprParser.expression());
        while (lexer.token() == Token.PUNC_COMMA) {
            lexer.nextToken();
            roles.add(exprParser.expression());
        }
        return new DALCreateRoleStatement(ifNotExists, roles);
    }

    public SQLStatement createServer() throws SQLSyntaxErrorException {
        Identifier serverName = null;
        Identifier wrapperName = null;
        List<Pair<Integer, Literal>> options = null;
        matchKeywords(Keywords.SERVER);
        serverName = identifier(false);
        match(Token.KW_FOREIGN);
        matchKeywords(Keywords.DATA);
        matchKeywords(Keywords.WRAPPER);
        wrapperName = identifier(false);
        matchKeywords(Keywords.OPTIONS);
        match(Token.PUNC_LEFT_PAREN);
        options = new ArrayList<>();
        Pair<Integer, Literal> option = serverOption();
        options.add(option);
        while (lexer.token() == Token.PUNC_COMMA) {
            lexer.nextToken();
            option = serverOption();
            options.add(option);
        }
        match(Token.PUNC_RIGHT_PAREN);
        return new DDLCreateServerStatement(serverName, wrapperName, options);
    }

    public SQLStatement createTablespace(boolean undo) throws SQLSyntaxErrorException {
        Identifier name = null;
        LiteralString fileName = null;
        Expression fileBlockSize = null;
        Boolean encryption = null;
        Identifier logFileGroup = null;
        Expression extentSize = null;
        Expression initialSize = null;
        Expression autoextendSize = null;
        Expression maxSize = null;
        Expression nodeGroupId = null;
        boolean isWait = false;
        LiteralString comment = null;
        Identifier engine = null;
        matchKeywords(Keywords.TABLESPACE);
        name = identifier(false);
        if (lexer.token() == Token.KW_ADD) {
            lexer.nextToken();
            matchKeywords(Keywords.DATAFILE);
            fileName = exprParser.parseString();
        }
        if (!undo) {
            if (lexer.token() == Token.KW_USE) {
                lexer.nextToken();
                matchKeywords(Keywords.LOGFILE);
                match(Token.KW_GROUP);
                logFileGroup = identifier(false);
                if (lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.EXTENT_SIZE) {
                    if (lexer.nextToken() == Token.OP_EQUALS) {
                        lexer.nextToken();
                    }
                    extentSize = exprParser.expression();
                }
                if (lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.INITIAL_SIZE) {
                    if (lexer.nextToken() == Token.OP_EQUALS) {
                        lexer.nextToken();
                    }
                    initialSize = exprParser.expression();
                }
                if (lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.AUTOEXTEND_SIZE) {
                    if (lexer.nextToken() == Token.OP_EQUALS) {
                        lexer.nextToken();
                    }
                    autoextendSize = exprParser.expression();
                }
                if (lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.MAX_SIZE) {
                    if (lexer.nextToken() == Token.OP_EQUALS) {
                        lexer.nextToken();
                    }
                    maxSize = exprParser.expression();
                }
                if (lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.NODEGROUP) {
                    if (lexer.nextToken() == Token.OP_EQUALS) {
                        lexer.nextToken();
                    }
                    nodeGroupId = exprParser.expression();
                }
                if (lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.WAIT) {
                    lexer.nextToken();
                    isWait = true;
                }
                if (lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.COMMENT) {
                    if (lexer.nextToken() == Token.OP_EQUALS) {
                        lexer.nextToken();
                    }
                    comment = exprParser.parseString();
                }
            } else {
                if (lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.FILE_BLOCK_SIZE) {
                    lexer.nextToken();
                    match(Token.OP_EQUALS);
                    fileBlockSize = exprParser.expression();
                }
                if (lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.ENCRYPTION) {
                    if (lexer.nextToken() == Token.OP_EQUALS) {
                        lexer.nextToken();
                    }
                    LiteralString string = exprParser.parseString();
                    if (string != null) {
                        byte[] result = BytesUtil.getValue(lexer.getSQL(), string.getString());
                        if (result.length == 3) {
                            byte v = result[1];
                            if (v == 'Y' || v == 'y') {
                                encryption = true;
                            } else if (v == 'N' || v == 'n') {
                                encryption = false;
                            } else {
                                throw new SQLSyntaxErrorException("unexpected DDL SQL!");
                            }
                        } else {
                            throw new SQLSyntaxErrorException("unexpected DDL SQL!");
                        }
                    }
                }
            }
        }
        if (lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.ENGINE) {
            if (lexer.nextToken() == Token.OP_EQUALS) {
                lexer.nextToken();
            }
            engine = identifier(false);
        }
        return new DDLCreateTablespaceStatement(undo, name, fileName, fileBlockSize, encryption, logFileGroup,
            extentSize, initialSize, autoextendSize, maxSize, nodeGroupId, isWait, comment, engine);
    }

    public SQLStatement createUser() throws SQLSyntaxErrorException {
        matchKeywords(Keywords.USER);
        boolean ifNotExists = false;
        List<Pair<Expression, AuthOption>> users = null;
        List<Expression> roles = null;
        Boolean requireNone = null;
        List<Pair<Integer, LiteralString>> tlsOptions = null;
        List<Pair<Integer, Long>> resourceOptions = null;
        Tuple3<Integer, Boolean, Long> passwordOption = null;
        Boolean lock = null;
        if (lexer.token() == Token.KW_IF) {
            lexer.nextToken();
            match(Token.KW_NOT);
            match(Token.KW_EXISTS);
            ifNotExists = true;
        }
        users = new ArrayList<>();
        users.add(user());
        while (lexer.token() == Token.PUNC_COMMA) {
            lexer.nextToken();
            users.add(user());
        }
        if (lexer.token() == Token.KW_DEFAULT) {
            lexer.nextToken();
            matchKeywords(Keywords.ROLE);
            roles = new ArrayList<>();
            roles.add(exprParser.expression());
            while (lexer.token() == Token.PUNC_COMMA) {
                lexer.nextToken();
                roles.add(exprParser.expression());
            }
        }
        if (lexer.token() == Token.KW_REQUIRE) {
            lexer.nextToken();
            if (lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.NONE) {
                lexer.nextToken();
                requireNone = true;
            } else {
                Pair<Integer, LiteralString> tlsOption = tlsOption();
                tlsOptions = new ArrayList<>();
                if (tlsOption.getKey() != DALCreateUserStatement.TLS_SSL
                    && tlsOption.getKey() != DALCreateUserStatement.TLS_X509) {
                    while (tlsOption != null) {
                        tlsOptions.add(tlsOption);
                        if (lexer.token() == Token.KW_AND) {
                            lexer.nextToken();
                        }
                        tlsOption = tlsOption();
                    }
                } else {
                    tlsOptions.add(tlsOption);
                    if (lexer.token() == Token.KW_AND) {
                        throw new SQLSyntaxErrorException("unexpected DDL SQL!");
                    }
                }
            }
        }
        if (lexer.token() == Token.KW_WITH) {
            lexer.nextToken();
            Pair<Integer, Long> resourceOption = resourceOption();
            resourceOptions = new ArrayList<>();
            while (resourceOption != null) {
                resourceOptions.add(resourceOption);
                resourceOption = resourceOption();
            }
        }
        if (lexer.token() == Token.IDENTIFIER) {
            if (lexer.parseKeyword() == Keywords.PASSWORD) {
                Integer type = null;
                Boolean isDefault = null;
                Long day = null;
                lexer.nextToken();
                switch (lexer.token()) {
                    case Token.KW_REQUIRE:
                        lexer.nextToken();
                        matchKeywords(Keywords.CURRENT);
                        type = DALCreateUserStatement.PASSWORD_REQUIRE_CURRENT;
                        if (lexer.token() == Token.KW_DEFAULT) {
                            isDefault = true;
                        } else {
                            matchKeywords(Keywords.OPTIONAL);
                            isDefault = false;
                        }
                        break;
                    case Token.IDENTIFIER: {
                        switch (lexer.parseKeyword()) {
                            case Keywords.EXPIRE:
                                type = DALCreateUserStatement.PASSWORD_EXPIRE;
                                if (lexer.nextToken() == Token.KW_DEFAULT) {
                                    isDefault = true;
                                    lexer.nextToken();
                                } else if (lexer.token() == Token.KW_INTERVAL) {
                                    lexer.nextToken();
                                    day = exprParser.longValue();
                                    matchKeywords(Keywords.DAY);
                                } else {
                                    matchKeywords(Keywords.NEVER);
                                    isDefault = false;
                                }
                                break;
                            case Keywords.HISTORY:
                                type = DALCreateUserStatement.PASSWORD_HISTORY;
                                if (lexer.nextToken() == Token.KW_DEFAULT) {
                                    isDefault = true;
                                    lexer.nextToken();
                                } else {
                                    day = exprParser.longValue();
                                }
                                break;
                            case Keywords.REUSE:
                                lexer.nextToken();
                                match(Token.KW_INTERVAL);
                                type = DALCreateUserStatement.PASSWORD_REUSE_INTERVAL;
                                if (lexer.token() == Token.KW_DEFAULT) {
                                    isDefault = true;
                                    lexer.nextToken();
                                } else {
                                    day = exprParser.longValue();
                                    matchKeywords(Keywords.DAY);
                                }
                                break;
                            default:
                                throw new SQLSyntaxErrorException("unexpected DDL SQL!");
                        }
                        break;
                    }
                    default:
                        throw new SQLSyntaxErrorException("unexpected DDL SQL!");
                }
                passwordOption = new Tuple3<>(type, isDefault, day);
            } else {
                matchKeywords(Keywords.ACCOUNT);
                if (lexer.token() == Token.KW_LOCK) {
                    lock = true;
                    lexer.nextToken();
                } else {
                    match(Token.KW_UNLOCK);
                    lock = false;
                }
            }
        }
        return new DALCreateUserStatement(ifNotExists, users, roles, requireNone, tlsOptions, resourceOptions,
            passwordOption, lock);
    }

    public SQLStatement createSpatialReferenceSystem(boolean isOrReplace) throws SQLSyntaxErrorException {
        boolean ifNotExists = false;
        List<Tuple3<Integer, LiteralString, Long>> attributies = null;
        matchKeywords(Keywords.REFERENCE);
        match(Token.KW_SYSTEM);
        if (!isOrReplace) {
            if (lexer.token() == Token.KW_IF) {
                lexer.nextToken();
                match(Token.KW_NOT);
                match(Token.KW_EXISTS);
                ifNotExists = true;
            }
        }
        long srid = exprParser.longValue();
        attributies = new ArrayList<>();
        attributies.add(getAttributie());
        while (lexer.token() != Token.EOF && lexer.token() != Token.PUNC_SEMICOLON) {
            attributies.add(getAttributie());
        }
        return new DDLCreateSpatialReferenceSystemStatement(isOrReplace, ifNotExists, srid, attributies);
    }

    public SQLStatement createIndex(Integer type) throws SQLSyntaxErrorException {
        if (type != null && type != DDLCreateIndexStatement.SPATIAL) {
            lexer.nextToken();
        }
        match(Token.KW_INDEX);
        Integer indexType = null;
        Integer algorithm = null;
        Integer lockOption = null;
        Identifier name = identifier();
        if (lexer.token() == Token.KW_USING) {
            lexer.nextToken();
            indexType = matchKeywords(Keywords.BTREE, Keywords.HASH) == 0 ? IndexOption.BTREE : IndexOption.HASH;
        }
        match(Token.KW_ON);
        Identifier table = identifier(true);
        List<IndexColumnName> columns = new ArrayList<>();
        match(Token.PUNC_LEFT_PAREN);
        for (int i = 0; lexer.token() != Token.PUNC_RIGHT_PAREN; ++i) {
            if (i > 0)
                match(Token.PUNC_COMMA);
            IndexColumnName indexColumnName = indexColumnName();
            columns.add(indexColumnName);
        }
        match(Token.PUNC_RIGHT_PAREN);
        List<IndexOption> options = indexOptions();
        do {
            switch (lexer.token()) {
                case Token.IDENTIFIER:
                    if (lexer.parseKeyword() == Keywords.ALGORITHM) {
                        if (lexer.nextToken() == Token.OP_EQUALS) {
                            lexer.nextToken();
                        }
                        if (lexer.token() == Token.KW_DEFAULT) {
                            algorithm = Algorithm.DEFAULT;
                            lexer.nextToken();
                        } else {
                            Identifier result = identifier();
                            byte[] id = result.getIdText();
                            if (BytesUtil.equalsIgnoreCase(id, "INPLACE".getBytes())) {
                                algorithm = Algorithm.INPLACE;
                            } else if (BytesUtil.equalsIgnoreCase(id, "COPY".getBytes())) {
                                algorithm = Algorithm.COPY;
                            }
                        }
                    } else {
                        throw new SQLSyntaxErrorException("unexcepted SQL!");
                    }
                    break;
                case Token.KW_LOCK:
                    if (lexer.nextToken() == Token.OP_EQUALS) {
                        lexer.nextToken();
                    }
                    if (lexer.token() == Token.KW_DEFAULT) {
                        lockOption = Lock.DEFAULT;
                        lexer.nextToken();
                    } else if (lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.NONE) {
                        lockOption = Lock.NONE;
                        lexer.nextToken();
                    } else {
                        Identifier result = identifier();
                        byte[] id = result.getIdText();
                        if (BytesUtil.equalsIgnoreCase(id, "SHARED".getBytes())) {
                            lockOption = Lock.SHARED;
                        } else if (BytesUtil.equalsIgnoreCase(id, "EXCLUSIVE".getBytes())) {
                            lockOption = Lock.EXCLUSIVE;
                        }
                    }
                    break;
            }
        } while (lexer.token() != Token.PUNC_SEMICOLON && lexer.token() != Token.EOF);
        return new DDLCreateIndexStatement(type, indexType, name, options, table, columns, algorithm, lockOption);
    }

    public SQLStatement createDatabase() throws SQLSyntaxErrorException {
        boolean ifNotExist = false;
        Identifier db = null;
        Identifier charset = null;
        Identifier collate = null;
        Boolean isEncryption = null;
        if (lexer.nextToken() == Token.KW_IF) {
            lexer.nextToken();
            match(Token.KW_NOT);
            match(Token.KW_EXISTS);
            ifNotExist = true;
        }
        db = identifier(false);
        if (lexer.token() == Token.KW_DEFAULT) {
            lexer.nextToken();
            if (lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.ENCRYPTION) {
                lexer.nextToken();
                if (lexer.token() == Token.OP_EQUALS) {
                    lexer.nextToken();
                }
                LiteralString string = exprParser.parseString();
                if (string != null) {
                    byte[] result = BytesUtil.getValue(lexer.getSQL(), string.getString());
                    if (result.length == 3) {
                        byte v = result[1];
                        if (v == 'Y' || v == 'y') {
                            isEncryption = true;
                        } else if (v == 'N' || v == 'n') {
                            isEncryption = false;
                        } else {
                            throw new SQLSyntaxErrorException("unexpected DDL SQL!");
                        }
                    } else {
                        throw new SQLSyntaxErrorException("unexpected DDL SQL!");
                    }
                }
            }
        }
        if (lexer.token() == Token.KW_CHARACTER) {
            lexer.nextToken();
            match(Token.KW_SET);
            if (lexer.token() == Token.OP_EQUALS) {
                lexer.nextToken();
            }
            charset = identifier(false);
        } else if (lexer.token() == Token.KW_COLLATE) {
            lexer.nextToken();
            if (lexer.token() == Token.OP_EQUALS) {
                lexer.nextToken();
            }
            collate = identifier(false);
        }
        return new DDLCreateDatabaseStatement(ifNotExist, db, charset, collate, isEncryption);
    }

    public PartitionOptions partionOptions() throws SQLSyntaxErrorException {
        PartitionOptions partitionOptions = new PartitionOptions();
        if (lexer.token() == Token.KW_PARTITION) {
            lexer.nextToken();
            match(Token.KW_BY);
            switch (lexer.token()) {
                case Token.KW_LINEAR:
                    lexer.nextToken();
                    partitionOptions.setLiner(true);
                    if (lexer.parseKeyword() == Keywords.HASH) {
                        lexer.nextToken();
                        match(Token.PUNC_LEFT_PAREN);
                        partitionOptions.setHash(exprParser.expression());
                        match(Token.PUNC_RIGHT_PAREN);
                    } else if (lexer.token() == Token.KW_KEY) {
                        lexer.nextToken();
                        partitionOptions.setKey(true);
                        if (lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.ALGORITHM) {
                            if (lexer.nextToken() == Token.OP_EQUALS) {
                                lexer.nextToken();
                            }
                            int intVal = lexer.integerValue().intValue();
                            lexer.nextToken();
                            if (intVal == 1) {
                                partitionOptions.setAlgorithm(1);
                            } else if (intVal == 2) {
                                partitionOptions.setAlgorithm(2);
                            }
                        }
                        match(Token.PUNC_LEFT_PAREN);
                        List<Identifier> keyColumns = new ArrayList<>();
                        keyColumns.add(identifier());
                        for (; lexer.token() == Token.PUNC_COMMA; ) {
                            lexer.nextToken();
                            keyColumns.add(identifier());
                        }
                        partitionOptions.setKeyColumns(keyColumns);
                        match(Token.PUNC_RIGHT_PAREN);
                    }
                    break;
                case Token.KW_RANGE:
                    if (lexer.nextToken() == Token.PUNC_LEFT_PAREN) {
                        lexer.nextToken();
                        partitionOptions.setRangeExpr(exprParser.expression());
                        match(Token.PUNC_RIGHT_PAREN);
                    } else if (lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.COLUMNS) {
                        lexer.nextToken();
                        match(Token.PUNC_LEFT_PAREN);
                        List<Identifier> rangeColumns = new ArrayList<>();
                        rangeColumns.add(identifier());
                        for (; lexer.token() == Token.PUNC_COMMA; ) {
                            lexer.nextToken();
                            rangeColumns.add(identifier());
                        }
                        partitionOptions.setRangeColumns(rangeColumns);
                        match(Token.PUNC_RIGHT_PAREN);
                    }
                    break;
                case Token.IDENTIFIER:
                    int key = lexer.parseKeyword();
                    if (key == Keywords.LIST) {
                        if (lexer.nextToken() == Token.PUNC_LEFT_PAREN) {
                            lexer.nextToken();
                            partitionOptions.setListExpr(exprParser.expression());
                            match(Token.PUNC_RIGHT_PAREN);
                        } else if (lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.COLUMNS) {
                            lexer.nextToken();
                            match(Token.PUNC_LEFT_PAREN);
                            List<Identifier> listColumns = new ArrayList<>();
                            listColumns.add(identifier());
                            for (; lexer.token() == Token.PUNC_COMMA; ) {
                                lexer.nextToken();
                                listColumns.add(identifier());
                            }
                            partitionOptions.setListColumns(listColumns);
                            match(Token.PUNC_RIGHT_PAREN);
                        }
                    } else if (key == Keywords.HASH) {
                        lexer.nextToken();
                        match(Token.PUNC_LEFT_PAREN);
                        partitionOptions.setHash(exprParser.expression());
                        match(Token.PUNC_RIGHT_PAREN);
                    } else {
                        throw new SQLSyntaxErrorException("unexpected SQL!");
                    }
                    break;
                case Token.KW_KEY:
                    lexer.nextToken();
                    partitionOptions.setKey(true);
                    if (lexer.parseKeyword() == Keywords.ALGORITHM) {
                        if (lexer.nextToken() == Token.OP_EQUALS) {
                            lexer.nextToken();
                        }
                        int intVal = lexer.integerValue().intValue();
                        lexer.nextToken();
                        if (intVal == 1) {
                            partitionOptions.setAlgorithm(1);
                        } else if (intVal == 2) {
                            partitionOptions.setAlgorithm(2);
                        }
                    }
                    match(Token.PUNC_LEFT_PAREN);
                    if (lexer.token() == Token.IDENTIFIER) {
                        List<Identifier> keyColumns = new ArrayList<>();
                        keyColumns.add(identifier());
                        for (; lexer.token() == Token.PUNC_COMMA; ) {
                            lexer.nextToken();
                            keyColumns.add(identifier());
                        }
                        partitionOptions.setKeyColumns(keyColumns);
                    }
                    match(Token.PUNC_RIGHT_PAREN);
                    break;
            }
            if (lexer.token() == Token.IDENTIFIER) {
                do {
                    switch (lexer.parseKeyword()) {
                        case Keywords.PARTITIONS:
                            lexer.nextToken();
                            partitionOptions.setPartitionNum(lexer.integerValue().longValue());
                            lexer.nextToken();
                            break;
                        case Keywords.SUBPARTITION:
                            lexer.nextToken();
                            match(Token.KW_BY);
                            if (lexer.token() == Token.KW_LINEAR) {
                                lexer.nextToken();
                                partitionOptions.setSubpartitionLiner(true);
                                if (lexer.token() == Token.KW_KEY) {
                                    lexer.nextToken();
                                    partitionOptions.setSubpartitionKey(true);
                                    if (lexer.parseKeyword() == Keywords.ALGORITHM) {
                                        if (lexer.nextToken() == Token.OP_EQUALS) {
                                            lexer.nextToken();
                                        }
                                        int intVal = lexer.integerValue().intValue();
                                        lexer.nextToken();
                                        if (intVal == 1) {
                                            partitionOptions.setSubpartitionAlgorithm(1);
                                        } else if (intVal == 2) {
                                            partitionOptions.setSubpartitionAlgorithm(2);
                                        }
                                    }
                                    match(Token.PUNC_LEFT_PAREN);
                                    List<Identifier> subKeyColumns = new ArrayList<>();
                                    subKeyColumns.add(identifier());
                                    for (; lexer.token() == Token.PUNC_COMMA; ) {
                                        lexer.nextToken();
                                        subKeyColumns.add(identifier());
                                    }
                                    partitionOptions.setSubpartitionKeyColumns(subKeyColumns);
                                    match(Token.PUNC_RIGHT_PAREN);
                                } else if (lexer.parseKeyword() == Keywords.HASH) {
                                    lexer.nextToken();
                                    match(Token.PUNC_LEFT_PAREN);
                                    partitionOptions.setSubpartitionHash(exprParser.expression());
                                    match(Token.PUNC_RIGHT_PAREN);
                                }
                            } else if (lexer.token() == Token.KW_KEY) {
                                lexer.nextToken();
                                partitionOptions.setSubpartitionKey(true);
                                if (lexer.parseKeyword() == Keywords.ALGORITHM) {
                                    if (lexer.nextToken() == Token.OP_EQUALS) {
                                        lexer.nextToken();
                                    }
                                    int intVal = lexer.integerValue().intValue();
                                    lexer.nextToken();
                                    if (intVal == 1) {
                                        partitionOptions.setSubpartitionAlgorithm(1);
                                    } else if (intVal == 2) {
                                        partitionOptions.setSubpartitionAlgorithm(2);
                                    }
                                }
                                match(Token.PUNC_LEFT_PAREN);
                                List<Identifier> subKeyColumns = new ArrayList<>();
                                subKeyColumns.add(identifier());
                                for (; lexer.token() == Token.PUNC_COMMA; ) {
                                    lexer.nextToken();
                                    subKeyColumns.add(identifier());
                                }
                                partitionOptions.setSubpartitionKeyColumns(subKeyColumns);
                                match(Token.PUNC_RIGHT_PAREN);
                            } else if (lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.HASH) {
                                lexer.nextToken();
                                match(Token.PUNC_LEFT_PAREN);
                                partitionOptions.setSubpartitionHash(exprParser.expression());
                                match(Token.PUNC_RIGHT_PAREN);
                            }
                            if (lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.SUBPARTITIONS) {
                                lexer.nextToken();
                                partitionOptions.setSubpartitionNum(lexer.integerValue().longValue());
                                lexer.nextToken();
                            }
                            break;
                    }
                } while (lexer.token() == Token.IDENTIFIER);
            }
            if (lexer.token() == Token.PUNC_LEFT_PAREN) {
                List<PartitionDefinition> partitionDefinitions = partitionDefinition();
                partitionOptions.setPartitionDefinitions(partitionDefinitions);
            }
            return partitionOptions;
        }
        return null;
    }

    private void createDefinitions(DDLCreateTableStatement stmt) throws SQLSyntaxErrorException {
        match(Token.PUNC_LEFT_PAREN);
        if (lexer.token() == Token.KW_LIKE) {
            return;
        }
        IndexDefinition indexDef = null;
        Identifier id = null;
        Identifier symbol = null;
        boolean needMatchComma = true;
        Expression expr = null;
        Boolean enforced = null;
        for (int i = 0; lexer.token() != Token.PUNC_RIGHT_PAREN; ++i) {
            if (i > 0 && needMatchComma) {
                match(Token.PUNC_COMMA);
            }
            if (!needMatchComma) {
                needMatchComma = true;
            }
            switch (lexer.token()) {
                case Token.KW_PRIMARY:
                    lexer.nextToken();
                    match(Token.KW_KEY);
                    indexDef = indexDefinition(IndexDefinition.PRIMARY, symbol);
                    stmt.setPrimaryKey(indexDef);
                    break;
                case Token.KW_INDEX:
                    lexer.nextToken();
                    indexDef = indexDefinition(IndexDefinition.INDEX, symbol);
                    stmt.addKey(indexDef);
                    break;
                case Token.KW_KEY:
                    lexer.nextToken();
                    indexDef = indexDefinition(IndexDefinition.KEY, symbol);
                    stmt.addKey(indexDef);
                    break;
                case Token.KW_UNIQUE:
                    switch (lexer.nextToken()) {
                        case Token.KW_INDEX:
                        case Token.KW_KEY:
                            lexer.nextToken();
                            break;
                    }
                    indexDef = indexDefinition(IndexDefinition.UNIQUE, symbol);
                    stmt.addUniqueIndex(indexDef);
                    break;
                case Token.KW_FULLTEXT:
                    switch (lexer.nextToken()) {
                        case Token.KW_INDEX:
                        case Token.KW_KEY:
                            lexer.nextToken();
                            break;
                    }
                    indexDef = indexDefinition(IndexDefinition.FULLTEXT, symbol);
                    if (indexDef.getIndexType() != null) {
                        throw new SQLSyntaxErrorException("FULLTEXT INDEX can specify no index_type");
                    }
                    stmt.addFullTextKey(indexDef);
                    break;
                case Token.KW_SPATIAL:
                    switch (lexer.nextToken()) {
                        case Token.KW_INDEX:
                        case Token.KW_KEY:
                            lexer.nextToken();
                            break;
                    }
                    indexDef = indexDefinition(IndexDefinition.SPATIAL, symbol);
                    if (indexDef.getIndexType() != null) {
                        throw new SQLSyntaxErrorException("SPATIAL INDEX can specify no index_type");
                    }
                    stmt.addSpatialKey(indexDef);
                    break;
                case Token.KW_CHECK:
                    lexer.nextToken();
                    match(Token.PUNC_LEFT_PAREN);
                    expr = exprParser.expression();
                    match(Token.PUNC_RIGHT_PAREN);
                    enforced = null;
                    if (lexer.token() == Token.KW_NOT) {
                        lexer.nextToken();
                        matchIdentifier("ENFORCED");
                        enforced = false;
                        stmt.addCheck(new Pair<Expression, Boolean>(expr, enforced));
                    } else if (lexer.token() == Token.IDENTIFIER) {
                        matchIdentifier("ENFORCED");
                        enforced = true;
                        stmt.addCheck(new Pair<Expression, Boolean>(expr, enforced));
                    }
                    break;
                case Token.IDENTIFIER:
                    Identifier columnName = identifier();
                    ColumnDefinition columnDef = columnDefinition();
                    stmt.addColumn(new Pair<Identifier, ColumnDefinition>(columnName, columnDef));
                    if (columnDef.getPrimaryKey() != null) {
                        List<IndexColumnName> cols = new ArrayList<>();
                        cols.add(new IndexColumnName(columnName, null, true));
                        stmt.setPrimaryKey(
                            new IndexDefinition(IndexDefinition.PRIMARY, columnName, null, cols, null, null));
                    } else if (columnDef.getUniqueKey() != null) {
                        List<IndexColumnName> cols = new ArrayList<>();
                        cols.add(new IndexColumnName(columnName, null, true));
                        stmt.addUniqueIndex(
                            new IndexDefinition(IndexDefinition.UNIQUE, columnName, null, cols, null, null));
                    }
                    break;
                case Token.KW_CONSTRAINT:
                    lexer.nextToken();
                    if (lexer.token() == Token.IDENTIFIER) {
                        symbol = identifier();
                        stmt.addChecksName(symbol);
                    }
                    if (lexer.token() == Token.KW_CHECK) {
                        lexer.nextToken();
                        match(Token.PUNC_LEFT_PAREN);
                        expr = exprParser.expression();
                        match(Token.PUNC_RIGHT_PAREN);
                    }
                    enforced = null;
                    if (lexer.token() == Token.KW_NOT) {
                        lexer.nextToken();
                        matchIdentifier("ENFORCED");
                        enforced = false;
                        stmt.addCheck(new Pair<Expression, Boolean>(expr, enforced));
                    } else if (lexer.token() == Token.IDENTIFIER) {
                        matchIdentifier("ENFORCED");
                        enforced = true;
                        stmt.addCheck(new Pair<Expression, Boolean>(expr, enforced));
                    }
                    if (lexer.token() != Token.PUNC_COMMA) {
                        needMatchComma = false;
                    }
                    break;
                case Token.KW_FOREIGN:
                    if (lexer.nextToken() == Token.KW_KEY) {
                        lexer.nextToken();
                        if (lexer.token() == Token.IDENTIFIER) {
                            id = identifier();
                        }
                        stmt.addSpatialKey(foreignKeyDefinition(symbol, id));
                    }
                    break;
                default:
                    throw new SQLSyntaxErrorException("unsupported column definition");
            }
            if (needMatchComma) {
                symbol = null;
            }
        }
        match(Token.PUNC_RIGHT_PAREN);
    }

    private boolean tableOptions(TableOptions options) throws SQLSyntaxErrorException {
        boolean matched = false;
        for (int i = 0; ; ++i) {
            boolean comma = false;
            if (i > 0 && lexer.token() == Token.PUNC_COMMA) {
                lexer.nextToken();
                comma = true;
            }
            if (!tableOption(options)) {
                if (comma) {
                    lexer.addCacheToken(Token.PUNC_COMMA);
                }
                break;
            } else {
                matched = true;
            }
        }
        return matched;
    }

    private boolean tableOption(TableOptions options) throws SQLSyntaxErrorException {
        Identifier id = null;
        Expression expr = null;
        os:
        switch (lexer.token()) {
            case Token.KW_CHARACTER:
                lexer.nextToken();
                match(Token.KW_SET);
                if (lexer.token() == Token.OP_EQUALS) {
                    lexer.nextToken();
                }
                id = identifier();
                options.setCharset(id);
                break;
            case Token.KW_COLLATE:
                lexer.nextToken();
                if (lexer.token() == Token.OP_EQUALS) {
                    lexer.nextToken();
                }
                id = identifier();
                options.setCollate(id);
                break;
            case Token.KW_DEFAULT:
                // | [DEFAULT] CHARSET [=] charset_name { MySQL 5.1 legacy}
                // | [DEFAULT] CHARACTER SET [=] charset_name
                // | [DEFAULT] COLLATE [=] collation_name
                switch (lexer.nextToken()) {
                    case Token.KW_CHARACTER:
                        lexer.nextToken();
                        match(Token.KW_SET);
                        if (lexer.token() == Token.OP_EQUALS) {
                            lexer.nextToken();
                        }
                        id = identifier();
                        options.setCharset(id);
                        break os;
                    case Token.KW_COLLATE:
                        lexer.nextToken();
                        if (lexer.token() == Token.OP_EQUALS) {
                            lexer.nextToken();
                        }
                        id = identifier();
                        options.setCollate(id);
                        break os;
                    case Token.IDENTIFIER:
                        if (lexer.parseKeyword() == Keywords.CHARSET) {
                            lexer.nextToken();
                            if (lexer.token() == Token.OP_EQUALS) {
                                lexer.nextToken();
                            }
                            id = identifier();
                            options.setCharset(id);
                            break os;
                        }
                    default:
                        lexer.addCacheToken(Token.KW_DEFAULT);
                        return false;
                }
            case Token.KW_INDEX:
                // | INDEX DIRECTORY [=] 'absolute path to directory'
                lexer.nextToken();
                if (lexer.token() == Token.IDENTIFIER && equalsKeyword(Keywords.DIRECTORY)) {
                    if (lexer.nextToken() == Token.OP_EQUALS) {
                        lexer.nextToken();
                    }
                    options.setIndexDirectory((LiteralString)exprParser.expression());
                    break;
                }
                // lexer.addCacheToke(KW_INDEX);
                return true;
            case Token.KW_UNION:
                // | UNION [=] (tbl_name[,tbl_name]...)
                if (lexer.nextToken() == Token.OP_EQUALS) {
                    lexer.nextToken();
                }
                match(Token.PUNC_LEFT_PAREN);
                List<Identifier> union = new ArrayList<Identifier>(2);
                for (int j = 0; lexer.token() != Token.PUNC_RIGHT_PAREN; ++j) {
                    if (j > 0)
                        match(Token.PUNC_COMMA);
                    id = identifier();
                    union.add(id);
                }
                match(Token.PUNC_RIGHT_PAREN);
                options.setUnion(union);
                break os;
            case Token.IDENTIFIER:
                switch (lexer.parseKeyword()) {
                    case Keywords.CHARSET:
                        // CHARSET [=] charset_name
                        lexer.nextToken();
                        if (lexer.token() == Token.OP_EQUALS) {
                            lexer.nextToken();
                        }
                        id = identifier();
                        options.setCharset(id);
                        break os;
                    case Keywords.ENGINE:
                        // ENGINE [=] engine_name
                        if (lexer.nextToken() == Token.OP_EQUALS) {
                            lexer.nextToken();
                        }
                        id = identifier();
                        options.setEngine(id);
                        break os;
                    case Keywords.AUTO_INCREMENT:
                        // | AUTO_INCREMENT [=] value
                        if (lexer.nextToken() == Token.OP_EQUALS) {
                            lexer.nextToken();
                        }
                        expr = exprParser.expression();
                        options.setAutoIncrement(expr);
                        break os;
                    case Keywords.AVG_ROW_LENGTH:
                        // | AVG_ROW_LENGTH [=] value
                        if (lexer.nextToken() == Token.OP_EQUALS) {
                            lexer.nextToken();
                        }
                        expr = exprParser.expression();
                        options.setAvgRowLength(expr);
                        break os;
                    case Keywords.CHECKSUM:
                        // | CHECKSUM [=] {0 | 1}
                        if (lexer.nextToken() == Token.OP_EQUALS) {
                            lexer.nextToken();
                        }
                        switch (lexer.token()) {
                            case Token.LITERAL_BOOL_FALSE:
                                lexer.nextToken();
                                options.setChecksum(TableOptions._0);
                            case Token.LITERAL_BOOL_TRUE:
                                lexer.nextToken();
                                options.setChecksum(TableOptions._1);
                                break;
                            case Token.LITERAL_NUM_PURE_DIGIT:
                                int intVal = lexer.integerValue().intValue();
                                lexer.nextToken();
                                if (intVal == 0) {
                                    options.setChecksum(TableOptions._0);
                                } else {
                                    options.setChecksum(TableOptions._1);
                                }
                                break;
                            default:
                                throw new SQLSyntaxErrorException("table option of CHECKSUM error");
                        }
                        break os;
                    case Keywords.DELAY_KEY_WRITE:
                        // | DELAY_KEY_WRITE [=] {0 | 1}
                        if (lexer.nextToken() == Token.OP_EQUALS) {
                            lexer.nextToken();
                        }
                        switch (lexer.token()) {
                            case Token.LITERAL_BOOL_FALSE:
                                lexer.nextToken();
                                options.setDelayKeyWrite(TableOptions._0);
                            case Token.LITERAL_BOOL_TRUE:
                                lexer.nextToken();
                                options.setDelayKeyWrite(TableOptions._1);
                                break;
                            case Token.LITERAL_NUM_PURE_DIGIT:
                                int intVal = lexer.integerValue().intValue();
                                lexer.nextToken();
                                if (intVal == 0) {
                                    options.setDelayKeyWrite(TableOptions._0);
                                } else {
                                    options.setDelayKeyWrite(TableOptions._1);
                                }
                                break;
                            default:
                                throw new SQLSyntaxErrorException("table option of DELAY_KEY_WRITE error");
                        }
                        break os;
                    case Keywords.COMMENT: {
                        // | COMMENT [=] 'string'
                        try {
                            if (lexer.nextToken() == Token.OP_EQUALS) {
                                lexer.nextToken();
                            }
                            options.setComment(exprParser.parseString());
                        } catch (SQLSyntaxErrorException e) {
                            if (e.getMessage() != null && e.getMessage().startsWith("unclosed string")) {
                                // 在字符集错误的情况下，DDL的COMMENT中可能出现该异常，为兼容该情况，需要保持兼容，并在输出的时候将引号补全
                                lexer.toTheEnd();
                                byte[] dataUnclosed = BytesUtil.getValue(lexer.getSQL(), lexer.tokenInfo());
                                byte[] data = new byte[dataUnclosed.length - 1];
                                System.arraycopy(dataUnclosed, 1, data, 0, data.length);
                                LiteralString comment = new LiteralString(null, data, false);
                                options.setComment(comment);
                                break os;
                            }
                            throw e;
                        }
                        break os;
                    }
                    case Keywords.CONNECTION:
                        // | CONNECTION [=] 'connect_string'
                        if (lexer.nextToken() == Token.OP_EQUALS) {
                            lexer.nextToken();
                        }
                        options.setConnection((LiteralString)exprParser.expression());
                        break os;
                    case Keywords.DATA:
                        // | DATA DIRECTORY [=] 'absolute path to directory'
                        lexer.nextToken();
                        matchKeywords(Keywords.DIRECTORY);
                        if (lexer.token() == Token.OP_EQUALS) {
                            lexer.nextToken();
                        }
                        options.setDataDirectory((LiteralString)exprParser.expression());
                        break os;
                    case Keywords.INSERT_METHOD:
                        // | INSERT_METHOD [=] { NO | FIRST | LAST }
                        if (lexer.nextToken() == Token.OP_EQUALS) {
                            lexer.nextToken();
                        }
                        if (lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.NO) {
                            options.setInsertMethod(TableOptions.INSERT_METHOD_NO);
                            lexer.nextToken();
                        } else {
                            switch (matchKeywords(Keywords.FIRST, Keywords.LAST)) {
                                case 0:
                                    options.setInsertMethod(TableOptions.INSERT_METHOD_FIRST);
                                    break;
                                case 1:
                                    options.setInsertMethod(TableOptions.INSERT_METHOD_LAST);
                                    break;
                            }
                        }
                        break os;
                    case Keywords.KEY_BLOCK_SIZE:
                        // | KEY_BLOCK_SIZE [=] value
                        if (lexer.nextToken() == Token.OP_EQUALS) {
                            lexer.nextToken();
                        }
                        options.setKeyBlockSize(exprParser.expression());
                        break os;
                    case Keywords.MAX_ROWS:
                        // | MAX_ROWS [=] value
                        if (lexer.nextToken() == Token.OP_EQUALS) {
                            lexer.nextToken();
                        }
                        options.setMaxRows(exprParser.longValue());
                        break os;
                    case Keywords.MIN_ROWS:
                        // | MIN_ROWS [=] value
                        if (lexer.nextToken() == Token.OP_EQUALS) {
                            lexer.nextToken();
                        }
                        options.setMinRows(exprParser.longValue());
                        break os;
                    case Keywords.PACK_KEYS:
                        // | PACK_KEYS [=] {0 | 1 | DEFAULT}
                        if (lexer.nextToken() == Token.OP_EQUALS) {
                            lexer.nextToken();
                        }
                        switch (lexer.token()) {
                            case Token.LITERAL_BOOL_FALSE:
                                lexer.nextToken();
                                options.setPackKeys(TableOptions._0);
                                break;
                            case Token.LITERAL_BOOL_TRUE:
                                lexer.nextToken();
                                options.setPackKeys(TableOptions._1);
                                break;
                            case Token.LITERAL_NUM_PURE_DIGIT:
                                int intVal = lexer.integerValue().intValue();
                                lexer.nextToken();
                                if (intVal == 0) {
                                    options.setPackKeys(TableOptions._0);
                                } else {
                                    options.setPackKeys(TableOptions._1);
                                }
                                break;
                            case Token.KW_DEFAULT:
                                lexer.nextToken();
                                options.setPackKeys(TableOptions._DEFAULT);
                                break;
                            default:
                                throw new SQLSyntaxErrorException("table option of PACK_KEYS error");
                        }
                        break os;
                    case Keywords.PASSWORD:
                        // | PASSWORD [=] 'string'
                        if (lexer.nextToken() == Token.OP_EQUALS) {
                            lexer.nextToken();
                        }
                        options.setPassword((LiteralString)exprParser.expression());
                        break os;
                    case Keywords.COMPRESSION:
                        // | COMPRESSION [=] {'ZLIB'|'LZ4'|'NONE'}
                        if (lexer.nextToken() == Token.OP_EQUALS) {
                            lexer.nextToken();
                        }
                        LiteralString string = exprParser.parseString();
                        if (string != null) {
                            options.setCompression(string);
                            // options.setCompression(
                            // Compression.valueOf(string.getUnescapedString(true)));
                        }
                        break os;
                    case Keywords.ENCRYPTION:
                        // | ENCRYPTION [=] {'Y' | 'N'}
                        if (lexer.nextToken() == Token.OP_EQUALS) {
                            lexer.nextToken();
                        }
                        string = exprParser.parseString();
                        if (string != null) {
                            byte[] result = BytesUtil.getValue(lexer.getSQL(), string.getString());
                            if (result.length == 3) {
                                byte v = result[1];
                                if (v == 'Y' || v == 'y') {
                                    options.setEncryption(true);
                                } else if (v == 'N' || v == 'n') {
                                    options.setEncryption(false);
                                } else {
                                    throw new SQLSyntaxErrorException("unexpected DDL SQL!");
                                }
                            } else {
                                throw new SQLSyntaxErrorException("unexpected DDL SQL!");
                            }
                        }
                        break os;
                    case Keywords.ROW_FORMAT:
                        // | ROW_FORMAT [=]
                        // {DEFAULT|DYNAMIC|FIXED|COMPRESSED|REDUNDANT|COMPACT}
                        if (lexer.nextToken() == Token.OP_EQUALS) {
                            lexer.nextToken();
                        }
                        switch (lexer.token()) {
                            case Token.KW_DEFAULT:
                                lexer.nextToken();
                                options.setRowFormat(TableOptions.ROW_FORMAT_DEFAULT);
                                break os;
                            case Token.IDENTIFIER:
                                switch (lexer.parseKeyword()) {
                                    case Keywords.DYNAMIC:
                                        lexer.nextToken();
                                        options.setRowFormat(TableOptions.ROW_FORMAT_DYNAMIC);
                                        break os;
                                    case Keywords.FIXED:
                                        lexer.nextToken();
                                        options.setRowFormat(TableOptions.ROW_FORMAT_FIXED);
                                        break os;
                                    case Keywords.COMPRESSED:
                                        lexer.nextToken();
                                        options.setRowFormat(TableOptions.ROW_FORMAT_COMPRESSED);
                                        break os;
                                    case Keywords.REDUNDANT:
                                        lexer.nextToken();
                                        options.setRowFormat(TableOptions.ROW_FORMAT_REDUNDANT);
                                        break os;
                                    case Keywords.COMPACT:
                                        lexer.nextToken();
                                        options.setRowFormat(TableOptions.ROW_FORMAT_COMPACT);
                                        break os;
                                }
                            default:
                                throw new SQLSyntaxErrorException("table option of ROW_FORMAT error");
                        }
                    case Keywords.STATS_AUTO_RECALC:
                        if (lexer.nextToken() == Token.OP_EQUALS) {
                            lexer.nextToken();
                        }
                        switch (lexer.token()) {
                            case Token.LITERAL_BOOL_FALSE:
                                lexer.nextToken();
                                options.setStatsAutoRecalc(TableOptions._0);
                                break;
                            case Token.LITERAL_BOOL_TRUE:
                                lexer.nextToken();
                                options.setStatsAutoRecalc(TableOptions._1);
                                break;
                            case Token.LITERAL_NUM_PURE_DIGIT:
                                int intVal = lexer.integerValue().intValue();
                                lexer.nextToken();
                                if (intVal == 0) {
                                    options.setStatsAutoRecalc(TableOptions._0);
                                } else {
                                    options.setStatsAutoRecalc(TableOptions._1);
                                }
                                break;
                            case Token.KW_DEFAULT:
                                lexer.nextToken();
                                options.setStatsAutoRecalc(TableOptions._DEFAULT);
                                break;
                            default:
                                throw new SQLSyntaxErrorException("table option of STATS_AUTO_RECALC error");
                        }
                        break os;
                    case Keywords.STATS_PERSISTENT:
                        if (lexer.nextToken() == Token.OP_EQUALS) {
                            lexer.nextToken();
                        }
                        switch (lexer.token()) {
                            case Token.LITERAL_BOOL_FALSE:
                                lexer.nextToken();
                                options.setStatsPresistent(TableOptions._0);
                                break;
                            case Token.LITERAL_BOOL_TRUE:
                                lexer.nextToken();
                                options.setStatsPresistent(TableOptions._1);
                                break;
                            case Token.LITERAL_NUM_PURE_DIGIT:
                                int intVal = lexer.integerValue().intValue();
                                lexer.nextToken();
                                if (intVal == 0) {
                                    options.setStatsPresistent(TableOptions._0);
                                } else {
                                    options.setStatsPresistent(TableOptions._1);
                                }
                                break;
                            case Token.KW_DEFAULT:
                                lexer.nextToken();
                                options.setStatsPresistent(TableOptions._DEFAULT);
                                break;
                            default:
                                throw new SQLSyntaxErrorException("table option of STATS_PERSISTENT error");
                        }
                        break os;
                    case Keywords.STATS_SAMPLE_PAGES:
                        if (lexer.nextToken() == Token.OP_EQUALS) {
                            lexer.nextToken();
                        }
                        options.setStatSamplePages(exprParser.expression());
                        break os;
                    case Keywords.TABLESPACE:
                        lexer.nextToken();
                        options.setTablespace(identifier());
                        if (lexer.token() == Token.IDENTIFIER) {
                            int key = lexer.parseKeyword();
                            if (key == Keywords.STORAGE) {
                                lexer.nextToken();
                                if (lexer.token() == Token.IDENTIFIER) {
                                    key = lexer.parseKeyword();
                                    if (key == Keywords.DISK) {
                                        options.setStorage(1);
                                    } else if (key == Keywords.MEMORY) {
                                        options.setStorage(2);
                                    } else {
                                        throw new SQLSyntaxErrorException("unexpected SQL!");
                                    }
                                    lexer.nextToken();
                                } else {
                                    throw new SQLSyntaxErrorException("unexpected SQL!");
                                }
                            }
                        }
                        break os;
                }
            default:
                return false;
        }
        return true;
    }

    private Tuple3<Integer, Identifier, DataType> getProcParameter() throws SQLSyntaxErrorException {
        Integer ProParameterType = null;
        if (lexer.token() == Token.KW_IN) {
            ProParameterType = DDLCreateProcedureStatement.IN;
            lexer.nextToken();
        } else if (lexer.token() == Token.KW_OUT) {
            ProParameterType = DDLCreateProcedureStatement.OUT;
            lexer.nextToken();
        } else if (lexer.token() == Token.KW_INOUT) {
            ProParameterType = DDLCreateProcedureStatement.INOUT;
            lexer.nextToken();
        }
        Identifier param = identifier();
        DataType dataType = dataType();
        return new Tuple3<Integer, Identifier, DataType>(ProParameterType, param, dataType);
    }

    public DataType dataType() throws SQLSyntaxErrorException {
        int typeName = 0;
        Integer length = null;
        Integer decimals = null;
        Identifier charSet = null;
        Identifier collation = null;
        List<LiteralString> collectionVals = null;
        unsigned = false;
        zerofill = false;
        binary = false;
        typeName:
        switch (lexer.token()) {
            case Token.KW_INT1:
            case Token.KW_TINYINT:
                // | TINYINT[(length)] [UNSIGNED] [ZEROFILL]
                typeName = DataType.TINYINT;
                if (lexer.nextToken() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    length = exprParser.intValue();
                    match(Token.PUNC_RIGHT_PAREN);
                }
                matchSignedUnsignedZerofill(lexer);
                break typeName;
            case Token.KW_INT2:
            case Token.KW_SMALLINT:
                // | SMALLINT[(length)] [UNSIGNED] [ZEROFILL]
                typeName = DataType.SMALLINT;
                if (lexer.nextToken() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    length = exprParser.intValue();
                    match(Token.PUNC_RIGHT_PAREN);
                }
                matchSignedUnsignedZerofill(lexer);
                break typeName;
            case Token.KW_MEDIUMINT:
                // | MEDIUMINT[(length)] [UNSIGNED] [ZEROFILL]
                typeName = DataType.MEDIUMINT;
                if (lexer.nextToken() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    length = exprParser.intValue();
                    match(Token.PUNC_RIGHT_PAREN);
                }
                matchSignedUnsignedZerofill(lexer);
                break typeName;
            case Token.KW_INT4:
            case Token.KW_INTEGER:
            case Token.KW_INT:
                // | INT[(length)] [UNSIGNED] [ZEROFILL]
                // | INTEGER[(length)] [UNSIGNED] [ZEROFILL]
                typeName = DataType.INT;
                if (lexer.nextToken() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    length = exprParser.intValue();
                    match(Token.PUNC_RIGHT_PAREN);
                }
                matchSignedUnsignedZerofill(lexer);
                break typeName;
            case Token.KW_INT8:
            case Token.KW_BIGINT:
                // | BIGINT[(length)] [UNSIGNED] [ZEROFILL]
                typeName = DataType.BIGINT;
                if (lexer.nextToken() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    length = exprParser.intValue();
                    match(Token.PUNC_RIGHT_PAREN);
                }
                matchSignedUnsignedZerofill(lexer);
                break typeName;
            case Token.KW_REAL:
                // | REAL[(length,decimals)] [UNSIGNED] [ZEROFILL]
                typeName = DataType.REAL;
                if (lexer.nextToken() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    length = exprParser.intValue();
                    match(Token.PUNC_COMMA);
                    decimals = exprParser.intValue();
                    match(Token.PUNC_RIGHT_PAREN);
                }
                matchSignedUnsignedZerofill(lexer);
                break typeName;
            case Token.KW_DOUBLE:
                // | DOUBLE[(length,decimals)] [UNSIGNED] [ZEROFILL]
                typeName = DataType.DOUBLE;
                lexer.nextToken();
                if (lexer.token() == Token.KW_PRECISION) {
                    lexer.nextToken();
                }
                if (lexer.token() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    length = exprParser.intValue();
                    match(Token.PUNC_COMMA);
                    decimals = exprParser.intValue();
                    match(Token.PUNC_RIGHT_PAREN);
                }
                matchSignedUnsignedZerofill(lexer);
                break typeName;
            case Token.KW_FLOAT:
                // | FLOAT[(length,decimals)] [UNSIGNED] [ZEROFILL]
                typeName = DataType.FLOAT;
                if (lexer.nextToken() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    length = exprParser.intValue();
                    if (lexer.token() == Token.PUNC_COMMA) {
                        lexer.nextToken();
                        decimals = exprParser.intValue();
                    }
                    match(Token.PUNC_RIGHT_PAREN);
                }
                matchSignedUnsignedZerofill(lexer);
                break typeName;
            case Token.KW_NUMERIC:
            case Token.KW_DECIMAL:
            case Token.KW_DEC:
                // | DECIMAL[(length[,decimals])] [UNSIGNED] [ZEROFILL]
                // | NUMERIC[(length[,decimals])] [UNSIGNED] [ZEROFILL]
                typeName = DataType.DECIMAL;
                if (lexer.nextToken() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    length = exprParser.intValue();
                    if (lexer.token() == Token.PUNC_COMMA) {
                        match(Token.PUNC_COMMA);
                        decimals = exprParser.intValue();
                    }
                    match(Token.PUNC_RIGHT_PAREN);
                }
                matchSignedUnsignedZerofill(lexer);
                break typeName;
            case Token.KW_CHAR:
                // | CHAR[(length)] [CHARACTER SET charset_name] [COLLATE
                // collation_name]
                typeName = DataType.CHAR;
                if (lexer.nextToken() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    length = exprParser.intValue();
                    match(Token.PUNC_RIGHT_PAREN);
                }
                if (lexer.token() == Token.KW_BINARY) {
                    lexer.nextToken();
                    binary = true;
                }
                if (lexer.token() == Token.KW_CHARACTER) {
                    lexer.nextToken();
                    match(Token.KW_SET);
                    charSet = identifier();
                } else if (lexer.token() == Token.IDENTIFIER && equalsKeyword(Keywords.CHARSET)) {
                    lexer.nextToken();
                    charSet = identifier();
                }
                if (lexer.token() == Token.KW_COLLATE) {
                    lexer.nextToken();
                    collation = identifier();
                }
                break typeName;
            case Token.KW_VARCHAR:
                // | VARCHAR(length) [CHARACTER SET charset_name] [COLLATE
                // collation_name]
                typeName = DataType.VARCHAR;
                lexer.nextToken();
                match(Token.PUNC_LEFT_PAREN);
                length = exprParser.intValue();
                match(Token.PUNC_RIGHT_PAREN);
                if (lexer.token() == Token.KW_BINARY) {
                    lexer.nextToken();
                    binary = true;
                }
                if (lexer.token() == Token.KW_CHARACTER) {
                    lexer.nextToken();
                    match(Token.KW_SET);
                    charSet = identifier();
                } else if (lexer.token() == Token.IDENTIFIER && equalsKeyword(Keywords.CHARSET)) {
                    lexer.nextToken();
                    charSet = identifier();
                }
                if (lexer.token() == Token.KW_COLLATE) {
                    lexer.nextToken();
                    collation = identifier();
                }
                break typeName;
            case Token.KW_BINARY:
                // | BINARY[(length)]
                typeName = DataType.BINARY;
                if (lexer.nextToken() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    length = exprParser.intValue();
                    match(Token.PUNC_RIGHT_PAREN);
                }
                break typeName;
            case Token.KW_VARBINARY:
                // | VARBINARY(length)
                typeName = DataType.VARBINARY;
                lexer.nextToken();
                match(Token.PUNC_LEFT_PAREN);
                length = exprParser.intValue();
                match(Token.PUNC_RIGHT_PAREN);
                break typeName;
            case Token.KW_TINYBLOB:
                typeName = DataType.TINYBLOB;
                if (lexer.nextToken() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    length = exprParser.intValue();
                    match(Token.PUNC_RIGHT_PAREN);
                }
                break typeName;
            case Token.KW_BLOB:
                typeName = DataType.BLOB;
                if (lexer.nextToken() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    length = exprParser.intValue();
                    match(Token.PUNC_RIGHT_PAREN);
                }
                break typeName;
            case Token.KW_MEDIUMBLOB:
                typeName = DataType.MEDIUMBLOB;
                if (lexer.nextToken() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    length = exprParser.intValue();
                    match(Token.PUNC_RIGHT_PAREN);
                }
                break typeName;
            case Token.KW_LONGBLOB:
                typeName = DataType.LONGBLOB;
                if (lexer.nextToken() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    length = exprParser.intValue();
                    match(Token.PUNC_RIGHT_PAREN);
                }
                break typeName;
            case Token.KW_TINYTEXT:
                // | TINYTEXT [BINARY] [CHARACTER SET charset_name] [COLLATE
                // collation_name]
                typeName = DataType.TINYTEXT;
                if (lexer.nextToken() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    length = exprParser.intValue();
                    match(Token.PUNC_RIGHT_PAREN);
                }
                if (lexer.token() == Token.KW_BINARY) {
                    lexer.nextToken();
                    binary = true;
                }
                if (lexer.token() == Token.KW_CHARACTER) {
                    lexer.nextToken();
                    match(Token.KW_SET);
                    charSet = identifier();
                } else if (lexer.token() == Token.IDENTIFIER && equalsKeyword(Keywords.CHARSET)) {
                    lexer.nextToken();
                    charSet = identifier();
                }
                if (lexer.token() == Token.KW_COLLATE) {
                    lexer.nextToken();
                    collation = identifier();
                }
                break typeName;
            case Token.KW_MEDIUMTEXT:
                // | MEDIUMTEXT [BINARY] [CHARACTER SET charset_name] [COLLATE
                // collation_name]
                typeName = DataType.MEDIUMTEXT;
                if (lexer.nextToken() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    length = exprParser.intValue();
                    match(Token.PUNC_RIGHT_PAREN);
                }
                if (lexer.token() == Token.KW_BINARY) {
                    lexer.nextToken();
                    binary = true;
                }
                if (lexer.token() == Token.KW_CHARACTER) {
                    lexer.nextToken();
                    match(Token.KW_SET);
                    charSet = identifier();
                } else if (lexer.token() == Token.IDENTIFIER && equalsKeyword(Keywords.CHARSET)) {
                    lexer.nextToken();
                    charSet = identifier();
                }
                if (lexer.token() == Token.KW_COLLATE) {
                    lexer.nextToken();
                    collation = identifier();
                }
                break typeName;
            case Token.KW_LONG:
                typeName = DataType.MEDIUMTEXT;
                if (lexer.nextToken() == Token.KW_VARCHAR) {
                    lexer.nextToken();
                }
                if (lexer.token() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    length = exprParser.intValue();
                    match(Token.PUNC_RIGHT_PAREN);
                }
                if (lexer.token() == Token.KW_BINARY) {
                    lexer.nextToken();
                    binary = true;
                }
                if (lexer.token() == Token.KW_CHARACTER) {
                    lexer.nextToken();
                    match(Token.KW_SET);
                    charSet = identifier();
                } else if (lexer.token() == Token.IDENTIFIER && equalsKeyword(Keywords.CHARSET)) {
                    lexer.nextToken();
                    charSet = identifier();
                }
                if (lexer.token() == Token.KW_COLLATE) {
                    lexer.nextToken();
                    collation = identifier();
                }
                break typeName;
            case Token.KW_LONGTEXT:
                // | LONGTEXT [BINARY] [CHARACTER SET charset_name] [COLLATE
                // collation_name]
                typeName = DataType.LONGTEXT;
                if (lexer.nextToken() == Token.PUNC_LEFT_PAREN) {
                    lexer.nextToken();
                    length = exprParser.intValue();
                    match(Token.PUNC_RIGHT_PAREN);
                }
                if (lexer.token() == Token.KW_BINARY) {
                    lexer.nextToken();
                    binary = true;
                }
                if (lexer.token() == Token.KW_CHARACTER) {
                    lexer.nextToken();
                    match(Token.KW_SET);
                    charSet = identifier();
                } else if (lexer.token() == Token.IDENTIFIER && equalsKeyword(Keywords.CHARSET)) {
                    lexer.nextToken();
                    charSet = identifier();
                }
                if (lexer.token() == Token.KW_COLLATE) {
                    lexer.nextToken();
                    collation = identifier();
                }
                break typeName;
            case Token.KW_SET:
                // | SET(value1,value2,value3,...) [CHARACTER SET charset_name]
                // [COLLATE collation_name]
                typeName = DataType.SET;
                lexer.nextToken();
                match(Token.PUNC_LEFT_PAREN);
                for (int i = 0; lexer.token() != Token.PUNC_RIGHT_PAREN; ++i) {
                    if (i > 0) {
                        match(Token.PUNC_COMMA);
                    } else {
                        collectionVals = new ArrayList<LiteralString>(2);
                    }
                    collectionVals.add(exprParser.parseString());
                }
                match(Token.PUNC_RIGHT_PAREN);
                if (lexer.token() == Token.KW_CHARACTER) {
                    lexer.nextToken();
                    match(Token.KW_SET);
                    charSet = identifier();
                } else if (lexer.token() == Token.IDENTIFIER && equalsKeyword(Keywords.CHARSET)) {
                    lexer.nextToken();
                    charSet = identifier();
                }
                if (lexer.token() == Token.KW_COLLATE) {
                    lexer.nextToken();
                    collation = identifier();
                }
                break typeName;
            case Token.IDENTIFIER:
                switch (lexer.parseKeyword()) {
                    case Keywords.SERIAL:
                        typeName = DataType.SERIAL;
                        lexer.nextToken();
                        break typeName;
                    case Keywords.BIT:
                        // BIT[(length)]
                        typeName = DataType.BIT;
                        if (lexer.nextToken() == Token.PUNC_LEFT_PAREN) {
                            lexer.nextToken();
                            length = exprParser.intValue();
                            match(Token.PUNC_RIGHT_PAREN);
                        }
                        break typeName;
                    case Keywords.BOOL:
                        typeName = DataType.BOOL;
                        lexer.nextToken();
                        break typeName;
                    case Keywords.BOOLEAN:
                        typeName = DataType.BOOLEAN;
                        lexer.nextToken();
                        break typeName;
                    case Keywords.DATE:
                        typeName = DataType.DATE;
                        lexer.nextToken();
                        break typeName;
                    case Keywords.FIXED:
                        typeName = DataType.FIXED;
                        lexer.nextToken();
                        break typeName;
                    case Keywords.TIME:
                        typeName = DataType.TIME;
                        if (lexer.nextToken() == Token.PUNC_LEFT_PAREN) {
                            lexer.nextToken();
                            length = exprParser.intValue();
                            match(Token.PUNC_RIGHT_PAREN);
                        }
                        break typeName;
                    case Keywords.TIMESTAMP:
                        typeName = DataType.TIMESTAMP;
                        if (lexer.nextToken() == Token.PUNC_LEFT_PAREN) {
                            lexer.nextToken();
                            length = exprParser.intValue();
                            match(Token.PUNC_RIGHT_PAREN);
                        }
                        break typeName;
                    case Keywords.DATETIME:
                        typeName = DataType.DATETIME;
                        if (lexer.nextToken() == Token.PUNC_LEFT_PAREN) {
                            lexer.nextToken();
                            length = exprParser.intValue();
                            match(Token.PUNC_RIGHT_PAREN);
                        }
                        break typeName;
                    case Keywords.YEAR:
                        typeName = DataType.YEAR;
                        if (lexer.nextToken() == Token.PUNC_LEFT_PAREN) {
                            lexer.nextToken();
                            length = exprParser.intValue();
                            match(Token.PUNC_RIGHT_PAREN);
                        }
                        break typeName;
                    case Keywords.TEXT:
                        // | TEXT [BINARY] [CHARACTER SET charset_name] [COLLATE
                        // collation_name]
                        typeName = DataType.TEXT;
                        if (lexer.nextToken() == Token.PUNC_LEFT_PAREN) {
                            lexer.nextToken();
                            length = exprParser.intValue();
                            match(Token.PUNC_RIGHT_PAREN);
                        }
                        if (lexer.token() == Token.KW_BINARY) {
                            lexer.nextToken();
                            binary = true;
                        }
                        if (lexer.token() == Token.KW_CHARACTER) {
                            lexer.nextToken();
                            match(Token.KW_SET);
                            charSet = identifier();
                        } else if (lexer.token() == Token.IDENTIFIER && equalsKeyword(Keywords.CHARSET)) {
                            lexer.nextToken();
                            charSet = identifier();
                        }
                        if (lexer.token() == Token.KW_COLLATE) {
                            lexer.nextToken();
                            collation = identifier();
                        }
                        break typeName;
                    case Keywords.ENUM:
                        // | ENUM(value1,value2,value3,...) [CHARACTER SET
                        // charset_name] [COLLATE collation_name]
                        typeName = DataType.ENUM;
                        lexer.nextToken();
                        match(Token.PUNC_LEFT_PAREN);
                        for (int i = 0; lexer.token() != Token.PUNC_RIGHT_PAREN; ++i) {
                            if (i > 0) {
                                match(Token.PUNC_COMMA);
                            } else {
                                collectionVals = new ArrayList<LiteralString>(2);
                            }
                            collectionVals.add(exprParser.parseString());
                        }
                        match(Token.PUNC_RIGHT_PAREN);
                        if (lexer.token() == Token.KW_CHARACTER) {
                            lexer.nextToken();
                            match(Token.KW_SET);
                            charSet = identifier();
                        } else if (lexer.token() == Token.IDENTIFIER && equalsKeyword(Keywords.CHARSET)) {
                            lexer.nextToken();
                            charSet = identifier();
                        }
                        if (lexer.token() == Token.KW_COLLATE) {
                            lexer.nextToken();
                            collation = identifier();
                        }
                        break typeName;
                    case Keywords.GEOMETRY: {
                        typeName = DataType.GEOMETRY;
                        lexer.nextToken();
                        break typeName;
                    }
                    case Keywords.POINT: {
                        typeName = DataType.POINT;
                        lexer.nextToken();
                        break typeName;
                    }
                    case Keywords.LINESTRING:
                        typeName = DataType.LINESTRING;
                        lexer.nextToken();
                        break typeName;
                    case Keywords.POLYGON:
                        typeName = DataType.POLYGON;
                        lexer.nextToken();
                        break typeName;
                    case Keywords.MULTIPOINT:
                        typeName = DataType.MULTIPOINT;
                        lexer.nextToken();
                        break typeName;
                    case Keywords.MULTILINESTRING:
                        typeName = DataType.MULTILINESTRING;
                        lexer.nextToken();
                        break typeName;
                    case Keywords.GEOMETRYCOLLECTION:
                        typeName = DataType.GEOMETRYCOLLECTION;
                        lexer.nextToken();
                        break typeName;
                    case Keywords.MULTIPOLYGON:
                        typeName = DataType.MULTIPOLYGON;
                        lexer.nextToken();
                        break typeName;
                    case Keywords.JSON:
                        typeName = DataType.JSON;
                        lexer.nextToken();
                        break typeName;
                }
            default:
                return null;
        }
        return new DataType(typeName, unsigned, zerofill, binary, length, decimals, charSet, collation, collectionVals);
    }

    private void matchSignedUnsignedZerofill(Lexer lexer) throws SQLSyntaxErrorException {
        while (possibleTokens.contains(lexer.token())) {
            if (lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.SIGNED) {
                unsigned = false;
                lexer.nextToken();
            } else if (lexer.token() == Token.KW_UNSIGNED) {
                unsigned = true;
                lexer.nextToken();
            } else if (lexer.token() == Token.KW_ZEROFILL) {
                zerofill = true;
                lexer.nextToken();
            } else {
                return;
            }
        }
    }

    private Characteristic getCharacteristics() throws SQLSyntaxErrorException {
        int type = 0;
        LiteralString comment = null;
        switch (lexer.token()) {
            case Token.IDENTIFIER: {
                switch (lexer.parseKeyword()) {
                    case Keywords.COMMENT:
                        lexer.nextToken();
                        type = Characteristic.COMMENT;
                        comment = exprParser.parseString();
                        return new Characteristic(type, comment);
                    case Keywords.LANGUAGE:
                        lexer.nextToken();
                        match(Token.KW_SQL);
                        type = Characteristic.LANGUAGE_SQL;
                        return new Characteristic(type, comment);
                    case Keywords.CONTAINS:
                        lexer.nextToken();
                        match(Token.KW_SQL);
                        type = Characteristic.CONTAINS_SQL;
                        return new Characteristic(type, comment);
                    case Keywords.NO:
                        lexer.nextToken();
                        match(Token.KW_SQL);
                        type = Characteristic.NO_SQL;
                        return new Characteristic(type, comment);
                }
                break;
            }
            case Token.KW_NOT:
                lexer.nextToken();
                match(Token.KW_DETERMINISTIC);
                type = Characteristic.NOT_DETERMINISTIC;
                return new Characteristic(type, comment);
            case Token.KW_DETERMINISTIC:
                lexer.nextToken();
                type = Characteristic.DETERMINISTIC;
                return new Characteristic(type, comment);
            case Token.KW_READS:
                lexer.nextToken();
                match(Token.KW_SQL);
                matchKeywords(Keywords.DATA);
                type = Characteristic.READS_SQL_DATA;
                return new Characteristic(type, comment);
            case Token.KW_MODIFIES:
                lexer.nextToken();
                match(Token.KW_SQL);
                matchKeywords(Keywords.DATA);
                type = Characteristic.MODIFIES_SQL_DATA;
                return new Characteristic(type, comment);
            case Token.KW_SQL:
                lexer.nextToken();
                matchKeywords(Keywords.SECURITY);
                if (lexer.token() == Token.IDENTIFIER) {
                    int key = lexer.parseKeyword();
                    if (key == Keywords.DEFINER) {
                        type = Characteristic.SQL_SECURITY_DEFINER;
                        lexer.nextToken();
                    } else if (key == Keywords.INVOKER) {
                        type = Characteristic.SQL_SECURITY_INVOKER;
                        lexer.nextToken();
                    } else {
                        throw new SQLSyntaxErrorException("expect DEFINER | INVOKER");
                    }
                } else {
                    throw new SQLSyntaxErrorException("expect DEFINER | INVOKER");
                }
                return new Characteristic(type, comment);
        }
        return null;
    }

    private Pair<Identifier, DataType> getFuncParameter() throws SQLSyntaxErrorException {
        Identifier param = identifier();
        DataType dataType = dataType();
        return new Pair<Identifier, DataType>(param, dataType);
    }

    private ScheduleDefinition scheduleDefinition() throws SQLSyntaxErrorException {
        Expression atTimestamp = null;
        List<Pair<LiteralNumber, Integer>> intervalList = null;
        LiteralNumber everyInterval = null;
        Integer everyIntervalQuantity = null;
        Expression startsTimestamp = null;
        Expression endsTimestamp = null;
        List<Pair<LiteralNumber, Integer>> startsIntervalList = null;
        List<Pair<LiteralNumber, Integer>> endsIntervalList = null;
        if (lexer.token() == Token.IDENTIFIER) {
            if (lexer.parseKeyword() == Keywords.AT) {
                lexer.nextToken();
                atTimestamp = exprParser.expression();
                intervalList = new ArrayList<>();
                while (lexer.token() == Token.OP_PLUS) {
                    lexer.nextToken();
                    match(Token.KW_INTERVAL);
                    LiteralNumber interval = exprParser.parseNumber();
                    Integer intervalQuantity = intervalQuantity();
                    Pair<LiteralNumber, Integer> integerPair = new Pair<>(interval, intervalQuantity);
                    intervalList.add(integerPair);
                }
                return new ScheduleDefinition(atTimestamp, intervalList);
            } else {
                matchKeywords(Keywords.EVERY);
                everyInterval = exprParser.parseNumber();
                everyIntervalQuantity = intervalQuantity();
                if (lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.STARTS) {
                    lexer.nextToken();
                    startsTimestamp = exprParser.expression();
                    startsIntervalList = new ArrayList<>();
                    while (lexer.token() == Token.OP_PLUS) {
                        lexer.nextToken();
                        match(Token.KW_INTERVAL);
                        LiteralNumber startsInterval = exprParser.parseNumber();
                        Integer startsIntervalQuantity = intervalQuantity();
                        Pair<LiteralNumber, Integer> integerPair = new Pair<>(startsInterval, startsIntervalQuantity);
                        startsIntervalList.add(integerPair);
                    }
                }
                if (lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.ENDS) {
                    lexer.nextToken();
                    endsTimestamp = exprParser.expression();
                    endsIntervalList = new ArrayList<>();
                    while (lexer.token() == Token.OP_PLUS) {
                        lexer.nextToken();
                        match(Token.KW_INTERVAL);
                        LiteralNumber endsInterval = exprParser.parseNumber();
                        Integer endsIntervalQuantity = intervalQuantity();
                        Pair<LiteralNumber, Integer> integerPair = new Pair<>(endsInterval, endsIntervalQuantity);
                        endsIntervalList.add(integerPair);
                    }
                }
                return new ScheduleDefinition(everyInterval, everyIntervalQuantity, startsTimestamp, endsTimestamp,
                    startsIntervalList, endsIntervalList);
            }
        } else {
            throw new SQLSyntaxErrorException("unexpected DDL SQL!");
        }
    }

    protected Integer intervalQuantity() throws SQLSyntaxErrorException {
        Integer intervalQuantity = null;
        switch (lexer.token()) {
            case Token.KW_DAY_HOUR:
                intervalQuantity = IntervalUnit.DAY_HOUR;
                break;
            case Token.KW_DAY_MICROSECOND:
                intervalQuantity = IntervalUnit.DAY_MICROSECOND;
                break;
            case Token.KW_DAY_MINUTE:
                intervalQuantity = IntervalUnit.DAY_MINUTE;
                break;
            case Token.KW_DAY_SECOND:
                intervalQuantity = IntervalUnit.DAY_SECOND;
                break;
            case Token.KW_HOUR_MICROSECOND:
                intervalQuantity = IntervalUnit.HOUR_MICROSECOND;
                break;
            case Token.KW_HOUR_MINUTE:
                intervalQuantity = IntervalUnit.HOUR_MINUTE;
                break;
            case Token.KW_HOUR_SECOND:
                intervalQuantity = IntervalUnit.HOUR_SECOND;
                break;
            case Token.KW_MINUTE_MICROSECOND:
                intervalQuantity = IntervalUnit.MINUTE_MICROSECOND;
                break;
            case Token.KW_MINUTE_SECOND:
                intervalQuantity = IntervalUnit.MINUTE_SECOND;
                break;
            case Token.KW_SECOND_MICROSECOND:
                intervalQuantity = IntervalUnit.SECOND_MICROSECOND;
                break;
            case Token.KW_YEAR_MONTH:
                intervalQuantity = IntervalUnit.YEAR_MONTH;
                break;
            case Token.IDENTIFIER:
                switch (lexer.parseKeyword()) {
                    case Keywords.DAY:
                        intervalQuantity = IntervalUnit.DAY;
                        break;
                    case Keywords.HOUR:
                        intervalQuantity = IntervalUnit.HOUR;
                        break;
                    case Keywords.MICROSECOND:
                        intervalQuantity = IntervalUnit.MICROSECOND;
                        break;
                    case Keywords.MINUTE:
                        intervalQuantity = IntervalUnit.MINUTE;
                        break;
                    case Keywords.MONTH:
                        intervalQuantity = IntervalUnit.MONTH;
                        break;
                    case Keywords.QUARTER:
                        intervalQuantity = IntervalUnit.QUARTER;
                        break;
                    case Keywords.SECOND:
                        intervalQuantity = IntervalUnit.SECOND;
                        break;
                    case Keywords.WEEK:
                        intervalQuantity = IntervalUnit.WEEK;
                        break;
                    case Keywords.YEAR:
                        intervalQuantity = IntervalUnit.YEAR;
                        break;
                }
                break;
        }
        lexer.nextToken();
        return intervalQuantity;
    }

    private Pair<Integer, Literal> serverOption() throws SQLSyntaxErrorException {
        Integer optionType = null;
        Literal optionLiteral = null;
        switch (lexer.token()) {
            case Token.KW_DATABASE:
                lexer.nextToken();
                optionType = DDLCreateServerStatement.DATABASE;
                optionLiteral = exprParser.parseString();
                break;
            case Token.IDENTIFIER: {
                switch (lexer.parseKeyword()) {
                    case Keywords.HOST:
                        lexer.nextToken();
                        optionType = DDLCreateServerStatement.HOST;
                        optionLiteral = exprParser.parseString();
                        break;
                    case Keywords.USER:
                        lexer.nextToken();
                        optionType = DDLCreateServerStatement.USER;
                        optionLiteral = exprParser.parseString();
                        break;
                    case Keywords.PASSWORD:
                        lexer.nextToken();
                        optionType = DDLCreateServerStatement.PASSWORD;
                        optionLiteral = exprParser.parseString();
                        break;
                    case Keywords.SOCKET:
                        lexer.nextToken();
                        optionType = DDLCreateServerStatement.SOCKET;
                        optionLiteral = exprParser.parseString();
                        break;
                    case Keywords.OWNER:
                        lexer.nextToken();
                        optionType = DDLCreateServerStatement.OWNER;
                        optionLiteral = exprParser.parseString();
                        break;
                    case Keywords.PORT:
                        lexer.nextToken();
                        optionType = DDLCreateServerStatement.PORT;
                        optionLiteral = exprParser.parseNumber();
                        break;
                }
            }
        }
        return new Pair<Integer, Literal>(optionType, optionLiteral);
    }

    private Pair<Expression, AuthOption> user() throws SQLSyntaxErrorException {
        Expression expr = null;
        AuthOption authOption = null;
        LiteralString authString = null;
        LiteralString currentAuthString = null;
        Boolean retainCurrent = null;
        Identifier authPlugin = null;
        LiteralString hashString = null;
        boolean discard = false;
        expr = exprParser.expression();
        if (lexer.token() == Token.IDENTIFIER) {
            if (lexer.parseKeyword() == Keywords.IDENTIFIED) {
                if (lexer.nextToken() == Token.KW_BY) {
                    lexer.nextToken();
                    authString = exprParser.parseString();
                    if (lexer.token() == Token.KW_REPLACE) {
                        lexer.nextToken();
                        currentAuthString = exprParser.parseString();
                    }
                    if (lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.RETAIN) {
                        lexer.nextToken();
                        matchKeywords(Keywords.CURRENT);
                        matchKeywords(Keywords.PASSWORD);
                        retainCurrent = true;
                    }
                } else if (lexer.token() == Token.KW_WITH) {
                    lexer.nextToken();
                    authPlugin = identifier(false);
                    if (lexer.token() == Token.KW_BY) {
                        lexer.nextToken();
                        authString = exprParser.parseString();
                        if (lexer.token() == Token.KW_REPLACE) {
                            lexer.nextToken();
                            currentAuthString = exprParser.parseString();
                        }
                        if (lexer.token() == Token.IDENTIFIER && lexer.parseKeyword() == Keywords.RETAIN) {
                            lexer.nextToken();
                            matchKeywords(Keywords.CURRENT);
                            matchKeywords(Keywords.PASSWORD);
                            retainCurrent = true;
                        }
                    }
                    if (lexer.token() == Token.KW_AS) {
                        lexer.nextToken();
                        hashString = exprParser.parseString();
                    }
                } else {
                    throw new SQLSyntaxErrorException("unexpected DDL SQL!");
                }
            } else {
                matchKeywords(Keywords.DISCARD);
                matchKeywords(Keywords.OLD);
                matchKeywords(Keywords.PASSWORD);
                discard = true;
            }
        } else {
            throw new SQLSyntaxErrorException("unexpected DDL SQL!");
        }
        authOption = new AuthOption(authString, currentAuthString, retainCurrent, authPlugin, hashString, discard);
        return new Pair<Expression, AuthOption>(expr, authOption);
    }

    private Pair<Integer, LiteralString> tlsOption() throws SQLSyntaxErrorException {
        Integer type = null;
        LiteralString string = null;
        switch (lexer.token()) {
            case Token.KW_SSL:
                lexer.nextToken();
                type = DALCreateUserStatement.TLS_SSL;
                break;
            case Token.IDENTIFIER: {
                switch (lexer.parseKeyword()) {
                    case Keywords.X509:
                        lexer.nextToken();
                        type = DALCreateUserStatement.TLS_X509;
                        break;
                    case Keywords.CIPHER:
                        lexer.nextToken();
                        type = DALCreateUserStatement.TLS_CIPHER;
                        string = exprParser.parseString();
                        break;
                    case Keywords.ISSUER:
                        lexer.nextToken();
                        type = DALCreateUserStatement.TLS_ISSUER;
                        string = exprParser.parseString();
                        break;
                    case Keywords.SUBJECT:
                        lexer.nextToken();
                        type = DALCreateUserStatement.TLS_SUBJECT;
                        string = exprParser.parseString();
                        break;
                    default:
                        return null;
                }
                break;
            }
            default:
                return null;
        }
        return new Pair<Integer, LiteralString>(type, string);
    }

    private ForeignKeyDefinition foreignKeyDefinition(Identifier symbol, Identifier indexName)
        throws SQLSyntaxErrorException {
        List<Identifier> columns = new ArrayList<Identifier>();
        match(Token.PUNC_LEFT_PAREN);
        for (int i = 0; lexer.token() != Token.PUNC_RIGHT_PAREN; ++i) {
            if (i > 0) {
                match(Token.PUNC_COMMA);
            }
            columns.add(identifier());
        }
        match(Token.PUNC_RIGHT_PAREN);
        ReferenceDefinition reference = referenceDefinition();
        return new ForeignKeyDefinition(symbol, indexName, columns, reference);
    }

    private ReferenceDefinition referenceDefinition() throws SQLSyntaxErrorException {
        match(Token.KW_REFERENCES);
        Identifier table = identifier(true);
        List<IndexColumnName> keys = new ArrayList<IndexColumnName>();
        match(Token.PUNC_LEFT_PAREN);
        for (int i = 0; lexer.token() != Token.PUNC_RIGHT_PAREN; ++i) {
            if (i > 0)
                match(Token.PUNC_COMMA);
            IndexColumnName indexColumnName = indexColumnName();
            keys.add(indexColumnName);
        }
        match(Token.PUNC_RIGHT_PAREN);
        Integer match = null;
        Integer onDelete = null;
        Integer onUpdate = null;
        loop:
        for (; lexer.token() != Token.EOF; ) {
            switch (lexer.token()) {
                case Token.KW_MATCH:
                    lexer.nextToken();
                    if (lexer.token() == Token.IDENTIFIER) {
                        int word = lexer.parseKeyword();
                        switch (word) {
                            case Keywords.FULL:
                                match = ReferenceDefinition.MATCH_FULL;
                                lexer.nextToken();
                                break;
                            case Keywords.PARTIAL:
                                match = ReferenceDefinition.MATCH_PARTIAL;
                                lexer.nextToken();
                                break;
                            case Keywords.SIMPLE:
                                match = ReferenceDefinition.MATCH_SIMPLE;
                                lexer.nextToken();
                                break;
                            default:
                                break loop;
                        }
                    } else {
                        break loop;
                    }
                    break;
                case Token.KW_ON: {
                    lexer.nextToken();
                    if (lexer.token() == Token.KW_DELETE) {
                        lexer.nextToken();
                        onDelete = referenceOption();
                        break;
                    } else if (lexer.token() == Token.KW_UPDATE) {
                        lexer.nextToken();
                        onUpdate = referenceOption();
                        break;
                    } else {
                        break loop;
                    }
                }
                default:
                    break loop;
            }
        }
        return new ReferenceDefinition(table, keys, match, onDelete, onUpdate);
    }

    private IndexColumnName indexColumnName() throws SQLSyntaxErrorException {
        // {col_name [(length)] | (expr)} [ASC | DESC]
        Expression len = null;
        Identifier colName = null;
        if (lexer.token() == Token.PUNC_LEFT_PAREN) {
            lexer.nextToken();
            len = exprParser.expression();
            match(Token.PUNC_RIGHT_PAREN);
        } else {
            colName = identifier();
            if (lexer.token() == Token.PUNC_LEFT_PAREN) {
                lexer.nextToken();
                len = exprParser.expression();
                match(Token.PUNC_RIGHT_PAREN);
            }
        }
        switch (lexer.token()) {
            case Token.KW_ASC:
                lexer.nextToken();
                return new IndexColumnName(colName, len, true);
            case Token.KW_DESC:
                lexer.nextToken();
                return new IndexColumnName(colName, len, false);
            default:
                return new IndexColumnName(colName, len, true);
        }
    }

    private Integer referenceOption() throws SQLSyntaxErrorException {
        switch (lexer.token()) {
            case Token.KW_RESTRICT:
                lexer.nextToken();
                return ReferenceDefinition.RESTRICT;
            case Token.KW_CASCADE:
                lexer.nextToken();
                return ReferenceDefinition.CASCADE;
            case Token.KW_SET:
                lexer.nextToken();
                if (lexer.token() == Token.LITERAL_NULL) {
                    lexer.nextToken();
                    return ReferenceDefinition.SET_NULL;
                } else if (lexer.token() == Token.KW_DEFAULT) {
                    lexer.nextToken();
                    return ReferenceDefinition.SET_DEFAULT;
                }
                break;
            case Token.IDENTIFIER:
                matchKeywords(Keywords.NO);
                matchKeywords(Keywords.ACTION);
                return ReferenceDefinition.NO_ACTION;
        }
        return null;
    }

    private IndexDefinition indexDefinition(Integer type, Identifier symbol) throws SQLSyntaxErrorException {
        List<IndexColumnName> columns = new ArrayList<IndexColumnName>(1);
        Identifier indexName = null;
        Integer indexType = null;
        if (lexer.token() == Token.IDENTIFIER) {
            indexName = identifier();
        }
        if (lexer.token() == Token.KW_USING) {
            lexer.nextToken();
            indexType = matchKeywords(Keywords.BTREE, Keywords.HASH) == 0 ? IndexOption.BTREE : IndexOption.HASH;
        }
        match(Token.PUNC_LEFT_PAREN);
        for (int i = 0; lexer.token() != Token.PUNC_RIGHT_PAREN; ++i) {
            if (i > 0)
                match(Token.PUNC_COMMA);
            IndexColumnName indexColumnName = indexColumnName();
            columns.add(indexColumnName);
        }
        match(Token.PUNC_RIGHT_PAREN);
        List<IndexOption> options = indexOptions();
        // IndexDefinition indexDefinition = new IndexDefinition(type, indexName,
        // type == IndexDefinition.FULLTEXT || type == IndexDefinition.SPATIAL ? null
        // : indexType,
        // columns, options, symbol);
        IndexDefinition indexDefinition = new IndexDefinition(type, indexName, indexType, columns, options, symbol);
        return indexDefinition;
    }

    private List<IndexOption> indexOptions() throws SQLSyntaxErrorException {
        List<IndexOption> list = null;
        for (; ; ) {
            main_switch:
            switch (lexer.token()) {
                case Token.KW_USING:
                    lexer.nextToken();
                    Integer indexType =
                        matchKeywords(Keywords.BTREE, Keywords.HASH) == 0 ? IndexOption.BTREE : IndexOption.HASH;
                    if (list == null) {
                        list = new ArrayList<IndexOption>(1);
                    }
                    list.add(new IndexOption(indexType, true));
                    break main_switch;
                case Token.KW_WITH:
                    lexer.nextToken();
                    matchKeywords(Keywords.PARSER);
                    Identifier id = identifier();
                    if (list == null) {
                        list = new ArrayList<IndexOption>(1);
                    }
                    list.add(new IndexOption(id));
                    break main_switch;
                case Token.IDENTIFIER:
                    switch (lexer.parseKeyword()) {
                        case Keywords.KEY_BLOCK_SIZE: {
                            lexer.nextToken();
                            if (lexer.token() == Token.OP_EQUALS) {
                                lexer.nextToken();
                            }
                            Expression val = exprParser.expression();
                            if (list == null) {
                                list = new ArrayList<IndexOption>(1);
                            }
                            list.add(new IndexOption(val));
                            break main_switch;
                        }
                        case Keywords.COMMENT: {
                            lexer.nextToken();
                            LiteralString string = (LiteralString)exprParser.expression();
                            if (list == null) {
                                list = new ArrayList<IndexOption>(1);
                            }
                            list.add(new IndexOption(string));
                            break main_switch;
                        }
                        case Keywords.VISIBLE: {
                            lexer.nextToken();
                            if (list == null) {
                                list = new ArrayList<IndexOption>(1);
                            }
                            list.add(new IndexOption(IndexOption.VISIBLE, false));
                            break main_switch;
                        }
                        case Keywords.INVISIBLE: {
                            lexer.nextToken();
                            if (list == null) {
                                list = new ArrayList<IndexOption>(1);
                            }
                            list.add(new IndexOption(IndexOption.INVISIBLE, false));
                            break main_switch;
                        }
                    }
                default:
                    return list;
            }
        }
    }

    private Pair<Integer, Long> resourceOption() throws SQLSyntaxErrorException {
        Integer type = null;
        Long count = null;
        switch (lexer.token()) {
            case Token.IDENTIFIER: {
                switch (lexer.parseKeyword()) {
                    case Keywords.MAX_QUERIES_PER_HOUR:
                        lexer.nextToken();
                        type = DALCreateUserStatement.MAX_QUERIES_PER_HOUR;
                        count = exprParser.longValue();
                        break;
                    case Keywords.MAX_UPDATES_PER_HOUR:
                        lexer.nextToken();
                        type = DALCreateUserStatement.MAX_UPDATES_PER_HOUR;
                        count = exprParser.longValue();
                        break;
                    case Keywords.MAX_CONNECTIONS_PER_HOUR:
                        lexer.nextToken();
                        type = DALCreateUserStatement.MAX_CONNECTIONS_PER_HOUR;
                        count = exprParser.longValue();
                        break;
                    case Keywords.MAX_USER_CONNECTIONS:
                        lexer.nextToken();
                        type = DALCreateUserStatement.MAX_USER_CONNECTIONS;
                        count = exprParser.longValue();
                        break;
                    default:
                        return null;
                }
                break;
            }
            default:
                return null;
        }
        return new Pair<Integer, Long>(type, count);
    }

    private Tuple3<Integer, LiteralString, Long> getAttributie() throws SQLSyntaxErrorException {
        Integer type = null;
        LiteralString string = null;
        Long org_id = null;
        if (lexer.token() == Token.IDENTIFIER) {
            switch (lexer.parseKeyword()) {
                case Keywords.NAME:
                    lexer.nextToken();
                    type = DDLCreateSpatialReferenceSystemStatement.NAME;
                    string = exprParser.parseString();
                    break;
                case Keywords.DEFINITION:
                    lexer.nextToken();
                    type = DDLCreateSpatialReferenceSystemStatement.DEFINITION;
                    string = exprParser.parseString();
                    break;
                case Keywords.ORGANIZATION:
                    lexer.nextToken();
                    type = DDLCreateSpatialReferenceSystemStatement.ORGANIZATION;
                    string = exprParser.parseString();
                    matchKeywords(Keywords.IDENTIFIED);
                    match(Token.KW_BY);
                    org_id = exprParser.longValue();
                    break;
                case Keywords.DESCRIPTION:
                    lexer.nextToken();
                    type = DDLCreateSpatialReferenceSystemStatement.DESCRIPTION;
                    string = exprParser.parseString();
                    break;
                default:
                    throw new SQLSyntaxErrorException("unexpected SQL!");
            }
        } else {
            throw new SQLSyntaxErrorException("unexpected SQL!");
        }
        return new Tuple3<>(type, string, org_id);
    }

    private List<PartitionDefinition> partitionDefinition() throws SQLSyntaxErrorException {
        List<PartitionDefinition> partitionDefinitions = new ArrayList<>();
        match(Token.PUNC_LEFT_PAREN);
        do {
            if (lexer.token() == Token.PUNC_COMMA) {
                lexer.nextToken();
            }
            match(Token.KW_PARTITION);
            PartitionDefinition partitionDefinition = new PartitionDefinition();
            partitionDefinition.setName(identifier());
            for (; lexer.token() != Token.PUNC_RIGHT_PAREN && lexer.token() != Token.PUNC_COMMA; ) {
                switch (lexer.token()) {
                    case Token.KW_VALUES:
                        lexer.nextToken();
                        if (lexer.token() == Token.IDENTIFIER) {
                            byte[] id = identifier(false).getIdText();
                            if (BytesUtil.equalsIgnoreCase(id, "LESS".getBytes())) {
                                id = identifier(false).getIdText();
                                if (BytesUtil.equalsIgnoreCase(id, "THAN".getBytes())) {
                                    if (lexer.token() == Token.KW_MAXVALUE) {
                                        partitionDefinition.setLessThanMaxvalue(true);
                                        lexer.nextToken();
                                    } else {
                                        match(Token.PUNC_LEFT_PAREN);
                                        List<Expression> exprs = new ArrayList<>();
                                        for (; lexer.token() != Token.PUNC_RIGHT_PAREN; ) {
                                            exprs.add(exprParser.expression());
                                            if (lexer.token() == Token.PUNC_COMMA) {
                                                lexer.nextToken();
                                            }
                                        }
                                        if (exprs.size() == 1) {
                                            partitionDefinition.setLessThan(exprs.get(0));
                                        } else {
                                            partitionDefinition.setLessThanList(exprs);
                                        }
                                        match(Token.PUNC_RIGHT_PAREN);
                                    }
                                }
                            }
                        } else if (lexer.token() == Token.KW_IN) {
                            lexer.nextToken();
                            match(Token.PUNC_LEFT_PAREN);
                            List<Expression> exprs = new ArrayList<>();
                            exprs.add(exprParser.expression());
                            for (; lexer.token() != Token.PUNC_RIGHT_PAREN; ) {
                                lexer.nextToken();
                                exprs.add(exprParser.expression());
                            }
                            partitionDefinition.setInList(exprs);
                            match(Token.PUNC_RIGHT_PAREN);
                        }
                        break;
                    case Token.IDENTIFIER:
                        switch (lexer.parseKeyword()) {
                            case Keywords.STORAGE:
                                lexer.nextToken();
                            case Keywords.ENGINE:
                                if (lexer.nextToken() == Token.OP_EQUALS) {
                                    lexer.nextToken();
                                }
                                partitionDefinition.setEngine(identifier());
                                break;
                            case Keywords.COMMENT:
                                if (lexer.nextToken() == Token.OP_EQUALS) {
                                    lexer.nextToken();
                                }
                                partitionDefinition.setComment((LiteralString)exprParser.expression());
                                break;
                            case Keywords.DATA:
                                lexer.nextToken();
                                matchKeywords(Keywords.DIRECTORY);
                                if (lexer.token() == Token.OP_EQUALS) {
                                    lexer.nextToken();
                                }
                                partitionDefinition.setDataDir((LiteralString)exprParser.expression());
                                break;
                            case Keywords.MAX_ROWS:
                                if (lexer.nextToken() == Token.OP_EQUALS) {
                                    lexer.nextToken();
                                }
                                partitionDefinition.setMaxRows(exprParser.longValue());
                                break;
                            case Keywords.MIN_ROWS:
                                if (lexer.nextToken() == Token.OP_EQUALS) {
                                    lexer.nextToken();
                                }
                                partitionDefinition.setMinRows(exprParser.longValue());
                                break;
                            case Keywords.TABLESPACE:
                                if (lexer.nextToken() == Token.OP_EQUALS) {
                                    lexer.nextToken();
                                }
                                partitionDefinition.setTablespace(identifier());
                                break;
                        }
                        break;
                    case Token.KW_INDEX:
                        lexer.nextToken();
                        matchKeywords(Keywords.DIRECTORY);
                        if (lexer.token() == Token.OP_EQUALS) {
                            lexer.nextToken();
                        }
                        partitionDefinition.setIndexDir((LiteralString)exprParser.expression());
                        break;
                    case Token.PUNC_LEFT_PAREN:
                        lexer.nextToken();
                        List<SubpartitionDefinition> subpartitionDefinitions = new ArrayList<>();
                        do {
                            matchKeywords(Keywords.SUBPARTITION);
                            SubpartitionDefinition subpartitionDefinition = new SubpartitionDefinition();
                            subpartitionDefinition.setLogicalName(identifier());
                            for (; lexer.token() != Token.PUNC_RIGHT_PAREN && lexer.token() != Token.PUNC_COMMA; ) {
                                switch (lexer.token()) {
                                    case Token.IDENTIFIER:
                                        switch (lexer.parseKeyword()) {
                                            case Keywords.STORAGE:
                                                lexer.nextToken();
                                            case Keywords.ENGINE:
                                                if (lexer.nextToken() == Token.OP_EQUALS) {
                                                    lexer.nextToken();
                                                }
                                                subpartitionDefinition.setEngine(identifier());
                                                break;
                                            case Keywords.COMMENT:
                                                if (lexer.nextToken() == Token.OP_EQUALS) {
                                                    lexer.nextToken();
                                                }
                                                subpartitionDefinition
                                                    .setComment((LiteralString)exprParser.expression());
                                                break;
                                            case Keywords.DATA:
                                                lexer.nextToken();
                                                matchKeywords(Keywords.DIRECTORY);
                                                if (lexer.token() == Token.OP_EQUALS) {
                                                    lexer.nextToken();
                                                }
                                                subpartitionDefinition
                                                    .setDataDir((LiteralString)exprParser.expression());
                                                break;
                                            case Keywords.MAX_ROWS:
                                                if (lexer.nextToken() == Token.OP_EQUALS) {
                                                    lexer.nextToken();
                                                }
                                                subpartitionDefinition.setMaxRows(exprParser.longValue());
                                                break;
                                            case Keywords.MIN_ROWS:
                                                if (lexer.nextToken() == Token.OP_EQUALS) {
                                                    lexer.nextToken();
                                                }
                                                subpartitionDefinition.setMinRows(exprParser.longValue());
                                                break;
                                            case Keywords.TABLESPACE:
                                                if (lexer.nextToken() == Token.OP_EQUALS) {
                                                    lexer.nextToken();
                                                }
                                                subpartitionDefinition.setTablespace(identifier());
                                                break;
                                        }
                                        break;
                                    case Token.KW_INDEX:
                                        lexer.nextToken();
                                        matchKeywords(Keywords.DIRECTORY);
                                        if (lexer.token() == Token.OP_EQUALS) {
                                            lexer.nextToken();
                                        }
                                        subpartitionDefinition.setIndexDir((LiteralString)exprParser.expression());
                                        break;
                                }
                            }
                            subpartitionDefinitions.add(subpartitionDefinition);
                            if (lexer.token() == Token.PUNC_COMMA) {
                                lexer.nextToken();
                            }
                        } while (lexer.parseKeyword() == Keywords.SUBPARTITION);
                        partitionDefinition.setSubpartitionDefinitions(subpartitionDefinitions);
                        match(Token.PUNC_RIGHT_PAREN);
                        break;
                }
            }
            partitionDefinitions.add(partitionDefinition);
        } while (lexer.token() == Token.PUNC_COMMA);
        match(Token.PUNC_RIGHT_PAREN);
        return partitionDefinitions;
    }

    private ColumnDefinition columnDefinition() throws SQLSyntaxErrorException {
        DataType dataType = dataType();
        boolean notNull = false;
        Expression defaultVal = null;
        Boolean unique = null;
        Boolean primary = null;
        Boolean autoIncrement = null;
        Integer format = null;
        LiteralString comment = null;
        ReferenceDefinition referenceDefinition = null;
        Expression as = null;
        Integer storage = null;
        Boolean stored = null;
        Boolean virtual = null;
        Expression onUpdate = null;
        Tuple3<Identifier, Expression, Boolean> checkConstraintDef = null;
        while (lexer.token() != Token.PUNC_COMMA) {
            if (possibleToken.contains(lexer.token())) {
                int key = lexer.parseKeyword();
                if (key == Keywords.FIRST || key == Keywords.AFTER) {
                    break;
                }
                switch (lexer.token()) {
                    case Token.KW_NOT:
                        lexer.nextToken();
                        match(Token.LITERAL_NULL);
                        notNull = true;
                        break;
                    case Token.LITERAL_NULL:
                        lexer.nextToken();
                        break;
                    case Token.KW_DEFAULT:
                        lexer.nextToken();
                        defaultVal = exprParser.expression();
                        break;
                    case Token.IDENTIFIER:
                        switch (lexer.parseKeyword()) {
                            case Keywords.AUTO_INCREMENT:
                                lexer.nextToken();
                                autoIncrement = true;
                                break;
                            case Keywords.COMMENT:
                                lexer.nextToken();
                                comment = exprParser.parseString();
                                break;
                            case Keywords.COLUMN_FORMAT:
                                switch (lexer.nextToken()) {
                                    case Token.KW_DEFAULT:
                                        format = ColumnDefinition.FORMAT_DEFAULT;
                                        break;
                                    case Token.IDENTIFIER:
                                        switch (lexer.parseKeyword()) {
                                            case Keywords.FIXED:
                                                format = ColumnDefinition.FORMAT_FIXED;
                                                break;
                                            case Keywords.DYNAMIC:
                                                format = ColumnDefinition.FORMAT_DYNAMIC;
                                                break;
                                        }
                                }
                                lexer.nextToken();
                                break;
                            case Keywords.STORAGE:
                                switch (lexer.nextToken()) {
                                    case Token.KW_DEFAULT:
                                        storage = ColumnDefinition.STORAGE_DEFAULT;
                                        break;
                                    case Token.IDENTIFIER:
                                        switch (lexer.parseKeyword()) {
                                            case Keywords.DISK:
                                                storage = ColumnDefinition.STORAGE_DISK;
                                                break;
                                            case Keywords.MEMORY:
                                                storage = ColumnDefinition.STORAGE_MEMORY;
                                                break;
                                        }
                                }
                                lexer.nextToken();
                                break;
                            default:
                                throw new SQLSyntaxErrorException("unsupported DDL");
                        }
                        break;
                    case Token.KW_REFERENCES:
                        referenceDefinition = referenceDefinition();
                        break;
                    case Token.KW_CONSTRAINT: {
                        Identifier symbol = null;
                        if (lexer.nextToken() == Token.IDENTIFIER) {
                            symbol = identifier();
                        }
                        match(Token.KW_CHECK);
                        match(Token.PUNC_LEFT_PAREN);
                        Expression expr = exprParser.expression();
                        match(Token.PUNC_RIGHT_PAREN);
                        Boolean enforced = null;
                        if (lexer.token() == Token.KW_NOT) {
                            lexer.nextToken();
                            matchIdentifier("ENFORCED");
                            enforced = false;
                        } else if (lexer.token() == Token.IDENTIFIER) {
                            matchIdentifier("ENFORCED");
                            enforced = true;
                        }
                        checkConstraintDef = new Tuple3<Identifier, Expression, Boolean>(symbol, expr, enforced);
                        break;
                    }
                    case Token.KW_CHECK: {
                        lexer.nextToken();
                        match(Token.PUNC_LEFT_PAREN);
                        Expression expr = exprParser.expression();
                        match(Token.PUNC_RIGHT_PAREN);
                        Boolean enforced = null;
                        if (lexer.token() == Token.KW_NOT) {
                            lexer.nextToken();
                            matchIdentifier("ENFORCED");
                            enforced = false;
                        } else if (lexer.token() == Token.IDENTIFIER) {
                            matchIdentifier("ENFORCED");
                            enforced = true;
                        }
                        checkConstraintDef = new Tuple3<Identifier, Expression, Boolean>(null, expr, enforced);
                        break;
                    }
                    case Token.KW_ON:
                        lexer.nextToken();
                        match(Token.KW_UPDATE);
                        onUpdate = exprParser.expression();
                        break;
                    case Token.KW_UNIQUE:
                        if (lexer.nextToken() == Token.KW_KEY) {
                            lexer.nextToken();
                        }
                        unique = true;
                        break;
                    case Token.KW_PRIMARY:
                        lexer.nextToken();
                        break;
                    case Token.KW_KEY:
                        match(Token.KW_KEY);
                        primary = true;
                        break;
                    case Token.KW_COLLATE:
                        lexer.nextToken();
                        dataType.setCollation(identifier());
                        break;
                    case Token.KW_VIRTUAL:
                        lexer.nextToken();
                        virtual = true;
                        break;
                    case Token.KW_STORED:
                        lexer.nextToken();
                        stored = true;
                        break;
                    case Token.KW_GENERATED:
                        lexer.nextToken();
                        if (equalsKeyword(Keywords.ALWAYS)) {
                            lexer.nextToken();
                        }
                        break;
                    case Token.KW_AS:
                        lexer.nextToken();
                        as = this.exprParser.expression();
                        break;
                    default:
                        lexer.nextToken();
                        break;
                }
            } else {
                // lexer.nextToken();
                break;
            }
        }
        return new ColumnDefinition(dataType, notNull, defaultVal, autoIncrement, unique, primary, comment, format,
            storage, referenceDefinition, as, virtual, stored, onUpdate, checkConstraintDef);
    }

}
